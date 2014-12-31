/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava.bio.BioException;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor;
import org.clothocad.model.Feature;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Part;
import org.clothocad.model.Polynucleotide;
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
    
    
    @Test
    public void testSinglePartUpload() {
        try {
            String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            ArrayList<NucSeq> nucSeqs = BenchlingAdaptor.getNucSeq(input);
            HashSet<Part> parts = BenchlingAdaptor.getMoCloParts(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Test
    public void testMultiPartUpload() {
        try {
            
            String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            ArrayList<NucSeq> nucSeqs = BenchlingAdaptor.getNucSeq(input);
            HashSet<Part> parts = BenchlingAdaptor.getMoCloParts(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
