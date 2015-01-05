/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;

/**
 * This class defines the grammar with which genetic regulatory networks are decomposed
 * It inputs genetic regulatory networks and returns a feature graph
 * 
 * @author evanappleton
 */
public class PhoenixGrammar {
    
    //Decomposition grammar constructor
    public PhoenixGrammar () {
        
    }
    
    /*---------------
    * TERMINALS
    *---------------*/
    public List<Terminal> defineTerminals () {
        
        List<Terminal> terminals = new ArrayList<Terminal>();
        terminals.add(new Terminal("promoter"));
        terminals.add(new Terminal("RBS"));
        terminals.add(new Terminal("CDS"));
        terminals.add(new Terminal("terminator"));
        terminals.add(new Terminal("spacer"));
        
        return terminals;
    }
    
    /*---------------
    * NONTERMINALS
    *---------------*/
    public List<Nonterminal> defineNonterminals () {
        
        return null;
    }
    
    /*---------------
    * PRODUCTION RULES
    *---------------*/
    public List<ProductionRule> defineProductionRules (List<Terminal> terminals, List<Nonterminal> nonterminals) {
        return null;
    }
    
    
}
