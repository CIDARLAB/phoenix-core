/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.GenbankReaderHelper;

/**
 *
 * This class contains methods for reading a Genbank file
 * 
 * @author evanappleton
 */
public class BenchlingAdaptor {
    
    //This method is for reading a Genbank file with Biojava 3.1.0
    //This parsing method seems flawed and cannot read features properly, currently on hold
    public static String readGenbankFileBiojava3 (File input) throws Exception {
    
        LinkedHashMap<String, DNASequence> readGenbankDNASequence = GenbankReaderHelper.readGenbankDNASequence(input, true);
        
        return "";
    }
    
    //This method is for reading a Genbank file with Biojava 1.9.0
    //This parsing method seems flawed and cannot read features properly, currently on hold
    public static String readGenbankFileBiojava1 (File input) throws Exception {
    
        BufferedReader reader = new BufferedReader(new FileReader(input));
        
        
        return "";
    }
    
}
