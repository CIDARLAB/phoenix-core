/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.EugeneAdaptor;
import org.cidarlab.phoenix.core.dom.Component.Orientation;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
//import org.cidarlab.phoenix.core.dom.Orientation;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Symbol;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;

/**
 * This class defines the grammar with which genetic regulatory networks are
 * decomposed It inputs genetic regulatory networks and returns a feature graph
 *
 * @author evanappleton
 */
public class PhoenixGrammar {
    
    /*---------------
     * 
     * DECOMPOSITION METHODS
     * 
     *---------------*/
    
    //Helper method to loop through a list of structures that need to be decomposed
    public static void decomposeAll (List<Module> rootModules) {
        for (Module structure : rootModules) {
            PhoenixGrammar.decompose(structure);
        }
    }
    
    //Method for decomposing one module into its structural terminals
    public static void decompose(Module node) {

        int stack = 0;
        List<PrimitiveModule> submoduleStack = null;
        Module child = null;

        //Parse Module type HIGHER_FUNCTION to get TRANSCRIPTIONAL_UNITs
        if (node.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {

            //If this call is for the root node, pre-process
            if (node.isRoot()) {

                Module forward = forwardModulePreProcessing(node);
                forward.getParents().add(node);
                decompose(forward);
                Module reverse = reverseModulePreProcessing(node);
                reverse.getParents().add(node);
                decompose(reverse);

                node.getChildren().add(forward);
                node.getChildren().add(reverse);

            } else {
                
                //Check for TUs
                int TUCount = 0;
                
                for (PrimitiveModule subnodes : node.getSubmodules()) {
                    
                    if (stack == 0) {
                        
                        //Promoters start the stack
                        FeatureRole pR = subnodes.getPrimitiveRole();
                        if (pR.equals(FeatureRole.PROMOTER) || pR.equals(FeatureRole.PROMOTER_CONSTITUTIVE) || pR.equals(FeatureRole.PROMOTER_INDUCIBLE) || pR.equals(FeatureRole.PROMOTER_REPRESSIBLE)) {
                            
                            stack = 1;
                            submoduleStack = new ArrayList<>();
                            
                            //Create new Module to be made for each TRANSCRIPTIONAL_UNIT
                            child = new Module(node.getName() + "_" + ModuleRole.TRANSCRIPTIONAL_UNIT.toString() + "_" + TUCount);
                            TUCount++;
                            child.setStage(node.getStage() + 1);  //Set the Stage of the Child Node
                            child.setRoot(false);                 //Wont be the root.     
                            child.setForward(true);               //These are all Forward oriented. 
                            child.setRole(ModuleRole.TRANSCRIPTIONAL_UNIT);         //Set Child as a TU
                            submoduleStack.add(subnodes.clone());
                        }

                    } else if (stack == 1) {

                        submoduleStack.add(subnodes.clone());
                        
                        //Termintors pop the stack
                        if (subnodes.getPrimitiveRole().equals(FeatureRole.TERMINATOR)) {
                            stack = 0;
                            child.setSubmodules(submoduleStack);
                            decompose(child);
                            node.getChildren().add(child);
                            child.getParents().add(node);
                        }
                    }
                }
            }
        
        //Parse Module type TRANSCRIPTIONAL_UNIT to get EXPRESSEEs and EXPRESSORs
        } else if (node.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            
            //Initialize EXPRESSEEs and EXPRESSOR
            List<Module> expresseeList = new ArrayList<>();
            
            Module expressor = new Module(node.getName() + "_" + ModuleRole.EXPRESSOR.toString());
            expressor.setStage(node.getStage() + 1);
            expressor.setRole(ModuleRole.EXPRESSOR);
            expressor.setRoot(false);
            
            submoduleStack = new ArrayList<>();
            
            for (PrimitiveModule primitive : node.getSubmodules()) {
                
                //If we run into a CDS, we know that this will be an EXPRESSEE and replace this spot with an TESTING SLOT IN EXPRESSOR
                FeatureRole pR = primitive.getPrimitiveRole();
                if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                                                           
                    Module expressee = new Module(node.getName() + "_" + ModuleRole.EXPRESSEE.toString());
                    
                    //Create a new EXPRESSEE from this CDS primitive and copy the feature
                    expressee = getExpresseeModule(primitive, expressee);
                    expressee.setStage(node.getStage() + 1);
                    expresseeList.add(expressee);

                    //Add a new TESTING primitve to the expressor composition
                    //This piece will get replaced in the next step (adding testing pieces)
                    PrimitiveModule testing = new PrimitiveModule(FeatureRole.TESTING,primitive.getPrimitive(),primitive.getModuleFeature());
                    submoduleStack.add(testing);
                    
                //Else, continue adding features to EXPRESSOR    
                } else {
                    
                    submoduleStack.add(primitive);
                }
            }
            
            //Finalize EXPRESSOR primitives and links
            expressor.setSubmodules(submoduleStack);

            expressor.getParents().add(node);
            node.getChildren().add(expressor);
            
            //Finalize EXPRESSEE links
            for (Module exp : expresseeList) {
                exp.getParents().add(node);
                node.getChildren().add(exp);
            }
        }       
    }

    //Pre-processing steps for the globally forward version of the structure
    private static Module forwardModulePreProcessing(Module node) {
        
        Module forwardModule = new Module(node.getName() + "_F");
        forwardModule.setRoot(false);
        forwardModule.setRole(ModuleRole.HIGHER_FUNCTION);
        forwardModule.setStage(node.getStage()+1);
        List<PrimitiveModule> primModules = new ArrayList<>();
        
        //Go through each of the modules primitives in forward order
        for (int i = 0; i < node.getSubmodules().size(); i++) {
            
            PrimitiveModule pm = node.getSubmodules().get(i).clone();
            
            //If the primitive is reverse oriented, make a wildcard primitive and feature
            if (node.getSubmodules().get(i).getPrimitive().getOrientation().equals(Orientation.REVERSE)) {
                
                PrimitiveModule wildCard = new PrimitiveModule(FeatureRole.WILDCARD, pm.getPrimitive().clone(), pm.getModuleFeature());
                wildCard.getPrimitive().setOrientation(Orientation.REVERSE);
                primModules.add(wildCard);
                
            //If not, copy components
            } else {
                
                PrimitiveModule forModule = new PrimitiveModule(pm.getPrimitiveRole(),pm.getPrimitive().clone(),pm.getModuleFeature());
                forModule.setPrimitiveRole(EugeneAdaptor.findRole(forModule.getPrimitive().getType()));
                primModules.add(forModule);
            }
        }
        
        forwardModule.setSubmodules(primModules);
        return forwardModule;
    }

    //Pre-processing steps for the globally reverse version of the structure
    private static Module reverseModulePreProcessing(Module node) {

        Module reverseModule = new Module(node.getName() + "_R");
        reverseModule.setRoot(false);
        reverseModule.setRole(ModuleRole.HIGHER_FUNCTION);
        reverseModule.setStage(node.getStage()+1);
        reverseModule.setForward(true);    //Needed?
        ArrayList<PrimitiveModule> primModules = new ArrayList<>();
        
        //Go through each of the modules primitives in reverse order
        for (int i = (node.getSubmodules().size() - 1); i >= 0; i--) {
            
            PrimitiveModule pm = node.getSubmodules().get(i).clone();
            
            //If the primitive is reverse oriented, make a wildcard primitive and feature
            if (pm.getPrimitive().getOrientation().equals(Orientation.FORWARD)) {
                
                PrimitiveModule wildCard = new PrimitiveModule(FeatureRole.WILDCARD, pm.getPrimitive().clone(), pm.getModuleFeature());
                wildCard.getPrimitive().setOrientation(Orientation.REVERSE);
                wildCard.setPrimitiveRole(FeatureRole.WILDCARD);
                wildCard.setModuleFeature(pm.getModuleFeature());
                primModules.add(wildCard);
            
            //If not, copy components
            } else {
                
                PrimitiveModule revModule = new PrimitiveModule(pm.getPrimitiveRole(),pm.getPrimitive().clone(),pm.getModuleFeature());
                revModule.getPrimitive().setOrientation(Orientation.FORWARD); // Again needed?
                revModule.setPrimitiveRole(EugeneAdaptor.findRole(revModule.getPrimitive().getType()));
                primModules.add(revModule);
            }

        }
        reverseModule.setSubmodules(primModules);
        return reverseModule;
    }

    //Create a new EXPRESSEE
    private static Module getExpresseeModule(PrimitiveModule node, Module expressee) {
        
        if (node.getPrimitiveRole().equals(FeatureRole.CDS)) {
            expressee.setRole(ModuleRole.EXPRESSEE);
        } else if (node.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSOR)) {
            expressee.setRole(ModuleRole.EXPRESSEE_REPRESSOR);
        } else if (node.getPrimitiveRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
            expressee.setRole(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR);
        } else if (node.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATOR)) {
            expressee.setRole(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR);
        } else if (node.getPrimitiveRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR)) {
            expressee.setRole(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR);
        }
        
        expressee.setRoot(false);
        expressee.getSubmodules().add(node);
        return expressee;
    }    
}