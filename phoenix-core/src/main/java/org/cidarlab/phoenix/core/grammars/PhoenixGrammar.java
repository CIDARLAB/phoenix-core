/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cidarlab.minieugene.dom.Component;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Orientation;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.PrimitiveModule.PrimitiveModuleRole;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Symbol;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;
import org.clothocad.model.Feature;
import org.clothocad.model.Person;

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
                for (PrimitiveModule subnodes : node.getSubmodules()) {
                    
                    if (stack == 0) {
                        
                        //Promoters start the stack
                        PrimitiveModuleRole pR = subnodes.getPrimitiveRole();
                        if (pR.equals(PrimitiveModuleRole.PROMOTER) || pR.equals(PrimitiveModuleRole.PROMOTER_CONSTITUTIVE) || pR.equals(PrimitiveModuleRole.PROMOTER_INDUCIBLE) || pR.equals(PrimitiveModuleRole.PROMOTER_REPRESSIBLE)) {
                            
                            stack = 1;
                            submoduleStack = new ArrayList<>();
                            moduleFeatures = new ArrayList<>();

                            //Create new Module to be made for each TRANSCRIPTIONAL_UNIT
                            child = new Module();
                            child.setStage(node.getStage() + 1);  //Set the Stage of the Child Node
                            child.setRoot(false);                 //Wont be the root.     
                            child.setForward(true);               //These are all Forward oriented. 
                            child.setRole(ModuleRole.TRANSCRIPTIONAL_UNIT);         //Set Child as a TU
                            moduleFeatures.add(subnodes.getModuleFeatures().get(0));
                            submoduleStack.add(subnodes);
                        }

                    } else if (stack == 1) {

                        moduleFeatures.add(subnodes.getModuleFeatures().get(0));
                        submoduleStack.add(subnodes);
                        
                        //Termintors pop the stack
                        if (subnodes.getPrimitiveRole().equals(PrimitiveModuleRole.TERMINATOR)) {
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
            List<Module> expresseeList = new ArrayList<Module>();
            
            Module expressor = new Module();            
            expressor.setStage(node.getStage() + 1);
            expressor.setRole(ModuleRole.EXPRESSOR);
            expressor.setRoot(false);
            
            moduleFeatures = new ArrayList<Feature>();
            submoduleStack = new ArrayList<PrimitiveModule>();
            
            for (PrimitiveModule primitive : node.getSubmodules()) {
                
                //If we run into a CDS, we know that this will be an EXPRESSEE and replace this spot with an TESTING SLOT IN EXPRESSOR
                PrimitiveModuleRole pR = primitive.getPrimitiveRole();
                if (pR.equals(PrimitiveModuleRole.CDS) || pR.equals(PrimitiveModuleRole.CDS_ACTIVATOR) || pR.equals(PrimitiveModuleRole.CDS_REPRESSOR)) {
                    
                    //Create a new EXPRESSEE from this CDS primitive and copy the feature
                    moduleFeatures.add(primitive.getModuleFeatures().get(0));
                    Module expressee = getExpresseeModule(primitive);
                    expressee.setStage(node.getStage() + 1);
                    expresseeList.add(expressee);

                    //Add a new TESTING primitve to the expressor composition
                    //This piece will get replaced in the next step (adding testing pieces)
                    PrimitiveModule testing = new PrimitiveModule();
                    testing.setModuleFeatures(primitive.getModuleFeatures());
                    testing.setPrimitive(primitive.getPrimitive());
                    testing.setPrimitiveRole(PrimitiveModuleRole.TESTING);
                    submoduleStack.add(testing);
                    
                //Else, continue adding features to EXPRESSOR    
                } else {
                    
                    moduleFeatures.add(primitive.getModuleFeatures().get(0));
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
        
        Module forwardModule = new Module();
        forwardModule.setRoot(false);
        forwardModule.setRole(ModuleRole.HIGHER_FUNCTION);
        forwardModule.setStage(node.getStage()+1);
        ArrayList<Feature> moduleFeature = new ArrayList<Feature>();
        ArrayList<PrimitiveModule> primModules = new ArrayList<PrimitiveModule>();
        
        //Go through each of the modules primitives in forward order
        for (int i = 0; i < node.getSubmodules().size(); i++) {
            
            PrimitiveModule pm = node.getSubmodules().get(i).clone();
            
            //If the primitive is reverse oriented, make a wildcard primitive and feature
            if (node.getSubmodules().get(i).getPrimitive().getOrientation().equals(Orientation.REVERSE)) {
                
                PrimitiveModule wildCard = new PrimitiveModule(PrimitiveModuleRole.WILDCARD, pm.getPrimitive().clone(), null);
                wildCard.getPrimitive().setOrientation(Orientation.REVERSE);
                wildCard.setPrimitiveRole(PrimitiveModuleRole.WILDCARD);
                wildCard.setModuleFeatures(pm.getModuleFeatures());
                primModules.add(wildCard);
                moduleFeature.add(pm.getModuleFeatures().get(0));
                
            //If not, copy components
            } else {
                
                PrimitiveModule forModule = new PrimitiveModule();
                forModule.setPrimitive(pm.getPrimitive().clone());
                forModule.setPrimitiveRole(pm.getPrimitiveRole());
                forModule.setModuleFeatures(pm.getModuleFeatures());
                forModule.setPrimitiveRole(findRole(forModule.getPrimitive().getType()));
                primModules.add(forModule);
                moduleFeature.add(pm.getModuleFeatures().get(0));
            }
        }
        
        forwardModule.setModuleFeatures(moduleFeature);
        forwardModule.setSubmodules(primModules);
        return forwardModule;
    }

    //Pre-processing steps for the globally reverse version of the structure
    private static Module reverseModulePreProcessing(Module node) {

        Module reverseModule = new Module();
        reverseModule.setRoot(false);
        reverseModule.setRole(ModuleRole.HIGHER_FUNCTION);
        reverseModule.setStage(node.getStage()+1);
        reverseModule.setForward(true);    //Needed?
        ArrayList<Feature> moduleFeature = new ArrayList<Feature>();
        ArrayList<PrimitiveModule> primModules = new ArrayList<PrimitiveModule>();
        
        //Go through each of the modules primitives in reverse order
        for (int i = (node.getSubmodules().size() - 1); i >= 0; i--) {
            
            PrimitiveModule pm = node.getSubmodules().get(i).clone();
            
            //If the primitive is reverse oriented, make a wildcard primitive and feature
            if (pm.getPrimitive().getOrientation().equals(Orientation.FORWARD)) {
                
                PrimitiveModule wildCard = new PrimitiveModule(PrimitiveModuleRole.WILDCARD, pm.getPrimitive().clone(), null);
                wildCard.getPrimitive().setOrientation(Orientation.REVERSE);
                wildCard.setPrimitiveRole(PrimitiveModuleRole.WILDCARD);
                wildCard.setModuleFeatures(pm.getModuleFeatures());
                primModules.add(wildCard);
                moduleFeature.add(pm.getModuleFeatures().get(0)); // May have to comment this out later on?
            
            //If not, copy components
            } else {
                
                PrimitiveModule revModule = new PrimitiveModule();
                revModule.setPrimitive(pm.getPrimitive().clone());
                revModule.setPrimitiveRole(pm.getPrimitiveRole());
                revModule.getPrimitive().setOrientation(Orientation.FORWARD); // Again needed?
                revModule.setModuleFeatures(pm.getModuleFeatures());
                revModule.setPrimitiveRole(findRole(revModule.getPrimitive().getType()));
                primModules.add(revModule);
                moduleFeature.add(pm.getModuleFeatures().get(0)); //Does anything change here?? (Due to the flip in the orientation?)
            }

        }
        reverseModule.setModuleFeatures(moduleFeature);
        reverseModule.setSubmodules(primModules);
        return reverseModule;
    }

    //Determine primitive role from Eugene component types
    public static PrimitiveModuleRole findRole(ComponentType type) {
        
        PrimitiveModuleRole role = PrimitiveModuleRole.WILDCARD;
        if (type.getName().startsWith("p")) {
            role = PrimitiveModuleRole.PROMOTER;
        } else if (type.getName().startsWith("ip")) {
            role = PrimitiveModuleRole.PROMOTER_INDUCIBLE;
        } else if (type.getName().startsWith("rp")) {
            role = PrimitiveModuleRole.PROMOTER_REPRESSIBLE;
        } else if (type.getName().startsWith("cp")) {
            role = PrimitiveModuleRole.PROMOTER_CONSTITUTIVE;
        } else if (type.getName().startsWith("rc")) {
            role = PrimitiveModuleRole.CDS_REPRESSOR;
        } else if (type.getName().startsWith("fc")) {
            role = PrimitiveModuleRole.CDS_ACTIVATOR;
        } else if (type.getName().startsWith("r")) {
            role = PrimitiveModuleRole.RBS;
        } else if (type.getName().startsWith("c")) {
            role = PrimitiveModuleRole.CDS;
        } else if (type.getName().startsWith("t")) {
            role = PrimitiveModuleRole.TERMINATOR;
        }
        return role;
    }
    
    //Create a new EXPRESSEE
    private static Module getExpresseeModule(PrimitiveModule node) {
        
        Module expressor = new Module();
        expressor.setRole(ModuleRole.EXPRESSEE);
        expressor.setModuleFeatures(node.getModuleFeatures());
        expressor.setRoot(false);
        expressor.getSubmodules().add(node);
        return expressor;
    }    
}
