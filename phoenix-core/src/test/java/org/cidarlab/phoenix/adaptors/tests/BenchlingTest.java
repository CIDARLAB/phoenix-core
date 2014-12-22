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
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.getFeatures;
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.getMoCloParts;
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.getNucSeq;
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.getPolynucleotide;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
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
    
    @Test
    public void testSinglePartUpload() {
        try {
            
            String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_single.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Feature> features = getFeatures(input);
            HashSet<Polynucleotide> polyNucs = getPolynucleotide(input);
            ArrayList<NucSeq> nucSeqs = getNucSeq(input);
            HashSet<Part> parts = getMoCloParts(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Test
    public void testMultiPartUpload() {
        try {
            
            String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
            File input = new File(filePath);
            
            //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
            HashSet<Feature> features = getFeatures(input);
            HashSet<Polynucleotide> polyNucs = getPolynucleotide(input);
            ArrayList<NucSeq> nucSeqs = getNucSeq(input);
            HashSet<Part> parts = getMoCloParts(input);
            
        } catch (FileNotFoundException | NoSuchElementException | BioException ex) {
            Logger.getLogger(BenchlingTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
