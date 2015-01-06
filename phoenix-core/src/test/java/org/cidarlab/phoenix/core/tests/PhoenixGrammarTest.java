/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.util.HashMap;
import java.util.List;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;

/**
 *
 * @author evanappleton
 */
public class PhoenixGrammarTest {
    
    //Test constructor
    public PhoenixGrammarTest() {
        
    }
    
    public void phoenixGrammar() {
        
        HashMap<Nonterminal, List<ProductionRule>> phoenixGrammarDef = PhoenixGrammar.definePhoenixGrammar();
        List<Grammar> grammars = PhoenixGrammar.instantiateGrammar(phoenixGrammarDef);
    }
    
    //Main testing class
    public static void main(String[] args) {
        
        PhoenixGrammarTest ph = new PhoenixGrammarTest();
        ph.phoenixGrammar();
    }
    
}
