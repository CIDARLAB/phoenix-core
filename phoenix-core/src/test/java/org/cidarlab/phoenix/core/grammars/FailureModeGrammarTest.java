/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.concurrent.atomic.AtomicInteger;
import org.antlr.v4.runtime.tree.ParseTree;
import org.cidarlab.phoenix.core.controller.Utilities;
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
    public void testRoadBlocking() {
        
        Utilities.printDebugStatement("RoadBlocking");
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("p p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("p <p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<t <c <r <p <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<t <c <r <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<t <c <r <p <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getRoadBlockingCount("<t <c <r <p <p <p <p p p r c t");
        
    }
    
    //@Test
    public void testGetSuperCoilingTree() {
        
        Utilities.printDebugStatement("Super Coiling");
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("p p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<p p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("p <p r c t");
    
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<t <c <r <p <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<t <c <r <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<t <c <r <p <p <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<t <c <r <p <p <p <p p p r c t");
        
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("<p p <p p <p");
        
        
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getSuperCoilingCount("t c r <p p c t r <p p t c <p");
        
        
    }
    
    
    @Test
    public void testGetTranscriptionalReadThroughTree() {
        
        Utilities.printDebugStatement("Transcriptional ReadThrough");
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("p r c t p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("<t <c <r <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("<t <c <r p <p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("<t p r c <c <r <p t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("p r c t <t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalReadThroughCount("p <t r c t <c <r <p");
    }
    
    //@Test
    public void testGetTranscriptionalInterferenceTree() {
        
        Utilities.printDebugStatement("Transcriptional Interference");
        AtomicInteger count = new AtomicInteger();
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("p r c t p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("<t <c <r <p p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("<t <c <r p <p r c t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("<t p r c <c <r <p t");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("p r c t <t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("p r c <t t <c <r <p");
        
        System.out.println(count.getAndIncrement());
        FailureModeGrammar.getTranscriptionalInterferenceCount("p <t r c t <c <r <p");
    }

}
