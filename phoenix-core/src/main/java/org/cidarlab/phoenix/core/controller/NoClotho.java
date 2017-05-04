/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.biojava.bio.BioException;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor;
import static org.cidarlab.phoenix.core.adaptors.ClothoAdaptor.annotate;
import org.cidarlab.phoenix.core.adaptors.IBioSimAdaptor;
import org.cidarlab.phoenix.core.dataprocessing.AnalyzeData;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.AssemblyParameters;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.ExperimentProcessedData;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Person;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.Vector;

/**
 *
 * @author prash
 */
public class NoClotho {

    @Getter
    @Setter
    private Set<Feature> features;

    @Getter
    @Setter
    private Set<Fluorophore> fluorophores;

    @Getter
    @Setter
    private Cytometer cytometer;

    @Getter
    @Setter
    private Set<Polynucleotide> polynucleotide;

    
    @Getter
    @Setter
    private AssemblyParameters ap;
    
    private AtomicInteger counter ;
    
    private Set<String> allIds; 
    
    public NoClotho() {
        features = new HashSet<Feature>();
        fluorophores = new HashSet<Fluorophore>();
        polynucleotide = new HashSet<Polynucleotide>();
        allIds = new HashSet<String>();
        counter = new AtomicInteger();
        initializeAssemblyParameters();
    }
    
    private void initializeAssemblyParameters(){
        String[] efficiency = new String[]{"1.0", "1.0", "1.0", "1.0"};
        ap = new AssemblyParameters();
        List<String> effArray = Arrays.asList(efficiency);
        
        this.ap.setEfficiency(effArray);
        this.ap.setMethod("moclo");
        this.ap.setOligoNameRoot("phoenix");
        this.ap.setName("default");
    
    }
    
    
    
    public static void main(String[] args) {
        
        String featureFilepath = Utilities.getResourcesFilepath() + "BenchlingGenbankFiles/phoenix_feature_lib.gb";
        String fluorFilepath = Utilities.getResourcesFilepath() + "FluorescentProteins/fp_spectra.csv";
        String plasmidFilepath = Utilities.getResourcesFilepath() + "BenchlingGenbankFiles/phoenix_plasmid_lib_72715.gb";
        String cytometerFilepath = Utilities.getResourcesFilepath() + "FluorescentProteins/cosbi_fortessa_bd.csv";
        NoClotho nc = new NoClotho();
        nc.addFeatures(featureFilepath);
        nc.addFluorophores(fluorFilepath);
        nc.addPlasmid(plasmidFilepath);
        nc.addCytometer(cytometerFilepath);
        nc.assignNoClothoID();
        
//        for(Fluorophore f:nc.fluorophores){
//            System.out.println(f.getName());
//        }
        
        String tmpfilepath = Utilities.getResourcesFilepath() + "tmp/";
        Utilities.makeDirectory(tmpfilepath);
        
        File structureFile = new File(Utilities.getResourcesFilepath() + "miniEugeneFiles/inverter.eug");
        try {
            //Step 1 : Get Best Module and Create Experiment Instructions
            Module bestModule = PhoenixController.initializeDesign(structureFile, null, nc);
            nc.assignID(bestModule);
            PhoenixController.createExperimentInstructions(bestModule, Utilities.getResourcesFilepath() + "InstructionFiles", nc);
            System.out.println("==========================================================================\n==============================End of Step 1.==============================\n==========================================================================\n\n");

            //Step 2 : Get Analyzed Data
            String directory = Utilities.getResourcesFilepath() + "RTest/results/";
            String keyFile = Utilities.getResourcesFilepath() + "InstructionFiles/testingInstructionsTest.csv";
            String mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest.csv";
            
            String ea_mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest_ea_filled.csv";
            AnalyzeData.fillOutNameMap(ea_mapFile, mapFile);
            
            Map<String, String> nameMap = AnalyzeData.parseKeyMapFiles(mapFile);

            Map<String, AssignedModule> expexe = new HashMap<String, AssignedModule>();
            expexe = PhoenixController.getShortNameEXPEXEMap(bestModule);
            
            
            
            Map<String, ExperimentProcessedData> processedMap = new HashMap<String, ExperimentProcessedData>();
            AnalyzeData.directoryWalk(directory, directory, processedMap);
//            System.out.println(processedMap.size());
//            System.out.println(processedMap.keySet());
//            System.out.println("\n\n=======================================\n");
//            
//            for(String expexeKey: expexe.keySet()){
//                System.out.print(expexe.get(expexeKey).getRole()  + "::" );
//                System.out.println(expexe.get(expexeKey).getFeatureString());
//            }
            
            for(String expexeKey: expexe.keySet()){
                
                AssignedModule am = expexe.get(expexeKey);
                for(String processKey : processedMap.keySet()){
                    if(am.getRole().equals(Module.ModuleRole.EXPRESSEE) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATOR)  || am.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSOR)){
                        if(!processedMap.get(processKey).getRole().equals(Module.ModuleRole.EXPRESSEE)){
                            continue;
                        }
                        boolean fpFlag = false;
                        boolean exeFlag = false;
                        boolean tagFlag = isTag(processKey);
                        boolean tagFeature = false;
                        String fusedFP = getFusedFP(processKey);
                        String exeFeature = getExpresee(processKey);
                        for(Feature f:am.getAllModuleFeatures()){
                            //System.out.println(f.getName());
                            if(f.getName().toLowerCase().equals(getFusedFPfeature(fusedFP).toLowerCase())){
                                fpFlag = true;
                            }
                            if(f.getName().toLowerCase().contains(exeFeature.toLowerCase())){
                                exeFlag = true;
                            }
                            if(f.getName().equals("TAG")){
                                tagFeature = true;
                            }
                        }
                        if(fpFlag && exeFlag){
                            if (tagFlag == tagFeature) {
                                List<String> tmpFiles = new ArrayList<String>();
                                //Do that Expressee Thaing here. 
                                Map<Double, String> reg = new HashMap<Double, String>();
                                for(Double d:processedMap.get(processKey).getRegulationData().keySet()){
                                    String filepath = tmpfilepath + "tmp" + nc.counter.getAndIncrement() + ".csv";
                                    tmpFiles.add(filepath);
                                    Utilities.writeToFile(filepath, processedMap.get(processKey).getRegulationData().get(d));
                                    reg.put(d, filepath);
                                }
                                String expresseeChannel = Utilities.getChannelsMap().get(getFusedFPfeature(fusedFP));
                                String regChannel = Utilities.getChannelsMap().get(getRegulatedFP(processKey));
                                String inducer = processedMap.get(processKey).getInducer();
                                System.out.println(inducer);
                                if(inducer == null){
                                    System.out.println(processKey);
                                }
                                //IBioSimAdaptor.estimateExpresseeParameters(processedMap.get(processKey).getDegradationFilepath(), reg, expresseeChannel, regChannel, true, processedMap.get(processKey).getInducer(), exeFeature, keyFile)
                                
                            }
                        }
                    }
                    if(am.getRole().equals(Module.ModuleRole.EXPRESSOR)){
                        if(!processedMap.get(processKey).getRole().equals(Module.ModuleRole.EXPRESSOR)){
                            continue;
                        }
                    }
                    
                }
            }
            

        } catch (Exception ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getExpresee(String val){
        String pieces[] = val.split("_");
        return pieces[0].trim();
    }
    
    private static String getFusedFP(String val){
        String pieces[] = val.split("_");
        return pieces[1].trim();
    }
    
    private static String getRegulatedFeature(String val){
        String pieces[] = val.split("_");
        for(int i=0;i< pieces.length;i++){
            if(pieces[i].trim().equals("EXPRESSEE")){
                return pieces[i+1].trim();
            }
        }
        return "";
    }
    
    
    
    private static String getRegulatedFP(String val){
        String pieces[] = val.split("_");
        String regF = "";
        for(int i=0;i< pieces.length;i++){
            if(pieces[i].trim().equals("EXPRESSEE")){
                regF = pieces[i+2].trim();
            }
        }
        switch(regF){
            case "B":
                return "EBFP2.ref";
            case "G":
                return "EGFPm.ref";
        }
        return "";
    }
    private static boolean isTag(String val){
        
        String pieces[] = val.split("_");
        if(pieces.length < 3){
            return false;
        }
        if(pieces[2].trim().equals("EXPRESSEE")){
            return false;
        }
        return true;
    }
   
    private static String getFusedFPfeature(String shortVer){
        switch(shortVer){
            case "GFP":
                return "EGFPm.ref";
        }
        
        return "";
    }
    
    
    
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    
    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
    
    public String getUUID(){
        
        String id = randomString(10);
        while(this.allIds.contains(id)){
            id = randomString(10);
        }
        this.allIds.add(id);
        return id;
//        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
//        UUID uuid = gen.generate();
//        return uuid.toString();
    }
    
    public void assignNoClothoID(){
        for(Feature f:this.features){
            f.setClothoID(getUUID());
        }
        for(Fluorophore f:this.fluorophores){
            f.setClothoID(getUUID());
        }
        for(Polynucleotide p:this.polynucleotide){
            p.setClothoID(getUUID());
        }
        this.cytometer.setClothoID(getUUID());
        this.ap.setClothoID(getUUID());
    }
    
    public void assignID(Module m){
        m.setClothoID(getUUID());
        
        for(AssignedModule am: m.getAssignedModules()){
            am.setClothoID(getUUID());
            for(AssignedModule cm:am.getControlModules()){
                cm.setClothoID(getUUID());
            }
            for(Experiment ex:am.getExperiments()){
                ex.setClothoID(getUUID());
            }
        }
        for(Module child:m.getChildren()){
            child.setClothoID(getUUID());
        }
        
    }
    
    
    
    
    public void addFeatures(String filepath) {
        File file = new File(filepath);
        try {
            this.features.addAll(BenchlingAdaptor.getFeatures(file));
            this.fluorophores.addAll(BenchlingAdaptor.getFluorophores(this.features));
            this.features.removeAll(this.fluorophores);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchElementException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BioException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addFluorophores(String filepath) {
        try {
            File file = new File(filepath);

            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            HashMap<String, HashMap<Double, Double>> spectralMaps = new HashMap<>();

            //The first line describes the spectra
            String line = reader.readLine();
            String[] spectra = line.split(",");
            int numSpectra = spectra.length;
            for (int i = 1; i < numSpectra; i++) {
                spectralMaps.put(spectra[i], new HashMap<Double, Double>());
            }
            line = reader.readLine();

            //Read each line of the input file to parse parts
            while (line != null) {
                String[] tokens = line.split(",");
                for (int j = 1; j < tokens.length; j++) {
                    if (!tokens[j].isEmpty()) {
                        spectralMaps.get(spectra[j]).put(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[j]));
                    }
                }
                line = reader.readLine();
            }

            for (String spectrum_name : spectralMaps.keySet()) {
                for (Fluorophore fl : this.fluorophores) {

                    //Match spectrums to fluorophore names
                    String flName = fl.getName();
                    if (spectrum_name.contains(flName.replaceAll(".ref", ""))) {

                        //Match excitation or emmission spectra
                        if (spectrum_name.contains("EX") || spectrum_name.contains("AB")) {
                            fl.setEx_spectrum(spectralMaps.get(spectrum_name));
                        } else if (spectrum_name.contains("EM")) {
                            fl.setEm_spectrum(spectralMaps.get(spectrum_name));
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPlasmid(String filepath){
        File file = new File(filepath);
        try {
            this.polynucleotide.addAll(BenchlingAdaptor.getPolynucleotide(file));
            removeDuplicatePartsVectors();
            annotateParts();
        } catch (NoSuchElementException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BioException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addCytometer(String filepath){
        File file = new File(filepath);
        HashSet<String> lasers = new HashSet<>();
        HashSet<String> filters = new HashSet<>();
        HashMap<String, ArrayList<String[]>> config = new HashMap<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String name = "";
            boolean laserMode = false;
            String laser;
            ArrayList<String[]> filterList = new ArrayList<>();
            while( (line = reader.readLine()) != null ){
                String pieces[] = line.split(",");
                if(laserMode){
                    if(!pieces[0].trim().isEmpty()){
                        laser = pieces[2].trim() + ":" + pieces[3].trim();
                        lasers.add(laser);
                        filterList = new ArrayList<>();
                        config.put(laser, filterList);
                    }
                    if (pieces.length > 6) {
                        if (pieces[7] != null) {
                            if (!pieces[7].trim().equals("")) {
                                String mirrorPieces[] = pieces[7].trim().split(" ");
                                String mirror = mirrorPieces[0].trim();//7
                                String filterPieces[] = pieces[8].trim().split(" ");
                                String filter = filterPieces[0].trim().replaceAll("/", ":");//8
                                filters.add(mirror);
                                filters.add(filter);
                                filterList.add(new String[]{mirror, filter});
                            }
                        }
                    }
                    
                
                }
                if(pieces[0].trim().equalsIgnoreCase("Configuration Name")){
                    name = pieces[1].trim();
                }
                if(pieces[0].trim().equalsIgnoreCase("Laser Name")){
                    laserMode = true;
                }
            }
            
            this.cytometer = new Cytometer(name, lasers, filters, config);
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeDuplicatePartsVectors(){
        
        Map<String, Part> sequencePartMap = new HashMap();
        Map<String, Vector> sequenceVectorMap = new HashMap();
        
        for(Polynucleotide pn:this.polynucleotide){
            sequencePartMap.put(pn.getPart().getSequence().getSeq(), pn.getPart());
            sequenceVectorMap.put(pn.getVector().getSequence().getSeq(), pn.getVector());
        }
        for (Polynucleotide pn : this.polynucleotide) {

            if (pn.getPart() != null && pn.getVector() != null) {

                //Replace parts with an existing part if applicable
                Part part = pn.getPart();
                String partSeq = part.getSequence().getSeq();
                String revPartSeq = Utilities.reverseComplement(partSeq);

                if (sequencePartMap.containsKey(partSeq)) {
                    Part existing = sequencePartMap.get(partSeq);
                    pn.setPart(existing);
                } else if (sequencePartMap.containsKey(revPartSeq)) {
                    Part existing = sequencePartMap.get(revPartSeq);
                    pn.setPart(existing);
                }

                //Replace vectors with an existing vector if applicable
                Vector vector = pn.getVector();
                String vecSeq = vector.getSequence().getSeq();
                String revVecSeq = vector.getSequence().getSeq();

                if (sequenceVectorMap.containsKey(vecSeq)) {
                    Vector existing = sequenceVectorMap.get(vecSeq);
                    pn.setVector(existing);
                } else if (sequenceVectorMap.containsKey(revVecSeq)) {
                    Vector existing = sequenceVectorMap.get(revVecSeq);
                    pn.setVector(existing);
                }
            }
        }
    }
    
    private void annotateParts(){
        
        List<Feature> allFeatures = new ArrayList<Feature>();
        allFeatures.addAll(this.features);
        allFeatures.addAll(this.fluorophores);
        
        for (Polynucleotide pn : this.polynucleotide) {
            if (pn.getPart() != null && pn.getVector() != null) {
                annotate(allFeatures, pn.getPart().getSequence());
                annotate(allFeatures, pn.getVector().getSequence());
                pn.getVector().findOriRes(pn.getVector().getSequence());
            }
        }
    }
    
    //Automatically annotate a NucSeq with a feature library
    private static void annotate(List<Feature> features, NucSeq ns) {

        Set<Annotation> annotations = new HashSet();
        String seq = ns.getSeq();
        for (Feature f : features) {

            if (!seq.isEmpty()) {

                //Form feature regex
                String fSeq = f.getSequence().getSequence();
                Color forwardColor = f.getForwardColor();
                Color reverseColor = f.getReverseColor();

                //Forward sequence search
                Pattern p = Pattern.compile(fSeq);
                Matcher m = p.matcher(seq);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    Annotation a = new Annotation(f, ns, forwardColor, reverseColor, start, end, new Person(), true, null);

                    //Only add if there is no duplicate annotation
                    boolean preexisting = false;
                    for (Annotation existingA : annotations) {
                        if (existingA.getStart() == a.getStart() && existingA.getEnd() == a.getEnd()) {
                            preexisting = true;
                        }
                    }

                    if (!preexisting && !f.getName().contains("TEST")) {
                        annotations.add(a);
                    }
                }

                //Reverse sequence search
                NucSeq fNucSeq = (NucSeq) f.getSequence();
                String revfSeq = fNucSeq.revComp();
                Pattern pR = Pattern.compile(revfSeq);
                Matcher mR = pR.matcher(seq);
                while (mR.find()) {
                    int start = mR.start();
                    int end = mR.end();
                    Annotation a = new Annotation(f, ns, reverseColor, forwardColor, start, end, new Person(), false, null);

                    //Only add if there is no duplicate annotation
                    boolean preexisting = false;
                    for (Annotation existingA : annotations) {
                        if (existingA.getStart() == a.getStart() && existingA.getEnd() == a.getEnd()) {
                            preexisting = true;
                        }
                    }

                    if (!preexisting && !f.getName().contains("TEST")) {
                        annotations.add(a);
                    }
                }
            }
            ns.setAnnotations(annotations);
        }
    }
    
}
