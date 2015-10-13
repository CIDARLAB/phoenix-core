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

    public static HashMap<Nonterminal, List<ProductionRule>> definePhoenixGrammar() {

        /*---------------
         * TERMINALS
         *---------------*/
        //These terminals will ultimately be filled by the parts library
        List<Terminal> terminals = new ArrayList<>();
        terminals.add(new Terminal("p1"));
        terminals.add(new Terminal("p2"));
        terminals.add(new Terminal("p3"));
        terminals.add(new Terminal("r1"));
        terminals.add(new Terminal("r2"));
        terminals.add(new Terminal("r3"));
        terminals.add(new Terminal("c1"));
        terminals.add(new Terminal("c2"));
        terminals.add(new Terminal("c3"));
        terminals.add(new Terminal("t1"));
        terminals.add(new Terminal("t2"));
        terminals.add(new Terminal("t2"));
        terminals.add(new Terminal("s1"));
        terminals.add(new Terminal("s2"));
        terminals.add(new Terminal("s3"));
        terminals.add(new Terminal("v1"));
        terminals.add(new Terminal("v2"));
        terminals.add(new Terminal("v3"));

        /*---------------
         * NONTERMINALS
         *---------------*/
        //Non terminals are abstract place-holders
        //Layer 1
        Nonterminal PROMOTER = new Nonterminal("PROMOTER");
        Nonterminal RBS = new Nonterminal("RBS");
        Nonterminal CDS = new Nonterminal("CDS");
        Nonterminal TERMINATOR = new Nonterminal("TERMINATOR");
        Nonterminal VECTOR = new Nonterminal("VECTOR");
        Nonterminal SPACER = new Nonterminal("SPACER");

        //Layer
        Nonterminal TRANSCRIPTION_RF = new Nonterminal("TRANSCRIPTION_RF");
        Nonterminal TRANSCRIPTION_START = new Nonterminal("TRANSCRIPTION_START");
        Nonterminal TRANSCRIPT = new Nonterminal("TRANSCRIPT");
        Nonterminal TRANSLATION_RF = new Nonterminal("TRANSLATION_RF");
        Nonterminal TRANSCRIPTION_END = new Nonterminal("TRANSCRIPTION_END");

        //Layer -> PERHAPS IN BREAKDOWN...
//        Nonterminal EXPRESSOR = new Nonterminal("EXPRESSOR");
//        Nonterminal EXPRESSEE = new Nonterminal("EXPRESSEE");
        
        //Layer
        Nonterminal TU = new Nonterminal("TU");
        Nonterminal INSERT = new Nonterminal("INSERT");

        /*---------------
         * PRODUCTION RULES
         *---------------*/
        List<ProductionRule> pR = new ArrayList<>();

        //PROMOTER -> All promoters in library
        List<Symbol> promoters = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("p")) {
                promoters.add(t);
            }
        }
        pR.add(new ProductionRule(PROMOTER, promoters));

        //RBS -> All RBSs in library
        List<Symbol> rbss = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("r")) {
                rbss.add(t);
            }
        }
        pR.add(new ProductionRule(RBS, rbss));

        //CDS -> All CDSs in library
        List<Symbol> cdss = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("c")) {
                cdss.add(t);
            }
        }
        pR.add(new ProductionRule(CDS, cdss));

        //TERMINATOR -> All terminators in library
        List<Symbol> terminators = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("t")) {
                terminators.add(t);
            }
        }
        pR.add(new ProductionRule(TERMINATOR, terminators));

        //VECTOR -> All vectors in library
        List<Symbol> vectors = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("v")) {
                vectors.add(t);
            }
        }
        pR.add(new ProductionRule(VECTOR, vectors));

        //SPACER -> All spacers in library
        List<Symbol> spacers = new ArrayList<>();
        for (Terminal t : terminals) {
            if (t.getName().startsWith("s")) {
                spacers.add(t);
            }
        }
        pR.add(new ProductionRule(SPACER, spacers));

        //TRANSLATION_RF -> RBS, CDS
        List<Symbol> trans_rfs = new ArrayList<>();
        trans_rfs.add(RBS);
        trans_rfs.add(CDS);
        pR.add(new ProductionRule(TRANSLATION_RF, trans_rfs));

        //TRANSCRIPT may have 1+ TRANSLATION_RF and 0+ SPACER
        //TRANSCRIPT -> TRANSLATION_RF, SPACER
        List<Symbol> trxs = new ArrayList<>();
        trxs.add(TRANSLATION_RF);
        trxs.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPT, trxs));

        //2+ TRANSLATION_RF
        List<Symbol> trxs_multitrans = new ArrayList<>();
        trxs_multitrans.add(TRANSLATION_RF);
        trxs_multitrans.add(TRANSCRIPT);
        pR.add(new ProductionRule(TRANSCRIPT, trxs_multitrans));

        //2+ SPACER
        List<Symbol> trxs_nospacer = new ArrayList<>();
        trxs_nospacer.add(TRANSCRIPT);
        trxs_nospacer.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPT, trxs_nospacer));

        //0 SPACER
        List<Symbol> trxs_multispacer = new ArrayList<>();
        trxs_multispacer.add(TRANSLATION_RF);
        pR.add(new ProductionRule(TRANSCRIPT, trxs_multispacer));

        //TRANSCRIPTION_START may have 1+ PROMOTER or 0+ SPACER
        //TRANSCRIPTION_START -> PROMOTER, SPACER
        List<Symbol> trx_start = new ArrayList<>();
        trx_start.add(PROMOTER);
        trx_start.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_START, trx_start));

        //2+ PROMOTER
        List<Symbol> trx_start_multiprom = new ArrayList<>();
        trx_start_multiprom.add(PROMOTER);
        trx_start_multiprom.add(TRANSCRIPTION_START);
        pR.add(new ProductionRule(TRANSCRIPTION_START, trx_start_multiprom));

        //2+ SPACER
        List<Symbol> trx_start_multispacer = new ArrayList<>();
        trx_start_multispacer.add(TRANSCRIPTION_START);
        trx_start_multispacer.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_START, trx_start_multispacer));

        //0 SPACER
        List<Symbol> trx_start_nospacer = new ArrayList<>();
        trx_start_nospacer.add(PROMOTER);
        pR.add(new ProductionRule(TRANSCRIPTION_START, trx_start_nospacer));

        //TRANSCRIPTION_START may have 1+ TERMINATOR or 0+ SPACER
        //TRANSCRIPTION_END -> TERMINATOR, SPACER
        List<Symbol> trx_end = new ArrayList<>();
        trx_end.add(TERMINATOR);
        trx_end.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_END, trx_end));

        //2+ TERMINATOR
        List<Symbol> trx_end_multiterm = new ArrayList<>();
        trx_end_multiterm.add(TERMINATOR);
        trx_end_multiterm.add(TRANSCRIPTION_END);
        pR.add(new ProductionRule(TRANSCRIPTION_END, trx_end_multiterm));

        //2+ SPACER
        List<Symbol> trx_end_multispacer = new ArrayList<>();
        trx_end_multispacer.add(TRANSCRIPTION_END);
        trx_end_multispacer.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_END, trx_end_multispacer));

        //0 SPACER
        List<Symbol> trx_end_nospacer = new ArrayList<>();
        trx_end_nospacer.add(TERMINATOR);
        pR.add(new ProductionRule(TRANSCRIPTION_END, trx_end_nospacer));

        //TRANSCRIPTION_RF may have 1+ TRANSCRIPTION_START and 1+ TRANSCRIPT
        //TRANSCRIPTION_RF -> TRANSCRIPTION_START, TRANSCRIPT
        List<Symbol> trx_rf = new ArrayList<>();
        trx_rf.add(TRANSCRIPTION_START);
        trx_rf.add(TRANSCRIPT);
        pR.add(new ProductionRule(TRANSCRIPTION_RF, trx_rf));

        //2+ TRANSCRIPTION_START
        List<Symbol> trx_rf_multitrxrf = new ArrayList<>();
        trx_rf_multitrxrf.add(TRANSCRIPTION_START);
        trx_rf_multitrxrf.add(TRANSCRIPTION_RF);
        pR.add(new ProductionRule(TRANSCRIPTION_RF, trx_rf_multitrxrf));

        //2+ TRANSCRIPT
        List<Symbol> trx_rf_multitrx = new ArrayList<>();
        trx_rf_multitrx.add(TRANSCRIPTION_RF);
        trx_rf_multitrx.add(TRANSCRIPT);
        pR.add(new ProductionRule(TRANSCRIPTION_RF, trx_rf_multitrx));

        //TRANSCRIPTION_RF may have 1+ TRANSCRIPT_RF and 1+ TRANSCRIPTION_END
        //TU -> TRANSCRIPTION_RF, TRANSCRIPTION_END
        List<Symbol> tu = new ArrayList<>();
        tu.add(TRANSCRIPTION_RF);
        tu.add(TRANSCRIPTION_END);
        pR.add(new ProductionRule(TU, tu));

        //2+ TRANSCRIPT_RF
        List<Symbol> tu_multitrxrf = new ArrayList<>();
        tu_multitrxrf.add(TRANSCRIPTION_RF);
        tu_multitrxrf.add(TU);
        pR.add(new ProductionRule(TU, tu_multitrxrf));

        //2+ TRANSCRIPTION_END
        List<Symbol> tu_multitrxend = new ArrayList<>();
        tu_multitrxend.add(TU);
        tu_multitrxend.add(TRANSCRIPTION_END);
        pR.add(new ProductionRule(TU, tu_multitrxend));

        //INSERT may have 0+ SPACER, 1+ TU, 0+ SPACER
        //INSERT -> TU
        List<Symbol> insert_tu = new ArrayList<>();
        insert_tu.add(TU);
        pR.add(new ProductionRule(INSERT, insert_tu));

        //PRE-SPACERS
        List<Symbol> insert_prespacer = new ArrayList<>();
        insert_prespacer.add(SPACER);
        insert_prespacer.add(INSERT);
        pR.add(new ProductionRule(INSERT, insert_prespacer));
        
        //2+ TUs FRONT
        List<Symbol> insert_multitu_front = new ArrayList<>();
        insert_multitu_front.add(TU);
        insert_multitu_front.add(INSERT);
        pR.add(new ProductionRule(INSERT, insert_multitu_front));
        
        //POST-SPACERS
        List<Symbol> insert_postspacer = new ArrayList<>();
        insert_postspacer.add(INSERT);
        insert_postspacer.add(SPACER);
        pR.add(new ProductionRule(INSERT, insert_postspacer));
        
        //2+ TUs FRONT
        List<Symbol> insert_multitu_back = new ArrayList<>();
        insert_multitu_back.add(INSERT);
        insert_multitu_back.add(TU);
        pR.add(new ProductionRule(INSERT, insert_multitu_back));        

        HashMap<Nonterminal, List<ProductionRule>> grammarMap = new HashMap<>();
        grammarMap.put(INSERT, pR);
        return grammarMap;
    }

    /*---------------
     * GRAMMAR
     *---------------*/
    public static List<Grammar> instantiateGrammar(HashMap<Nonterminal, List<ProductionRule>> grammarMap) {

        List<Grammar> grammars = new ArrayList<>();
        for (Nonterminal start : grammarMap.keySet()) {
            Grammar grammar = new Grammar(grammarMap.get(start), start);
            grammars.add(grammar);
            System.out.println(grammar.toString());
        }

        return grammars;
    }
    
    /*---------------
     * 
     * DECOMPOSITION METHODS
     * 
     *---------------*/
    
    //Helper method to loop through a list of structures that need to be decomposed
    public static void decomposeAll (List<Module> modules) {
        for (Module structure : modules) {
            PhoenixGrammar.decompose(structure);
        }
    }
    
    //Method for decomposing one module into its structural terminals
    public static void decompose(Module node) {

        int stack = 0;
        ArrayList<Feature> moduleFeatures = null;
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
                            moduleFeatures = new ArrayList<>();

                            //Create new Module to be made for each TRANSCRIPTIONAL_UNIT
                            child = new Module(node.getName() + "_" + ModuleRole.TRANSCRIPTIONAL_UNIT.toString() + "_" + TUCount);
                            TUCount++;
                            child.setStage(node.getStage() + 1);  //Set the Stage of the Child Node
                            child.setRoot(false);                 //Wont be the root.     
                            child.setForward(true);               //These are all Forward oriented. 
                            child.setRole(ModuleRole.TRANSCRIPTIONAL_UNIT);         //Set Child as a TU
                            moduleFeatures.add(subnodes.getModuleFeature().clone());
                            submoduleStack.add(subnodes.clone());
                        }

                    } else if (stack == 1) {

                        moduleFeatures.add(subnodes.getModuleFeature().clone());
                        submoduleStack.add(subnodes.clone());
                        
                        //Termintors pop the stack
                        if (subnodes.getPrimitiveRole().equals(FeatureRole.TERMINATOR)) {
                            stack = 0;
                            child.setModuleFeatures(moduleFeatures);
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
            int expressorCount = 0;
            int expresseeCount = 0; 
            
            Module expressor = new Module(node.getName() + "_" + ModuleRole.EXPRESSOR.toString());
            expressorCount++;
            expressor.setStage(node.getStage() + 1);
            expressor.setRole(ModuleRole.EXPRESSOR);
            expressor.setRoot(false);
            
            moduleFeatures = new ArrayList<>();
            submoduleStack = new ArrayList<>();
            
            for (PrimitiveModule primitive : node.getSubmodules()) {
                
                //If we run into a CDS, we know that this will be an EXPRESSEE and replace this spot with an TESTING SLOT IN EXPRESSOR
                FeatureRole pR = primitive.getPrimitiveRole();
                if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                                                           
                    Module expressee = new Module(node.getName() + "_" + ModuleRole.EXPRESSEE.toString());
                    expresseeCount++;
                    
                    //Create a new EXPRESSEE from this CDS primitive and copy the feature
                    moduleFeatures.add(primitive.getModuleFeature());
                    expressee = getExpresseeModule(primitive, expressee);
                    expressee.setStage(node.getStage() + 1);
                    expresseeList.add(expressee);

                    //Add a new TESTING primitve to the expressor composition
                    //This piece will get replaced in the next step (adding testing pieces)
                    PrimitiveModule testing = new PrimitiveModule(FeatureRole.TESTING,primitive.getPrimitive(),primitive.getModuleFeature());
                    submoduleStack.add(testing);
                    
                //Else, continue adding features to EXPRESSOR    
                } else {
                    
                    moduleFeatures.add(primitive.getModuleFeature());
                    submoduleStack.add(primitive);
                }
            }
            
            //Finalize EXPRESSOR primitives and links
            expressor.setModuleFeatures(moduleFeatures);
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
        ArrayList<Feature> moduleFeature = new ArrayList<>();
        ArrayList<PrimitiveModule> primModules = new ArrayList<>();
        
        //Go through each of the modules primitives in forward order
        for (int i = 0; i < node.getSubmodules().size(); i++) {
            
            PrimitiveModule pm = node.getSubmodules().get(i).clone();
            
            //If the primitive is reverse oriented, make a wildcard primitive and feature
            if (node.getSubmodules().get(i).getPrimitive().getOrientation().equals(Orientation.REVERSE)) {
                
                PrimitiveModule wildCard = new PrimitiveModule(FeatureRole.WILDCARD, pm.getPrimitive().clone(), pm.getModuleFeature());
                wildCard.getPrimitive().setOrientation(Orientation.REVERSE);
                wildCard.setPrimitiveRole(FeatureRole.WILDCARD);
                wildCard.setModuleFeature(pm.getModuleFeature());
                primModules.add(wildCard);
                moduleFeature.add(pm.getModuleFeature());
                
            //If not, copy components
            } else {
                
                PrimitiveModule forModule = new PrimitiveModule(pm.getPrimitiveRole(),pm.getPrimitive().clone(),pm.getModuleFeature());
                forModule.setPrimitiveRole(EugeneAdaptor.findRole(forModule.getPrimitive().getType()));
                primModules.add(forModule);
                moduleFeature.add(pm.getModuleFeature());
            }
        }
        
        forwardModule.setModuleFeatures(moduleFeature);
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
        ArrayList<Feature> moduleFeature = new ArrayList<>();
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
                moduleFeature.add(pm.getModuleFeature()); // May have to comment this out later on?
            
            //If not, copy components
            } else {
                
                PrimitiveModule revModule = new PrimitiveModule(pm.getPrimitiveRole(),pm.getPrimitive().clone(),pm.getModuleFeature());
                revModule.getPrimitive().setOrientation(Orientation.FORWARD); // Again needed?
                revModule.setPrimitiveRole(EugeneAdaptor.findRole(revModule.getPrimitive().getType()));
                primModules.add(revModule);
                moduleFeature.add(pm.getModuleFeature()); //Does anything change here?? (Due to the flip in the orientation?)
            }

        }
        reverseModule.setModuleFeatures(moduleFeature);
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
        
        expressee.getModuleFeatures().add(node.getModuleFeature());
        expressee.setRoot(false);
        expressee.getSubmodules().add(node);
        return expressee;
    }    
}
