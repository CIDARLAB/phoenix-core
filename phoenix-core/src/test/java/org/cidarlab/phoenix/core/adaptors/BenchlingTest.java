/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.BioException;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author evanappleton
 */
public class BenchlingTest {
 
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
        
        filepath = BenchlingAdaptor.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    
    //@Test
    public void testSinglePartUpload() {
        try {
            String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
//    @Test
    public void testMultiPartUpload() {
        try {
            
            String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_6315.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testFeatureLibraryUpload() {
        try {
            
            String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb";
            File input = new File(filePath);
            
            //Get features from a multi-part genbank file
            BenchlingAdaptor.getFeatures(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Test
    public void testFluorophoreUpload() {
        try {
            
            String filePath = getFilepath() + "/src/main/resources/FluorescentProteins/phoenix_fp_set.gb";
            File input = new File(filePath);
            
            //Get features, FPs from a multi-part genbank file
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Fluorophore> FPs = BenchlingAdaptor.getFluorophores(features);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public BenchlingTest() {
//        
//    }
//    
//    public static void main(String[] args) {
//        BenchlingTest t = new BenchlingTest();
//        t.testFeatureLibraryUpload();
//        String t2 = "";
//    }
}
