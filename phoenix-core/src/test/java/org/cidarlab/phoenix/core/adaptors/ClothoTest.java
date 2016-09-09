/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.controller.Args;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
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
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.closeConnection();
    }
    
//    @Test
    public void testMultiPartUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.closeConnection();
    }
    
//    @Test
    public void testPlasmidLibraryUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_72715.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.closeConnection();
    }
    
//    @Test
    public void testPlasmidLibraryBPOnlyUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_bp_only.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        conn.closeConnection();
    }
    
//    @Test
    public void testGenericUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/generic.gb";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadSequences(toLoad, false,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.closeConnection();
    }
    
//    @Test
    public void testFeatureUpload() {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb";
        File toLoad = new File(filePath);
        try {
//            ClothoAdaptor.queryFeatures();
            Map featureQuery = new HashMap();
            featureQuery.put("schema", Feature.class.getCanonicalName());
        
            ClothoAdaptor.uploadSequences(toLoad, true,clothoObject);
            ClothoAdaptor.queryFeatures(featureQuery,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        String fluorfilePath = getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv";
        File toFluorLoad = new File(fluorfilePath);
        try {
            ClothoAdaptor.uploadFluorescenceSpectrums(toFluorLoad,clothoObject);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conn.closeConnection();
    }
    
//    @Test
    public void testCytometerUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/FluorescentProteins/cosbi_fortessa_bd.csv";
        File toLoad = new File(filePath);
        try {
            ClothoAdaptor.uploadCytometerData(toLoad,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        conn.closeConnection();
    }
    
//    @Test
    public void testQuery() {
        System.out.println("Start Test");
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        Map featureQuery = new HashMap();
        featureQuery.put("schema", Feature.class.getCanonicalName());
        ClothoAdaptor.queryFeatures(featureQuery,clothoObject);
        System.out.println("End of query Features");
        
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", Fluorophore.class.getCanonicalName());
        ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject);
        System.out.println("End of query Flourophores");
        
        Map polyNucQuery = new HashMap();
        polyNucQuery.put("schema", Polynucleotide.class.getCanonicalName());
        ClothoAdaptor.queryPolynucleotides(polyNucQuery,clothoObject);
        System.out.println("End of query Polynucleotides");
        
        Map partQuery = new HashMap();
        partQuery.put("schema", Part.class.getCanonicalName());
        ClothoAdaptor.queryParts(partQuery,clothoObject);
        System.out.println("End of query Parts");
        
        Map cytometerQuery = new HashMap();
        cytometerQuery.put("schema", Cytometer.class.getCanonicalName());
        ClothoAdaptor.queryCytometers(cytometerQuery,clothoObject);
        System.out.println("End of query Cytometers");
        
        System.out.println("End of Tests");
        
        conn.closeConnection();
    }
    
    public ClothoTest() {
    }
    
    public static void main(String[] args) {
        ClothoTest t = new ClothoTest();
        t.testFeatureUpload();
        t.testPlasmidLibraryUpload();
        t.testCytometerUpload();
    }
}
