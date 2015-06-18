/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.*;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.AssemblyParameters;
import org.cidarlab.phoenix.core.dom.LTLFunction;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Person;
import org.cidarlab.phoenix.core.dom.SmallMolecule;

/**
 * This class has all methods for sending and receiving information to Clotho
 * 
 * @author evanappleton
 */
public class ClothoAdaptor {
    
    /*
     * 
     * DATA UPLOAD METHODS
     * 
     */
    
    /*
     * This method is for reading a Benchling-produced Multipart Genbank file with Biojava 1.9.0
     * It creates Clotho Polynucleotides, Parts, Sequences, Annotations and Features
     */
    public static void uploadSequences(File input, boolean featureLib) throws Exception {
        
        //If this file is a feature library, only get features and save those to Clotho
        if (featureLib) {
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Fluorophore> fluorophores = BenchlingAdaptor.getFluorophores(features);
            features.removeAll(fluorophores);
            createFeatures(features);
            createFluorophores(fluorophores);
            
        } else {
 
            //Get polynucleotides, nucseqs and parts from a multi-part genbank file
            //This automatic annotations of part NucSeqs relies on previously uploaded features
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            removeDuplicateParts(polyNucs);
            HashSet<Feature> annotationFeatures = queryFeatures();
            annotationFeatures.addAll(queryFluorophores());
            annotateParts(annotationFeatures, polyNucs);

            //Save all polynucleotides, nucseqs and parts to Clotho
            createPolynucleotides(polyNucs);
        }       
    }
    
    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadFluorescenceSpectrums (File input) throws FileNotFoundException, IOException {
        
        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        HashMap<String, HashMap<Double, Double>> spectralMaps = new HashMap<>();
        
        //The first line describes the spectra
        String line = reader.readLine();
        String[] spectra = line.split(",");
        int numSpectra = spectra.length;
        for (int i = 1; i < numSpectra; i ++) {
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
        
        //Look for each Fluorophore and see if their names match any of these spectrums
        HashSet<Fluorophore> queryFluorophores = queryFluorophores();
        for (String spectrum_name : spectralMaps.keySet()) {
            for (Fluorophore fl : queryFluorophores) {
                
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
        
        createFluorophores(queryFluorophores);
    }
    
    
    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadCytometer (File input) throws FileNotFoundException, IOException {
      
        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        
        //Initialize parameters
        String name = "";
        HashSet<String> lasers = new HashSet<>();
        HashSet<String> filters = new HashSet<>();
        HashMap<String, ArrayList<String[]>> config = new HashMap<>();
        
        //The first line describes the spectra
        String line = reader.readLine();
        while (line != null) {

            String[] vals = line.split(",");

            //Find name
            if (vals[0].equalsIgnoreCase("Configuration Name")) {
                name = vals[1];
            }

            //Once cytometer key rows are hit
            if (vals[0].equalsIgnoreCase("Laser Name")) {

                line = reader.readLine();

                //Internal loop for each laser
                while (line != null) {

                    String[] cvals = line.split(",");
                    String laser = cvals[2] + ":" + cvals[3];
                    lasers.add(laser);
                    
                    String mirror = cvals[7].substring(0, cvals[7].length() - 3);
                    String filter = cvals[8].substring(0, cvals[8].length() - 3).replaceAll("/", ":");
                    filters.add(mirror);
                    filters.add(filter);
                    ArrayList<String[]> filterList = new ArrayList<>();
                    filterList.add(new String[]{mirror, filter});
                    config.put(laser, filterList);

                    line = reader.readLine();
                    cvals = line.split(",");

                    //Loop kicked into once it reaches the lasers section of the file
                    while (cvals[0].equals("")) {

                        //Only look at row with filters, not empty slots
                        if (cvals.length >= 9) {
                            String newMirror;
                            String newFilter;
                            
                            //If there is a longpass filter
                            if (!cvals[7].isEmpty()) {
                                newMirror = cvals[7].substring(0, cvals[7].length() - 3);
                                filters.add(newMirror);

                                //If there is a bandpass filter
                                if (!cvals[8].isEmpty()) {
                                    newFilter = cvals[8].substring(0, cvals[8].length() - 3).replaceAll("/", ":");
                                    filters.add(newFilter);
                                    filterList.add(new String[]{newMirror, newFilter});
                                }

                            } else {
                                
                                //If there is a bandpass filter
                                if (!cvals[8].isEmpty()) {
                                    newFilter = cvals[8].substring(0, cvals[8].length() - 3).replaceAll("/", ":");
                                    filters.add(newFilter);
                                    filterList.add(new String[]{newFilter});
                                }
                            }
                        }

                        line = reader.readLine();
                        if (line != null) {
                            cvals = line.split(",");
                        } else {
                            cvals = new String[]{"end"};
                        }
                    }
                }
            } else {
                line = reader.readLine();
            }
        }   

        Cytometer c = new Cytometer(name, lasers, filters, config);
        createCytometer(c);        
    }
    
    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadCytometryData (List<File> input, List<Experiment> experiment) throws FileNotFoundException, IOException {
        
    }
    
    /*
     * 
     * CLOTHO OBJECT CREATION METHODS
     * 
     */

    //Add polynucleotides to Clotho via Clotho Server API
    public static void createPolynucleotides(HashSet<Polynucleotide> polyNucs) {
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        for (Polynucleotide pn : polyNucs) {

            //Polynucleotide schema
            Map createPolynucleotide = new HashMap();
            createPolynucleotide.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
            createPolynucleotide.put("name", pn.getAccession());
            createPolynucleotide.put("accession", pn.getAccession().substring(0, pn.getAccession().length() - 15));
            createPolynucleotide.put("description", pn.getDescription());
            createPolynucleotide.put("sequence", pn.getSequence());
            createPolynucleotide.put("isLinear", pn.isLinear());
            createPolynucleotide.put("isSingleStranded", pn.isSingleStranded());
            createPolynucleotide.put("submissionDate", pn.getSubmissionDate().toString());
            
            //Clotho ID
            if (pn.getClothoID() != null) {
                createPolynucleotide.put("id", pn.getClothoID());
            }
            else{
                createPolynucleotide.put("id", pn.getAccession());
                pn.setClothoID(pn.getAccession());
            }
            
            //NucSeq data
            Map createNucSeqMain = createNucSeq(pn.getSequence());

            //Part and vector
            Map createPart = createPart(pn.getPart(), clothoObject);
            Map createVec = createPart(pn.getVector(), clothoObject);

            createPolynucleotide.put("sequence", createNucSeqMain);
            createPolynucleotide.put("part", createPart);
            createPolynucleotide.put("vector", createVec);
            createPolynucleotide.put("isDV", pn.isDV());
            createPolynucleotide.put("level", pn.getLevel());
            
            clothoObject.create(createPolynucleotide);            
        }
        conn.closeConnection();
    }

    //Add features to Clotho via Clotho Server API
    public static void createFeatures(HashSet<Feature> features) {

        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        for (Feature f : features) {
            
            //Feature schema
            Map createFeature = new HashMap();
            createFeature.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
            createFeature.put("name", f.getName());
            createFeature.put("forwardColor", f.getForwardColor().toString());
            createFeature.put("reverseColor", f.getReverseColor().toString());

            //NucSeq sub-schema
            Map createSequence = new HashMap();
            createSequence.put("schema", "org.cidarlab.phoenix.core.dom.Sequence");
            createSequence.put("sequence", f.getSequence().getSequence());
            createFeature.put("sequence", createSequence);
            
            //FeatureRole sub-schema
            Map createFeatureRole = new HashMap();
            createFeatureRole.put("schema", "org.cidarlab.phoenix.core.dom.Feature.FeatureRole");
            createFeatureRole.put("FeatureRole", f.getRole().toString());
            createFeature.put("role", createFeatureRole);
            
            List<Arc> arcs = f.getArcs();
            List<Map> arcList = new ArrayList<>();
            
            if (arcs != null) {
                for (Arc a : arcs) {

                    //Arc sub-schema
                    //This assignment in particular assumes feature name and clothoID are the same
                    Map createArc = new HashMap();
                    createArc.put("schema", "org.cidarlab.phoenix.core.dom.Arc");
                    createArc.put("regulator", a.getRegulator().getName());
                    createArc.put("regulatee", a.getRegulatee().getName());

                    //ArcRole sub-sub-schema
                    Map createArcRole = new HashMap();
                    createArcRole.put("schema", "org.cidarlab.phoenix.core.dom.Arc.ArcRole");
                    createArcRole.put("ArcRole", a.getRole().toString());
                    createArc.put("role", createArcRole);

                    List<SmallMolecule> molecules = a.getMolecules();
                    List<Map> smList = new ArrayList<>();
                    
                    if (molecules != null) {
                        for (SmallMolecule sm : molecules) {

                            //SmallMolecule sub-sub-schema
                            Map createSmallMolecule = new HashMap();
                            createSmallMolecule.put("schema", "org.cidarlab.phoenix.core.dom.SmallMolecule");
                            createSmallMolecule.put("name", sm.getName());

                            //SmallMoleculeRol sub-sub-sub-schema
                            Map createSmallMoleculeRole = new HashMap();
                            createSmallMoleculeRole.put("schema", "org.cidarlab.phoenix.core.dom.SmallMolecule.SmallMoleculeRole");
                            createSmallMoleculeRole.put("SmallMoleculeRole", sm.getRole().toString());
                            createSmallMolecule.put("role", createSmallMoleculeRole);

                            smList.add(createSmallMolecule);
                        }

                        createArc.put("molecules", smList);
                    }
                    arcList.add(createArc);
                }

                createFeature.put("arcs", arcList);
            }
            
            //Clotho ID
            if (f.getClothoID() != null) {
                createFeature.put("id", f.getClothoID());
            }
            else{
                createFeature.put("id", f.getName());
                f.setClothoID(f.getName());
            }
            clothoObject.set(createFeature);
        }
        conn.closeConnection();
    }
    
    //Add fluorophores to Clotho via Clotho Server API
    public static void createFluorophores(HashSet<Fluorophore> flourophores) {

        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        for (Fluorophore f : flourophores) {

            //Fluorophore schema
            Map createFluorophore = new HashMap();
            createFluorophore.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
            createFluorophore.put("name", f.getName());
            createFluorophore.put("forwardColor", f.getForwardColor().toString());
            createFluorophore.put("reverseColor", f.getReverseColor().toString());
            createFluorophore.put("brightness", f.getBrightness());
            createFluorophore.put("emission_max", f.getEmission_max());
            createFluorophore.put("excitation_max", f.getExcitation_max());
            createFluorophore.put("oligomerization", f.getOligomerization());
            
            //Emission and excitation spectrums HashMaps            
            JSONArray em_array = new JSONArray();
            for (Double wavelength : f.getEm_spectrum().keySet()) {
                JSONObject em = new JSONObject();
                em.put("x", wavelength.toString());
                em.put("y", f.getEm_spectrum().get(wavelength).toString());
                em_array.add(em);
            }
            
            JSONArray ex_array = new JSONArray();            
            for (Double wavelength : f.getEx_spectrum().keySet()) {
                JSONObject ex = new JSONObject();
                ex.put("x", wavelength.toString());
                ex.put("y", f.getEx_spectrum().get(wavelength).toString());
                ex_array.add(ex);
            }
                        
            //NucSeq sub-schema
            Map createSequence = new HashMap();
            createSequence.put("schema", "org.cidarlab.phoenix.core.dom.Sequence");
            createSequence.put("sequence", f.getSequence().getSequence());
            createFluorophore.put("sequence", createSequence);
            
            //FeatureRole sub-schema
            Map createFeatureRole = new HashMap();
            createFeatureRole.put("schema", "org.cidarlab.phoenix.core.dom.FeatureRole");
            createFeatureRole.put("FeatureRole", f.getRole().toString());
            createFluorophore.put("role", createFeatureRole);
            
            //Clotho ID
            String id = "";
            if (f.getClothoID() != null) {
                createFluorophore.put("id", f.getClothoID());
                id = f.getClothoID();
            } else {
                createFluorophore.put("id", f.getName());
                f.setClothoID(f.getName());
                id = f.getName();
            }
            clothoObject.set(createFluorophore);
            
            createFluorophore = new HashMap();
            createFluorophore.put("id", id);
            createFluorophore.put("em_spectrum", em_array);
            clothoObject.set(createFluorophore);
            
            createFluorophore = new HashMap();
            createFluorophore.put("id", id);
            createFluorophore.put("ex_spectrum", ex_array);
            clothoObject.set(createFluorophore);
        }
        conn.closeConnection();
    }
    
    
    public static void createModule(Module module, Clotho clothoObject){
        createModuleTree(module,clothoObject);
        setNeighbors(module,clothoObject);
        
    }
    
    public static void setNeighbors(Module module, Clotho clothoObject){
        
        Map setNeighbor  = new HashMap();
        setNeighbor.put("id", module.getClothoID());
        
        JSONArray childrenIds = new JSONArray();
        JSONArray parentIds = new JSONArray();
        
        for(Module child:module.getChildren()){
            childrenIds.add(child.getClothoID());
        }
        
        for(Module parent:module.getParents()){
            parentIds.add(parent.getClothoID());
        }
        
        setNeighbor.put("children", childrenIds);
        setNeighbor.put("parents", parentIds);
        
        clothoObject.set(setNeighbor);
        for(Module child:module.getChildren()){
            setNeighbors(child,clothoObject);
        }
    }
    
    public static void createModuleTree(Module module, Clotho clothoObject){
        
        Map createModule = new HashMap();
        createModule.put("name", module.getName());
        createModule.put("schema", "org.cidarlab.phoenix.core.dom.Module");
        createModule.put("stage", module.getStage());
        createModule.put("role", module.getRole().toString());
        createModule.put("isForward", module.isForward());
        createModule.put("isRoot", module.isRoot());
        if (module.getClothoID() != null) {
            createModule.put("id", module.getClothoID());
        } else {
            createModule.put("id", module.getName());
        }
        JSONArray featureIds = new JSONArray();
        for (Feature f : module.getModuleFeatures()) {
            featureIds.add(createFloopyFeatures(f, clothoObject));
        }
        createModule.put("features", featureIds);
        createModule.put("ltlFunction", createLTLFunction(module.getFunction()));
        //Should be someway to create Primitive Modules
        String id = (String)clothoObject.create(createModule);
        module.setClothoID(id);
        
        for (Module child : module.getChildren()) {
            createModuleTree(child, clothoObject);
        }
    }
    
    public static String createFloopyFeatures(Feature feature, Clotho clothoObject){
        String id = "";
        
        return id;
    }
    
    public static Map createLTLFunction(LTLFunction ltl){
        Map createltl = new HashMap();
        return createltl;
    }
    
    //Add parts to Clotho via Clotho Server API
    public static Map createPart(Part p, Clotho clothoObject) {

        //Part schema
        Map createPart = new HashMap();
        createPart.put("schema", "org.cidarlab.phoenix.core.dom.Part");
        createPart.put("name", p.getName());
        createPart.put("isVector", p.isVector());
        createPart.put("sequence", createNucSeq(p.getSequence()));

        //Clotho ID
        if (p.getClothoID() != null) {
            createPart.put("id", p.getClothoID());
        } else {
            createPart.put("id", p.getName());
        }

        if (clothoObject != null) {
            clothoObject.set(createPart);
        }

        return createPart;
    }

    //Add nucseqs to Clotho via Clotho Server API
    public static Map createNucSeq(NucSeq ns) {

        //NucSeq schema
        Map createNucSeqMain = new HashMap();
        createNucSeqMain.put("schema", "org.cidarlab.phoenix.core.dom.NucSeq");
        createNucSeqMain.put("name", ns.getName());
        createNucSeqMain.put("sequence", ns.getSeq());
        createNucSeqMain.put("isCircular", ns.isCircular());
        createNucSeqMain.put("isSingleStranded", ns.isSingleStranded());

        Set<Annotation> annotations = ns.getAnnotations();
        List<Map> annotationList = new ArrayList<>();

        //Get all annotations
        for (Annotation annotation : annotations) {

            Map createAnnotation = new HashMap();
            createAnnotation.put("schema", "org.cidarlab.phoenix.core.dom.Annotation");
            createAnnotation.put("start", annotation.getStart());
            createAnnotation.put("end", annotation.getEnd());
            createAnnotation.put("forwardColor", annotation.getForwardColor().toString());
            createAnnotation.put("reverseColor", annotation.getReverseColor().toString());
            createAnnotation.put("isForwardStrand", annotation.isForwardStrand());

            //Feature schema - assumed one feature per annotation
            Feature f = annotation.getFeature();
            Map createFeature = new HashMap();
            createFeature.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
            createFeature.put("forwardColor", f.getForwardColor().toString());
            createFeature.put("reverseColor", f.getReverseColor().toString());
            createFeature.put("name", f.getName().replaceAll(".ref", ""));
            
            //FeatureRole sub-schema
            if (f.getRole() != null) {
                Map createFeatureRole = new HashMap();
                createFeatureRole.put("schema", "org.cidarlab.phoenix.core.dom.FeatureRole");
                createFeatureRole.put("FeatureRole", f.getRole().toString());
                createFeature.put("role", createFeatureRole);
            }            

            //NucSeq sub-schema
            Map createNucSeqSub = new HashMap();
            createNucSeqSub.put("schema", "org.cidarlab.phoenix.core.dom.Sequence");
            createNucSeqSub.put("sequence", f.getSequence().getSequence());

            createFeature.put("sequence", createNucSeqSub);

            //Get this feature's Person
            Person author = annotation.getAuthor();
            Map createPerson = new HashMap();
            createPerson.put("schema", "org.cidarlab.phoenix.core.dom.Person");
            createPerson.put("givenName", author.getGivenName());
            createPerson.put("surName", author.getSurName());
            createPerson.put("emailAddress", author.getEmailAddress());

            createAnnotation.put("feature", createFeature);
            createAnnotation.put("author", createPerson);

            annotationList.add(createAnnotation);
        }

        createNucSeqMain.put("annotations", annotationList);

        //Clotho ID
        if (ns.getClothoID() != null) {
            createNucSeqMain.put("id", ns.getClothoID());
        }

        return createNucSeqMain;
    }
    
    //Add fluorophores to Clotho via Clotho Server API
    public static void createCytometer(Cytometer c) {

        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        //Fluorophore schema
        Map createCytometer = new HashMap();
        createCytometer.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        createCytometer.put("name", c.getName());

        //Filter set        
        List<String> filters = new JSONArray();
        for (String filter : c.getFilters()) {
            filters.add(filter);
        }
        createCytometer.put("filters", filters);
        
        //Laser set        
        List<String> lasers = new JSONArray();
        for (String laser : c.getFilters()) {
            lasers.add(laser);
        }
        createCytometer.put("lasers", lasers);
        
        JSONArray config = new JSONArray();
        for (String laser : c.getConfiguration().keySet()) {
            JSONObject set = new JSONObject();
            
            List<List<String>> filterSets = new ArrayList<>();            
            for (String[] filterSet : c.getConfiguration().get(laser)) {
                
                List<String> filterPair = new ArrayList<>();
                for (String filter : filterSet) {
                    filterPair.add(filter);
                }
                filterSets.add(filterPair);
            }
            
            set.put(laser, filterSets);
            config.add(set);
        }
        createCytometer.put("configuration", config);
        
        //Clotho ID
        if (c.getClothoID() != null) {
            createCytometer.put("id", c.getClothoID());
        } else {
            createCytometer.put("id", c.getName());
            c.setClothoID(c.getName());
        }
        
        clothoObject.create(createCytometer);

        conn.closeConnection();
    }
    
    //Add polynucleotides to Clotho via Clotho Server API
    public static void createAssemblyParameters(AssemblyParameters aP) {
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        Map createAssmParam = new HashMap();
        createAssmParam.put("schema", "org.cidarlab.phoenix.core.dom.AssemblyParameters");
        createAssmParam.put("method", aP.getMethod());
        createAssmParam.put("name", aP.getName());
        createAssmParam.put("oligoNameRoot", aP.getOligoNameRoot());
        createAssmParam.put("meltingTemperature", aP.getMeltingTemperature());
        createAssmParam.put("minPCRLength", aP.getMinPCRLength());
        createAssmParam.put("targetHomologyLength", aP.getTargetHomologyLength());
        createAssmParam.put("minCloneLength", aP.getMinCloneLength());
        createAssmParam.put("maxPrimerLength", aP.getMaxPrimerLength());
        
        if (aP.getRecommended() != null) {
            List<String> recommended = new ArrayList<>(aP.getRecommended());
            createAssmParam.put("recommended", recommended);
        }
        if (aP.getRequired() != null) {
            List<String> required = new ArrayList<>(aP.getRequired());
            createAssmParam.put("required", required);
        }
        if (aP.getDiscouraged() != null) {
            List<String> discouraged = new ArrayList<>(aP.getDiscouraged());
            createAssmParam.put("discouraged", discouraged);
        }
        if (aP.getForbidden() != null) {
            List<String> forbidden = new ArrayList<>(aP.getForbidden());
            createAssmParam.put("forbidden", forbidden);
        }
        if (aP.getEfficiency() != null) {
            List<String> efficiency = new ArrayList<>(aP.getEfficiency());
            createAssmParam.put("efficiency", efficiency); 
        }

        //Clotho ID
        if (aP.getClothoID() != null) {
            createAssmParam.put("id", aP.getClothoID());
        } else {
            createAssmParam.put("id", aP.getName());
            aP.setClothoID(aP.getName());
        }
        clothoObject.create(createAssmParam);

        conn.closeConnection();
    }
    
    /*
     * 
     * CLOTHO OBJECT QUERY METHODS
     * 
     */    
    
    //Get all Clotho Features
    //The way features and arcs are handled here seems kinda fucked, but works
    public static HashSet<Feature> queryFeatures() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        //Arc searching hashes to avoid query recursion
        HashMap<String, List<HashMap<String, String>>> arcHash = new HashMap<>(); //key: feature name, value: list of regulator, regluatee name pairs
        HashMap<String, Feature> featureNameHash = new HashMap<>(); //key: feature name, value: Feature
        HashMap<HashMap<String, String>, Arc> regNamesArcsHash = new HashMap<>(); //key: regulator, regluatee name pair, value: arc
        
        HashSet<Feature> features = new HashSet<>();
        
        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;
        
        for (int i = 0; i < array.size(); i++) {
            
            Feature feature = new Feature();
            
            //Get feature fields
            JSONObject jsonFeature = array.getJSONObject(i);
            String fwdColorSt = jsonFeature.get("forwardColor").toString();
            String[] rgbfwd = fwdColorSt.substring(15, fwdColorSt.length()-1).split(",");
            Color fwdColor = new Color(Integer.valueOf(rgbfwd[0].substring(2)), Integer.valueOf(rgbfwd[1].substring(2)), Integer.valueOf(rgbfwd[2].substring(2)));
            String revColorSt = jsonFeature.get("reverseColor").toString();
            String[] rgbrev = revColorSt.substring(15, revColorSt.length()-1).split(",");
            Color revColor = new Color(Integer.valueOf(rgbrev[0].substring(2)), Integer.valueOf(rgbrev[1].substring(2)), Integer.valueOf(rgbrev[2].substring(2)));
            String name = jsonFeature.get("name").toString();
            
            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) jsonFeature.get("sequence");
            String seq = jsonSequence.get("sequence").toString();            
            NucSeq sequence = new NucSeq(seq);
            
            //Get FeatureRole
            JSONObject jsonFeatureRole = (JSONObject) jsonFeature.get("role");
            String roleString = jsonFeatureRole.get("FeatureRole").toString();
            feature.setRole(Feature.FeatureRole.valueOf(roleString));
            
            feature.setForwardColor(fwdColor);
            feature.setReverseColor(revColor);
            feature.setName(name);
            feature.setSequence(sequence);
            feature.setClothoID(jsonFeature.get("id").toString());
            
            //Get arcs
            JSONArray arrayArcs = (JSONArray) jsonFeature.get("arcs");
            
            if (arrayArcs != null) {
                for (int j = 0; j < arrayArcs.size(); j++) {

                    Arc arc = new Arc();

                    //Get arc fields
                    JSONObject jsonArc = arrayArcs.getJSONObject(j);
                    String regulator = jsonArc.get("regulator").toString();
                    String regulatee = jsonArc.get("regulatee").toString();

                    //Get ArcRole
                    JSONObject jsonArcRole = (JSONObject) jsonArc.get("role");
                    String arcRoleString = jsonArcRole.get("ArcRole").toString();
                    arc.setRole(Arc.ArcRole.valueOf(arcRoleString));

                    //Get small molecules
                    JSONArray arraySMs = (JSONArray) jsonArc.get("molecules");
                    
                    if (arraySMs != null) {
                        for (int k = 0; k < arraySMs.size(); k++) {

                            SmallMolecule sm = new SmallMolecule();

                            //Get small molecule fields
                            JSONObject jsonSM = arraySMs.getJSONObject(k);
                            String smName = jsonSM.get("name").toString();
                            sm.setName(smName);

                            //Get ArcRole
                            JSONObject jsonSMRole = (JSONObject) jsonSM.get("role");
                            String smRoleString = jsonSMRole.get("SmallMoleculeRole").toString();
                            sm.setRole(SmallMolecule.SmallMoleculeRole.valueOf(smRoleString));

                            if (arc.getMolecules() != null) {
                                arc.getMolecules().add(sm);
                            } else {
                                List<SmallMolecule> SMs = new ArrayList<>();
                                SMs.add(sm);
                                arc.setMolecules(SMs);
                            }
                        }
                    }

                    //Save feature names to arcHash for second pass
                    HashMap<String, String> reg = new HashMap<>();
                    reg.put("regulator", regulator);
                    reg.put("regulatee", regulatee);

                    regNamesArcsHash.put(reg, arc);

                    if (arcHash.get(name) != null) {
                        arcHash.get(name).add(reg);
                    } else {
                        List<HashMap<String, String>> regPairs = new ArrayList<>();
                        regPairs.add(reg);
                        arcHash.put(name, regPairs);
                    }
                }
            }

            featureNameHash.put(feature.getName(), feature);

            features.add(feature);
        }        

        //Loop to match features in arcHash
        for (String featureName : arcHash.keySet()) {

            Feature f = featureNameHash.get(featureName);
            List<HashMap<String, String>> regs = arcHash.get(featureName);

            for (HashMap<String, String> reg : regs) {

                Arc a = regNamesArcsHash.get(reg);
                a.setRegulator(featureNameHash.get(reg.get("regulator")));
                a.setRegulatee(featureNameHash.get(reg.get("regulatee")));

                if (f.getArcs() != null) {
                    f.getArcs().add(a);
                } else {
                    List<Arc> arcs = new ArrayList<>();
                    arcs.add(a);
                    f.setArcs(arcs);
                }
            }
        }

        conn.closeConnection();
        
        return features;
    }
    
    //Get all Clotho Fluorophores
    public static HashSet<Fluorophore> queryFluorophores() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        HashSet<Fluorophore> fluorophores = new HashSet<>();
        
        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        Object query = clothoObject.query(map);
        JSONArray arrayFluorophore = (JSONArray) query;
        
        for (int i = 0; i < arrayFluorophore.size(); i++) {
            
            Fluorophore fluorophore = new Fluorophore();
            
            //Get fluorophore fields
            JSONObject jsonFluorophore = arrayFluorophore.getJSONObject(i);
            String fwdColorSt = jsonFluorophore.get("forwardColor").toString();
            String[] rgbfwd = fwdColorSt.substring(15, fwdColorSt.length()-1).split(",");
            Color fwdColor = new Color(Integer.valueOf(rgbfwd[0].substring(2)), Integer.valueOf(rgbfwd[1].substring(2)), Integer.valueOf(rgbfwd[2].substring(2)));
            String revColorSt = jsonFluorophore.get("reverseColor").toString();
            String[] rgbrev = revColorSt.substring(15, revColorSt.length()-1).split(",");
            Color revColor = new Color(Integer.valueOf(rgbrev[0].substring(2)), Integer.valueOf(rgbrev[1].substring(2)), Integer.valueOf(rgbrev[2].substring(2)));
            String name = jsonFluorophore.get("name").toString();
            Integer oligo = Integer.valueOf(jsonFluorophore.get("oligomerization").toString());
            Double brightness = Double.valueOf(jsonFluorophore.get("brightness").toString());
            Double ex = Double.valueOf(jsonFluorophore.get("excitation_max").toString());
            Double em = Double.valueOf(jsonFluorophore.get("emission_max").toString());
            
            //Get excitation and emmission spectrums
            HashMap<Double, Double> em_spectrum = new HashMap<>();
            JSONArray jsonEm_spectrum = (JSONArray) jsonFluorophore.get("em_spectrum");
            for (int j = 0; j < jsonEm_spectrum.size(); j++) {
                JSONObject jsonObject = jsonEm_spectrum.getJSONObject(j);
                em_spectrum.put(Double.valueOf(jsonObject.get("x").toString()), Double.valueOf(jsonObject.get("y").toString()));
            }
            fluorophore.setEm_spectrum(em_spectrum);
         
            HashMap<Double, Double> ex_spectrum = new HashMap<>();
            JSONArray jsonEx_spectrum = (JSONArray) jsonFluorophore.get("ex_spectrum");
            for (int k = 0; k < jsonEx_spectrum.size(); k++) {
                JSONObject jsonObject = jsonEx_spectrum.getJSONObject(k);
                ex_spectrum.put(Double.valueOf(jsonObject.get("x").toString()), Double.valueOf(jsonObject.get("y").toString()));
            }
            fluorophore.setEx_spectrum(ex_spectrum);
            
            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) jsonFluorophore.get("sequence");
            String seq = jsonSequence.get("sequence").toString();            
            NucSeq sequence = new NucSeq(seq);
            
            //Get FeatureRole
            JSONObject jsonFeatureRole = (JSONObject) jsonFluorophore.get("role");
            String roleString = jsonFeatureRole.get("FeatureRole").toString();
            fluorophore.setRole(Feature.FeatureRole.valueOf(roleString));
            
            fluorophore.setForwardColor(fwdColor);
            fluorophore.setReverseColor(revColor);
            fluorophore.setName(name);
            fluorophore.setSequence(sequence);
            fluorophore.setOligomerization(oligo);
            fluorophore.setBrightness(brightness);
            fluorophore.setEmission_max(em);
            fluorophore.setExcitation_max(ex);
            fluorophore.setClothoID(jsonFluorophore.get("id").toString());
            
            fluorophores.add(fluorophore);
        } 
        
        conn.closeConnection();
        
        return fluorophores;
    }
    
    //Get all Clotho NucSeqs
    public static NucSeq getNucSeqs(JSONObject jsonNucSeq) {

        String seq = jsonNucSeq.get("sequence").toString();
        boolean circular = Boolean.parseBoolean(jsonNucSeq.get("isCircular").toString());
        boolean ss = Boolean.parseBoolean(jsonNucSeq.get("isSingleStranded").toString());

        NucSeq ns = new NucSeq(seq, ss, circular);
        ns.setName(jsonNucSeq.get("name").toString());

        //Get all Annotations
        JSONArray arrayAnnotations = (JSONArray) jsonNucSeq.get("annotations");

        for (int j = 0; j < arrayAnnotations.size(); j++) {

            //Get annotation fields
            JSONObject jsonAnnotation = arrayAnnotations.getJSONObject(j);
            int startAn = Integer.valueOf(jsonAnnotation.get("start").toString());
            int endAn = Integer.valueOf(jsonAnnotation.get("end").toString());
            boolean fwdStAn = Boolean.parseBoolean(jsonAnnotation.get("isForwardStrand").toString());

            String fwdColorStAn = jsonAnnotation.get("forwardColor").toString();
            String[] rgbfwdAn = fwdColorStAn.substring(15, fwdColorStAn.length() - 1).split(",");
            Color fwdColorAn = new Color(Integer.valueOf(rgbfwdAn[0].substring(2)), Integer.valueOf(rgbfwdAn[1].substring(2)), Integer.valueOf(rgbfwdAn[2].substring(2)));
            String revColorStAn = jsonAnnotation.get("reverseColor").toString();
            String[] rgbrevAn = revColorStAn.substring(15, revColorStAn.length() - 1).split(",");
            Color revColorAn = new Color(Integer.valueOf(rgbrevAn[0].substring(2)), Integer.valueOf(rgbrevAn[1].substring(2)), Integer.valueOf(rgbrevAn[2].substring(2)));

            //Get feature fields
            Feature feature = new Feature();
            JSONObject jsonFeature = (JSONObject) jsonAnnotation.get("feature");
            String fwdColorSt = jsonFeature.get("forwardColor").toString();
            String[] rgbfwd = fwdColorSt.substring(15, fwdColorSt.length() - 1).split(",");
            Color fwdColor = new Color(Integer.valueOf(rgbfwd[0].substring(2)), Integer.valueOf(rgbfwd[1].substring(2)), Integer.valueOf(rgbfwd[2].substring(2)));
            String revColorSt = jsonFeature.get("reverseColor").toString();
            String[] rgbrev = revColorSt.substring(15, revColorSt.length() - 1).split(",");
            Color revColor = new Color(Integer.valueOf(rgbrev[0].substring(2)), Integer.valueOf(rgbrev[1].substring(2)), Integer.valueOf(rgbrev[2].substring(2)));
            String fname = jsonFeature.get("name").toString();

            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) jsonFeature.get("sequence");
            String fseq = jsonSequence.get("sequence").toString();
            NucSeq fsequence = new NucSeq(fseq);

            //Get FeatureRole
            if (jsonFeature.has("role")) {
                JSONObject jsonFeatureRole = (JSONObject) jsonFeature.get("role");
                String roleString = jsonFeatureRole.get("FeatureRole").toString();
                feature.setRole(Feature.FeatureRole.valueOf(roleString));
            }
            
            feature.setForwardColor(fwdColor);
            feature.setReverseColor(revColor);
            feature.setName(fname);
            feature.setSequence(fsequence);

            //Get person
            Person author = new Person();
            JSONObject jsonPerson = (JSONObject) jsonAnnotation.get("author");
            author.setGivenName(jsonPerson.get("givenName").toString());
            author.setSurName(jsonPerson.get("surName").toString());
            author.setEmailAddress(jsonPerson.get("emailAddress").toString());

            //Assign all the annotation values to the object
            Annotation annotation = new Annotation(feature, fsequence, fwdColorAn, revColorAn, startAn, endAn, author, fwdStAn, null);
            ns.addAnnotation(annotation);
        }

        return ns;
    }
    
    public static Module getModule(String rootModule){
        Module module = new Module(rootModule);
        Map map = new HashMap();
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        map = (Map) clothoObject.get(rootModule);
        
        module.setName(map.get("name").toString());
        module.setClothoID(map.get("id").toString());
        module.setRole(Module.ModuleRole.valueOf(map.get("role").toString()));
        module.setStage((int)map.get("stage"));
        module.setForward((boolean)map.get("isForward"));
        module.setRoot((boolean)map.get("isRoot"));
        
        JSONArray children = new JSONArray();
        children = (JSONArray) map.get("children");
        for(Object childObj:children){
            Module childModule = getModule(childObj.toString());
            childModule.getParents().add(module);
            module.getChildren().add(childModule);
        }
        
        return module;
    }
    
    //Get all Clotho Parts
    public static Part getParts(JSONObject jsonPart) {

        //Get Part fields
        String name = jsonPart.get("name").toString();

        //Get NucSeq fields
        JSONObject jsonNucSeq = (JSONObject) jsonPart.get("sequence");
        Part p = Part.generateBasic(name, "", getNucSeqs(jsonNucSeq), null, null);

        return p;
    }
    
    //Get all Clotho Parts
    public static HashSet<Part> queryParts() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        HashSet<Part> parts = new HashSet<>();

        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.Part");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;

        for (int i = 0; i < array.size(); i++) {
                        
            //Get Part fields
            JSONObject jsonPart = array.getJSONObject(i);
            String name = jsonPart.get("name").toString();
            
            //Get NucSeq fields
            JSONObject jsonNucSeq = (JSONObject) jsonPart.get("sequence");
            Part p = Part.generateBasic(name, "", getNucSeqs(jsonNucSeq), null, null);
            p.setClothoID(jsonPart.get("id").toString());
            
            parts.add(p);
        }
        conn.closeConnection();
        
        return parts;
    }
    
    //Get all Clotho Polynucleotides
    public static HashSet<Polynucleotide> queryPolynucleotides() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        HashSet<Polynucleotide> polynucs = new HashSet<>();

        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;

        for (int i = 0; i < array.size(); i++) {
                        
            Polynucleotide pn = new Polynucleotide();
            
            //Get Polynucleotide fields
            JSONObject jsonPolynuc = array.getJSONObject(i);
            pn.setName(jsonPolynuc.get("name").toString());
            pn.setAccession(jsonPolynuc.get("accession").toString());
            pn.setDescription(jsonPolynuc.get("description").toString());            
            pn.setLinear(Boolean.parseBoolean(jsonPolynuc.get("isLinear").toString()));
            pn.setSingleStranded(Boolean.parseBoolean(jsonPolynuc.get("isSingleStranded").toString()));
            if(jsonPolynuc.containsKey("submissionDate"))
            {
                if(jsonPolynuc.get("submissionDate")!=null)
                {
                    pn.setSubmissionDate(new Date(jsonPolynuc.get("submissionDate").toString()));
                }
            }
            
            //Imbedded objects
            JSONObject jsonNucSeq = (JSONObject) jsonPolynuc.get("sequence");
            pn.setSequence(getNucSeqs(jsonNucSeq));
            JSONObject jsonPart = (JSONObject) jsonPolynuc.get("part");
            pn.setPart(getParts(jsonPart));
            JSONObject jsonVec = (JSONObject) jsonPolynuc.get("vector");
            pn.setVector(getParts(jsonVec));
            
            pn.setClothoID(jsonPolynuc.get("id").toString());
            pn.setDV(Boolean.parseBoolean(jsonPolynuc.get("isDV").toString()));
            pn.setLevel(Integer.valueOf(jsonPolynuc.get("level").toString()));
            polynucs.add(pn);
        }
        conn.closeConnection();
        
        return polynucs;
    }
    
    //Get all Clotho Cytometers
    public static HashSet<Cytometer> queryCytometers() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        HashSet<Cytometer> cytometers = new HashSet<>();
        
        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        Object query = clothoObject.query(map);
        JSONArray arrayCytometer = (JSONArray) query;
        
        for (int i = 0; i < arrayCytometer.size(); i++) {
            
            Cytometer c = new Cytometer();
            
            //Initialize parameters
            HashSet<String> lasers = new HashSet<>();
            HashSet<String> filters = new HashSet<>();
            HashMap<String, ArrayList<String[]>> config = new HashMap<>();
            
            //Get fluorophore fields
            JSONObject jsonCytometer = arrayCytometer.getJSONObject(i);
            c.setName(jsonCytometer.get("name").toString());
            
            List<String> laserList = (List<String>) jsonCytometer.get("lasers");
            lasers.addAll(laserList);
            c.setLasers(lasers);
            
            List<String> filterList = (List<String>) jsonCytometer.get("filters");
            filters.addAll(filterList);
            c.setFilters(filters);
            
            JSONArray laserFilterArray = (JSONArray) jsonCytometer.get("configuration");
            for (int j = 0; j < laserFilterArray.size(); j++) {

                //Get configuration
                JSONObject jsonConfig = laserFilterArray.getJSONObject(j);
                for (Object laserObj : jsonConfig.keySet()) {
                    String laser = laserObj.toString();
                    List<List<String>> filterSets = (List<List<String>>) jsonConfig.get(laserObj);
                    
                    ArrayList<String[]> filterSetList = new ArrayList<>();
                    for (List<String> filterPair : filterSets) {
                        
                        Object[] objectList = filterPair.toArray();
                        String[] stringArray = Arrays.copyOf(objectList, objectList.length, String[].class);
                        filterSetList.add(stringArray);
                    }
                    config.put(laser, filterSetList);
                }
            }
            
            c.setConfiguration(config);            
            c.setClothoID(jsonCytometer.get("id").toString());
            
            cytometers.add(c);
        }         
        conn.closeConnection();
        
        return cytometers;
    }
    
    //Get assembly parameters
    public static AssemblyParameters queryAssemblyParameters (String ID) {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        AssemblyParameters aP = new AssemblyParameters();
        
        Map map = new HashMap();
        map.put("schema", "org.cidarlab.phoenix.core.dom.AssemblyParameters");
        Object query = clothoObject.query(map);
        JSONArray arrayAP = (JSONArray) query;
        
        for (int i = 0; i < arrayAP.size(); i++) {
            
            JSONObject jsonAP = arrayAP.getJSONObject(i);
            AssemblyParameters candidate = new AssemblyParameters(jsonAP);
            
            if (ID != null) {
                if (candidate.getClothoID().equalsIgnoreCase(ID)) {
                    aP = candidate;
                    break;
                }
            }
        }
        
        conn.closeConnection();
        
        return aP;
    }
    
    //Remove parts from a set with duplicate sequence
    public static void removeDuplicateParts (HashSet<Polynucleotide> polyNucs) {
        
        HashMap<String, Part> sequencePartMap = new HashMap();
        
        //Put all existing parts in the Map
        HashSet<Part> queryParts = queryParts();
        for (Part p : queryParts) {
            sequencePartMap.put(p.getSequence().getSeq(), p);
        }
        
        //Only add parts with new sequence to the output
        for (Polynucleotide pn : polyNucs) {

            if (pn.getPart() != null && pn.getVector() != null) {

                //Replace parts if necessary
                Part part = pn.getPart();
                String partSeq = part.getSequence().getSeq();
                String revPartSeq = Utilities.reverseComplement(partSeq);
                
                if (sequencePartMap.containsKey(partSeq)) {
                    Part existing = sequencePartMap.get(partSeq);
                    if (!existing.isVector()) {
                        pn.setPart(existing);
//                    } else {
//                        sequencePartMap.put(partSeq, part);
                    }
                } else if (sequencePartMap.containsKey(revPartSeq)) {
                    Part existing = sequencePartMap.get(revPartSeq);
                    if (!existing.isVector()) {
                        pn.setPart(existing);
//                    } else {
//                        sequencePartMap.put(revPartSeq, part);
                    }
                } else {
                    sequencePartMap.put(partSeq, part);
                } 

                //Replace vectors if necessary
                Part vector = pn.getVector();
                String vecSeq = vector.getSequence().getSeq();
                String revVecSeq = vector.getSequence().getSeq();
                
                if (sequencePartMap.containsKey(vecSeq)) {                    
                    Part existing = sequencePartMap.get(vecSeq);
                    if (existing.isVector()) {
                        pn.setVector(existing);
//                    } else {
//                        sequencePartMap.put(vecSeq, vector);
                    }
                } else if (sequencePartMap.containsKey(vecSeq)) {
                    Part existing = sequencePartMap.get(revVecSeq);
                    if (existing.isVector()) {
                        pn.setVector(existing);
//                    } else {
//                        sequencePartMap.put(revVecSeq, vector);
                    }
                } else {
                    sequencePartMap.put(vecSeq, vector);
                }
            }
        }
    }
    
    //Annotate part and vector of a polynucleotide
    public static void annotateParts(HashSet<Feature> features, HashSet<Polynucleotide> polyNucs) {
        
        for (Polynucleotide pn : polyNucs) {      
            if (pn.getPart() != null && pn.getVector() != null) {
                annotate(features, pn.getPart().getSequence());
                annotate(features, pn.getVector().getSequence());
            }
        }
    }
    
    //Automatically annotate a NucSeq with a feature library
    public static void annotate(HashSet<Feature> features, NucSeq ns) {
        
        Set<Annotation> annotations = new HashSet();
        String seq = ns.getSeq();
        for (Feature f : features) {
            
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
                    if (existingA.getStart() == a.getStart() && existingA.getEnd() == a.getEnd() ) {
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
