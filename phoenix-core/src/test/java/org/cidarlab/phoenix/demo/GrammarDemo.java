package org.cidarlab.phoenix.demo;

import org.cidarlab.phoenix.core.formalgrammar.Enumerator;
import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Symbol;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;

import java.util.List;
import java.util.ArrayList;

/**
 * This file should evolve into the core Phoenix decomposition grammar
 * 
 * @author Ernst Oberortner
 */
public class GrammarDemo {

	public GrammarDemo() {
		// some initializations may come
		// along the way
	}
	
	/**
	 * In the switchDemo method we demonstrate the 
	 * specification of a the structure of 
	 * a genetic Toggle Switch using context-free grammars.
	 */
	public void switchDemo() {

		/*
		 * Grammar ToggleSwitch := <
		 * 
		 * NT := {ToggleSwitch, revCassette, fwdCassette},
		 * 
		 * T  := {reporter1, promoter1, promoter2, repressor1, repressor2},
		 * 
		 * PR := {
		 *     ToggleSwitch --> revCassette fwdCassette Reporter
		 *     revCassette  --> Repressor Promoter
		 *     fwdCassette  --> Promoter Repressor
		 *     
		 *     Reporter     --> reporter1
		 *     Promoter     --> promoter1 | promoter2
		 *     Repressor    --> repressor1 | repressor2
		 * },
		 * 
		 * S  := {ToggleSwitch}
		 *
		 * >
		 */
		

		/*---------------
		 * NON-TERMINALS
		 *---------------*/

		// Part Types
		Nonterminal ToggleSwitch = new Nonterminal("ToggleSwitch");
		Nonterminal revCassette = new Nonterminal("revCassette");
		Nonterminal fwdCassette = new Nonterminal("fwdCassette");
		Nonterminal Promoter = new Nonterminal("Promoter");
		Nonterminal Repressor = new Nonterminal("Repressor");
		Nonterminal Reporter = new Nonterminal("Reporter");
		
		/*---------------
		 * TERMINALS
		 *---------------*/
		
		// Promoters
		Terminal prom1 = new Terminal("promoter1");
		Terminal prom2 = new Terminal("promoter2");
		
		// Repressors
		Terminal repr1 = new Terminal("repressor1");
		Terminal repr2 = new Terminal("repressor2");
		
		// Reporters
		Terminal repo1 = new Terminal("reporter1");
		
		/*------------------
		 * PRODUCTION RULES
		 *------------------*/
		List<ProductionRule> prodRules = new ArrayList<ProductionRule>();
		
		// ToggleSwitch --> revCassette fwdCassette Reporter		
		List<Symbol> lhs_ts = new ArrayList<Symbol>();
		lhs_ts.add(revCassette);
		lhs_ts.add(fwdCassette);
		lhs_ts.add(Reporter);
		prodRules.add(new ProductionRule(ToggleSwitch, lhs_ts));
		
		// revCassette  --> Repressor Promoter
		List<Symbol> lhs_rev = new ArrayList<Symbol>();
		lhs_rev.add(Repressor);
		lhs_rev.add(Promoter);
		prodRules.add(new ProductionRule(revCassette, lhs_rev));
		
		// fwdCassette  --> Promoter Repressor
		List<Symbol> lhs_fwd = new ArrayList<Symbol>();
		lhs_fwd.add(Promoter);
		lhs_fwd.add(Repressor);
		prodRules.add(new ProductionRule(fwdCassette, lhs_fwd));
		
		// Promoter --> promoter1 | promoter2
		List<Symbol> loProm = new ArrayList<Symbol>();
		loProm.add(prom1);
		loProm.add(prom2);
		prodRules.add(new ProductionRule(Promoter, loProm));

		// Repressor --> repressor1 | repressor2
		List<Symbol> loRepr = new ArrayList<Symbol>();
		loRepr.add(repr1);
		loRepr.add(repr2);
		prodRules.add(new ProductionRule(Repressor, loRepr));		

		// Reporter --> repressor1
		List<Symbol> loRepos = new ArrayList<Symbol>();
		loRepos.add(repo1);
		prodRules.add(new ProductionRule(Reporter, loRepos));
		
		/*---------
		 * GRAMMAR
		 *---------*/
		Grammar gTS = new Grammar(prodRules, ToggleSwitch);
		System.out.println(gTS.toString());
		
		
//		/*
//		 * ENUMERATE ALL
//		 */
//		Enumerator e = new Enumerator(gTS);
//		e.enumerate();
		
	}
	
	/**
	 * the MAIN
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		GrammarDemo gd = new GrammarDemo();
		gd.switchDemo();
	}

}
