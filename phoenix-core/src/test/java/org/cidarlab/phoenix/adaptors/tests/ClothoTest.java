/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author evanappleton
 */
public class ClothoTest {
    
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
    public void testSinglePartUpload() {
        
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.clothoGenbankUpload(toLoad);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Test
    public void testMultiPartUpload() {
        
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.clothoGenbankUpload(toLoad);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Test
    public void testGenericUpload() {
        
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/generic.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.clothoGenbankUpload(toLoad);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
