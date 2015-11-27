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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Detector;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Medium;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Sample;
import org.cidarlab.phoenix.core.dom.Sample.SampleType;
import org.cidarlab.phoenix.core.dom.Titration;

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
    public static File generateTestingInstructions(List<AssignedModule> amodules, String filePath) throws IOException {
        
        File testingInstructions = new File(filePath + "/testingInstructionsTest.csv");
        FileWriter instructionsFileWriter = new FileWriter(testingInstructions);
//        BufferedWriter instructionsBufferedWriter;
//        instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter);
        try (BufferedWriter instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter)) {
            instructionsBufferedWriter.write("FILENAME,PART,CONTROL,MEDIA,TIME,REGULATION");
            instructionsBufferedWriter.newLine();
            instructionsBufferedWriter.write(",,beads,,,");
            instructionsBufferedWriter.newLine();
            instructionsBufferedWriter.write(",,negative,,,");
            instructionsBufferedWriter.newLine();
            
            
            //Search for Color Controls
            Set<AssignedModule> colorControlSet = new HashSet<>();
            Set<String> color = new HashSet<>();
            System.out.println("Color Controls!!");
            for(AssignedModule amodule:amodules){
                for(AssignedModule control:amodule.getControlModules()){
                    if(control.getRole().equals(ModuleRole.COLOR_CONTROL)){
                        if (!colorControlSet.contains(control)) {
                            colorControlSet.add(control);
                            for (PrimitiveModule pm : control.getSubmodules()) {
                                if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                                    String channelColor = Utilities.getChannelsMap().get(pm.getModuleFeature().getName());
                                    if(!color.contains(channelColor)){
                                        color.add(channelColor);
                                        String line = ",,"+channelColor+",,,";
                                        instructionsBufferedWriter.write(line);
                                        instructionsBufferedWriter.newLine();
                                        
                                        System.out.println(channelColor);
                                        
                                    }
                                    
                                }
                            }
                        }
                        
                    }
                }
            }
            
            for(AssignedModule amodule:amodules){
                String line = "";
                for(Experiment experiment:amodule.getExperiments()){
                    List<String> times = experiment.getTimes();
                    List<Medium> media = experiment.getMediaConditions();
                    
                    switch(experiment.getExType()){
                        case EXPRESSION:
                            for (Medium medium : media) {
                                for (String time : times) {
                                    line = "," + amodule.getShortName() + ",," + medium.getName() + "," + time + ",";
                                    for (int i = 0; i < 3; i++) {
                                        instructionsBufferedWriter.write(line);
                                        instructionsBufferedWriter.newLine();
                                    }
                                }
                            }
                            break;
                        case DEGRADATION:
                            //System.out.println("Degradation::");
                            for(PrimitiveModule pm:amodule.getSubmodules()){
                                if(pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)){
                                    List<Arc> arcs = new ArrayList<Arc>();
                                    arcs = pm.getModuleFeature().getArcs();
                                    for(Arc arc:arcs){
                                        Feature regulator = arc.getRegulator();
                                        String regulatorName = regulator.getName();
                                        Titration titration = Utilities.getSmallMoleculeTitration().get(regulatorName);
                                        for(Medium medium:media){
                                            Double titreVal = titration.getTitrationValues().get(titration.getTitrationValues().size()-1);
                                            int titreInt = titreVal.intValue();
                                            String mediumVal = medium.getName() +" + ("+ titreInt + " "+titration.getUnits()+" "+titration.getSmallMolecule()+")";
                                            for (String time : times) {
                                                line = "," + amodule.getShortName() + ",," + mediumVal + "," + time + ",";
                                                for (int i = 0; i < 3; i++) {
                                                    instructionsBufferedWriter.write(line);
                                                    instructionsBufferedWriter.newLine();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case REGULATION:
                            
                            System.out.println("Regulation ::");
                            //Find regulation Control Channel
                            String regChannel= "";
                            for(AssignedModule controlModule:amodule.getControlModules()){
                                if(controlModule.getRole().equals(ModuleRole.REGULATION_CONTROL)){
                                    System.out.println("Regulation Control Found");
                                    for(PrimitiveModule pm:controlModule.getSubmodules()){
                                        if(pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT)){
                                            regChannel = Utilities.getChannelsMap().get(pm.getModuleFeature().getName());
                                        }
                                    }
                                }
                            }
                            
                            //Find Expressee Channel
                            String expFPChannel = "";
                            for(PrimitiveModule pm:amodule.getSubmodules()){
                                if(pm.getModuleFeature().getRole().equals(FeatureRole.CDS_FLUORESCENT)){
                                    expFPChannel = Utilities.getChannelsMap().get(pm.getModuleFeature().getName());
                                }
                            }
                            String regulationVal = expFPChannel+"| "+regChannel;
                            for(PrimitiveModule pm:amodule.getSubmodules()){
                                if(pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)){
                                    List<Arc> arcs = new ArrayList<Arc>();
                                    arcs = pm.getModuleFeature().getArcs();
                                    for(Arc arc:arcs){
                                        Feature regulator = arc.getRegulator();
                                        String regulatorName = regulator.getName();
                                        Titration titration = Utilities.getSmallMoleculeTitration().get(regulatorName);
                                        for(Medium medium:media){
                                            for (Double titreVal : titration.getTitrationValues()) {
                                                int titreInt = titreVal.intValue();
                                                String mediumVal = medium.getName() + " + (" + titreInt + " " + titration.getUnits() + " " + titration.getSmallMolecule() + ")";
                                                for (String time : times) {
                                                    line = "," + amodule.getShortName() + ",," + mediumVal + "," + time + ","+regulationVal;
                                                    for (int i = 0; i < 3; i++) {
                                                        instructionsBufferedWriter.write(line);
                                                        instructionsBufferedWriter.newLine();
                                                    }
                                                }
                                            }
                                            
                                        }
                                    }
                                }
                            }
                            
                            
                            break;
                        case SMALL_MOLECULE:
                            System.out.println("Small Molecule");
                            //Find the Expressee Titration
                            Titration expresseeTitre = new Titration();
                            for(PrimitiveModule pm:amodule.getSubmodules()){
                                if(pm.getModuleFeature().getRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)){
                                    System.out.println("Expressee Titre Module Feature " + pm.getModuleFeature().getName());
                                    expresseeTitre = Utilities.getSmallMoleculeTitration().get(pm.getModuleFeature().getName());
                                }
                            }
                            
                            for(PrimitiveModule pm:amodule.getSubmodules()){
                                if(pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)){
                                    for(Arc arc:pm.getModuleFeature().getArcs()){
                                        Titration promExpresseeTitre = new Titration();
                                        promExpresseeTitre = Utilities.getSmallMoleculeTitration().get(arc.getRegulator().getName());
                                        for (Medium medium : media) {
                                            
                                            for (Double titreVal : expresseeTitre.getTitrationValues()) {
                                                String mediumVal = medium.getName() + "_"+promExpresseeTitre.getTitrationValues().get(promExpresseeTitre.getTitrationValues().size()-1).intValue()+"_"+promExpresseeTitre.getUnits()+"_"+promExpresseeTitre.getSmallMolecule();
                                                mediumVal += " + ("+titreVal.intValue()+" "+expresseeTitre.getUnits()+" "+expresseeTitre.getSmallMolecule()+")";
                                                for (String time : times) {
                                                    line = "," + amodule.getShortName() + ",," + mediumVal + "," + time + ",";
                                                    for (int i = 0; i < 3; i++) {
                                                        instructionsBufferedWriter.write(line);
                                                        instructionsBufferedWriter.newLine();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        
        return testingInstructions;
    }
    
    public static File generateNameMapFile(List<AssignedModule> amodules, String filePath) throws IOException{
        File testingMap = new File(filePath + "/nameMapFileTest.csv");
        FileWriter mapFileWriter = new FileWriter(testingMap);
        try (BufferedWriter writer = new BufferedWriter(mapFileWriter)) {
            writer.write("Short Name,Name,Custom Name");
            writer.newLine();
            for(AssignedModule amodule:amodules){
                writer.write(amodule.getShortName()+","+amodule.getName()+",");
                writer.newLine();
            }
        
        }
        
        
        return testingMap;
        
    }
    
    
    //Method for producing testing instructions from Experiments
    /*
    public static File generateTestingInstructions(List<Experiment> experiments, String filePath) throws IOException {
        
        File testingInstructions = new File(filePath + "/testingInstructionsTest.csv");
        FileWriter instructionsFileWriter = new FileWriter(testingInstructions);
//        BufferedWriter instructionsBufferedWriter;
//        instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter);
        try (BufferedWriter instructionsBufferedWriter = new BufferedWriter(instructionsFileWriter)) {
            instructionsBufferedWriter.write("FILENAME,PART,CONTROL,MEDIA,TIME,REGULATION");
            instructionsBufferedWriter.write(",,beads,,,");
            instructionsBufferedWriter.write(",,negative,,,");
            
            //Get all the samples from all experiments under consideration
            HashSet<String> sampleIDs = new HashSet<String>();
            List<Sample> allSamples = new ArrayList<>();
            
            for (Experiment ex : experiments) {
                HashSet<String> sampleIDsThisExpt = new HashSet<>();
                List<Sample> allSamplesEx = new ArrayList<>();
                //allSamplesEx = ex.getAllSamples();
                for (Sample s : allSamplesEx) {
                    if (s.getType().equals(SampleType.NEGATIVE) || s.getType().equals(SampleType.FLUORESCENT) || s.getType().equals(SampleType.BEADS)) {
                        if (!sampleIDs.contains(s.getClothoID())) {
                            allSamples.add(s);
                            sampleIDs.add(s.getClothoID());
                        }
                    } else if (s.getType().equals(SampleType.EXPRESSION_DEGRATATION) || s.getType().equals(SampleType.REGULATION)) {
                        if (!sampleIDs.contains(s.getClothoID())) {
                            sampleIDsThisExpt.add(s.getClothoID());
                            allSamples.add(s);
                        }
                    } else {
                        allSamples.add(s);
                    }
                }
                sampleIDs.addAll(sampleIDsThisExpt);
            }
            
            //Sort the sample lines
            if (!allSamples.isEmpty()) {
                Collections.sort(allSamples, new Comparator<Sample>() {
                    @Override
                    public int compare(Sample s1, Sample s2) {
                        
                        String name1 = "";
                        List<Polynucleotide> polynucleotides1 = s1.getPolynucleotides();
                        if (!s1.getType().equals(SampleType.BEADS) && !s1.getType().equals(SampleType.NEGATIVE)) {
                            for (Polynucleotide pN : polynucleotides1) {
                                if (name1.isEmpty()) {
                                    name1 = pN.getClothoID();
                                } else {
                                    name1 = name1 + "_" + pN.getClothoID();
                                }
                            }
                        } else {
                            name1 = s1.getType().toString();
                        }

                        String name2 = "";
                        if (!s2.getType().equals(SampleType.BEADS) && !s2.getType().equals(SampleType.NEGATIVE)) {
                            List<Polynucleotide> polynucleotides2 = s2.getPolynucleotides();
                            for (Polynucleotide pN : polynucleotides2) {
                                if (name2.isEmpty()) {
                                    name2 = pN.getClothoID();
                                } else {
                                    name2 = name2 + "|" + pN.getClothoID();
                                }
                            }
                        } else {
                            name2 = s2.getType().toString();
                        }

                        return name1.compareTo(name2);
                    }
                });
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
                            name = name + "|" + pN.getClothoID();
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
    */
    public static File generateshortNameMapFile(List<Experiment> experiments, String filepath){
        File file = new File(filepath);
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bwriter = new BufferedWriter(writer);
            for(Experiment experiment:experiments){
                bwriter.write(experiment.getAmShortName()+","+experiment.getAmName()+","+experiment.getName());
            }
            bwriter.close();
        } catch (IOException ex) {
            Logger.getLogger(PhoenixInstructions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return file;
    }
    
    //This is the method for generating a file that maps the phoenix short names to custom user names
    /*
    public static File generateNameMapFile (File keyFile, String filePath) throws IOException {

        //First, read a keyfile and find all unique names in the PART column
        BufferedReader reader = new BufferedReader(new FileReader(keyFile.getAbsolutePath()));
        String line = reader.readLine();
        
        //Read each line of the input file to parse parts
        ArrayList<String> uniquePartNames = new ArrayList();
        while (line != null) {
            while (line.matches("^[\\s,]+")) {
                line = reader.readLine();
            }
            
            //Obtain information to find relevant samples in an experiment
            String[] sampleVals = line.split(",");
            
            if (sampleVals.length >= 2) {
                String partName = sampleVals[1].trim();
                if (!uniquePartNames.contains(partName)) {
                    uniquePartNames.add(partName);
                }
            }
        }
        
        //Create a new file for the map for custom names
        File nameMapFile = new File(filePath + "/nameMapFileTest.csv");
        FileWriter nameMapFileWriter = new FileWriter(nameMapFile);
        
        try (BufferedWriter nameMapBufferedWriter = new BufferedWriter(nameMapFileWriter)) {
            nameMapBufferedWriter.write("PHOENIX_SHORTNAME,CUSTOM_NAME");
            
            //For each unique part name, create a line in the map file
            for (String part : uniquePartNames) {
                nameMapBufferedWriter.write("\n,," + part + ",");
            }
        }
        
        return nameMapFile;
    }
    */
    //Method for reading results file from R
    //This method needs to be fixed to assign results to the experiment, not the samples
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
        List<Sample> allSamples = new ArrayList<>();
        //allSamples = e.getAllSamples();
        
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
                
//                if (sPnNames.equals(pnNames) && sMediaName.equalsIgnoreCase(mediaName) && sSmallMoleculeName.equalsIgnoreCase(smallMoleculeName) && sConcentration.equalsIgnoreCase(concentration)) {
//                    
//                    if (s.getResults() == null) {
//                        s.setResults(new HashMap());
//                    }
//                                        
//                    //Assign values to the detectors in the results file
//                    for (int j = 3; j < sampleVals.length; j++) {
//                        double parseDouble = Double.parseDouble(sampleVals[j]);
//                        Detector d = detectors.get(j - 3);
//                        s.getResults().put(d, parseDouble);
//                    }
//                }
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
