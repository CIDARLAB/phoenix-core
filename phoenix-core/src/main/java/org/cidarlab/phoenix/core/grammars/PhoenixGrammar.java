/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cidarlab.phoenix.core.formalgrammar.*;

/**
 * This class defines the grammar with which genetic regulatory networks are decomposed
 * It inputs genetic regulatory networks and returns a feature graph
 * 
 * @author evanappleton
 */
public class PhoenixGrammar {
    
    public static HashMap<Nonterminal, List<ProductionRule>> definePhoenixGrammar () {
        
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
        Nonterminal PLASMID = new Nonterminal("PLASMID");
   
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
        
        //TRANSCRIPTION_RF -> TRANSCRIPTION_START, TRANSCRIPT
        List<Symbol> trx_rf_multitrxrf = new ArrayList<>();
	trx_rf_multitrxrf.add(TRANSCRIPTION_START);
        trx_rf_multitrxrf.add(TRANSCRIPTION_RF);
        pR.add(new ProductionRule(TRANSCRIPTION_RF, trx_rf_multitrxrf));
        
        //TRANSCRIPTION_RF -> TRANSCRIPTION_START, TRANSCRIPT
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
        
        //PLASMID may have 1+ TU
        //PLASMID -> TU, VECTOR
        List<Symbol> plasmid = new ArrayList<>();
	plasmid.add(TU);
        plasmid.add(VECTOR);
        pR.add(new ProductionRule(PLASMID, plasmid));
        
        //PLASMID -> TU, VECTOR
        List<Symbol> plasmid_multitu = new ArrayList<>();
	plasmid_multitu.add(TU);
        plasmid_multitu.add(PLASMID);
        pR.add(new ProductionRule(PLASMID, plasmid_multitu));
        
        HashMap<Nonterminal, List<ProductionRule>> grammarMap = new HashMap<>();
        grammarMap.put(PLASMID, pR);
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
}
