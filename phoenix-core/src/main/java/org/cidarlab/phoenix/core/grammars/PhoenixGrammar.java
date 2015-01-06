/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Symbol;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;

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
//        terminals.add(new Terminal("s1"));
//        terminals.add(new Terminal("s2"));
//        terminals.add(new Terminal("s3"));
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
        
        //TRANSCRIPT -> TRANSLATION_RF, SPACER
        List<Symbol> trxs = new ArrayList<>();
	trxs.add(TRANSLATION_RF);
        trxs.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPT, trxs));
        
        //TRANSCRIPTION_START -> PROMOTER, SPACER
        List<Symbol> trx_start = new ArrayList<>();
	trx_start.add(PROMOTER);
        trx_start.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_START, trx_start));
        
        //TRANSCRIPTION_END -> TERMINATOR, SPACER
        List<Symbol> trx_end = new ArrayList<>();
	trx_end.add(TERMINATOR);
        trx_end.add(SPACER);
        pR.add(new ProductionRule(TRANSCRIPTION_END, trx_end));
        
        //TRANSCRIPTION_RF -> TRANSCRIPTION_START, TRANSCRIPT
        List<Symbol> trx_rf = new ArrayList<>();
	trx_rf.add(TRANSCRIPTION_START);
        trx_rf.add(TRANSCRIPT);
        pR.add(new ProductionRule(TRANSCRIPTION_RF, trx_rf));
        
        //TU -> TRANSCRIPTION_RF, TRANSCRIPTION_END
        List<Symbol> tu = new ArrayList<>();
	tu.add(TRANSCRIPTION_RF);
        tu.add(TRANSCRIPTION_END);
        pR.add(new ProductionRule(TU, tu));
        
        //TU -> TRANSCRIPTION_RF, TRANSCRIPTION_END
        List<Symbol> plasmid = new ArrayList<>();
	plasmid.add(TU);
        plasmid.add(VECTOR);
        pR.add(new ProductionRule(PLASMID, plasmid));        
        
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
