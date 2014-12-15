/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;

/**
 *
 * This class contains methods for reading a Genbank file
 * 
 * @author evanappleton
 */
public class BenchlingAdaptor {
    
    //This method is for reading a Genbank file with Biojava 1.9.0
    //This parsing method seems flawed and cannot read features properly, currently on hold
    public static String readGenbankFileBiojava (File input) throws Exception {
    
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);
        while (readGenbank.hasNext()) {
            Sequence nextSequence = readGenbank.nextSequence();
            String newSt = "";
        }
        return "";
    }
    
}
