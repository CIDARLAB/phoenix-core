package org.cidarlab.phoenix.core.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cidarlab.phoenix.core.formalgrammar.Grammar;
import org.cidarlab.phoenix.core.formalgrammar.Terminal;
import org.cidarlab.phoenix.core.formalgrammar.ProductionRule;
import org.cidarlab.phoenix.core.formalgrammar.Symbol;
import org.cidarlab.phoenix.core.formalgrammar.Nonterminal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GrammarTest {

	@BeforeClass
	public static void setUpBeforeClass() 
			throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() 
			throws Exception {
	}

	@Before
	public void setUp() 
			throws Exception {
	}

	@After
	public void tearDown() 
			throws Exception {
	}

	@Test
	public void testPrimitiveDefinitions() {

		try {
			new Terminal(null);
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}

		try {
			new Terminal("");
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}
		
		assertTrue(new Terminal("part1").isTerminal());
	}

	@Test
	public void testTypeDefinitions() {

		try {
			new Nonterminal(null);
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}

		try {
			new Nonterminal("");
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}
		
		assertFalse(new Nonterminal("PartType").isTerminal());
	}
	
	@Test
	public void testProductionRulesDefinitions() {
		
		try {
			new ProductionRule(null, null);
		} catch(Exception e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));
		}

		try {
			new ProductionRule(null, new ArrayList<Symbol>());
		} catch(Exception e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));
		}

		Nonterminal nt = new Nonterminal("PartType");
		List<Symbol> ts = new ArrayList<Symbol>();
		ts.add(new Terminal("p1"));
		ProductionRule pr = new ProductionRule(nt, ts);
		assertTrue(pr.getNonterminal().equals(nt));
		assertTrue(pr.getProduction().equals(ts));
	}
	
	@Test
	public void testGrammarDefinition() {
		
		try {
			new Grammar(null, null);
		} catch(Exception e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));
		}
		try {
			new Grammar(new ArrayList<ProductionRule>(), null);
		} catch(Exception e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));
		}
		
		
		Nonterminal nt = new Nonterminal("PartType");
		List<Symbol> ts = new ArrayList<Symbol>();
		ts.add(new Terminal("p1"));
		List<ProductionRule> prs = new ArrayList<ProductionRule>();
		prs.add(new ProductionRule(nt, ts));
		Grammar g = new Grammar(prs, nt);
		assertTrue(g.getTerminals().containsAll(ts) && g.getTerminals().size()==ts.size());
		assertTrue(g.getProductions().containsAll(prs) && g.getProductions().size()==prs.size());
		assertTrue(g.getStartSymbol().equals(nt));
		
		
		
	}
	
}
