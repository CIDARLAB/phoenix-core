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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author evanappleton
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    
    @Test
    public void testPlasmidLibraryUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_6315.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
//    @Test
    public void testPlasmidLibraryBPOnlyUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_bp_only.gb";
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
    
    @Test
    public void testFeatureUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.queryFeatures();
            ClothoAdaptor.uploadSequences(toLoad, true);
            ClothoAdaptor.queryFeatures();
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        String fluorfilePath = getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv";
        File toFluorLoad = new File(fluorfilePath);
        try {
            ClothoAdaptor.uploadFluorescenceSpectrums(toFluorLoad);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Test
    public void testQuery() {
        System.out.println("Start Test");
        
        ClothoAdaptor.queryFeatures();
        System.out.println("End of query Features");
        
        ClothoAdaptor.queryFluorophores();
        System.out.println("End of query Flourophores");
    
        ClothoAdaptor.queryPolynucleotides();
        System.out.println("End of query Polynucleotides");
    
        ClothoAdaptor.queryParts();
        System.out.println("End of query Parts");
        
        ClothoAdaptor.queryCytometers();
        System.out.println("End of query Cytometers");
        System.out.println("End of Tests");
    }
    
    @Test
    public void testCytometerUpload() {
        
        String filePath = getFilepath() + "/src/main/resources/FluorescentProteins/cosbi_fortessa_bd.csv";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadCytometer(toLoad);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
      
//    public ClothoTest() {
//        
//    }
//    
//    public static void main(String[] args) {
//        ClothoTest t = new ClothoTest();
////        t.testQuery();
////        t.testMultiPartUpload();
//        t.testPlasmidLibraryUpload();
//        String t2 = "";
//    }
}
