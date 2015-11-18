/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.StringReader;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.HashSet;

//import org.cidarlab.phoenix.core.dom.AssignedModule;
//import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
//import org.cidarlab.phoenix.core.dom.Module;
//import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
//import org.cidarlab.phoenix.core.dom.PrimitiveModule;
//import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.text.parser.FormulaParserLL3;
import org.sbml.jsbml.text.parser.IFormulaParser;
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
     * Methods for SBML model creation
     */  
    
    private static SBMLDocument createDegradationModel(String degradedID, String degradedName) {
    	SBMLDocument degradationDoc = new SBMLDocument(3, 1);
    	Model degradationModel = degradationDoc.createModel();
    	Compartment cell = degradationModel.createCompartment("cell");
    	cell.setName("cell");
    	cell.setConstant(false);
    	Species degraded = createSpecies(degradedID, degradedName, degradationModel);
    	createDegradationReaction(degraded, degradationModel);
    	return degradationDoc;
    }
    
    private static SBMLDocument createExpressionModel(String expressedID, String expressedName) {
    	SBMLDocument expressionDoc = createDegradationModel(expressedID, expressedName);
    	createExpressionReaction(expressionDoc.getModel().getSpecies(expressedID), expressionDoc.getModel());
    	return expressionDoc;
    }
    
    private static SBMLDocument createRepressionModel(String expressedID, String expressedName, String repressorID, String repressorName) {
    	SBMLDocument repressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, repressionDoc.getModel());
    	createRepressibleExpressionReaction(repressionDoc.getModel().getSpecies(expressedID), repressor, repressionDoc.getModel());
    	return repressionDoc;
    }
    
    private static SBMLDocument createActivationModel(String expressedID, String expressedName, String activatorID, String activatorName) {
    	SBMLDocument activationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, activationDoc.getModel());
    	createActivatableExpressionReaction(activationDoc.getModel().getSpecies(expressedID), activator, activationDoc.getModel());
    	return activationDoc;
    }
    
    private static SBMLDocument createInductionRepressionModel(String expressedID, String expressedName, String repressorID, String repressorName, 
    		String inducerID, String inducerName) {
    	SBMLDocument inductionRepressionDoc = createDegradationModel(expressedID, expressedName);
    	Species repressor = createSpecies(repressorID, repressorName, inductionRepressionDoc.getModel());
    	Species inducer = createSpecies(inducerID, inducerName, inductionRepressionDoc.getModel());
    	createInducibleRepressibleExpressionReaction(inductionRepressionDoc.getModel().getSpecies(expressedID), repressor, inducer,
    			inductionRepressionDoc.getModel());
    	return inductionRepressionDoc;
    }
    
    private static SBMLDocument createInductionActivationModel(String expressedID, String expressedName, String activatorID, String activatorName,
    		String inducerID, String inducerName) {
    	SBMLDocument inductionActivationDoc = createDegradationModel(expressedID, expressedName);
    	Species activator = createSpecies(activatorID, activatorName, inductionActivationDoc.getModel());
    	Species inducer = createSpecies(inducerID, inducerName, inductionActivationDoc.getModel());
    	createInducibleActivatableExpressionReaction(inductionActivationDoc.getModel().getSpecies(expressedID), activator, inducer, 
    			inductionActivationDoc.getModel());
    	return inductionActivationDoc;
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
    	return createExpressionReaction(expressed, parentModel, false);
    }
    
    private static Reaction createExpressionReaction(Species expressed, Model parentModel, boolean isRegulated) {
    	Reaction expression = parentModel.createReaction(expressed.getId() + "_expression");
    	expression.setName(expressed.getName() + "_expression");
    	expression.setFast(false);
    	expression.setReversible(false);
    	SpeciesReference product = expression.createProduct(expressed);
    	product.setStoichiometry(1.0);
    	product.setConstant(true);
    	if (!isRegulated) {
    		KineticLaw expressionLaw = expression.createKineticLaw();
    		LocalParameter expressionRate = expressionLaw.createLocalParameter("k_EXE");
    		expressionRate.setName("k_EXE");
    		expressionRate.setValue(1.0);
    		expressionLaw.setMath(parseFormula(expressionRate.getId()));
    	}
    	return expression;
    }
    
    private static Reaction createRepressibleExpressionReaction(Species expressed, Species repressor, Model parentModel) {
    	return createRepressibleExpressionReaction(expressed, repressor, parentModel, false);
    }
    
    private static Reaction createRepressibleExpressionReaction(Species expressed, Species repressor, Model parentModel, boolean isInducible) {
    	Reaction repressibleExpression = createExpressionReaction(expressed, parentModel, true);
    	repressibleExpression.createModifier(repressor);
    	if (!isInducible) {
    		KineticLaw repressibleExpressionLaw = repressibleExpression.createKineticLaw();
    		LocalParameter maxExpressionRate = repressibleExpressionLaw.createLocalParameter("k_EXR");
    		maxExpressionRate.setName("k_EXR");
    		maxExpressionRate.setValue(1.0);
    		LocalParameter repressionBindingEquilibrium = repressibleExpressionLaw.createLocalParameter("K_r");
    		repressionBindingEquilibrium.setName("K_r");
    		repressionBindingEquilibrium.setValue(1.0);
    		repressibleExpressionLaw.setMath(parseFormula(maxExpressionRate.getId() 
    				+ "/(1+"+ repressionBindingEquilibrium.getId() + "*" + repressor.getId() + ")"));
    	}
    	return repressibleExpression;
    }
    
    private static Reaction createActivatableExpressionReaction(Species expressed, Species activator, Model parentModel) {
    	return createActivatableExpressionReaction(expressed, activator, parentModel, false);
    }
    
    private static Reaction createActivatableExpressionReaction(Species expressed, Species activator, Model parentModel, boolean isInducible) {
    	Reaction activatableExpression = createExpressionReaction(expressed, parentModel, true);
    	activatableExpression.createModifier(activator);
    	if (!isInducible) {
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
    	}
    	return activatableExpression;
    }
    
    private static Reaction createInducibleRepressibleExpressionReaction(Species expressed, Species repressor, Species inducer, Model parentModel) {
    	Reaction inducibleRepressibleExpression = createRepressibleExpressionReaction(expressed, repressor, parentModel, true);
    	inducibleRepressibleExpression.createModifier(inducer);
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
    
    private static Reaction createInducibleActivatableExpressionReaction(Species expressed, Species activator, Species inducer, Model parentModel) {
    	Reaction inducibleActivatableExpression = createActivatableExpressionReaction(expressed, activator, parentModel, true);
    	inducibleActivatableExpression.createModifier(inducer);
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
    
    //Parse a string fomula to make an ASTNode for a KineticLaw object
    //String syntax at: http://sbml.org/Special/Software/JSBML/latest-stable/build/apidocs/org/sbml/jsbml/text/parser/FormulaParserLL3.html
    private static ASTNode parseFormula(String formula) {
        ASTNode mathFormula = null;
        try {
            IFormulaParser parser = new FormulaParserLL3(new StringReader(""));
            mathFormula = ASTNode.parseFormula(formula, parser);
        } catch (ParseException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return mathFormula;
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
