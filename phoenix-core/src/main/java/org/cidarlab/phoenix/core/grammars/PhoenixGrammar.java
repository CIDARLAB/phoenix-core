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
import org.cidarlab.phoenix.core.dom.Module.ModuleType;
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
    public static void assignChildren(Module node) {
        int stack = 0;
        ArrayList<Feature> moduleFeatures = null;
        List<PrimitiveModule> submoduleStack = null;
        Module child = null;

        
        //<editor-fold desc="If Module is of type Higher Function">
        String seq = "";
        if (node.getType().equals(ModuleRole.HIGHER_FUNCTION)) {

            if (node.isRoot()) {
                
                Module forward = forwardModulePreProcessing(node);
                forward.getParents().add(node);
                assignChildren(forward);
                Module reverse = reverseModulePreProcessing(node);
                reverse.getParents().add(node);
                assignChildren(reverse);

                node.getChildren().add(forward);
                node.getChildren().add(reverse);

            } 
            else {
                //<editor-fold desc="Check for TUs">
                for (PrimitiveModule subnodes : node.getSubmodules()) {
                    if (stack == 0) {
                        if (subnodes.getPrimitiveType().equals(PrimitiveModuleRole.PROMOTER)) {
                            seq = "";
                                seq += subnodes.getPrimitive().getType().getName();
                            stack = 1;
                            submoduleStack = new ArrayList<>();
                            moduleFeatures = new ArrayList<>();

                            child = new Module();
                            child.setStage(node.getStage() + 1);  //Set the Stage of the Child Node
                            child.setRoot(false);                 //Wont be the root.     
                            child.setForward(true);               //These are all Forward oriented. 
                            child.setType(ModuleRole.TRANSCRIPTIONAL_UNIT);         //Set Child as a TU

                            moduleFeatures.add(subnodes.getModuleFeatures().get(0));
                            submoduleStack.add(subnodes);
                        }

                    }
                    if (stack == 1) {

                    //Set Features for the Module 
                        if (subnodes.getPrimitiveType().equals(PrimitiveModuleRole.WILDCARD)) 
                            seq += "W";
                        else
                            seq += subnodes.getPrimitive().getType().getName();

                        moduleFeatures.add(subnodes.getModuleFeatures().get(0));
                        submoduleStack.add(subnodes);
                        if (subnodes.getPrimitiveType().equals(PrimitiveModuleRole.TERMINATOR)) {
                            stack = 0;
                            child.setModuleFeatures(moduleFeatures);
                            child.setSubmodules(submoduleStack);
                            node.getChildren().add(child);
                            assignChildren(child);
                            node.getChildren().add(child);
                            child.getParents().add(node);
                            System.out.println(seq);
                        }
                    }
                }
            //</editor-fold>
            }
            
            
            
        } //</editor-fold>
        
        //<editor-fold desc="If Module is type : TRANSCRIPTIONAL UNIT">
        else if (node.getType().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            List<Module> expressorList = new ArrayList<Module>();
            Module expressee = new Module();
            expressee.setStage(node.getStage()+1);
            expressee.setType(ModuleRole.EXPRESSEE);
            moduleFeatures = new ArrayList<Feature>();
            submoduleStack = new ArrayList<PrimitiveModule>();
            for (PrimitiveModule subnodes : node.getSubmodules()) {
                if(subnodes.getPrimitiveType().equals(PrimitiveModuleRole.CDS))
                {
                    moduleFeatures.add(subnodes.getModuleFeatures().get(0));//Most Likely comment out??
                    Module expressor = getExpressorModule(subnodes);
                    expressor.setStage(node.getStage()+1);
                    expressorList.add(expressor);
                    
                    PrimitiveModule testing = new PrimitiveModule();
                    testing.setModuleFeatures(subnodes.getModuleFeatures()); //changes?
                    testing.setPrimitive(subnodes.getPrimitive()); //Valid here?
                    testing.setPrimitiveType(PrimitiveModuleRole.TESTING);
                    submoduleStack.add(testing);
                }
                else
                {
                    moduleFeatures.add(subnodes.getModuleFeatures().get(0));
                    submoduleStack.add(subnodes);
                }
            }
            expressee.setModuleFeatures(moduleFeatures);
            expressee.setSubmodules(submoduleStack);
            
            expressee.getParents().add(node);
            node.getChildren().add(expressee);
            for(Module exp:expressorList)
            {
                exp.getParents().add(node);
                node.getChildren().add(exp);
            }
        }
        //</editor-fold>
    }

    public static Module forwardModulePreProcessing(Module node) {
        Module forwardModule = new Module();
        forwardModule.setRoot(false);
        forwardModule.setType(ModuleRole.HIGHER_FUNCTION);
        forwardModule.setStage(node.getStage()+1);
        ArrayList<Feature> moduleFeature = new ArrayList<Feature>();
        ArrayList<PrimitiveModule> primModules = new ArrayList<PrimitiveModule>();
        for (int i = (node.getSubmodules().size() - 1); i >= 0; i--) {
            if (node.getSubmodules().get(i).getPrimitive().getOrientation().equals(Orientation.REVERSE)) {
                PrimitiveModule wildCard = new PrimitiveModule();
                wildCard.setPrimitiveType(PrimitiveModuleRole.WILDCARD);
                wildCard.setModuleFeatures(node.getSubmodules().get(i).getModuleFeatures());
                primModules.add(wildCard);

                moduleFeature.add(node.getSubmodules().get(i).getModuleFeatures().get(0)); // May have to comment this out later on?
            } else {
                PrimitiveModule forModule = new PrimitiveModule();
                forModule.setPrimitive(node.getSubmodules().get(i).getPrimitive());
                forModule.setModuleFeatures(node.getSubmodules().get(i).getModuleFeatures());
                forModule.setPrimitiveType(findRole(forModule.getPrimitive().getType()));
                primModules.add(forModule);

                moduleFeature.add(node.getSubmodules().get(i).getModuleFeatures().get(0)); //Does anything change here?? (Due to the flip in the orientation?)
            }
        }
        forwardModule.setModuleFeatures(moduleFeature);
        forwardModule.setSubmodules(primModules);
        return forwardModule;
    }

    public static Module reverseModulePreProcessing(Module node) {

        Module reverseModule = new Module();
        reverseModule.setRoot(false);
        reverseModule.setType(ModuleRole.HIGHER_FUNCTION);
        reverseModule.setStage(node.getStage()+1);
        reverseModule.setForward(true);    //Needed?
        ArrayList<Feature> moduleFeature = new ArrayList<Feature>();
        ArrayList<PrimitiveModule> primModules = new ArrayList<PrimitiveModule>();
        for (int i = (node.getSubmodules().size() - 1); i >= 0; i--) {
            if (node.getSubmodules().get(i).getPrimitive().getOrientation().equals(Orientation.FORWARD)) {
                PrimitiveModule wildCard = new PrimitiveModule();
                wildCard.setPrimitiveType(PrimitiveModuleRole.WILDCARD);
                wildCard.setModuleFeatures(node.getSubmodules().get(i).getModuleFeatures());
                primModules.add(wildCard);

                moduleFeature.add(node.getSubmodules().get(i).getModuleFeatures().get(0)); // May have to comment this out later on?
            } else {
                PrimitiveModule revModule = new PrimitiveModule();
                revModule.setPrimitive(node.getSubmodules().get(i).getPrimitive());
                revModule.getPrimitive().setOrientation(Orientation.FORWARD); // Again needed?
                revModule.setModuleFeatures(node.getSubmodules().get(i).getModuleFeatures());
                revModule.setPrimitiveType(findRole(revModule.getPrimitive().getType()));
                primModules.add(revModule);

                moduleFeature.add(node.getSubmodules().get(i).getModuleFeatures().get(0)); //Does anything change here?? (Due to the flip in the orientation?)
            }
        }
        reverseModule.setModuleFeatures(moduleFeature);
        reverseModule.setSubmodules(primModules);
        return reverseModule;
    }

    public static PrimitiveModuleRole findRole(ComponentType type) {
        PrimitiveModuleRole role = PrimitiveModuleRole.WILDCARD;
        if (type.getName().startsWith("p")) {
            role = PrimitiveModuleRole.PROMOTER;
        } else if (type.getName().startsWith("r")) {
            role = PrimitiveModuleRole.RBS;
        } else if (type.getName().startsWith("c")) {
            role = PrimitiveModuleRole.CDS;
        } else if (type.getName().startsWith("t")) {
            role = PrimitiveModuleRole.TERMINATOR;
        }
        return role;
    }
    
    public static Module getExpressorModule(PrimitiveModule node)
    {
        Module expressor = new Module();
        expressor.setType(ModuleRole.EXPRESSOR);
        expressor.setModuleFeatures(node.getModuleFeatures());
        expressor.getSubmodules().add(node);
        return expressor;
    }
}
