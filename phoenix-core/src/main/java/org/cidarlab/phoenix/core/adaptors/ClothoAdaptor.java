/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.*;
import org.clothocad.model.Feature;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Part;
import org.clothocad.model.Polynucleotide;

/**
 * This class has all methods for sending and receiving information to Clotho
 * 
 * @author evanappleton
 */
public class ClothoAdaptor {
    
    /*
     * This method is for reading a Benchling-produced Multipart Genbank file with Biojava 1.9.0
     * It creates Clotho Polynucleotides, Parts, Sequences, Annotations and Features
     */
    public static void clothoGenbankUpload(File input) throws Exception {

        //Get annotations, features, polynucleotides, nucseqs and parts from a multi-part genbank file
        HashSet<Feature> features = getFeatures(input);
        HashSet<Polynucleotide> polyNucs = getPolynucleotide(input);
        ArrayList<NucSeq> nucSeqs = getNucSeq(input);
        HashSet<Part> parts = getMoCloParts(input);
        
        //
    }
    
}
