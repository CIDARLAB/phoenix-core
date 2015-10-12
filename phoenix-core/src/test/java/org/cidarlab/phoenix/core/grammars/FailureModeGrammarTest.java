/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.concurrent.atomic.AtomicInteger;
import org.antlr.v4.runtime.tree.ParseTree;
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
public class FailureModeGrammarTest {
    
    public FailureModeGrammarTest() {
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
     * Test of getForwardParseTree method, of class FailureModeGrammar.
     */
    //@Test
    public void testGetForwardParseTree() {
        
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("p p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("p <p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<t <c <r <p <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<t <c <r <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<t <c <r <p <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingTree("<t <c <r <p <p <p <p p p r c t");
        
    }
    
    @Test
    public void testGetSuperCoilingTree() {
        
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("p p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("p <p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<t <c <r <p <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<t <c <r <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<t <c <r <p <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingTree("<t <c <r <p <p <p <p p p r c t");
        
    }
    
    
}
