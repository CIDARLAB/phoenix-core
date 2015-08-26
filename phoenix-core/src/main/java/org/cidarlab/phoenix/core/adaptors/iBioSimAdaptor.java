/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
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
 */
public class iBioSimAdaptor {
    
    //Run simulation on modules with k-values to predict best matches in next unassigned stage
    public static List<Module> runSimulations (List<Module> modules) {
        return null;
    }
    
    /*
    * METHODS FOR SBML MODEL CREATION
    */    
    
    //Make SBML Documents, which contain models, based upon each type of module
    //The modules in the tree will be passed in, not assigned modules
    public static void makeSBMLDocument(Module m) {
     
        for (AssignedModule aM : m.getAssignedModules()) {
            if (aM.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL) || aM.getRole().equals(ModuleRole.EXPRESSOR)) {
                makeEXPDEGModel(aM);
            } else if (aM.getRole().equals(ModuleRole.EXPRESSEE) || aM.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || aM.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                makeRegulationModel(aM);
            } else if (aM.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                
                //Find expressors and expressees
                ArrayList<Module> expressors = new ArrayList();
                ArrayList<Module> expressees = new ArrayList();
                for (Module child : m.getChildren()) {
                    if (child.getRole().equals(ModuleRole.EXPRESSEE) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                        expressees.add(child);
                    } else if (child.getRole().equals(ModuleRole.EXPRESSOR)) {
                        expressors.add(child);
                    }
                }
                
                //Make a model for all combinatrial expressors and expressees
                for (Module expressee : expressees) {
                    for (Module expressor : expressors) {
                        makeTUModel(aM, expressee.getAssignedModules(), expressor.getAssignedModules());
                    }
                }                
            } else if (aM.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
                
                //Find expressors and expressees
                ArrayList<Module> TUs = new ArrayList();
                for (Module child : m.getChildren()) {
                    if (child.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                        TUs.add(child);
                    } 
                }
             
                //Make a model for all combinatrial expressors and expressees
                for (Module TU : TUs) {
                    makeSpecificationModel(aM, TU.getAssignedModules());
                }
            }
        }
    }    
    
    //Makes SBML Model for single expression cassettes
    private static void makeEXPDEGModel (Module m) {
        
        //Search the module to find the name of the species being expressed i.e. REGULATORS or FPs
        String productID = "";
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.isForward()) {
                if (pm.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION) || pm.getPrimitiveRole().equals(FeatureRole.CDS_LINKER) || pm.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR) || pm.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSOR)) {
                    if (productID.isEmpty()) {
                        productID = pm.getModuleFeature().getName().replaceAll(".ref", "");
                    } else {
                        productID = productID + "_" + pm.getModuleFeatures().get(0).getName().replaceAll(".ref", "");
                    }
                }
            }
        }
        
        //Create new model reactions for protein expression        
        Reaction proteinExpression = new Reaction(m.getName() + "_pEXP");
        Species product = new Species(productID);                                               
        SpeciesReference productSpecRef = proteinExpression.createProduct(product);
        
        //Expression kinetic law
        KineticLaw expressionKL = new KineticLaw(proteinExpression);
        LocalParameter k0 = new LocalParameter("k0_" + m.getName());
        k0.setValue(1.0);
        expressionKL.addLocalParameter(k0);
        expressionKL.setMath(parseFormula(k0.getId()));
        
        //Create new model reactions for protein degradation
        Reaction proteinDegradation = new Reaction(m.getName() + "_pDEG");          
        proteinDegradation.addReactant(productSpecRef);
        
        //Degradation kinetic law
        KineticLaw degradationKL = new KineticLaw(proteinDegradation);
        LocalParameter gamma = new LocalParameter("gamma");
        gamma.setValue(1.0);
        LocalParameter kD = new LocalParameter("kD_" + productSpecRef.getId());
        kD.setValue(1.0);
        degradationKL.addLocalParameter(gamma);
        degradationKL.addLocalParameter(kD);
        degradationKL.setMath(parseFormula("(" + gamma.getId() + "*" + productSpecRef.getId() + "/" + kD.getId() + ")/(1+(" + productSpecRef.getId() + "/" + kD.getId() + "))"));
        
        Model model = new Model();
        model.addReaction(proteinExpression);
        model.addReaction(proteinDegradation);
        
        SBMLDocument sbmlDoc = new SBMLDocument();
        sbmlDoc.setModel(model);
        m.setSBMLDocument(sbmlDoc);
    }
    
    //Makes SBML Model for regulation controls
    private static void makeRegulationModel (AssignedModule expressee) {
        
        //Make the regular model for the EXPRESSEE
        makeEXPDEGModel(expressee);
        
        HashSet<Module> regulationControls = new HashSet();
        for (Module cM : expressee.getControlModules()) {
            if (cM.getRole().equals(ModuleRole.REGULATION_CONTROL)) {
                regulationControls.add(cM);
            }
        }
        
        //Get reactions from the EXPREESSE
        Reaction expresseeExpression = expressee.getSBMLDocument().getModel().getReaction(expressee.getName() + "_pEXP");
        Reaction expresseeDegradation = expressee.getSBMLDocument().getModel().getReaction(expressee.getName() + "_pDEG");
        
        //Create expression and degradation reactions for both the regulation control
        for (Module rCM : regulationControls) {
            makeEXPDEGModel(rCM);
            Reaction rCMExpression = rCM.getSBMLDocument().getModel().getReaction(rCM.getName() + "_pEXP");
            
            KineticLaw rCMExpressionKL = rCMExpression.getKineticLaw();
            LocalParameter kL = new LocalParameter("kL_" + expressee.getName());
            kL.setValue(1.0);
            rCMExpressionKL.addLocalParameter(kL);
            LocalParameter k0 = rCMExpressionKL.getLocalParameter("k0_" + rCM.getName());
            
            SpeciesReference productSpecRef = expresseeExpression.getProduct(expressee.getName() + "_pEXP");
            rCMExpression.addModifier(new ModifierSpeciesReference(productSpecRef.getSpeciesInstance()));
            rCMExpressionKL.setMath(parseFormula(k0.getId() + "/(1+" + kL.getId() + "*" + productSpecRef.getId() + ")"));
            
            Model model = rCM.getSBMLDocument().getModel();
            model.addReaction(expresseeExpression);
            model.addReaction(expresseeDegradation);
        }
    }
    
    //Makes combinatorial SBML Models for Transcriptional Units
    private static void makeTUModel (Module TU, List<AssignedModule> expressees, List<AssignedModule> expressors) {

        //Search combinatorial space of arcs for valid expressee and expressor pairs
        
        //Grab the expression reaction from the expressor and degradation reactions from the expressees        
        
        Model model = new Model();
        
        SBMLDocument sbmlDoc = new SBMLDocument();
        sbmlDoc.setModel(model);
        TU.setSBMLDocument(sbmlDoc);
    }
    
    //Makes combinatorial SBML Models for Specification models
    private static void makeSpecificationModel (Module specified, List<AssignedModule> TUs) {
 
        //Search combinatorial space of arcs for valid TUs
        
        //Use expression and degradation reactions from TUs, link together and add regulation modification to to reactions based upon arcs
        
        Model model = new Model();
        
        SBMLDocument sbmlDoc = new SBMLDocument();
        sbmlDoc.setModel(model);
        specified.setSBMLDocument(sbmlDoc);
    }

    //Parse a string fomula to make an ASTNode for a KineticLaw object
    //String syntax at: http://sbml.org/Special/Software/JSBML/latest-stable/build/apidocs/org/sbml/jsbml/text/parser/FormulaParserLL3.html
    public static ASTNode parseFormula(String formula) {
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
}
