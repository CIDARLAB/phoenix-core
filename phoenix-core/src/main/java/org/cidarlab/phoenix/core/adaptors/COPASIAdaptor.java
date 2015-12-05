/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.text.parser.FormulaParserLL3;
import org.sbml.jsbml.text.parser.IFormulaParser;
import org.sbml.jsbml.util.compilers.FormulaCompiler;
import org.sbml.jsbml.util.compilers.FormulaCompilerLibSBML;
import org.sbml.jsbml.text.parser.ParseException;

/**
 *
 * @author evanappleton
 * @author nicholasroehner
 */
public class COPASIAdaptor {
    
//    //Run simulation on modules with k-values to predict best matches in next unassigned stage
//    public static List<Module> runSimulations (List<Module> modules) {
//        return null;
//    }
	
	/*
     * Methods for SBML model composition
     */  
	
	private static void addFeedbackToExpressionModel(Model mod, HashMap<String, String> connections) {
		if (connections.isEmpty()) {
			connections.putAll(inferConnection(mod, mod));
		}
		Set<Reaction> expressions = getModelExpressions(mod);
		for (Reaction expression : expressions) {
			substituteReactionModifiers(connections, expression);
		}
	}
	
	private static SBMLDocument composeExpressionModels(Model mod1, Model mod2) {
		return composeExpressionModels(mod1, mod2, new HashMap<String, String>());
	}
	
	private static SBMLDocument composeExpressionModels(Model mod1, Model mod2, HashMap<String, String> connections) {
		Set<Species> inputs1 = getModelInputs(mod1);
		Set<Species> outputs1 = getModelOutputs(mod1);
		Set<Species> inputs2 = getModelInputs(mod2);
		Set<Species> outputs2 = getModelOutputs(mod2);
		Set<Reaction> degradations1 = getModelDegradations(mod1);
		Set<Reaction> expressions1 = getModelExpressions(mod1);
		Set<Reaction> degradations2 = getModelDegradations(mod2);
		Set<Reaction> expressions2 = getModelExpressions(mod2);
		if (connections.isEmpty()) {
			connections.putAll(inferConnection(mod1, mod2));
		}
		SBMLDocument composedDoc = createCompartmentModel("cell", "cell");
		Model composedMod = composedDoc.getModel();
		Compartment cell = composedMod.getCompartment(0);
		for (Species input1 : inputs1) {
			Species input = input1.clone();
			input.setCompartment(cell);
			composedMod.addSpecies(input);
		}
		for (Species output1 : outputs1) {
			Species output = output1.clone();
			if (connections.containsValue(output.getId())) {
				output.unsetSBOTerm();
			}
			output.setCompartment(cell);
			composedMod.addSpecies(output);
		}
		for (Species input2 : inputs2) {
			Species input = input2.clone();
			if (!connections.containsKey(input.getId())) {
				input.setCompartment(cell);
				composedMod.addSpecies(input);
			}
		}
		for (Species output2 : outputs2) {
			Species output = output2.clone();
			output.setCompartment(cell);
			composedMod.addSpecies(output);
		}
		for (Reaction degradation1 : degradations1) {
			composedMod.addReaction(degradation1.clone());
		}
		for (Reaction degradation2 : degradations2) {
			composedMod.addReaction(degradation2.clone());
		}
		for (Reaction expression1 : expressions1) {
			composedMod.addReaction(expression1.clone());
		}
		for (Reaction expression2 : expressions2) {
			Reaction expression = expression2.clone();
			substituteReactionModifiers(connections, expression);
			composedMod.addReaction(expression);
		}
		return composedDoc;
	}
	
	private static Set<Species> getModelInputs(Model mod) {
		Set<Species> inputs = new HashSet<Species>();
		for (Species spec : mod.getListOfSpecies()) {
			if (spec.isSetSBOTerm() && spec.getSBOTermID().equals(SBOTerm.INPUT.getID())) {
				inputs.add(spec);
			}
		}
		return inputs;
	}
	
	private static Set<Species> getModelOutputs(Model mod) {
		Set<Species> outputs = new HashSet<Species>();
		for (Species spec : mod.getListOfSpecies()) {
			if (spec.isSetSBOTerm() && spec.getSBOTermID().equals(SBOTerm.OUTPUT.getID())) {
				outputs.add(spec);
			}
		}
		return outputs;
	}
	
	private static Set<Reaction> getModelDegradations(Model mod) {
		Set<Reaction> degradations = new HashSet<Reaction>();
		for (Reaction rxn : mod.getListOfReactions()) {
			if (rxn.isSetSBOTerm() && rxn.getSBOTermID().equals(SBOTerm.DEGRADATION.getID())) {
				degradations.add(rxn);
			}
		}
		return degradations;
	}
	
	private static Set<Reaction> getModelExpressions(Model mod) {
		Set<Reaction> expressions = new HashSet<Reaction>();
		for (Reaction rxn : mod.getListOfReactions()) {
			if (rxn.isSetSBOTerm() && rxn.getSBOTermID().equals(SBOTerm.EXPRESSION.getID())) {
				expressions.add(rxn);
			}
		}
		return expressions;
	}
	
	private static void substituteReactionModifiers(HashMap<String, String> substitutions, Reaction rxn) {
		String formula = compileFormula(rxn.getKineticLaw().getMath());
		for (ModifierSpeciesReference modifier : rxn.getListOfModifiers()) {
			if (substitutions.containsKey(modifier.getSpecies())) {
				modifier.setSpecies(substitutions.get(modifier.getSpecies()));
				formula.replaceAll(modifier.getSpecies(), substitutions.get(modifier.getSpecies()));
			}
		}
		rxn.getKineticLaw().setMath(parseFormula(formula));
	}
	
	private static HashMap<String, String> inferConnection(Model mod1, Model mod2) {
		Set<Species> outputs = getModelInputs(mod1);
		Set<Species> inputs = getModelInputs(mod2);
		HashMap<String, String> connections = new HashMap<String, String>();
		if (connections.isEmpty() && !inputs.isEmpty() && !outputs.isEmpty()) {
			connections.put(inputs.iterator().next().getId(), outputs.iterator().next().getId());
		}
		return connections;
	}
    
    /*
     * Methods for SBML model creation
     */  
	
	private static SBMLDocument createCompartmentModel(String compartmentID) {
    	return createCompartmentModel(compartmentID, compartmentID);
	}
	
	private static SBMLDocument createCompartmentModel(String compartmentID, String compartmentName) {
		SBMLDocument compartmentDoc = new SBMLDocument(3, 1);
    	Model compartmentMod = compartmentDoc.createModel();
    	Compartment cell = compartmentMod.createCompartment(compartmentID);
    	cell.setName(compartmentName);
    	cell.setConstant(false);
    	return compartmentDoc;
	}
	
	private static SBMLDocument createDegradationModel(String degradedID) {
    	return createDegradationModel(degradedID, degradedID);
    }
    
    private static SBMLDocument createDegradationModel(String degradedID, String degradedName) {
    	SBMLDocument degradationDoc = createCompartmentModel("cell", "cell");
    	Species degraded = createSpecies(degradedID, degradedName, degradationDoc.getModel());
    	degraded.setSBOTerm(SBOTerm.OUTPUT.getID());
    	createDegradationReaction(degraded, degradationDoc.getModel());
    	return degradationDoc;
    }
    
    private static SBMLDocument createExpressionModel(String expressedID) {
    	return createExpressionModel(expressedID, expressedID);
    }
    
    private static SBMLDocument createExpressionModel(String expressedID, String expressedName) {
    	SBMLDocument expressionDoc = createDegradationModel(expressedID, expressedName);
    	createExpressionReaction(expressionDoc.getModel().getSpecies(expressedID), expressionDoc.getModel());
    	return expressionDoc;
    }
    
    private static SBMLDocument createRepressionModel(String repressorID, String expressedID) {
    	return createRepressionModel(repressorID, expressedID, repressorID, expressedID);
    }
    
    private static SBMLDocument createRepressionModel(String repressorID, String expressedID, String repressorName, String expressedName) {
    	SBMLDocument repressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, repressionDoc.getModel());
    	repressor.setSBOTerm(SBOTerm.INPUT.getID());
    	createRepressibleExpressionReaction(repressor, repressionDoc.getModel().getSpecies(expressedID), repressionDoc.getModel());
    	return repressionDoc;
    }
    
    private static SBMLDocument createActivationModel(String activatorID, String expressedID) {
    	return createActivationModel(activatorID, expressedID, activatorID, expressedID);
    }
    
    private static SBMLDocument createActivationModel(String activatorID, String expressedID, String activatorName, String expressedName) {
    	SBMLDocument activationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, activationDoc.getModel());
    	activator.setSBOTerm(SBOTerm.INPUT.getID());
    	createActivatableExpressionReaction(activator, activationDoc.getModel().getSpecies(expressedID), activationDoc.getModel());
    	return activationDoc;
    }
    
    private static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID) {
    	return createInductionRepressionModel(expressedID, repressorID, inducerID, expressedID, repressorID, inducerID);
    }
    
    private static SBMLDocument createInductionRepressionModel(String inducerID, String repressorID, String expressedID,   
    		String inducerName, String repressorName, String expressedName) {
    	SBMLDocument inductionRepressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, inductionRepressionDoc.getModel());
    	repressor.setSBOTerm(SBOTerm.INPUT.getID());
    	Species inducer = createSpecies(inducerID, inducerName, inductionRepressionDoc.getModel());
    	inducer.setSBOTerm(SBOTerm.INPUT.getID());
    	createInducibleRepressibleExpressionReaction(inducer, repressor, inductionRepressionDoc.getModel().getSpecies(expressedID),  
    			inductionRepressionDoc.getModel());
    	return inductionRepressionDoc;
    }
    
    private static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID) {
    	return createInductionActivationModel(inducerID, activatorID, expressedID, inducerID, activatorID, expressedID);
    }
    
    private static SBMLDocument createInductionActivationModel(String inducerID, String activatorID, String expressedID, 
    		String inducerName, String activatorName, String expressedName) {
    	SBMLDocument inductionActivationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, inductionActivationDoc.getModel());
    	activator.setSBOTerm(SBOTerm.INPUT.getID());
    	Species inducer = createSpecies(inducerID, inducerName, inductionActivationDoc.getModel());
    	inducer.setSBOTerm(SBOTerm.INPUT.getID());
    	createInducibleActivatableExpressionReaction(inducer, activator, inductionActivationDoc.getModel().getSpecies(expressedID),   
    			inductionActivationDoc.getModel());
    	return inductionActivationDoc;
    }
    
    private static Species createSpecies(String speciesID,  Model parentModel) {
    	return createSpecies(speciesID, speciesID, parentModel);
    }
    
    private static Species createSpecies(String speciesID, String speciesName, Model parentModel) {
    	Species s;
    	if (parentModel.getNumCompartments() > 0) {
    		s = parentModel.createSpecies(speciesID, parentModel.getCompartment(0));
    	} else {
    		s = parentModel.createSpecies(speciesID);
    	}
    	s.setName(speciesName);
    	s.setHasOnlySubstanceUnits(true);
    	s.setBoundaryCondition(false);
    	s.setConstant(false);
    	return s;
    }
    
    private static Reaction createDegradationReaction(Species degraded, Model parentModel) {
    	Reaction degradation = parentModel.createReaction(degraded.getId() + "_degradation");
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
    
    private static Reaction createExpressionReaction(Species expressed, Model parentModel) {
    	Reaction expression = createQualitativeExpressionReaction(expressed, parentModel);
    	KineticLaw expressionLaw = expression.createKineticLaw();
		LocalParameter expressionRate = expressionLaw.createLocalParameter("k_EXE");
		expressionRate.setName("k_EXE");
		expressionRate.setValue(1.0);
		expressionLaw.setMath(parseFormula(expressionRate.getId()));
    	return expression;
    }
    
    private static Reaction createQualitativeExpressionReaction(Species expressed, Model parentModel) {
    	Reaction expression = parentModel.createReaction(expressed.getId() + "_expression");
    	expression.setName(expressed.getName() + "_expression");
    	expression.setFast(false);
    	expression.setReversible(false);
    	expression.setSBOTerm(SBOTerm.EXPRESSION.getID());
    	SpeciesReference product = expression.createProduct(expressed);
    	product.setStoichiometry(1.0);
    	product.setConstant(true);
    	return expression;
    }
    
    private static Reaction createRepressibleExpressionReaction(Species repressor, Species expressed, Model parentModel) {
    	Reaction repressibleExpression = createQualitativeRepressibleExpressionReaction(repressor, expressed, parentModel);
    	KineticLaw repressibleExpressionLaw = repressibleExpression.createKineticLaw();
		LocalParameter maxExpressionRate = repressibleExpressionLaw.createLocalParameter("k_EXR");
		maxExpressionRate.setName("k_EXR");
		maxExpressionRate.setValue(1.0);
		LocalParameter repressionBindingEquilibrium = repressibleExpressionLaw.createLocalParameter("K_r");
		repressionBindingEquilibrium.setName("K_r");
		repressionBindingEquilibrium.setValue(1.0);
		repressibleExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
				+ "/(1+"+ repressionBindingEquilibrium.getId() + "*" + repressor.getId() + ")"));
    	return repressibleExpression;
    }
    
    private static Reaction createQualitativeRepressibleExpressionReaction(Species repressor, Species expressed, Model parentModel) {
    	Reaction repressibleExpression = createQualitativeExpressionReaction(expressed, parentModel);
    	ModifierSpeciesReference modifier = repressibleExpression.createModifier(repressor);
    	modifier.setSBOTerm(SBOTerm.REPRESSOR.getID());
    	return repressibleExpression;
    }
    
    private static Reaction createActivatableExpressionReaction(Species activator, Species expressed, Model parentModel) {
    	Reaction activatableExpression = createQualitativeActivatableExpressionReaction(activator, expressed, parentModel);
    	KineticLaw activatableExpressionLaw = activatableExpression.createKineticLaw();
		LocalParameter maxExpressionRate = activatableExpressionLaw.createLocalParameter("k_EXA");
		maxExpressionRate.setName("k_EXA");
		maxExpressionRate.setValue(1.0);
		LocalParameter activationBindingEquilibrium = activatableExpressionLaw.createLocalParameter("K_a");
		activationBindingEquilibrium.setName("K_a");
		activationBindingEquilibrium.setValue(1.0);
		activatableExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
				+ "*" + activationBindingEquilibrium.getId() + "*" + activator.getId()
				+  "/(1+"+ activationBindingEquilibrium.getId() + "*" + activator.getId() + ")"));
    	return activatableExpression;
    }
    
    private static Reaction createQualitativeActivatableExpressionReaction(Species activator, Species expressed, Model parentModel) {
    	Reaction activatableExpression = createQualitativeExpressionReaction(expressed, parentModel);
    	ModifierSpeciesReference modifier = activatableExpression.createModifier(activator);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return activatableExpression;
    }
    
    private static Reaction createInducibleRepressibleExpressionReaction(Species inducer, Species repressor, Species expressed, Model parentModel) {
    	Reaction inducibleRepressibleExpression = createQualitativeInducibleRepressibleExpressionReaction(inducer, repressor, expressed, parentModel);
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
    	inducibleRepressibleExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
    			+ "/(1+"+ repressionBindingEqulibrium.getId() + "*" + repressor.getId() 
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + "))"));
    	return inducibleRepressibleExpression;
    }
    
    private static Reaction createQualitativeInducibleRepressibleExpressionReaction(Species inducer, Species repressor, Species expressed, Model parentModel) {
    	Reaction inducibleRepressibleExpression = createQualitativeRepressibleExpressionReaction(repressor, expressed, parentModel);
    	ModifierSpeciesReference modifier = inducibleRepressibleExpression.createModifier(inducer);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return inducibleRepressibleExpression;
    }
    
    private static Reaction createInducibleActivatableExpressionReaction(Species inducer, Species activator, Species expressed, Model parentModel) {
    	Reaction inducibleActivatableExpression = createQualitativeInducibleActivatableExpressionReaction(inducer, activator, expressed, parentModel);
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
    	inducibleActivatableExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
    			+ "*" + activationBindingEqulibrium.getId() + "*" + activator.getId() + "*" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() 
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + ")"
    			+ "/(1+"+ activationBindingEqulibrium.getId() + "*" + activator.getId() + "*" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId()
    			+ "/(1+" + inducibleBindingEquilibrium.getId() + "*" + inducer.getId() + "))"));
    	return inducibleActivatableExpression;
    }
    
    private static Reaction createQualitativeInducibleActivatableExpressionReaction(Species inducer, Species activator, Species expressed, Model parentModel) {
    	Reaction inducibleActivatableExpression = createQualitativeActivatableExpressionReaction(activator, expressed, parentModel);
    	ModifierSpeciesReference modifier = inducibleActivatableExpression.createModifier(inducer);
    	modifier.setSBOTerm(SBOTerm.ACTIVATOR.getID());
    	return inducibleActivatableExpression;
    }
    
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
