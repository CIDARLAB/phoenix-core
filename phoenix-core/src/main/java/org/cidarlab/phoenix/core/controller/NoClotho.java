/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

//import com.fasterxml.uuid.EthernetAddress;
//import com.fasterxml.uuid.Generators;
//import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.collect.BiMap;
import edu.utah.ece.async.ibiosim.dataModels.util.exceptions.BioSimException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import lombok.Getter;
import lombok.Setter;
import org.biojava.bio.BioException;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor;
import org.cidarlab.phoenix.core.adaptors.IBioSimAdaptor;
import org.cidarlab.phoenix.core.adaptors.SynBioHubAdaptor;
import org.cidarlab.phoenix.core.dataprocessing.AnalyzeData;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.Arc.ArcRole;
import org.cidarlab.phoenix.core.dom.AssemblyParameters;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.ExperimentProcessedData;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Person;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.SmallMolecule;
import org.cidarlab.phoenix.core.dom.SmallMolecule.SmallMoleculeRole;
import org.cidarlab.phoenix.core.dom.Vector;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Location;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.Range;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SequenceAnnotation;
import org.sbolstandard.core2.SequenceOntology;
import org.synbiohub.frontend.SynBioHubException;

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
    
    private SynBioHubAdaptor synBioHub;
    
    public NoClotho() {
        features = new HashSet<Feature>();
        fluorophores = new HashSet<Fluorophore>();
        polynucleotide = new HashSet<Polynucleotide>();
        allIds = new HashSet<String>();
        counter = new AtomicInteger();
        initializeAssemblyParameters();
        synBioHub = new SynBioHubAdaptor("https://synbiohub.cidarlab.org/");
    }

    private void initializeAssemblyParameters() {
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
        
//        nc.addFeaturesFromRepo("https://synbiohub.programmingbiology.org/public/garuda_20170531/garuda_20170531_collection/1.0");
        
        
        nc.addFeaturesFromRepo("https://synbiohub.cidarlab.org/public/phoenix_feature_lib/phoenix_feature_lib_collection/1");
        nc.addPlasmidFromRepo("https://synbiohub.cidarlab.org/public/phoenix_plasmid_lib/phoenix_plasmid_lib_collection/1");
        
        
//        nc.addFeatures(featureFilepath);
        nc.addFluorophores(fluorFilepath);
//        nc.addPlasmid(plasmidFilepath);
        nc.addCytometer(cytometerFilepath);
        nc.assignNoClothoID();

//        System.out.println("LIST of Features");
//        for(Feature f:nc.fluorophores){
//            System.out.println(f.getName());
//        }
//        for(Fluorophore f:nc.fluorophores){
//            System.out.println(f.getName());
//        }
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
            
//            String mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest.csv";
//
//            String ea_mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest_ea_filled.csv";

            String mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest_no_ref.csv";
            
            String ea_mapFile = Utilities.getResourcesFilepath() + "InstructionFiles/nameMapFileTest_ea_filled_no_ref.csv";

            AnalyzeData.fillOutNameMap(ea_mapFile, mapFile);

            Map<String, String> nameMap = AnalyzeData.parseKeyMapFiles(mapFile);

            Map<String, AssignedModule> expexe = new HashMap<String, AssignedModule>();
            expexe = PhoenixController.getShortNameEXPEXEMap(bestModule);

            Map<String, ExperimentProcessedData> processedMap = new HashMap<String, ExperimentProcessedData>();
            AnalyzeData.directoryWalk(directory, directory, processedMap);

            //assignExperimentResults(expexe, processedMap, nc);
            generateAllAssignments(bestModule);
                
        } catch (Exception ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void generateAllAssignments(Module module){
        List<Module> order = new ArrayList<Module>();
        getOrderedModules(module,order);
        System.out.println("EXP EXE " +order.size());
        int index = 0;
        List<Integer> currentIndex = new ArrayList<Integer>();
        for(Module m:order){
            currentIndex.add(0);
        }
        traversePossibilities(order, currentIndex);
        
        
    }
    
    private static void traversePossibilities(List<Module> order, List<Integer> currentIndex){
       
        Map<Module,Integer> assigned = new HashMap<Module,Integer>();
        int header = 0;
        while(assigned.size()!= order.size()){
            if(!assigned.containsKey(order.get(header))){
                assigned.put(order.get(header), currentIndex.get(header));
                //Check if Module is Expressee
            } else {
                header++;
            }
        }
        
        for(int i=0;i<order.size();i++){
            System.out.print(order.get(i).getAssignedModules().get(currentIndex.get(i)).getRoleFeature() + " || ");
        }
        System.out.println("");    
        boolean complete = true;
        //Reset counter
        for(int i=currentIndex.size()-1;i>=0;i--){
            if(currentIndex.get(i) < order.get(i).getAssignedModules().size()-1){
                int indx = currentIndex.get(i);
                indx++;
                currentIndex.set(i, indx);
                complete = false;
                break;
            } else{
                currentIndex.set(i, 0);
            } 
        }
        
        if(!complete){
            traversePossibilities(order,currentIndex);
        }
    }
    
    
    private static void getOrderedModules(Module module, List<Module> order){
        if(module.getRole().equals(ModuleRole.EXPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSOR)){
            order.add(module);
        }
        for(Module child:module.getChildren()){
            getOrderedModules(child,order);
        }
    }
    
    private static void assignExperimentResults(Map<String, AssignedModule> expexe, Map<String, ExperimentProcessedData> processedMap, NoClotho nc) throws FileNotFoundException, BioSimException {
        String tmpfilepath = Utilities.getResourcesFilepath() + "tmp/";
        Utilities.makeDirectory(tmpfilepath);

        for (String expexeKey : expexe.keySet()) {

            AssignedModule am = expexe.get(expexeKey);
            for (String processKey : processedMap.keySet()) {
                if (am.getRole().equals(Module.ModuleRole.EXPRESSEE) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATOR) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || am.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSOR)) {
                    if (!processedMap.get(processKey).getRole().equals(Module.ModuleRole.EXPRESSEE)) {
                        continue;
                    }
                    boolean fpFlag = false;
                    boolean exeFlag = false;
                    boolean tagFlag = isTag(processKey);
                    boolean tagFeature = false;
                    String fusedFP = getFusedFP(processKey);
                    String exeFeature = getExpresee(processKey);
                    for (Feature f : am.getAllModuleFeatures()) {
                        //System.out.println(f.getName());
                        if (f.getName().toLowerCase().equals(getFusedFPfeature(fusedFP).toLowerCase())) {
                            fpFlag = true;
                        }
                        if (f.getName().toLowerCase().contains(exeFeature.toLowerCase())) {
                            exeFlag = true;
                        }
                        if (f.getName().equals("TAG")) {
                            tagFeature = true;
                        }
                    }
                    if (fpFlag && exeFlag) {
                        if (tagFlag == tagFeature) {
                            List<String> tmpFiles = new ArrayList<String>();
                            //Do that Expressee Thaing here. 
                            Map<Double, String> reg = new HashMap<Double, String>();
                            for (Double d : processedMap.get(processKey).getRegulationData().keySet()) {
                                String filepath = tmpfilepath + "tmp" + nc.counter.getAndIncrement() + ".csv";
                                tmpFiles.add(filepath);
                                Utilities.writeToFile(filepath, processedMap.get(processKey).getRegulationData().get(d));
                                reg.put(d, filepath);
                            }
                            String expresseeChannel = Utilities.getChannelsMap().get(getFusedFPfeature(fusedFP));
                            String regulatedFP = getRegulatedFP(processKey);
                            String regChannel = Utilities.getChannelsMap().get(getRegulatedFP(processKey));
                            String inducer = processedMap.get(processKey).getInducer();
                            System.out.println(inducer);
                            if (inducer == null) {
                                System.out.println(processKey);
                            }
                            try {
                                am.setSBMLDocument(IBioSimAdaptor.estimateExpresseeParameters(processedMap.get(processKey).getDegradationFilepath(), reg, expresseeChannel, regChannel, true, processedMap.get(processKey).getInducer(), exeFeature, regulatedFP)); //Ask Curtis!
                            } catch (XMLStreamException ex) {
                                Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                }
            }
            if (am.getRole().equals(Module.ModuleRole.EXPRESSOR)) {
                System.out.println("Expressor encountered ::");
                String promoter = "";
                String fpFeature = "";
                String fp = "";
                for (Feature f : am.getAllModuleFeatures()) {
                    if (f.getRole().equals(FeatureRole.PROMOTER) || f.getRole().equals(FeatureRole.PROMOTER_CONSTITUTIVE) || f.getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || f.getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)) {
                        String featureName = f.getName().substring(0, f.getName().indexOf(".ref"));
                        promoter += featureName + "_";
                    }
                    if (f.getRole().equals(FeatureRole.CDS_FLUORESCENT) || f.getRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                        System.out.println("FP FOUND :: " + f.getRole());
                        fpFeature = f.getName();
                        fp = getExpressorFP(f.getName());
                    }
                }
                promoter += "EXPRESSOR";
                System.out.println(promoter);
                System.out.println(fp);
                if ((!processedMap.containsKey(fp)) || (!processedMap.containsKey(promoter))) {
                    System.out.println("Experimental data for " + fp + " or " + promoter + " not found.");
                    continue;
                }

                String fpChannel = Utilities.getChannelsMap().get(fpFeature);
                String fpFeatureShort = fpFeature.substring(0, fpFeature.indexOf(".ref"));
                try {
                    am.setSBMLDocument(IBioSimAdaptor.estimateExpressorParameters(processedMap.get(fp).getExpExpressor(), processedMap.get(promoter).getExpExpressor(), fpChannel, fpFeatureShort));

                } catch (XMLStreamException ex) {
                    Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private static String getExpresee(String val) {
        String pieces[] = val.split("_");
        return pieces[0].trim();
    }

    private static String getFusedFP(String val) {
        String pieces[] = val.split("_");
        return pieces[1].trim();
    }

    private static String getRegulatedFeature(String val) {
        String pieces[] = val.split("_");
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].trim().equals("EXPRESSEE")) {
                return pieces[i + 1].trim();
            }
        }
        return "";
    }

    private static String getExpressorFP(String fp) {

        switch (fp) {
            case "EGFPm.ref":
                return "gfp_EXP";
            case "EBFP2.ref":
                return "bfp_EXP";
            case "T-Sapphire.ref":
                return "TS_EXP";
            case "DsRed.ref":
                return "rfp_EXP";
//            case "" :
//                break;
//                
        }
        return "";
    }

    private static String getRegulatedFP(String val) {
        String pieces[] = val.split("_");
        String regF = "";
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].trim().equals("EXPRESSEE")) {
                regF = pieces[i + 2].trim();
            }
        }
        switch (regF) {
            case "B":
                return "EBFP2.ref";
            case "G":
                return "EGFPm.ref";
        }
        return "";
    }

    private static boolean isTag(String val) {

        String pieces[] = val.split("_");
        if (pieces.length < 3) {
            return false;
        }
        if (pieces[2].trim().equals("EXPRESSEE")) {
            return false;
        }
        return true;
    }

    private static String getFusedFPfeature(String shortVer) {
        switch (shortVer) {
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

    public String getUUID() {

        String id = randomString(10);
        while (this.allIds.contains(id)) {
            id = randomString(10);
        }
        this.allIds.add(id);
        return id;
//        TimeBasedGenerator gen = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
//        UUID uuid = gen.generate();
//        return uuid.toString();
    }

    public void assignNoClothoID() {
        for (Feature f : this.features) {
            f.setClothoID(getUUID());
        }
        for (Fluorophore f : this.fluorophores) {
            f.setClothoID(getUUID());
        }
        for (Polynucleotide p : this.polynucleotide) {
            p.setClothoID(getUUID());
        }
        this.cytometer.setClothoID(getUUID());
        this.ap.setClothoID(getUUID());
    }

    public void assignID(Module m) {
        m.setClothoID(getUUID());

        for (AssignedModule am : m.getAssignedModules()) {
            am.setClothoID(getUUID());
            for (AssignedModule cm : am.getControlModules()) {
                cm.setClothoID(getUUID());
            }
            for (Experiment ex : am.getExperiments()) {
                ex.setClothoID(getUUID());
            }
        }
        for (Module child : m.getChildren()) {
            child.setClothoID(getUUID());
        }

    }
    
    public void addFeaturesFromRepo(String collectionURI) {
        //Arc hashes
        HashMap<String, HashSet<String>> promoterRegulator = new HashMap<>();
        HashMap<String, HashSet<String>> regulatorPromoter = new HashMap<>();
        HashMap<String, HashSet<String>> regulatorMolecule = new HashMap<>();
        try {
            int count = 0;
            for (ComponentDefinition cd : synBioHub.getSBOL(collectionURI).getComponentDefinitions()) {
//                System.out.println(cd.getDisplayId());
//                count++;
                for (SequenceAnnotation sa : cd.getSequenceAnnotations()) {
                    String name = cd.getDisplayId();
                    int startFeat = 0;
                    int endFeat = 0;
                    NucSeq nucSeq = new NucSeq();
                    Color fwd = new Color(0);
                    Color rev = new Color(0);
                    for (Location locas : sa.getLocations()) {
                        if (locas instanceof Range) {
                            startFeat = ((Range) locas).getStart();
                            endFeat = ((Range) locas).getEnd();
                        }
                    }

                    //Correct sequence for circular sequences by adding beginnning and end sequence
                    String seqString = cd.getImpliedNucleicAcidSequence();
                    seqString = seqString.concat(seqString);
                    if (startFeat > endFeat) {
                        seqString = seqString.concat(seqString);
                        endFeat = endFeat + seqString.length();
                    }

                    //Check for linearity
                    boolean linearity = true;
                    boolean ss = true;
                    for (org.sbolstandard.core2.Annotation a : cd.getAnnotations()) {
                        if (a.getQName().getNamespaceURI().equals("http://www.ncbi.nlm.nih.gov/genbank#")) {
                            if (a.getQName().getLocalPart().equals("molecule")) {
                                if (a.getStringValue().equals("ds-DNA")) {
                                    ss = false;
                                } else {
                                    ss = true;
                                }
                            }
                        }
                    }

                    for (URI uri : cd.getTypes()) {
                        if (uri.equals(SequenceOntology.CIRCULAR)) {
                            linearity = false;
                        }
                    }
//                if (.getAnnotation().containsProperty("DIVISION")) {
//                    if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
//                        linearity = false;
//                    } else {
//                        linearity = true;
//                    }
//                }

                    //Get rest of feature information and apply it to the features
                    nucSeq = new NucSeq(seqString.substring(startFeat - 1, endFeat), ss, linearity);

                    fwd = new Color(0);
                    rev = new Color(0);
                    for (org.sbolstandard.core2.Annotation a : sa.getAnnotations()) {
                        if (a.getQName().getNamespaceURI().equals("http://sbols.org/genBankConversion#")) {
                            if (a.getQName().getLocalPart().equals("ApEinfo_fwdcolor")) {
                                fwd = Color.decode(a.getStringValue());
                            } else if (a.getQName().getLocalPart().equals("ApEinfo_revcolor")) {
                                rev = Color.decode(a.getStringValue());
                            } else if (a.getQName().getLocalPart().equals("label")) {
                                name = a.getStringValue();
                            }
                        }
                    }
                    boolean keywords = false;
                    for (org.sbolstandard.core2.Annotation a : cd.getAnnotations()) {
                        if (a.getQName().getNamespaceURI().equals("http://www.ncbi.nlm.nih.gov/genbank#") && a.getQName().getLocalPart().equals("keywords")) {
                            keywords = true;
                            String tagstring = a.getStringValue();
                            HashMap<String, String> tags = new HashMap<>();
                            String[] tokens = tagstring.split("\" ");
                            for (String token : tokens) {
                                token = token.replace("\"", "").trim();
//                            token = token.substring(token.indexOf("\"") + 1);
                                String[] split = token.split(":");
                                if (split.length == 2) {
                                    tags.put(split[0].trim(), split[1].trim());
                                }
                            }

                            //Set role assuming type and sub-type are present
                            if (tags.containsKey("part-type") && tags.containsKey("part-subtype")) {

                                String type = tags.get("part-type").replaceAll("\"", "").trim();
                                String subtype = tags.get("part-subtype").replaceAll("\"", "").trim();

                                //Only in the case of a fluorescent protein do we make a special feature
                                if (subtype.contains("Fluorescent") && type.equalsIgnoreCase("CDS")) {

                                    Fluorophore fp = new Fluorophore(name);// + ".ref");
                                    fp.setSequence(nucSeq);
                                    fp.setForwardColor(fwd);
                                    fp.setReverseColor(rev);
                                    fp.setRole(Feature.FeatureRole.CDS_FLUORESCENT);

                                    if (tags.containsKey("brightness") && tags.containsKey("excitation") && tags.containsKey("emission") && tags.containsKey("oligomerization")) {
                                        fp.setOligomerization(Integer.valueOf(tags.get("oligomerization").replaceAll("\"", "").trim()));
                                        fp.setBrightness(Double.valueOf(tags.get("brightness").replaceAll("\"", "").trim()));
                                        fp.setExcitation_max(Double.valueOf(tags.get("excitation").replaceAll("\"", "").trim()));
                                        fp.setEmission_max(Double.valueOf(tags.get("emission").replaceAll("\"", "").trim()));
                                    }

                                    fluorophores.add(fp);

                                } else {

                                    Feature clothoFeature = new Feature(name); // + ".ref");
                                    clothoFeature.setSequence(nucSeq);
                                    clothoFeature.setForwardColor(fwd);
                                    clothoFeature.setReverseColor(rev);

                                    if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("constitutive")) {
                                        clothoFeature.setRole(Feature.FeatureRole.PROMOTER_CONSTITUTIVE);
                                    } else if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("repressible")) {
                                        clothoFeature.setRole(Feature.FeatureRole.PROMOTER_REPRESSIBLE);

                                        if (tags.containsKey("regulation-regulator")) {
                                            if (promoterRegulator.containsKey(clothoFeature.getName())) {
                                                promoterRegulator.get(clothoFeature.getName()).add(tags.get("regulation-regulator").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-regulator").replaceAll("\"", ""));
                                                promoterRegulator.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("inducible")) {
                                        clothoFeature.setRole(Feature.FeatureRole.PROMOTER_INDUCIBLE);

                                        if (tags.containsKey("regulation-regulator")) {
                                            if (promoterRegulator.containsKey(clothoFeature.getName())) {
                                                promoterRegulator.get(clothoFeature.getName()).add(tags.get("regulation-regulator").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-regulator").replaceAll("\"", ""));
                                                promoterRegulator.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("5'UTR")) {
                                        clothoFeature.setRole(Feature.FeatureRole.RBS);
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("repressor")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_REPRESSOR);

                                        if (tags.containsKey("regulation-promoter")) {
                                            if (regulatorPromoter.containsKey(clothoFeature.getName())) {
                                                regulatorPromoter.get(clothoFeature.getName()).add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                                regulatorPromoter.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("activator")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_REPRESSOR);

                                        if (tags.containsKey("regulation-promoter")) {
                                            if (regulatorPromoter.containsKey(clothoFeature.getName())) {
                                                regulatorPromoter.get(clothoFeature.getName()).add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                                regulatorPromoter.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("repressible-repressor")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_REPRESSIBLE_REPRESSOR);

                                        if (tags.containsKey("regulation-promoter")) {
                                            if (regulatorPromoter.containsKey(clothoFeature.getName())) {
                                                regulatorPromoter.get(clothoFeature.getName()).add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                                regulatorPromoter.put(clothoFeature.getName(), newMatches);
                                            }
                                        }

                                        if (tags.containsKey("regulation-molecule")) {
                                            if (regulatorMolecule.containsKey(clothoFeature.getName())) {
                                                regulatorMolecule.get(clothoFeature.getName()).add(tags.get("regulation-molecule").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-molecule").replaceAll("\"", ""));
                                                regulatorMolecule.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("activatible-activator")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR);

                                        if (tags.containsKey("regulation-promoter")) {
                                            if (regulatorPromoter.containsKey(clothoFeature.getName())) {
                                                regulatorPromoter.get(clothoFeature.getName()).add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-promoter").replaceAll("\"", ""));
                                                regulatorPromoter.put(clothoFeature.getName(), newMatches);
                                            }
                                        }

                                        if (tags.containsKey("regulation-molecule")) {
                                            if (regulatorMolecule.containsKey(clothoFeature.getName())) {
                                                regulatorMolecule.get(clothoFeature.getName()).add(tags.get("regulation-molecule").replaceAll("\"", ""));
                                            } else {
                                                HashSet<String> newMatches = new HashSet<>();
                                                newMatches.add(tags.get("regulation-molecule").replaceAll("\"", ""));
                                                regulatorMolecule.put(clothoFeature.getName(), newMatches);
                                            }
                                        }
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("linker")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_LINKER);
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("resistance")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_RESISTANCE);
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.contains("Tag")) {
                                        clothoFeature.setRole(Feature.FeatureRole.CDS_TAG);
                                    } else if (type.equalsIgnoreCase("CDS") && subtype.contains("Marker")) {
                                        clothoFeature.setRole(Feature.FeatureRole.MARKER);
                                    } else if (type.equalsIgnoreCase("terminator")) {
                                        clothoFeature.setRole(Feature.FeatureRole.TERMINATOR);
                                    } else if (type.equalsIgnoreCase("origin")) {
                                        clothoFeature.setRole(Feature.FeatureRole.ORIGIN);
                                    }

                                    features.add(clothoFeature);
                                }
                            }
                        }
                    }
                    if (!keywords) {

                        Feature clothoFeature = new Feature(name);
                        clothoFeature.setSequence(nucSeq);
                        clothoFeature.setForwardColor(fwd);
                        clothoFeature.setReverseColor(rev);
                        features.add(clothoFeature);
                    }

                }
            }
            System.out.println(count);
        } catch (SynBioHubException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Set<Feature> clothoFeatures = new HashSet<Feature>();
        clothoFeatures.addAll(features);
        clothoFeatures.addAll(fluorophores);
        
        //Add regulation arcs from arc maps
        HashSet<Arc> allArcs = new HashSet<>();
        HashMap<HashSet<Feature>, Arc> arcHash = new HashMap<>();
        HashSet<SmallMolecule> allMolecules = new HashSet<>();
        
        for (Feature f : clothoFeatures) {

            //Promoters
            if (promoterRegulator.containsKey(f.getName())) {

                HashSet<String> regulators = promoterRegulator.get(f.getName());
                for (String regulator : regulators) {
                    HashSet<Arc> arcsThisP = new HashSet<>();
                    
                    for (Feature match : clothoFeatures) {

                        if (match.getName().equalsIgnoreCase(regulator)) { //.replace(".ref", "").equalsIgnoreCase(regulator)) {
                            
                            if (f.getRole().equals(FeatureRole.PROMOTER_INDUCIBLE)) {

                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(match, f, ArcRole.ACTIVATION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisP.add(arc);
                                
                            } else if (f.getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)) {
                                
                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(match, f, ArcRole.REPRESSION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisP.add(arc);
                            }
                        }
                    }
                    
                    List<Arc> arcList = new ArrayList<>(arcsThisP);
                    f.setArcs(arcList);
                }
            }
            
            //Regulators
            if (regulatorPromoter.containsKey(f.getName())) {
                
                HashSet<String> promoters = regulatorPromoter.get(f.getName());
                for (String promoter : promoters) {
                    HashSet<Arc> arcsThisR = new HashSet<>();
                    
                    for (Feature match : clothoFeatures) {
                        
                        if (match.getName().equalsIgnoreCase(promoter)) { //.replace(".ref", "").equalsIgnoreCase(promoter)) {
                            
                            if (f.getRole().equals(FeatureRole.CDS_ACTIVATOR)) {
                                
                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(f, match, ArcRole.ACTIVATION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisR.add(arc);
                            
                            } else if (f.getRole().equals(FeatureRole.CDS_REPRESSOR)) {
                                
                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(f, match, ArcRole.REPRESSION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisR.add(arc);
                            
                            } else if (f.getRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR)) {
                                
                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(f, match, ArcRole.ACTIVATION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisR.add(arc);
                                
                                List<SmallMolecule> smThisArc = new ArrayList<>();
                                
                                //If this arc has a small molecule regulator indicated
                                if (regulatorMolecule.containsKey(f.getName())) {
                                    HashSet<String> molecules = regulatorMolecule.get(f.getName());
                                
                                    for (String molecule : molecules) {
                                        
                                        SmallMolecule sm = new SmallMolecule(molecule, SmallMoleculeRole.ACTIVATION);
                                        smThisArc.add(sm);
                                    }
                                    
                                    arc.setMolecules(smThisArc);
                                    allMolecules.addAll(smThisArc);
                                }                                
                                arcsThisR.add(arc);
                                
                            } else if (f.getRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                                
                                HashSet<Feature> pair = new HashSet<>();
                                pair.add(f);
                                pair.add(match);
                                Arc arc;
                                if (arcHash.containsKey(pair)) {
                                    arc = arcHash.get(pair);
                                } else {
                                    arc = new Arc(f, match, ArcRole.REPRESSION);
                                    arcHash.put(pair, arc);
                                }
                                arcsThisR.add(arc);
                                
                                List<SmallMolecule> smThisArc = new ArrayList<>();
                                
                                //If this arc has a small molecule regulator indicated
                                if (regulatorMolecule.containsKey(f.getName())) {
                                    HashSet<String> molecules = regulatorMolecule.get(f.getName());
                                
                                    for (String molecule : molecules) {
                                        
                                        SmallMolecule sm = new SmallMolecule(molecule, SmallMoleculeRole.REPRESSION);
                                        smThisArc.add(sm);
                                    }
                                    
                                    arc.setMolecules(smThisArc);
                                    allMolecules.addAll(smThisArc);
                                }
                                arcsThisR.add(arc);
                            }
                        }
                    }
                    List<Arc> arcList = new ArrayList<>(arcsThisR);
                    f.setArcs(arcList);
                    allArcs.addAll(arcsThisR);
                }
            }
        }
        
//        File file = new File(filepath);
//        try {
//            this.features.addAll(BenchlingAdaptor.getFeatures(file));
//            this.fluorophores.addAll(BenchlingAdaptor.getFluorophores(this.features));
//            this.features.removeAll(this.fluorophores);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchElementException ex) {
//            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BioException ex) {
//            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
//        }
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

    public void addPlasmid(String filepath) {
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
    
    public void addPlasmidFromRepo(String collectionURI) {

        try {
            
            for (ComponentDefinition cd : synBioHub.getSBOL(collectionURI).getComponentDefinitions()) {
                
                Polynucleotide polyNuc = new Polynucleotide();

                for (org.sbolstandard.core2.Annotation a : cd.getAnnotations()) {
                    if (a.getQName().getNamespaceURI().equals("http://www.ncbi.nlm.nih.gov/genbank#")) {
                        if (a.getQName().getLocalPart().equals("molecule")) {
                            if (a.getStringValue().equals("ds-DNA")) {
                                polyNuc.setSingleStranded(false);
                            } else {
                                polyNuc.setSingleStranded(true);
                            }
                        } else if (a.getQName().getLocalPart().equals("comment")) {
                            polyNuc.setDescription(a.getStringValue());
                        } else if (a.getQName().getLocalPart().equals("date")) {
                            Date date = new Date(a.getStringValue());
                            polyNuc.setSubmissionDate(date);
                        }
                    }
                }
                
                for (URI uri : cd.getTypes()) {
                    if (uri.equals(SequenceOntology.CIRCULAR)) {
                        polyNuc.setLinear(false);
                    }
                    else if (uri.equals(SequenceOntology.LINEAR)) {
                        polyNuc.setLinear(true);
                    }
                }
//            //Check for linearity
//            if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
//                polyNuc.setLinear(false);
//            } else {
//                polyNuc.setLinear(true);
//            }
//
//            //Check for strandedness
//            if (seq.getAnnotation().containsProperty("CIRCULAR")) {
//                if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
//                    polyNuc.setSingleStranded(false);
//                } else {
//                    polyNuc.setSingleStranded(true);
//                }
//            } else {
//                polyNuc.setSingleStranded(true);
//            }            

                //
                if (cd.getImpliedNucleicAcidSequence().contains(BenchlingAdaptor._lacZalphaL0) || cd.getImpliedNucleicAcidSequence().contains(Utilities.reverseComplement(BenchlingAdaptor._lacZalphaL0)) || cd.getImpliedNucleicAcidSequence().contains(BenchlingAdaptor._lacZalphaL1) || cd.getImpliedNucleicAcidSequence().contains(Utilities.reverseComplement(BenchlingAdaptor._lacZalphaL1))) {
                    polyNuc.setDV(true);
                }

                //Get sequence
                polyNuc.setSequence(new NucSeq(cd.getImpliedNucleicAcidSequence()));

                //Get part and vector
//            getMoCloParts(polyNuc, seq, vectorName);
                getMoCloParts(polyNuc, cd);

                polyNuc.setAccession(cd.getDisplayId() + "_Polynucleotide");

                polynucleotide.add(polyNuc);
            }
//            removeDuplicatePartsVectors();
            annotateParts();
        } catch (SynBioHubException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoClotho.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchElementException ex) {
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
            while ((line = reader.readLine()) != null) {
                String pieces[] = line.split(",");
                if (laserMode) {
                    if (!pieces[0].trim().isEmpty()) {
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
                if (pieces[0].trim().equalsIgnoreCase("Configuration Name")) {
                    name = pieces[1].trim();
                }
                if (pieces[0].trim().equalsIgnoreCase("Laser Name")) {
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

    private void removeDuplicatePartsVectors() {

        Map<String, Part> sequencePartMap = new HashMap();
        Map<String, Vector> sequenceVectorMap = new HashMap();

        for (Polynucleotide pn : this.polynucleotide) {
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

    private void annotateParts() {

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
    
    /*
     * Creates a Part set from a Biojava sequence object
     * This will create basic parts out of all incoming plasmids
     */
    public static HashSet<Part> getMoCloParts(Polynucleotide pn, ComponentDefinition cd) throws FileNotFoundException, NoSuchElementException, BioException {        
        
        HashSet<Part> partSet = new HashSet<>();

        //First we check for MoClo format... This is is a pretty hacky, inflexible way to do it
        //If MoClo sites are found, all features in between sites used to determine parts

        //Correct sequence for circular sequences by adding beginnning and end sequence
        String seqString = cd.getImpliedNucleicAcidSequence();
        String searchSeq = cd.getImpliedNucleicAcidSequence();
//        if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
            searchSeq = seqString.substring(seqString.length() - 5) + seqString;
//        }
        
        int start;
        int end;
        int level;

        //Looks for flanking BbsI or BsaI sites if there are more than one, this method will break
        //This also assumes there are either exactly two of each site, not both or a mix
        boolean containsBBsI = searchSeq.contains(BenchlingAdaptor._BbsIfwd) && searchSeq.contains(BenchlingAdaptor._BbsIrev);
        boolean containsBsaI = searchSeq.contains(BenchlingAdaptor._BsaIfwd) && searchSeq.contains(BenchlingAdaptor._BsaIrev);

        if (containsBBsI && !containsBsaI) {

            //If there is only one and exactly one BbsI site in each direction does it conform to MoClo format
            if (searchSeq.indexOf(BenchlingAdaptor._BbsIfwd) == searchSeq.lastIndexOf(BenchlingAdaptor._BbsIfwd) && searchSeq.indexOf(BenchlingAdaptor._BbsIrev) == searchSeq.lastIndexOf(BenchlingAdaptor._BbsIrev)) {
                start = searchSeq.indexOf(BenchlingAdaptor._BbsIfwd) + 8 - 5;
                end = searchSeq.indexOf(BenchlingAdaptor._BbsIrev) - 2 - 5;
                level = 1;
            } else {
                return partSet;
            }

        } else if (containsBsaI && !containsBBsI) {

            //If there is only one and exactly one BbsI site in each direction does it conform to MoClo format
            if (searchSeq.indexOf(BenchlingAdaptor._BsaIfwd) == searchSeq.lastIndexOf(BenchlingAdaptor._BsaIfwd) && searchSeq.indexOf(BenchlingAdaptor._BsaIrev) == searchSeq.lastIndexOf(BenchlingAdaptor._BsaIrev)) {
                start = searchSeq.indexOf(BenchlingAdaptor._BsaIfwd) + 7 - 5;
                end = searchSeq.indexOf(BenchlingAdaptor._BsaIrev) - 1 - 5;
                level = 0;
            } else {
                return partSet;
            }

        } else if (containsBsaI && containsBBsI) {

            //Destination vector edge case
            if (searchSeq.indexOf(BenchlingAdaptor._BsaIfwd) == searchSeq.lastIndexOf(BenchlingAdaptor._BsaIfwd) && searchSeq.indexOf(BenchlingAdaptor._BsaIrev) == searchSeq.lastIndexOf(BenchlingAdaptor._BsaIrev) && searchSeq.indexOf(BenchlingAdaptor._BbsIfwd) == searchSeq.lastIndexOf(BenchlingAdaptor._BbsIfwd) && searchSeq.indexOf(BenchlingAdaptor._BbsIrev) == searchSeq.lastIndexOf(BenchlingAdaptor._BbsIrev)) {

                //Find if the plasmid has the level0 or level1 lacZalpha fragment to determine the level
                String lacZsearch = seqString + seqString;
                if (lacZsearch.contains(BenchlingAdaptor._lacZalphaL0) || lacZsearch.contains(Utilities.reverseComplement(BenchlingAdaptor._lacZalphaL0))) {
                    start = searchSeq.indexOf(BenchlingAdaptor._BsaIfwd) + 7 - 5;
                    end = searchSeq.indexOf(BenchlingAdaptor._BsaIrev) - 1 - 5;
                    level = 0;
                } else if (lacZsearch.contains(BenchlingAdaptor._lacZalphaL1) || lacZsearch.contains(Utilities.reverseComplement(BenchlingAdaptor._lacZalphaL1))) {
                    start = searchSeq.indexOf(BenchlingAdaptor._BbsIfwd) + 8 - 5;
                    end = searchSeq.indexOf(BenchlingAdaptor._BbsIrev) - 2 - 5;
                    level = 1;
                } else {
                    return partSet;
                }

            } else {
                return partSet;
            }
        } else {
            return partSet;
        }

        //Correct for indexing
        if (end <= 0) {
            end = end + seqString.length();
        }
        if (start >= seqString.length()) {
            start = seqString.length() - start;
        }

        //Get part sequences
        //If the part range goes through index 0, the start index will be after the end index, so the sequence needs to be adjusted
        String partSeq;
        String vecSeq;
        
        if (start > end) {
            partSeq = seqString.substring(start, seqString.length()) + seqString.substring(0, end + 1);
            vecSeq = partSeq.substring(partSeq.length() - 4) + seqString.substring(start, end) + partSeq.substring(0, 4);            
        } else {
            partSeq = seqString.substring(start, end);
            vecSeq = partSeq.substring(partSeq.length() - 4) + seqString.substring(end, seqString.length()) + seqString.substring(0, start) + partSeq.substring(0, 4);
        }
        
        //Find MoClo OHs
        String LO;
        String RO;
        RO = partSeq.substring(partSeq.length() - 4);
        LO = partSeq.substring(0, 4);
        BiMap<String, String> moCloOHseqs = Utilities.getMoCloOHseqs();
        BiMap<String, String> inverse = moCloOHseqs.inverse();
        
        if (inverse.containsKey(LO)) {
            LO = inverse.get(LO);
        }
        if (inverse.containsKey(RO)) {
            RO = inverse.get(RO);
        }
        RO = RO.replaceAll("\\*", "#");
        LO = LO.replaceAll("\\*","#");
        
        //Make a new Part and Vector
        Part part = null;
        Vector vector = null;
        
        //Get rid of these tags and add a field to part with two features that constitute the vector
        //There will be some assumptions about MoClo format here as well
        
        //Generate parts and vector parts
        boolean comment = false;
        for (org.sbolstandard.core2.Annotation a : cd.getAnnotations()) {
            if (a.getQName().toString().equals("genbank:comment")) {
                part = Part.generateBasic(cd.getDisplayId() + "_part_" + LO + "_" + RO, a.getStringValue(), new NucSeq(partSeq), null, null);
                vector = new Vector(cd.getDisplayId() + "_vector_" + LO + "_" + RO, "", new NucSeq(vecSeq), null, null, null, null);
                comment = true;
            }
        }
        if (!comment) {
            part = Part.generateBasic(cd.getDisplayId() + "_part_" + LO + "_" + RO, "", new NucSeq(partSeq), null, null);
            vector = new Vector(cd.getDisplayId() + "_vector_" + LO + "_" + RO, "", new NucSeq(vecSeq), null, null, null, null);
        }

        partSet.add(part);
        partSet.add(vector);

        pn.setPart(part);
        pn.setVector(vector);
        pn.setLevel(level);
        
        return partSet;
    }
    
}
