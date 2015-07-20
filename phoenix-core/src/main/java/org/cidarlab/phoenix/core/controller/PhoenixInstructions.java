/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Detector;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Medium;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
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
//        BufferedWriter instructionsBufferedWriter;
//        instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter);
        try (BufferedWriter instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter)) {
            instructionsBufferedWriter.write("FILENAME,PART,CONTROL,MEDIA,TIME");
            
            //Get all the samples from all experiments under consideration
            HashSet<String> sampleIDs = new HashSet<String>();
            List<Sample> allSamples = new ArrayList<>();
            for (Experiment ex : experiments) {
                List<Sample> allSamplesEx = ex.getAllSamples();
                for (Sample s : allSamplesEx) {
                    if (!sampleIDs.contains(s.getClothoID())) {
                        allSamples.add(s);
                        sampleIDs.add(s.getClothoID());
                    }
                }
            }
            
            //Write one line per sample - this will change to triplicates after debugging
            for (Sample s : allSamples) {
                if (s.getType().equals(SampleType.BEADS) || s.getType().equals(SampleType.NEGATIVE)) {                
                    instructionsBufferedWriter.write("\n,," + s.getType() + ",,");
                } else if (s.getType().equals(SampleType.FLUORESCENT)) {
                    
                    String name = "";
                    List<Polynucleotide> polynucleotides = s.getPolynucleotides();
                    for (Polynucleotide pN : polynucleotides) {
                        if (name.isEmpty()) {
                            name = pN.getClothoID();
                        } else {
                            name = name + "_" + pN.getClothoID();
                        }
                    }
                    
                    instructionsBufferedWriter.write("\n," + name + "," + s.getType() + ",,");
                } else {
                    
                    String name = "";
                    List<Polynucleotide> polynucleotides = s.getPolynucleotides();
                    for (Polynucleotide pN : polynucleotides) {
                        if (name.isEmpty()) {
                            name = pN.getClothoID();
                        } else {
                            name = name + "|" + pN.getClothoID();
                        }
                    }
                    
                    Medium media = s.getMedia();
                    String mediaString = media.getName();                
                    if (media.getSmallmolecule()!= null) {
                        mediaString = media.getSmallmolecule().getConcentration() + "_" + media.getSmallmolecule().getName() + "_" + mediaString;
                    }
                    instructionsBufferedWriter.write("\n," + name + ",," + mediaString + "," + s.getTime() + ",");
                }   
            }
        }
        
        return testingInstructions;
    }
    
    
    //Method for reading results file from R
    public static void parseTestingResults(File resultsFile, Experiment e) throws FileNotFoundException, IOException {
        
        BufferedReader reader = new BufferedReader(new FileReader(resultsFile.getAbsolutePath()));
        String line = reader.readLine();
        
        //Initialize detector set
        String[] columns = line.split(",");
        List<Detector> detectors = new ArrayList<>();
        for (int i = 3; i < columns.length; i++) {
            Detector d = new Detector();
            d.setParameter(columns[i]);
            detectors.add(d);
        }
        
        //Get samples from input experiment
        List<Sample> allSamples = e.getAllSamples();
        
        line = reader.readLine();
        
        //Read each line of the input file to parse parts
        while (line != null) {
            while (line.matches("^[\\s,]+")) {
                line = reader.readLine();
            }
            
            //Obtain information to find relevant samples in an experiment
            String[] sampleVals = line.split(",");
            
            //Get polynucleotide names
            String polynucleotideNames = sampleVals[0];
            String[] singlePnNames = polynucleotideNames.split("|");
            List<String> pnNames = new ArrayList(Arrays.asList(singlePnNames));
            
            String time = sampleVals[2];
            
            //Get media
            String media = sampleVals[1];
            String mediaName = "";
            String smallMoleculeName = "";
            String concentration = "";
            String[] mediaConditions = media.split("_");
            if (mediaConditions.length == 1) {
                mediaName = mediaConditions[0];
            } else if (mediaConditions.length == 3) {
                smallMoleculeName = mediaConditions[1];
                concentration = mediaConditions[0];
            }                        
            
            for (Sample s : allSamples) {
                
                List<String> sPnNames = new ArrayList<>();
                for (Polynucleotide pn : s.getPolynucleotides()) {
                    sPnNames.add(pn.getAccession());
                }
                
                String sMediaName = s.getMedia().getName();
                
                String sSmallMoleculeName = "";
                String sConcentration = "";
                if (s.getMedia().getSmallmolecule() != null) {
                    sSmallMoleculeName = s.getMedia().getSmallmolecule().getName();
                    sConcentration = s.getMedia().getSmallmolecule().getConcentration().toString();
                }
                
                if (sPnNames.equals(pnNames) && sMediaName.equalsIgnoreCase(mediaName) && sSmallMoleculeName.equalsIgnoreCase(smallMoleculeName) && sConcentration.equalsIgnoreCase(concentration)) {
                    
                    if (s.getResults() == null) {
                        s.setResults(new HashMap());
                    }
                                        
                    //Assign values to the detectors in the results file
                    for (int j = 3; j < sampleVals.length; j++) {
                        double parseDouble = Double.parseDouble(sampleVals[j]);
                        Detector d = detectors.get(j - 3);
                        s.getResults().put(d, parseDouble);
                    }
                }
            }
                       
            line = reader.readLine();
        }
        
    }
    
    //Method for changing key file name in R script
    public static void changeKeyFileName (File Rscript, String keyFileName) {
        
        try {
            String currentLine, editedLine;
            
            String path = Rscript.getAbsolutePath();
            File outTest = new File(path.substring(0, path.lastIndexOf("/") + 1) + "analyze_" + keyFileName + ".R");
            FileWriter fw = new FileWriter(outTest);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.flush();
            
            FileReader fr = new FileReader(Rscript);
            BufferedReader br = new BufferedReader(fr);

            while ((currentLine = br.readLine()) != null) {
                if (currentLine.startsWith("key <- read.csv")) {
                    editedLine = "key <- read.csv(\"" + keyFileName + ".csv\", header = TRUE)"; 
                    bw.write(editedLine + "\n");
                } else if (currentLine.startsWith("filename <-")) {
                    editedLine = "filename <- \"" + keyFileName + ".csv\", header = TRUE)"; 
                    bw.write(editedLine + "\n");
                } else {
                    bw.write(currentLine + "\n");
                }
            }
            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
