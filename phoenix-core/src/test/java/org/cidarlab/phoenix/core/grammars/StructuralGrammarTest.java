/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prash
 */
public class StructuralGrammarTest {

    public StructuralGrammarTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of structureParse method, of class StructuralGrammar.
     */
    @Test
    public void testStructureParse() {
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("<t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p p p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p c t p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p r c t p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("<t <c <r <p p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("<t <c <r p <p r c t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("<t p r c <c <r <p t");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p r c t <t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("p <t r c t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        StructuralGrammar.getForwardParseTree("");
    }

}
