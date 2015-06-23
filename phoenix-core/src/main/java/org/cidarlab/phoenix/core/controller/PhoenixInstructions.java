/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Medium;
import org.cidarlab.phoenix.core.dom.Sample;
import org.cidarlab.phoenix.core.dom.Sample.SampleType;

/**
 * This class is responsible for producing and interpreting experimental instructions in Phoenix
 * This includes part assembly and determination of testing points
 * 
 * @author evanappleton
 */
public class PhoenixInstructions {
    
    public static String getFilepath() {
        String filepath = PhoenixInstructions.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0, filepath.indexOf("/target/"));
        return filepath;
    }
    
    //Method for producing testing instructions from Experiments
    public static File generateTestingInstructions(List<Experiment> experiments, String filePath) throws IOException {
        
        File testingInstructions = new File(filePath + "/testingInstructionsTest.csv");
        FileWriter instructionsFileWriter = new FileWriter(testingInstructions);
        try (BufferedWriter instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter)) {
            instructionsBufferedWriter.write("FILENAME,PART,CONTROL,MEDIA,TIME");
            
            //Get all the samples from all experiments under consideration
            List<Sample> allSamples = new ArrayList<>();
            for (Experiment ex : experiments) {
                allSamples.addAll(ex.getAllSamples());
            }
            
            //Write one line per sample - this will change to triplicates after debugging
            for (Sample s : allSamples) {
                if (s.getType().equals(SampleType.BEADS) || s.getType().equals(SampleType.NEGATIVE)) {                
                    instructionsBufferedWriter.write("\n," + s.getClothoID() + "," + s.getType() + "," + "," + ",");
                } else if (s.getType().equals(SampleType.FLUORESCENT)) {
                    instructionsBufferedWriter.write("\n," + s.getClothoID() + "," + s.getType() + "," + "," + ",");
                } else {
                    Medium media = s.getMedia();
                    String mediaString = media.getName();                
                    if (media.getSmallmolecule()!= null) {
                        mediaString = media.getConcentration() + "_" + media.getSmallmolecule() + "_" + mediaString;
                    }
                    instructionsBufferedWriter.write("\n," + s.getClothoID() + ",," + mediaString + "," + s.getTime() + ",");
                }   
            }
        }
        
        return testingInstructions;
    }
    
    
    //Method for producing testing instructions from Experiments
    public static void parseTestingInstructions(File instructionsFile) {
        
    }
}
