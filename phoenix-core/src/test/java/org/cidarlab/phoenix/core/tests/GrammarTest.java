package org.cidarlab.phoenix.core.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cidarlab.phoenix.core.grammar.Grammar;
import org.cidarlab.phoenix.core.grammar.Primitive;
import org.cidarlab.phoenix.core.grammar.ProductionRule;
import org.cidarlab.phoenix.core.grammar.Symbol;
import org.cidarlab.phoenix.core.grammar.Type;
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
			new Primitive(null);
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}

		try {
			new Primitive("");
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}
		
		assertTrue(new Primitive("part1").isTerminal());
	}

	@Test
	public void testTypeDefinitions() {

		try {
			new Type(null);
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}

		try {
			new Type("");
		} catch(Exception e) {
			assertTrue(e.getLocalizedMessage().equals("Invalid name!"));
		}
		
		assertFalse(new Type("PartType").isTerminal());
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

		Type nt = new Type("PartType");
		List<Symbol> ts = new ArrayList<Symbol>();
		ts.add(new Primitive("p1"));
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
		
		
		Type nt = new Type("PartType");
		List<Symbol> ts = new ArrayList<Symbol>();
		ts.add(new Primitive("p1"));
		List<ProductionRule> prs = new ArrayList<ProductionRule>();
		prs.add(new ProductionRule(nt, ts));
		Grammar g = new Grammar(prs, nt);
		assertTrue(g.getTerminals().containsAll(ts) && g.getTerminals().size()==ts.size());
		assertTrue(g.getProductions().containsAll(prs) && g.getProductions().size()==prs.size());
		assertTrue(g.getStartSymbol().equals(nt));
		
		
		
	}
	
}
