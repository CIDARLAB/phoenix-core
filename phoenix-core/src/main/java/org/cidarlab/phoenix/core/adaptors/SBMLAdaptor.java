/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.cidarlab.phoenix.core.dom.AssignedModule;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.NamedSBase;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.SimpleSpeciesReference;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.text.parser.FormulaParserLL3;
import org.sbml.jsbml.text.parser.IFormulaParser;
import org.sbml.jsbml.util.compilers.FormulaCompiler;
import org.sbml.jsbml.util.compilers.FormulaCompilerLibSBML;
import org.sbml.jsbml.text.parser.ParseException;

/**
 *
 * @author nicholasroehner
 * @author evanappleton
 * @author prash
 */
public class SBMLAdaptor {
	
	private static void testAdaptor() {
		SBMLDocument degradationDoc = createDegradationModel("GFP");

		SBMLDocument expressionDoc = createExpressionModel("GFP");

		SBMLDocument repressionDoc = createRepressionModel("LacI", "GFP");

		SBMLDocument activationDoc = createActivationModel("GAL4VP16", "GFP");

		SBMLDocument inductionRepressionDoc = createInductionRepressionModel("IPTG", "LacI", "GFP");

		SBMLDocument inductionActivationDoc = createInductionActivationModel("Arabinose", "AraC", "GFP");

		SBMLDocument selfRepressionDoc = createRepressionModel("LacI", "GFP");
		HashMap<String, String> substitutions = new HashMap<String, String>();
		substitutions.put("GFP", "LacI");
		substituteSpecies(substitutions, selfRepressionDoc.getModel());

		SBMLDocument inductionInverterDoc1 = createInductionRepressionModel("IPTG", "LacI", "TetR");
		SBMLDocument inductionInverterDoc2 = createInductionRepressionModel("aTc", "TetR", "LacI");
		SBMLDocument toggleDoc = composeExpressionModels(inductionInverterDoc1.getModel(), inductionInverterDoc2.getModel());

		List<Model> inverterMods = new LinkedList<Model>();
		inverterMods.add(createRepressionModel("LacI", "TetR").getModel());
		inverterMods.add(createRepressionModel("TetR", "cI").getModel());
		inverterMods.add(createRepressionModel("cI", "LacI").getModel());
		SBMLDocument repressilatorDoc = composeExpressionModels(inverterMods);
	}
	
        public static void createSBMLfiles(AssignedModule amodule,String filepath){
            SBMLWriter writer = new SBMLWriter();
            int count =0;
            for(SBMLDocument doc:amodule.getSBMLDocument()){
                
                try {
                    writer.write(doc, filepath + "/" + amodule.getShortName()+"_"+count);
                    count++;
                } catch (XMLStreamException ex) {
                    Logger.getLogger(SBMLAdaptor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SBMLAdaptor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SBMLException ex) {
                    Logger.getLogger(SBMLAdaptor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        
	/*
     * Methods for composing SBML models of gene expression
     */  
	
	public static SBMLDocument composeExpressionModels(Model mod1, Model mod2) {
		return composeExpressionModels(mod1, mod2, new HashMap<String, String>());
	}
	
	public static SBMLDocument composeExpressionModels(Model mod1, Model mod2, HashMap<String, String> substitutions) {
		List<Model> mods = new LinkedList<Model>();
		mods.add(mod1);
		mods.add(mod2);
		return composeExpressionModels(mods, substitutions);
	}
	
	public static SBMLDocument composeExpressionModels(List<Model> mods) {
		return composeExpressionModels(mods, new HashMap<String, String>());
	}
	
	public static SBMLDocument composeExpressionModels(List<Model> mods, HashMap<String, String> substitutions) {
		SBMLDocument composedDoc = createCompartmentModel("cell", "cell");
		Model composedMod = composedDoc.getModel();
		for (Model mod : mods) {
			for (Species spec : mod.getListOfSpecies()) {
				Species composedSpec = spec.clone();
				if (versionSBase(composedSpec, composedMod)) {
					substitutions.put(composedSpec.getId(), spec.getId());
				}
				composedSpec.setCompartment(composedMod.getCompartment(0));
				composedMod.addSpecies(composedSpec);
			}
			for (Reaction rxn : mod.getListOfReactions()) {
				Reaction composedRxn = rxn.clone();
				if (composedRxn.getSBOTermID().equals(SBOTerm.EXPRESSION.getID())) {
					versionSBase(composedRxn, composedMod);
					composedMod.addReaction(composedRxn);
				} else if (composedRxn.getSBOTermID().equals(SBOTerm.DEGRADATION.getID()) && !composedMod.containsReaction(composedRxn.getId())) {
					composedMod.addReaction(composedRxn);
				}
			}
		}
		if (substitutions.isEmpty()) {
			for (int i = 1; i < mods.size(); i++) {
				substitutions.putAll(inferIOSubstitutions(mods.get(i - 1), mods.get(i)));
			}
		}
		if (!substitutions.isEmpty()) {
			substituteSpecies(substitutions, composedMod);
		}
		return composedDoc;
	}
	
	private static boolean versionSBase(NamedSBase sb, Model mod) {
		String versionedID = sb.getId();
		int version = 0;
		while (mod.containsUniqueNamedSBase(versionedID)) {
			version++;
			versionedID = sb.getId() + version;
		}
		if (version > 0) {
			sb.setId(versionedID);
			 return true;
		} else {
			return false;
		}
	}
	
	public static void substituteSpecies(HashMap<String, String> substitutions, Model mod) {
		for (String substitutedID : substitutions.keySet()) {
			Species substitutedSpec = mod.getSpecies(substitutedID);
			Species substituteSpec = mod.getSpecies(substitutions.get(substitutedID));
			if (substituteSpec.isSetSBOTerm()) {
				if (substituteSpec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
					if (substitutedSpec.isSetSBOTerm()) {
						if (substitutedSpec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
							mod.removeSpecies(substitutedSpec);
						} else if (substitutedSpec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
							mod.removeSpecies(substituteSpec);
							substitutedSpec.setId(substituteSpec.getId());
							substitutedSpec.unsetSBOTerm();
						} else {
							mod.removeSpecies(substituteSpec);
							substitutedSpec.setId(substituteSpec.getId());
						}
					} else {
						mod.removeSpecies(substituteSpec);
						substitutedSpec.setId(substituteSpec.getId());
					}
				} else if (substituteSpec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
					mod.removeSpecies(substitutedSpec);
					if (substitutedSpec.isSetSBOTerm()) {
						if (substitutedSpec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
							substituteSpec.unsetSBOTerm();
						} else if (!substitutedSpec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
							substituteSpec.setSBOTerm(substitutedSpec.getSBOTerm());
						}
					}
				} else if (substitutedSpec.isSetSBOTerm() && substitutedSpec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
					mod.removeSpecies(substituteSpec);
					substitutedSpec.setId(substituteSpec.getId());
					substitutedSpec.setSBOTerm(substituteSpec.getSBOTerm());
				} else {
					mod.removeSpecies(substitutedSpec);
				}
			} else if (substitutedSpec.isSetSBOTerm()) {
				if (substitutedSpec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
					mod.removeSpecies(substitutedSpec);
				} else if (substitutedSpec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
					mod.removeSpecies(substituteSpec);
					substitutedSpec.setId(substituteSpec.getId());
					substitutedSpec.unsetSBOTerm();
				} else {
					mod.removeSpecies(substitutedSpec);
					substituteSpec.setSBOTerm(substitutedSpec.getSBOTerm());
				}
			} else {
				mod.removeSpecies(substitutedSpec);
			}
		}
		for (Reaction rxn : mod.getListOfReactions()) {
			substituteSpeciesReferences(substitutions, rxn);
		}
	}
	
	private static void substituteSpeciesReferences(HashMap<String, String> substitutions, Reaction rxn) {
		String formula = compileFormula(rxn.getKineticLaw().getMath());
		Set<SimpleSpeciesReference> specRefs = new HashSet<SimpleSpeciesReference>();
		specRefs.addAll(rxn.getListOfModifiers());
		specRefs.addAll(rxn.getListOfReactants());
		specRefs.addAll(rxn.getListOfProducts());
		for (SimpleSpeciesReference specRef : specRefs) {
			if (substitutions.containsKey(specRef.getSpecies())) {
				formula.replaceAll(specRef.getSpecies(), substitutions.get(specRef.getSpecies()));
				specRef.setSpecies(substitutions.get(specRef.getSpecies()));
			}
		}
		rxn.getKineticLaw().setMath(parseFormula(formula));
	}
	
	private static HashMap<String, String> inferIOSubstitutions(Model mod1, Model mod2) {
		Set<Species> outputs = getOutputs(mod1);
		Set<Species> inputs = getInputs(mod2);
		HashMap<String, String> substitutions = new HashMap<String, String>();
		if (!inputs.isEmpty() && !outputs.isEmpty()) {
			substitutions.put(inputs.iterator().next().getId(), outputs.iterator().next().getId());
		}
		return substitutions;
	}
	
	/*
     * Methods for creating SBML models of gene expression
     */
	
	public static SBMLDocument createCompartmentModel(String compID) {
    	return createCompartmentModel(compID, compID);
	}
	
	public static SBMLDocument createCompartmentModel(String compID, String compName) {
		SBMLDocument compartmentDoc = new SBMLDocument(3, 1);
    	Model compartmentMod = compartmentDoc.createModel();
    	Compartment cell = compartmentMod.createCompartment(compID);
    	cell.setName(compName);
    	cell.setConstant(false);
    	return compartmentDoc;
	}
	
	public static SBMLDocument createDegradationModel(String degradedID) {
    	return createDegradationModel(degradedID, degradedID);
    }
    
    public static SBMLDocument createDegradationModel(String degradedID, String degradedName) {
    	SBMLDocument degradationDoc = createCompartmentModel("cell", "cell");
    	Species degraded = createSpecies(degradedID, degradedName, degradationDoc.getModel());
    	degraded.setSBOTerm(SBOTerm.OUTPUT.getID());
    	createDegradationReaction(degraded, degradationDoc.getModel());
    	return degradationDoc;
    }
    

    public static SBMLDocument createExpressionModel(String expressedID) {
    	return createExpressionModel(expressedID, expressedID);
    }
    
    public static SBMLDocument createExpressionModel(String expressedID, String expressedName) {
    	SBMLDocument expressionDoc = createDegradationModel(expressedID, expressedName);
    	createExpressionReaction(expressionDoc.getModel().getSpecies(expressedID), expressionDoc.getModel());
    	return expressionDoc;
    }
   
    public static SBMLDocument createRepressionModel(String repressorID, String expressedID) {
    	return createRepressionModel(repressorID, expressedID, repressorID, expressedID, 2.0);
    }
    
    public static SBMLDocument createRepressionModel(String repressorID, String expressedID, double cooperativity) {
    	return createRepressionModel(repressorID, expressedID, repressorID, expressedID, cooperativity);
    }
    
    public static SBMLDocument createRepressionModel(String repressorID, String expressedID, String repressorName, String expressedName) {
    	return createRepressionModel(repressorID, expressedID, repressorName, expressedName, 2.0);
    }
    
    public static SBMLDocument createRepressionModel(String repressorID, String expressedID, String repressorName, String expressedName, 
    		double cooperativity) {
    	SBMLDocument repressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, repressionDoc.getModel());
    	repressor.setSBOTerm(SBOTerm.INPUT.getID());
    	createRepressibleExpressionReaction(repressor, repressionDoc.getModel().getSpecies(expressedID), cooperativity, repressionDoc.getModel());
    	return repressionDoc;
    }
    
    public static SBMLDocument createActivationModel(String activatorID, String expressedID) {
    	return createActivationModel(activatorID, expressedID, activatorID, expressedID, 2.0);
    }
    
    public static SBMLDocument createActivationModel(String activatorID, String expressedID, double cooperativity) {
    	return createActivationModel(activatorID, expressedID, activatorID, expressedID, cooperativity);
    }
    
    public static SBMLDocument createActivationModel(String activatorID, String expressedID, String activatorName, String expressedName) {
    	return createActivationModel(activatorID, expressedID, activatorName, expressedName, 2.0);
    }
    
    public static SBMLDocument createActivationModel(String activatorID, String expressedID, String activatorName, String expressedName,
    		double cooperativity) {
    	SBMLDocument activationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, activationDoc.getModel());
    	activator.setSBOTerm(SBOTerm.INPUT.getID());
    	createActivatableExpressionReaction(activator, activationDoc.getModel().getSpecies(expressedID), cooperativity, activationDoc.getModel());
    	return activationDoc;
    }
    
    public static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID) {
    	return createInductionRepressionModel(inducerID, repressorID, expressedID, inducerID, repressorID, expressedID, 2.0);
    }
    
    public static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID, double cooperativity) {
    	return createInductionRepressionModel(inducerID, repressorID, expressedID, inducerID, repressorID, expressedID, cooperativity);
    }
    
    public static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID,   
    		String inducerName, String repressorName, String expressedName) {
    	return createInductionRepressionModel(inducerID, repressorID, expressedID, inducerName, repressorName, expressedName, 2.0);
    }
    
    public static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID,   
    		String inducerName, String repressorName, String expressedName, double cooperativity) {
    	SBMLDocument inductionRepressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, inductionRepressionDoc.getModel());
    	repressor.setSBOTerm(SBOTerm.INPUT.getID());
    	Species inducer = createSpecies(inducerID, inducerName, inductionRepressionDoc.getModel());
    	inducer.setSBOTerm(SBOTerm.INPUT.getID());
    	createInducibleRepressibleExpressionReaction(inducer, repressor, inductionRepressionDoc.getModel().getSpecies(expressedID), cooperativity,
    			inductionRepressionDoc.getModel());
    	return inductionRepressionDoc;
    }
    
    public static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID) {
    	return createInductionActivationModel(inducerID, activatorID, expressedID, inducerID, activatorID, expressedID, 2.0);
    }
    
    public static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID, double cooperativity) {
    	return createInductionActivationModel(inducerID, activatorID, expressedID, inducerID, activatorID, expressedID);
    }
    
    public static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID, 
    		String inducerName, String activatorName, String expressedName) {
    	return createInductionActivationModel(inducerID, activatorID, expressedID, inducerName, activatorName, expressedName, 2.0);
    }
    
    public static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID, 
    		String inducerName, String activatorName, String expressedName, double cooperativity) {
    	SBMLDocument inductionActivationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, inductionActivationDoc.getModel());
    	activator.setSBOTerm(SBOTerm.INPUT.getID());
    	Species inducer = createSpecies(inducerID, inducerName, inductionActivationDoc.getModel());
    	inducer.setSBOTerm(SBOTerm.INPUT.getID());
    	createInducibleActivatableExpressionReaction(inducer, activator, inductionActivationDoc.getModel().getSpecies(expressedID),   
    			cooperativity, inductionActivationDoc.getModel());
    	return inductionActivationDoc;
    }
    
    private static Species createSpecies(String specID, Model mod) {
    	return createSpecies(specID, specID, mod);
    }
    
    private static Species createSpecies(String specID, String specName, Model mod) {
    	Species spec;
    	if (mod.getNumCompartments() > 0) {
    		spec = mod.createSpecies(specID, mod.getCompartment(0));
    	} else {
    		spec = mod.createSpecies(specID);
    	}
    	spec.setName(specName);
    	spec.setHasOnlySubstanceUnits(true);
    	spec.setBoundaryCondition(false);
    	spec.setConstant(false);
    	return spec;
    }
    
    private static Reaction createDegradationReaction(Species degraded, Model mod) {
    	Reaction degradation = mod.createReaction(degraded.getId() + "_degradation");
    	degradation.setName(degraded.getName() + "_degradation");
    	degradation.setFast(false);
    	degradation.setReversible(false);
    	degradation.setSBOTerm(SBOTerm.DEGRADATION.getID());
    	SpeciesReference reactant = degradation.createReactant(degraded);
    	reactant.setStoichiometry(1.0);
    	reactant.setConstant(true);
    	KineticLaw degradationLaw = degradation.createKineticLaw();
    	LocalParameter maxDegradationRate = degradationLaw.createLocalParameter("y");
    	maxDegradationRate.setName("y");
    	maxDegradationRate.setValue(1.0);
    	LocalParameter degradationBindingEqulibrium = degradationLaw.createLocalParameter("K_d");
    	degradationBindingEqulibrium.setName("K_d");
    	degradationBindingEqulibrium.setValue(1.0);
    	degradationLaw.setMath(parseFormula(maxDegradationRate.getId() 
    			+ "*" + degraded.getId() + "/" + degradationBindingEqulibrium.getId() 
    			+ "/(1+" + degraded.getId() + "/" + degradationBindingEqulibrium.getId() + ")"));
    	return degradation;
    }
    
    private static Reaction createExpressionReaction(Species expressed, Model mod) {
    	Reaction expression = createRatelessExpressionReaction(expressed, mod);
    	KineticLaw expressionLaw = expression.createKineticLaw();
		LocalParameter expressionRate = expressionLaw.createLocalParameter("k_EXE");
		expressionRate.setName("k_EXE");
		expressionRate.setValue(1.0);
		expressionLaw.setMath(parseFormula(expressionRate.getId()));
    	return expression;
    }
    
    private static Reaction createRatelessExpressionReaction(Species expressed, Model mod) {
    	Reaction expression = mod.createReaction(expressed.getId() + "_expression");
    	expression.setName(expressed.getName() + "_expression");
    	expression.setFast(false);
    	expression.setReversible(false);
    	expression.setSBOTerm(SBOTerm.EXPRESSION.getID());
    	SpeciesReference product = expression.createProduct(expressed);
    	product.setStoichiometry(1.0);
    	product.setConstant(true);
    	return expression;
    }
    
    private static Reaction createRepressibleExpressionReaction(Species repressor, Species expressed, double cooperativity, Model mod) {
    	Reaction repressibleExpression = createRatelessRepressibleExpressionReaction(repressor, expressed, mod);
    	KineticLaw repressibleExpressionLaw = repressibleExpression.createKineticLaw();
		LocalParameter maxExpressionRate = repressibleExpressionLaw.createLocalParameter("k_EXR");
		maxExpressionRate.setName("k_EXR");
		maxExpressionRate.setValue(1.0);
		LocalParameter repressionBindingEquilibrium = repressibleExpressionLaw.createLocalParameter("K_r");
		repressionBindingEquilibrium.setName("K_r");
		repressionBindingEquilibrium.setValue(1.0);
		LocalParameter coop = repressibleExpressionLaw.createLocalParameter("n");
		coop.setName("n");
		coop.setValue(cooperativity);
		repressibleExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
				+ "/(1+"+ repressionBindingEquilibrium.getId() + "*" + repressor.getId() + "^" + coop + ")"));
    	return repressibleExpression;
    }
    
    private static Reaction createRatelessRepressibleExpressionReaction(Species repressor, Species expressed, Model mod) {
    	Reaction repressibleExpression = createRatelessExpressionReaction(expressed, mod);
    	ModifierSpeciesReference modifier = repressibleExpression.createModifier(repressor);
    	modifier.setSBOTerm(SBOTerm.REPRESSOR.getID());
    	return repressibleExpression;
    }
    
    private static Reaction createActivatableExpressionReaction(Species activator, Species expressed, double cooperativity, Model mod) {
    	Reaction activatableExpression = createRatelessActivatableExpressionReaction(activator, expressed, mod);
    	KineticLaw activatableExpressionLaw = activatableExpression.createKineticLaw();
		LocalParameter maxExpressionRate = activatableExpressionLaw.createLocalParameter("k_EXA");
		maxExpressionRate.setName("k_EXA");
		maxExpressionRate.setValue(1.0);
		LocalParameter activationBindingEquilibrium = activatableExpressionLaw.createLocalParameter("K_a");
		activationBindingEquilibrium.setName("K_a");
		activationBindingEquilibrium.setValue(1.0);
		LocalParameter coop = activatableExpressionLaw.createLocalParameter("n");
		coop.setName("n");
		coop.setValue(cooperativity);
		activatableExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
				+ "*" + activationBindingEquilibrium.getId() + "*" + activator.getId() + "^" + coop
				+  "/(1+"+ activationBindingEquilibrium.getId() + "*" + activator.getId() + "^" + coop + ")"));
    	return activatableExpression;
    }
    
    private static Reaction createRatelessActivatableExpressionReaction(Species activator, Species expressed, Model mod) {
    	Reaction activatableExpression = createRatelessExpressionReaction(expressed, mod);
    	ModifierSpeciesReference modifier = activatableExpression.createModifier(activator);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return activatableExpression;
    }
    
    private static Reaction createInducibleRepressibleExpressionReaction(Species inducer, Species repressor, Species expressed, 
    		double cooperativity, Model mod) {
    	Reaction inducibleRepressibleExpression = createRatelessInducibleRepressibleExpressionReaction(inducer, repressor, expressed, mod);
    	KineticLaw inducibleRepressibleExpressionLaw = inducibleRepressibleExpression.createKineticLaw();
    	LocalParameter maxExpressionRate = inducibleRepressibleExpressionLaw.createLocalParameter("k_EXR");
    	maxExpressionRate.setName("k_EXR");
    	maxExpressionRate.setValue(1.0);
    	LocalParameter repressionBindingEqulibrium = inducibleRepressibleExpressionLaw.createLocalParameter("K_r");
    	repressionBindingEqulibrium.setName("K_r");
    	repressionBindingEqulibrium.setValue(1.0);
    	LocalParameter inducibleBindingEquilibrium = inducibleRepressibleExpressionLaw.createLocalParameter("K_i");
    	inducibleBindingEquilibrium.setName("K_i");
    	inducibleBindingEquilibrium.setValue(1.0);
    	LocalParameter coop = inducibleRepressibleExpressionLaw.createLocalParameter("n");
		coop.setName("n");
		coop.setValue(cooperativity);
    	inducibleRepressibleExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
    			+ "/(1+"+ repressionBindingEqulibrium.getId() + "*" + repressor.getId() + "^" + coop 
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + "))"));
    	return inducibleRepressibleExpression;
    }
    
    private static Reaction createRatelessInducibleRepressibleExpressionReaction(Species inducer, Species repressor, Species expressed, Model mod) {
    	Reaction inducibleRepressibleExpression = createRatelessRepressibleExpressionReaction(repressor, expressed, mod);
    	ModifierSpeciesReference modifier = inducibleRepressibleExpression.createModifier(inducer);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return inducibleRepressibleExpression;
    }
    
    private static Reaction createInducibleActivatableExpressionReaction(Species inducer, Species activator, Species expressed, 
    		double cooperativity, Model mod) {
    	Reaction inducibleActivatableExpression = createRatelessInducibleActivatableExpressionReaction(inducer, activator, expressed, mod);
    	KineticLaw inducibleActivatableExpressionLaw = inducibleActivatableExpression.createKineticLaw();
    	LocalParameter maxExpressionRate = inducibleActivatableExpressionLaw.createLocalParameter("k_EXA");
    	maxExpressionRate.setName("k_EXA");
    	maxExpressionRate.setValue(1.0);
    	LocalParameter activationBindingEqulibrium = inducibleActivatableExpressionLaw.createLocalParameter("K_a");
    	activationBindingEqulibrium.setName("K_a");
    	activationBindingEqulibrium.setValue(1.0);
    	LocalParameter inducibleBindingEquilibrium = inducibleActivatableExpressionLaw.createLocalParameter("K_i");
    	inducibleBindingEquilibrium.setName("K_i");
    	inducibleBindingEquilibrium.setValue(1.0);
    	LocalParameter coop = inducibleActivatableExpressionLaw.createLocalParameter("n");
		coop.setName("n");
		coop.setValue(cooperativity);
    	inducibleActivatableExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
    			+ "*" + activationBindingEqulibrium.getId() + "*" + activator.getId() + "^" + coop + "*" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() 
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + ")"
    			+ "/(1+"+ activationBindingEqulibrium.getId() + "*" + activator.getId() + "^" + coop + "*" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId()
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + "))"));
    	return inducibleActivatableExpression;
    }
    
    private static Reaction createRatelessInducibleActivatableExpressionReaction(Species inducer, Species activator, Species expressed, Model mod) {
    	Reaction inducibleActivatableExpression = createRatelessActivatableExpressionReaction(activator, expressed, mod);
    	ModifierSpeciesReference modifier = inducibleActivatableExpression.createModifier(inducer);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return inducibleActivatableExpression;
    }
	
	/*
     * Methods for parsing and compiling mathematical formulas
     */
    
    //Parse a string fomula to make an ASTNode for a KineticLaw object
    //String syntax at: http://sbml.org/Special/Software/JSBML/latest-stable/build/apidocs/org/sbml/jsbml/text/parser/FormulaParserLL3.html
    private static ASTNode parseFormula(String formula) {
        ASTNode tree = null;
        try {
            IFormulaParser parser = new FormulaParserLL3(new StringReader(""));
            tree = ASTNode.parseFormula(formula, parser);
        } catch (ParseException e) {
            return null;
        }
        return tree;
    }
    
    private static String compileFormula(ASTNode tree) {
    	String formula = null;
        try {
        	FormulaCompiler compiler = new FormulaCompilerLibSBML();
            formula = ASTNode.formulaToString(tree, compiler);
        } catch (SBMLException e) {
            return null;
        } 
        return formula;
    }
    
    /*
     * Methods for getting SBML elements annotated with SBO terms
     */
	
    private static Set<Species> getInputs(Model mod) {
		Set<Species> inputs = new HashSet<Species>();
		for (Species spec : mod.getListOfSpecies()) {
			if (spec.isSetSBOTerm() && spec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
				inputs.add(spec);
			}
		}
		return inputs;
	}
	
	private static Set<Species> getOutputs(Model mod) {
		Set<Species> outputs = new HashSet<Species>();
		for (Species spec : mod.getListOfSpecies()) {
			if (spec.isSetSBOTerm() && spec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
				outputs.add(spec);
			}
		}
		return outputs;
	}
	
	public enum SBOTerm {
    	EXPRESSION("SBO:0000589"),
    	DEGRADATION("SBO:0000179"),
    	INPUT("SBO:0000600"),
    	OUTPUT("SBO:0000601"),
    	ACTIVATOR("SBO:0000459"),
    	REPRESSOR("SBO:0000020");
    	
    	private final String id;
    	
    	SBOTerm(String id) {
    		this.id = id;
    	}
    	
    	public String getID() {
    		return id;
    	}
    }
	
//  //Run simulation on modules with k-values to predict best matches in next unassigned stage
//  public static List<Module> runSimulations (List<Module> modules) {
//      return null;
//  }
    
//    /*
//    * METHODS FOR SBML MODEL CREATION
//    */    
//    
//    //Make SBML Documents, which contain models, based upon each type of module
//    //The modules in the tree will be passed in, not assigned modules
//    public static void makeSBMLDocument(Module m) {
//     
//        for (AssignedModule aM : m.getAssignedModules()) {
//            if (aM.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL) || aM.getRole().equals(ModuleRole.EXPRESSOR)) {
//                makeEXPDEGModel(aM);
//            } else if (aM.getRole().equals(ModuleRole.EXPRESSEE) || aM.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
//                makeRegulationModel(aM);
//            } else if (aM.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
//                
//                //Find expressors and expressees
//                ArrayList<Module> expressors = new ArrayList();
//                ArrayList<Module> expressees = new ArrayList();
//                for (Module child : m.getChildren()) {
//                    if (child.getRole().equals(ModuleRole.EXPRESSEE) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
//                        expressees.add(child);
//                    } else if (child.getRole().equals(ModuleRole.EXPRESSOR)) {
//                        expressors.add(child);
//                    }
//                }
//                
//                //Make a model for all combinatrial expressors and expressees
//                for (Module expressee : expressees) {
//                    for (Module expressor : expressors) {
//                        makeTUModel(aM, expressee.getAssignedModules(), expressor.getAssignedModules());
//                    }
//                }                
//            } else if (aM.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
//                
//                //Find expressors and expressees
//                ArrayList<Module> TUs = new ArrayList();
//                for (Module child : m.getChildren()) {
//                    if (child.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
//                        TUs.add(child);
//                    } 
//                }
//             
//                //Make a model for all combinatrial expressors and expressees
//                for (Module TU : TUs) {
//                    makeSpecificationModel(aM, TU.getAssignedModules());
//                }
//            }
//        }
//    }    
//    
//    //Makes SBML Model for single expression cassettes
//    private static void makeEXPDEGModel (Module m) {
//        
//        //Search the module to find the name of the species being expressed i.e. REGULATORS or FPs
//        String productID = "";
//        for (PrimitiveModule pm : m.getSubmodules()) {
//            if (pm.isForward()) {
//                if (pm.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION) || pm.getPrimitiveRole().equals(FeatureRole.CDS_LINKER) || pm.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSOR)) {
//                    if (productID.isEmpty()) {
//                        productID = pm.getModuleFeature().getName().replaceAll(".ref", "");
//                    } else {
//                        productID = productID + "_" + pm.getModuleFeatures().get(0).getName().replaceAll(".ref", "");
//                    }
//                }
//            }
//        }
//        
//        //Create new model reactions for protein expression        
//        Reaction proteinExpression = new Reaction(m.getName() + "_pEXP");
//        Species product = new Species(productID);                                               
//        SpeciesReference productSpecRef = proteinExpression.createProduct(product);
//        
//        //Expression kinetic law
//        KineticLaw expressionKL = new KineticLaw(proteinExpression);
//        LocalParameter k0 = new LocalParameter("k0_" + m.getName());
//        k0.setValue(1.0);
//        expressionKL.addLocalParameter(k0);
//        expressionKL.setMath(parseFormula(k0.getId()));
//        
//        //Create new model reactions for protein degradation
//        Reaction proteinDegradation = new Reaction(m.getName() + "_pDEG");          
//        proteinDegradation.addReactant(productSpecRef);
//        
//        //Degradation kinetic law
//        KineticLaw degradationKL = new KineticLaw(proteinDegradation);
//        LocalParameter gamma = new LocalParameter("gamma");
//        gamma.setValue(1.0);
//        LocalParameter kD = new LocalParameter("kD_" + productSpecRef.getId());
//        kD.setValue(1.0);
//        degradationKL.addLocalParameter(gamma);
//        degradationKL.addLocalParameter(kD);
//        degradationKL.setMath(parseFormula(gamma.getId() + "*" + productSpecRef.getId() + "/" + kD.getId() + "/(1+" + productSpecRef.getId() + "/" + kD.getId() + ")"));
//        
//        Model model = new Model();
//        model.addReaction(proteinExpression);
//        model.addReaction(proteinDegradation);
//        
//        SBMLDocument sbmlDoc = new SBMLDocument();
//        sbmlDoc.setModel(model);
//        m.setSBMLDocument(sbmlDoc);
//    }
//    
//    //Makes SBML Model for regulation controls
//    private static void makeRegulationModel (AssignedModule expressee) {
//        
//        //Make the regular model for the EXPRESSEE
//        makeEXPDEGModel(expressee);
//        
//        HashSet<Module> regulationControls = new HashSet();
//        for (Module cM : expressee.getControlModules()) {
//            if (cM.getRole().equals(ModuleRole.REGULATION_CONTROL)) {
//                regulationControls.add(cM);
//            }
//        }
//        
//        //Get reactions from the EXPREESSE
//        Reaction expresseeExpression = expressee.getSBMLDocument().getModel().getReaction(expressee.getName() + "_pEXP");
//        Reaction expresseeDegradation = expressee.getSBMLDocument().getModel().getReaction(expressee.getName() + "_pDEG");
//        
//        //Create expression and degradation reactions for both the regulation control
//        for (Module rCM : regulationControls) {
//            makeEXPDEGModel(rCM);
//            Reaction rCMExpression = rCM.getSBMLDocument().getModel().getReaction(rCM.getName() + "_pEXP");
//            
//            KineticLaw rCMExpressionKL = rCMExpression.getKineticLaw();
//            LocalParameter kL = new LocalParameter("kL_" + expressee.getName());
//            kL.setValue(1.0);
//            rCMExpressionKL.addLocalParameter(kL);
//            LocalParameter k0 = rCMExpressionKL.getLocalParameter("k0_" + rCM.getName());
//            
//            SpeciesReference productSpecRef = expresseeExpression.getProduct(expressee.getName() + "_pEXP");
//            rCMExpression.addModifier(new ModifierSpeciesReference(productSpecRef.getSpeciesInstance()));
//            rCMExpressionKL.setMath(parseFormula(k0.getId() + "/(1+" + kL.getId() + "*" + productSpecRef.getId() + ")"));
//            
//            Model model = rCM.getSBMLDocument().getModel();
//            model.addReaction(expresseeExpression);
//            model.addReaction(expresseeDegradation);
//        }
//    }
//    
//    //Makes combinatorial SBML Models for Transcriptional Units
//    private static void makeTUModel (Module TU, List<AssignedModule> expressees, List<AssignedModule> expressors) {
//
//        //Search combinatorial space of arcs for valid expressee and expressor pairs
//        
//        //Grab the expression reaction from the expressor and degradation reactions from the expressees        
//        
//        Model model = new Model();
//        
//        SBMLDocument sbmlDoc = new SBMLDocument();
//        sbmlDoc.setModel(model);
//        TU.setSBMLDocument(sbmlDoc);
//    }
//    
//    //Makes combinatorial SBML Models for Specification models
//    private static void makeSpecificationModel (Module specified, List<AssignedModule> TUs) {
// 
//        //Search combinatorial space of arcs for valid TUs
//        
//        //Use expression and degradation reactions from TUs, link together and add regulation modification to to reactions based upon arcs
//        
//        Model model = new Model();
//        
//        SBMLDocument sbmlDoc = new SBMLDocument();
//        sbmlDoc.setModel(model);
//        specified.setSBMLDocument(sbmlDoc);
//    }

    
}
