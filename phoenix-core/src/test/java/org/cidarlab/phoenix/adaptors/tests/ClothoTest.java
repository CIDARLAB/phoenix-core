/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.controller.PhoenixController;
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
    
    public String getFilepath()
    {
        String filepath="";
        
        filepath = PhoenixController.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
//    @Test
    public void testSinglePartUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//    @Test
    public void testMultiPartUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//    @Test
    public void testGenericUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/generic.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//    @Test
    public void testFeatureUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, true);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void testFluorSpectraUpload() {
    
        String filePath = getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadFluorescenceSpectrums(toLoad);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Test
    public void testQuery() {
        
        ClothoAdaptor.queryFeatures();
        ClothoAdaptor.queryFluorophores();
        ClothoAdaptor.queryNucSeqs();
        ClothoAdaptor.queryPolynucleotides();
        ClothoAdaptor.queryParts();
    }
      
    public ClothoTest() {
        
    }
    
    public static void main(String[] args) {
        ClothoTest t = new ClothoTest();
//        t.testFeatureUpload();
        t.testQuery();
        t.testFluorSpectraUpload();

    }
}
