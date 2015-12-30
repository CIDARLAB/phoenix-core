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
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.clothoapi.clotho3javaapi.Clotho;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.Arc.ArcRole;
import org.cidarlab.phoenix.core.dom.AssemblyParameters;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.ExperimentResults;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Medium;
import org.cidarlab.phoenix.core.dom.Medium.MediaType;
import org.cidarlab.phoenix.core.dom.STLFunction;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Person;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Sample;
import org.cidarlab.phoenix.core.dom.Sample.SampleType;
import org.cidarlab.phoenix.core.dom.Sequence;
import org.cidarlab.phoenix.core.dom.SmallMolecule;
import org.cidarlab.phoenix.core.dom.Strain;
import org.cidarlab.phoenix.core.dom.Vector;

/**
 * This class has all methods for sending and receiving information to Clotho
 *
 * @author prash
 * @author evanappleton
 */
public class ClothoAdaptor {

    //<editor-fold desc="Upload Methods">
    /*
     * This method is for reading a Benchling-produced Multipart Genbank file with Biojava 1.9.0
     * It creates Clotho Polynucleotides, Parts, Sequences, Annotations and Features
     */
    public static void uploadSequences(File input, boolean featureLib, Clotho clothoObject) throws Exception {

        //If this file is a feature library, only get features and save those to Clotho
        if (featureLib) {
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Fluorophore> fluorophores = BenchlingAdaptor.getFluorophores(features);
            features.removeAll(fluorophores);

            System.out.println("From Benchling Fluorophores:: " + fluorophores.size());

            for (Feature f : features) {
                createFeature(f, clothoObject);
            }
            for(Fluorophore fluorophore:fluorophores){
                createFluorophore(fluorophore, clothoObject);
            }
            
        } else {

            //Get polynucleotides, nucseqs and parts from a multi-part genbank file
            //This automatic annotations of part NucSeqs relies on previously uploaded features
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            removeDuplicateParts(polyNucs, clothoObject);
            Map featureQuery = new HashMap();
            featureQuery.put("schema", Feature.class.getCanonicalName());
            List<Feature> annotationFeatures = queryFeatures(featureQuery, clothoObject);

            Map fluorophoreQuery = new HashMap();
            fluorophoreQuery.put("schema", Fluorophore.class.getCanonicalName());
            annotationFeatures.addAll(queryFluorophores(fluorophoreQuery, clothoObject));
            annotateParts(annotationFeatures, polyNucs);

            //Save all polynucleotides, nucseqs and parts to Clotho
            for(Polynucleotide polynucleotide:polyNucs){
                createPolynucleotide(polynucleotide,clothoObject);
            }
        }
    }

    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadFluorescenceSpectrums(File input, Clotho clothoObject) throws FileNotFoundException, IOException {

        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
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

        //Look for each Fluorophore and see if their names match any of these spectrums
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", Fluorophore.class.getCanonicalName());
        List<Fluorophore> queryFluorophores = queryFluorophores(fluorophoreQuery, clothoObject);
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
        for (Fluorophore fluorophore : queryFluorophores) {
            createFluorophore(fluorophore, clothoObject);
        }
    }

    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadCytometer(File input, Clotho clothoObject) throws FileNotFoundException, IOException {

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
        createCytometer(c, clothoObject);
    }

    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static void uploadCytometryData(List<File> input, List<Experiment> experiment) throws FileNotFoundException, IOException {

    }

    //</editor-fold>
    
    //<editor-fold desc="Create Methods">
    
    //<editor-fold desc="Map Creation Methods">
    
    public static Map createNucSeqMap(NucSeq ns) {

        //NucSeq schema
        Map createNucSeqMain = new HashMap();
        createNucSeqMain.put("schema", NucSeq.class.getCanonicalName());
        createNucSeqMain.put("name", ns.getName());
        createNucSeqMain.put("sequence", ns.getSeq());
        createNucSeqMain.put("isCircular", ns.isCircular());
        createNucSeqMain.put("isSingleStranded", ns.isSingleStranded());

        Set<Annotation> annotations = ns.getAnnotations();
        List<Map> annotationList = new ArrayList<>();

        //Get all annotations
        for (Annotation annotation : annotations) {

            Map createAnnotation = new HashMap();
            createAnnotation.put("schema", Annotation.class.getCanonicalName());
            createAnnotation.put("start", annotation.getStart());
            createAnnotation.put("end", annotation.getEnd());
            createAnnotation.put("forwardColor", annotation.getForwardColor().getRGB());
            createAnnotation.put("reverseColor", annotation.getReverseColor().getRGB());
            createAnnotation.put("isForwardStrand", annotation.isForwardStrand());

            //Feature schema - assumed one feature per annotation
            Feature f = annotation.getFeature();
            Map createFeature = new HashMap();
            createFeature.put("schema", Feature.class.getCanonicalName());
            createFeature.put("forwardColor", f.getForwardColor().getRGB());
            createFeature.put("reverseColor", f.getReverseColor().getRGB());
            createFeature.put("name", f.getName().replaceAll(".ref", ""));

            //FeatureRole sub-schema
            if (f.getRole() != null) {
                createFeature.put("role", f.getRole().toString());
            }

            //NucSeq sub-schema
            Map createNucSeqSub = new HashMap();
            createNucSeqSub.put("schema", Sequence.class.getCanonicalName());
            createNucSeqSub.put("sequence", f.getSequence().getSequence());

            createFeature.put("sequence", createNucSeqSub);

            //Get this feature's Person
            Person author = annotation.getAuthor();
            Map createPerson = new HashMap();
            createPerson.put("schema", Person.class.getCanonicalName());
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
    
    public static Map createSmallMoleculeMap(SmallMolecule smolecule) {
        Map map = new HashMap();
        map.put("name", smolecule.getName());
        map.put("schema", SmallMolecule.class.getCanonicalName());
        map.put("role", smolecule.getRole().toString());
        if(smolecule.getConcentration()!=null){
            map.put("concentration", smolecule.getConcentration());
        }
        
        return map;
    }
    
    public static Map createStrainMap(Strain strain) {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Strain.class.getCanonicalName());
        map.put("name", strain.getName());
        return map;
    }
    
    public static Map createMediumMap(Medium medium) {
        String id = "";
        Map map = new HashMap();
        map.put("schema", Medium.class.getCanonicalName());
//        map.put("concentration", medium.getConcentration());
        map.put("name", medium.getName());
        map.put("type", medium.getType());
        Map smallMoleculeMap = new HashMap();
//        smallMoleculeMap = createSmallMoleculeMap(medium.getSmallmolecule());
        if (medium.getSmallmolecule() != null) {
            smallMoleculeMap = createSmallMoleculeMap(medium.getSmallmolecule());
        }
        map.put("smallMolecule", smallMoleculeMap);
        return map;
    }
    
    public static Map createPolynucleotideMap(Polynucleotide pn,Clotho clothoObject){
        Map map = new HashMap();
        map.put("schema", Polynucleotide.class.getCanonicalName());
        map.put("name", pn.getAccession());
        map.put("accession", pn.getAccession().substring(0, pn.getAccession().length() - 15));
        map.put("description", pn.getDescription());
        map.put("sequence", pn.getSequence());
        map.put("isLinear", pn.isLinear());
        map.put("isSingleStranded", pn.isSingleStranded());
        map.put("submissionDate", pn.getSubmissionDate().toString());

        //Clotho ID
        if (pn.getClothoID() != null) {
            map.put("id", pn.getClothoID());
        } else {
            map.put("id", pn.getAccession());
            pn.setClothoID(pn.getAccession());
        }

        //NucSeq data
        Map createNucSeqMain = createNucSeqMap(pn.getSequence());

        //Change this. Make it point to a Clotho Id instead.
        //Part and vector
        String partId = createPart(pn.getPart(), clothoObject);
        String vectorId = createVector(pn.getVector(), clothoObject);
        //Map createPart = createPart(pn.getPart(), clothoObject);
        //Map createVec = createPart(pn.getVector(), clothoObject);

        map.put("sequence", createNucSeqMain);
        map.put("part", partId);
        map.put("vector", vectorId);
        map.put("isDV", pn.isDV());
        map.put("level", pn.getLevel());
        
        return map;
    }
    
    public static Map createPrimitiveModuleMap(PrimitiveModule pmodule,Clotho clothoObject) {
        Map map = new HashMap();
        map.put("schema", PrimitiveModule.class.getCanonicalName());
        map.put("moduleFeature", createFeature(pmodule.getModuleFeature(),clothoObject));
        map.put("primitiveRole", pmodule.getPrimitiveRole().toString());
        map.put("primitive", createPrimitiveMap(pmodule.getPrimitive()));
        
        return map;
    }

    public static Map createPrimitiveMap(Primitive primitive) {
        Map map = new HashMap();
        map.put("name", primitive.getName());
        map.put("type", primitive.getType());
        return map;
    }

    public static Map createFeatureMap(Feature f) {
        //System.out.println("In Create Feature Map");
        Map map = new HashMap();
        map.put("schema", Feature.class.getCanonicalName());
        map.put("name", f.getName());
        map.put("role", f.getRole().toString());
        
        map.put("forwardColor", f.getForwardColor().getRGB());
        map.put("reverseColor", f.getReverseColor().getRGB());
        
        if (f.getClothoID() != null) {
            if (!f.isFP()) { //Better way to do this??
                map.put("id", f.getClothoID());
            }
        }
        
        //NucSeq sub-schema
        Map createSequence = new HashMap();
        createSequence.put("schema", Sequence.class.getCanonicalName());
        createSequence.put("sequence", f.getSequence().getSequence());
        map.put("sequence", createSequence);
        
        List<Map> arcList = new ArrayList<>();
        for (Arc a : f.getArcs()) {
            arcList.add(createArcMap(a));
        }
        map.put("arcs", arcList);

        return map;
    }
    
    public static Map createArcMap(Arc a){
        //System.out.println("In Create Arc Map");
        Map map = new HashMap();
        map.put("schema", Arc.class.getCanonicalName());
        map.put("regulator", a.getRegulator().getName());
        map.put("regulatee", a.getRegulatee().getName());
        //System.out.println("Regulator :: "+a.getRegulator().getName());
        //System.out.println("Regulatee :: "+a.getRegulatee().getName());
        
        map.put("role", a.getRole().toString());
        List<Map> smList = new ArrayList<>();

        for (SmallMolecule sm : a.getMolecules()) {
            smList.add(createSmallMoleculeMap(sm));
        }
        map.put("molecules", smList);

        return map;
    }
    
    public static Map createExperimentMap(Experiment experiment){
        Map map = new HashMap();
        map.put("schema", Experiment.class.getCanonicalName());
        map.put("name", experiment.getName());
        map.put("exType", experiment.getExType());
        if (experiment.getClothoID() != null) {
            map.put("id", experiment.getClothoID());
        }
        
        /*JSONArray experimentTimes = new JSONArray();
        for (String time : experiment.getTimes()) {
            experimentTimes.add(time);
        }
        map.put("times", experimentTimes);*/
        map.put("times", experiment.getTimes());
        
        JSONArray mediaConditions = new JSONArray();
        for (Medium medium : experiment.getMediaConditions()) {
            mediaConditions.add(createMediumMap(medium));
        }
        map.put("mediaConditions", mediaConditions);
        
        return map;
    }
    
    public static Map createExperimentResultsMap(ExperimentResults exResults){
        return null;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Clotho Object Creation Methods">
    
    //<editor-fold  desc="Create a Module Tree in Clotho">
    
    public static String createModuleTree(Module module, Clotho clothoObject) {
        String id = "";
        //Recursively create a Module Tree in Clotho. Returns the id of the Root Module
        id = createModule(module, clothoObject);
        //Recursively call each Module and set it's Parents and Children
        setModuleNeighbors(module, clothoObject);

        return id;
    }

    public static void setModuleNeighbors(Module module, Clotho clothoObject) {

        Map setNeighbor = new HashMap();
        setNeighbor.put("id", module.getClothoID());

        JSONArray childrenIds = new JSONArray();
        JSONArray parentIds = new JSONArray();
        JSONArray assignedModuleIds = new JSONArray();

        for (Module child : module.getChildren()) {
            childrenIds.add(child.getClothoID());
        }

        for (Module parent : module.getParents()) {
            parentIds.add(parent.getClothoID());
        }

        setNeighbor.put("children", childrenIds);
        setNeighbor.put("parents", parentIds);

        clothoObject.set(setNeighbor);

        //Recursively call Children of the Module and set Neighbors. 
        for (Module child : module.getChildren()) {
            setModuleNeighbors(child, clothoObject);
        }
    }

    public static String createModule(Module module, Clotho clothoObject) {

        String id = "";
        Map map = new HashMap();

        map.put("name", module.getName());
        map.put("schema", Module.class.getCanonicalName());
        map.put("stage", module.getStage());
        map.put("role", module.getRole().toString());
        map.put("isForward", module.isForward());
        map.put("isRoot", module.isRoot());
        if (module.getClothoID() != null) {
            map.put("id", module.getClothoID());
        }

        //Create and add AssignedModules
        JSONArray assignedModuleIds = new JSONArray();
        for (AssignedModule amodule : module.getAssignedModules()) {
            assignedModuleIds.add(createAssignedModule(amodule, clothoObject));
        }
        map.put("assignedModules", assignedModuleIds);

        //Primitive Modules (Sub Modules)
        JSONArray submodules = new JSONArray();
        for (PrimitiveModule pm : module.getSubmodules()) {
            submodules.add(createPrimitiveModuleMap(pm,clothoObject));
        }
        map.put("submodules", submodules);

        
        map.put("function", createSTLFunction(module.getFunction(), clothoObject));

        id = (String) clothoObject.set(map);
        module.setClothoID(id);

        //Recursive call for all children of this module
        for (Module child : module.getChildren()) {
            createModule(child, clothoObject);
        }

        return id;
    }
    
    //</editor-fold>

    public static String createAssignedModule(AssignedModule amodule, Clotho clothoObject) {
        String id = "";
        Map map = new HashMap();
        map.put("name", amodule.getName());
        map.put("schema", AssignedModule.class.getCanonicalName());
        map.put("function", createSTLFunction(amodule.getFunction(), clothoObject));
        map.put("isForward", amodule.isForward());
        map.put("role", amodule.getRole().toString());

        if (amodule.getClothoID() != null) {
            map.put("id", amodule.getClothoID());
        }
        JSONArray exptIds = new JSONArray();
        for (Experiment experiment : amodule.getExperiments()) {
            exptIds.add(createExperiment(experiment, clothoObject));
        }
        map.put("experiments", exptIds);

        JSONArray controlModuleIds = new JSONArray();
        for (Module cmodule : amodule.getControlModules()) {
            controlModuleIds.add(createModule(cmodule, clothoObject));
        }
        map.put("controlModules", controlModuleIds);
        map.put("shortName", amodule.getShortName());
        
        id = (String) clothoObject.set(map);
        amodule.setClothoID(id);
        return id;
    }
    
    public static String createExperiment(Experiment experiment, Clotho clothoObject) {
        String id = (String) clothoObject.set(createExperimentMap(experiment));
        experiment.setClothoID(id);
        return id;
    }
    
    public static String createPolynucleotide(Polynucleotide pn, Clotho clothoObject) {
        String id = (String) clothoObject.set(createPolynucleotideMap(pn,clothoObject));
        pn.setClothoID(id);
        return id;
    }
    
    public static String createFeature(Feature f, Clotho clothoObject) {
        
        Map map = new HashMap();
        map = createFeatureMap(f);
        
        String id = (String) clothoObject.set(map);
        f.setClothoID(id);
        return id;
    }

    public static String createFluorophore(Fluorophore f, Clotho clothoObject) {
        String id = "";
        //Fluorophore schema
        Map createFluorophore = new HashMap();
        createFluorophore.put("schema", Fluorophore.class.getCanonicalName());
        createFluorophore.put("name", f.getName());
        createFluorophore.put("forwardColor", f.getForwardColor().getRGB());
        createFluorophore.put("reverseColor", f.getReverseColor().getRGB());
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
        createSequence.put("schema", Sequence.class.getCanonicalName());
        createSequence.put("sequence", f.getSequence().getSequence());
        createFluorophore.put("sequence", createSequence);

        //FeatureRole sub-schema
        createFluorophore.put("role", f.getRole().toString());

        //Clotho ID
        if (f.getClothoID() != null) {
            createFluorophore.put("id", f.getClothoID());
            //id = f.getClothoID();
        }
        //else {
        //    createFluorophore.put("id", f.getName());
        //    f.setClothoID(f.getName());
        //}
        createFluorophore.put("em_spectrum", em_array);
        createFluorophore.put("ex_spectrum", ex_array);

        id = (String) clothoObject.set(createFluorophore);
        f.setClothoID(id);
        return id;
    }
    
    public static String createSTLFunction(STLFunction ltl, Clotho clothoObject) {
        String id = "";
        Map map = new HashMap();
        map.put("schema", STLFunction.class.getCanonicalName());
        return id;
    }
    
    //Add parts to Clotho via Clotho Server API
    public static String createPart(Part p, Clotho clothoObject) {

        //Part schema
        String id = "";
        Map createPart = new HashMap();
        createPart.put("schema", Part.class.getCanonicalName());
        createPart.put("name", p.getName());
//        createPart.put("isVector", p.isVector());
        createPart.put("sequence", createNucSeqMap(p.getSequence()));
        
        if(p.getDescription() != null){
            createPart.put("description", p.getDescription());
        }
        
        //Clotho ID
        if (p.getClothoID() != null) {
            createPart.put("id", p.getClothoID());
        } else {
            createPart.put("id", p.getName());
        }

        id = (String) clothoObject.set(createPart);
        p.setClothoID(id);
        return id;
    }
    
    //Add parts to Clotho via Clotho Server API
    public static String createVector(Vector vector, Clotho clothoObject) {

        //Part schema
        String id = "";
        Map createVector = new HashMap();
        createVector.put("schema", Vector.class.getCanonicalName());
        createVector.put("name", vector.getName());
//        createPart.put("isVector", p.isVector());
        createVector.put("sequence", createNucSeqMap(vector.getSequence()));
        
        if(vector.getDescription() != null){
            createVector.put("description", vector.getDescription());
        }
        
        String originId = createFeature(vector.getOrigin(),clothoObject);
        createVector.put("origin", originId);
        
        String resistanceId = createFeature(vector.getResistance(),clothoObject);
        createVector.put("resistance", resistanceId);
        
        //Clotho ID
        if (vector.getClothoID() != null) {
            createVector.put("id", vector.getClothoID());
        } else {
            createVector.put("id", vector.getName());
        }

        id = (String) clothoObject.set(createVector);
        vector.setClothoID(id);
        return id;
    }
    
    //Add fluorophores to Clotho via Clotho Server API
    public static String createCytometer(Cytometer c, Clotho clothoObject) {
        String id = "";

        //Fluorophore schema
        Map createCytometer = new HashMap();
        createCytometer.put("schema", Cytometer.class.getCanonicalName());
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

        id = (String) clothoObject.set(createCytometer);
        c.setClothoID(id);

        return id;
    }

    //Add polynucleotides to Clotho via Clotho Server API
    public static String createAssemblyParameters(AssemblyParameters aP, Clotho clothoObject) {

        String id = "";
        Map createAssmParam = new HashMap();
        createAssmParam.put("schema", AssemblyParameters.class.getCanonicalName());
        createAssmParam.put("method", aP.getMethod());
        createAssmParam.put("name", aP.getName());
        createAssmParam.put("oligoNameRoot", aP.getOligoNameRoot());
        createAssmParam.put("meltingTemperature", aP.getMeltingTemperature());
        createAssmParam.put("minPCRLength", aP.getMinPCRLength());
        createAssmParam.put("targetHomologyLength", aP.getTargetHomologyLength());
        createAssmParam.put("minCloneLength", aP.getMinCloneLength());
        createAssmParam.put("maxPrimerLength", aP.getMaxPrimerLength());

        List<String> recommended = new ArrayList<>(aP.getRecommended());
        createAssmParam.put("recommended", recommended);

        List<String> required = new ArrayList<>(aP.getRequired());
        createAssmParam.put("required", required);

        List<String> discouraged = new ArrayList<>(aP.getDiscouraged());
        createAssmParam.put("discouraged", discouraged);

        List<String> forbidden = new ArrayList<>(aP.getForbidden());
        createAssmParam.put("forbidden", forbidden);

        List<String> efficiency = new ArrayList<>(aP.getEfficiency());
        createAssmParam.put("efficiency", efficiency);

        //Clotho ID
        if (aP.getClothoID() != null) {
            createAssmParam.put("id", aP.getClothoID());
        }

        id = (String) clothoObject.set(createAssmParam);
        aP.setClothoID(id);

        return id;

    }
    //</editor-fold>

    //</editor-fold>
    
    //<editor-fold desc="Map to Object">
    
    public static Part mapToPart(Map map){
        
        String name = (String)map.get("name");
        String description = "";
        if(map.containsKey("description")){
            description = (String)map.get("description");
        }
        Part p = Part.generateBasic(name, description, mapToNucSeq((Map)map.get("sequence")), null, null);
//        p.setVector(Boolean.parseBoolean(map.get("isVector").toString()));
        p.setClothoID((String)map.get("id"));
        
        return p;
    }
    
    public static Module mapToModule(Map map,Clotho clothoObject){
        Module module = new Module(map.get("name").toString());
        module.setClothoID(map.get("id").toString());
        module.setRole(Module.ModuleRole.valueOf(map.get("role").toString()));
        module.setStage((int) map.get("stage"));
        module.setForward((boolean) map.get("isForward"));
        module.setRoot((boolean) map.get("isRoot"));

        JSONArray featureIds = new JSONArray();
        featureIds = (JSONArray) map.get("moduleFeatures");
        JSONArray featureJSONArray = new JSONArray();
        for (Object obj : featureIds) {
            String featureId = (String) obj;

            featureJSONArray.add(clothoObject.get(featureId));
        }
        List<Feature> featureSet = new ArrayList<Feature>();
        featureSet = convertJSONArrayToFeatures(featureJSONArray,clothoObject);

        List<Feature> features = new ArrayList<Feature>();
        for (Feature f : featureSet) {
            features.add(f);
        }
        
        
        return module;
    }
    
    public static PrimitiveModule mapToPrimitiveModule(Map map,Clotho clothoObject){
        FeatureRole role = FeatureRole.valueOf((String)map.get("primitiveRole"));
        Map primitiveMap = new HashMap();
        primitiveMap = (Map)map.get("primitive");
        Primitive primitive = new Primitive(new ComponentType((String)primitiveMap.get("type")),(String)primitiveMap.get("name"));
        Map moduleFeatureMap = new HashMap();
        moduleFeatureMap = (Map)clothoObject.get((String)map.get("moduleFeature"));
        return new PrimitiveModule(role,primitive,mapToFeatureWithArcs(moduleFeatureMap,clothoObject));
    }
    
    public static Feature mapToFeatureWithArcs(Map map,Clotho clothoObject){
        Feature feature = new Feature((String)map.get("name"));
        feature.setForwardColor(new Color((int)map.get("forwardColor")));
        feature.setReverseColor(new Color((int)map.get("reverseColor")));
        feature.setClothoID((String)map.get("id"));
        feature.setSequence(mapToSequence((Map)map.get("sequence")));
        feature.setRole(FeatureRole.valueOf((String)map.get("role")));
        
        HashMap<String,Feature> featureList = new HashMap<>();
        featureList.put(feature.getName(), feature);
        //Something for ARCS???
        for(Map arcMap: (List<Map>)map.get("arcs")){
            Arc arc = new Arc();
            arc.setRole(ArcRole.valueOf((String)arcMap.get("role")));
            boolean otherIsRegulatee = false;
            String otherFeatureName = (String)arcMap.get("regulator");
            if(((String)arcMap.get("regulator")).equals(feature.getName())){
                otherFeatureName = (String)arcMap.get("regulatee");
                otherIsRegulatee = true;
            }
            Map queryOtherFeature = new HashMap();
            queryOtherFeature.put("schema", Feature.class.getCanonicalName());
            queryOtherFeature.put("name", otherFeatureName);
            Map otherFeatureMap = (Map)clothoObject.queryOne(queryOtherFeature);
            Feature otherFeature = mapToFeature(otherFeatureMap);
            if(otherIsRegulatee){
                arc.setRegulator(feature);
                arc.setRegulatee(otherFeature);
                feature.getArcs().add(arc);
                otherFeature.getArcs().add(arc);
            }
            
        }
        return feature;
    }
    
    public static Feature mapToFeature(Map map) {
        Feature feature = new Feature((String) map.get("name"));
        feature.setForwardColor(new Color((int) map.get("forwardColor")));
        feature.setReverseColor(new Color((int) map.get("reverseColor")));
        feature.setClothoID((String) map.get("id"));
        feature.setSequence(mapToSequence((Map) map.get("sequence")));
        feature.setRole(FeatureRole.valueOf((String) map.get("role")));
        return feature;
    }
    
    public static Sequence mapToSequence(Map map){
        Sequence sequence = new NucSeq((String)map.get("sequence"));
        
        return sequence;
    }
    
    public static Vector mapToVector(Map map,Clotho clothoObject){
        
        String name = (String)map.get("name");
        String description = "";
        if(map.containsKey("description")){
            description = (String)map.get("description");
        }
        Feature origin = (Feature)getFeature((String)map.get("origin"),clothoObject);
        Feature resistance = (Feature)getFeature((String)map.get("origin"),clothoObject);
        Vector vector = new Vector(name, description, mapToNucSeq((Map)map.get("sequence")), null, null,origin,resistance);
        vector.setClothoID((String)map.get("id"));
        
        return vector;
    }
    
    public static NucSeq mapToNucSeq(Map map){
        
        String seq = map.get("sequence").toString();
        boolean circular = Boolean.parseBoolean(map.get("isCircular").toString());
        boolean ss = Boolean.parseBoolean(map.get("isSingleStranded").toString());

        NucSeq ns = new NucSeq(seq, ss, circular);
        ns.setName(map.get("name").toString() + ".ref");
        
        //Get all Annotations
        List<Map> arrayAnnotations = new ArrayList<>();
        arrayAnnotations = (List<Map>)map.get("annotations");

        for (Map annotationMap:arrayAnnotations) {

            //Get annotation fields
            
            int startAn = Integer.valueOf(annotationMap.get("start").toString());
            int endAn = Integer.valueOf(annotationMap.get("end").toString());
            boolean fwdStAn = Boolean.parseBoolean(annotationMap.get("isForwardStrand").toString());
            
            /*
            String fwdColorStAn = annotationMap.get("forwardColor").toString();
            String[] rgbfwdAn = fwdColorStAn.substring(15, fwdColorStAn.length() - 1).split(",");
            Color fwdColorAn = new Color(Integer.valueOf(rgbfwdAn[0].substring(2)), Integer.valueOf(rgbfwdAn[1].substring(2)), Integer.valueOf(rgbfwdAn[2].substring(2)));
            String revColorStAn = annotationMap.get("reverseColor").toString();
            String[] rgbrevAn = revColorStAn.substring(15, revColorStAn.length() - 1).split(",");
            Color revColorAn = new Color(Integer.valueOf(rgbrevAn[0].substring(2)), Integer.valueOf(rgbrevAn[1].substring(2)), Integer.valueOf(rgbrevAn[2].substring(2)));
            */
            
            //Get feature fields
            JSONObject jsonFeature = (JSONObject) annotationMap.get("feature");
            Feature feature = new Feature((String)jsonFeature.get("name"));
            
            /*
            String fwdColorSt = jsonFeature.get("forwardColor").toString();
            String[] rgbfwd = fwdColorSt.substring(15, fwdColorSt.length() - 1).split(",");
            Color fwdColor = new Color(Integer.valueOf(rgbfwd[0].substring(2)), Integer.valueOf(rgbfwd[1].substring(2)), Integer.valueOf(rgbfwd[2].substring(2)));
            String revColorSt = jsonFeature.get("reverseColor").toString();
            String[] rgbrev = revColorSt.substring(15, revColorSt.length() - 1).split(",");
            Color revColor = new Color(Integer.valueOf(rgbrev[0].substring(2)), Integer.valueOf(rgbrev[1].substring(2)), Integer.valueOf(rgbrev[2].substring(2)));
            */
            
            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) jsonFeature.get("sequence");
            String fseq = jsonSequence.get("sequence").toString();
            NucSeq fsequence = new NucSeq(fseq);

            //Get FeatureRole
            if (jsonFeature.has("role")) {
                feature.setRole(FeatureRole.valueOf((String)jsonFeature.get("role")));
            }
            feature.setForwardColor(new Color((int)jsonFeature.get("forwardColor")));
            feature.setReverseColor(new Color((int)jsonFeature.get("reverseColor")));
        
            feature.setSequence(fsequence);

            //Get person
            Person author = new Person();
            JSONObject jsonPerson = (JSONObject) annotationMap.get("author");
            author.setGivenName(jsonPerson.get("givenName").toString());
            author.setSurName(jsonPerson.get("surName").toString());
            author.setEmailAddress(jsonPerson.get("emailAddress").toString());
            
            
            //Assign all the annotation values to the object
            Annotation annotation = new Annotation(feature, fsequence, new Color((int)annotationMap.get("forwardColor")), new Color((int)annotationMap.get("reverseColor")), startAn, endAn, author, fwdStAn, null);
            ns.addAnnotation(annotation);
        }
        
        ns.setClothoID((String)map.get("id"));
        
        return ns;
        //String name
    }
    
    public static Fluorophore mapToFluorophore(Map map){
        
            String name = (String)map.get("name");
            Fluorophore fluorophore = new Fluorophore(name);

            //Get fluorophore fields
            /*
            String fwdColorSt = map.get("forwardColor").toString();
            String[] rgbfwd = fwdColorSt.substring(15, fwdColorSt.length() - 1).split(",");
            Color fwdColor = new Color(Integer.valueOf(rgbfwd[0].substring(2)), Integer.valueOf(rgbfwd[1].substring(2)), Integer.valueOf(rgbfwd[2].substring(2)));
            String revColorSt = map.get("reverseColor").toString();
            String[] rgbrev = revColorSt.substring(15, revColorSt.length() - 1).split(",");
            Color revColor = new Color(Integer.valueOf(rgbrev[0].substring(2)), Integer.valueOf(rgbrev[1].substring(2)), Integer.valueOf(rgbrev[2].substring(2)));
            */
            
            
            Integer oligo = Integer.valueOf(map.get("oligomerization").toString());
            Double brightness = Double.valueOf(map.get("brightness").toString());
            Double ex = Double.valueOf(map.get("excitation_max").toString());
            Double em = Double.valueOf(map.get("emission_max").toString());

            //Get excitation and emmission spectrums
            HashMap<Double, Double> em_spectrum = new HashMap<>();
            JSONArray jsonEm_spectrum = (JSONArray) map.get("em_spectrum");
            for (int j = 0; j < jsonEm_spectrum.size(); j++) {
                JSONObject jsonObject = jsonEm_spectrum.getJSONObject(j);
                em_spectrum.put(Double.valueOf(jsonObject.get("x").toString()), Double.valueOf(jsonObject.get("y").toString()));
            }
            fluorophore.setEm_spectrum(em_spectrum);

            HashMap<Double, Double> ex_spectrum = new HashMap<>();
            JSONArray jsonEx_spectrum = (JSONArray) map.get("ex_spectrum");
            for (int k = 0; k < jsonEx_spectrum.size(); k++) {
                JSONObject jsonObject = jsonEx_spectrum.getJSONObject(k);
                ex_spectrum.put(Double.valueOf(jsonObject.get("x").toString()), Double.valueOf(jsonObject.get("y").toString()));
            }
            fluorophore.setEx_spectrum(ex_spectrum);

            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) map.get("sequence");
            String seq = jsonSequence.get("sequence").toString();
            NucSeq sequence = new NucSeq(seq);

            //Get FeatureRole
            fluorophore.setRole(FeatureRole.valueOf((String)map.get("role")));
            
            fluorophore.setForwardColor(new Color((int)map.get("forwardColor")));
            fluorophore.setReverseColor(new Color((int)map.get("reverseColor")));
            
            fluorophore.setName(name);
            fluorophore.setSequence(sequence);
            fluorophore.setOligomerization(oligo);
            fluorophore.setBrightness(brightness);
            fluorophore.setEmission_max(em);
            fluorophore.setExcitation_max(ex);
            fluorophore.setClothoID(map.get("id").toString());
            return fluorophore;
            
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Query Methods">
    
    public static List<Fluorophore> queryFluorophores(Map map, Clotho clothoObject) {
        
        map.put("schema", Fluorophore.class.getCanonicalName());
        
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;
        List<Fluorophore> fluorophores = new ArrayList<>();
        for(Object object:array){
            fluorophores.add(mapToFluorophore((Map)object)) ;
        }
        return fluorophores;
    }
    
    public static List<Vector> queryVectors(Map map, Clotho clothoObject){
        
        map.put("schema", Vector.class.getCanonicalName());
        
        List<Vector> vectors = new ArrayList<>();
        Object query = clothoObject.query(map);
        JSONArray array = new JSONArray();
        array = (JSONArray) query;
        for(Object object:array){
            vectors.add(mapToVector((Map)object,clothoObject));
        }
        return vectors;
    }
    
    public static List<Part> queryParts(Map map, Clotho clothoObject) {

        map.put("schema", Part.class.getCanonicalName());
        
        List<Part> parts = new ArrayList<>();
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;
        for(Object object:array){
            parts.add(mapToPart((Map)object));
        }
        return parts;
    }
    
    public static Feature querySingleFeature(Map map, Clotho clothoObject){
        map.put("schema", Feature.class.getCanonicalName());
        Map query = (Map)((JSONObject)clothoObject.queryOne(map));
        HashMap<String,Feature> featureListMap = new HashMap<String,Feature>();
        Feature feature = mapToFeature(query);
        featureListMap.put(feature.getName(), feature);
        JSONArray arrayArcs = (JSONArray) query.get("arcs");
        if(arrayArcs!=null){
            for (int j = 0; j < arrayArcs.size(); j++) {
                    JSONObject jsonArc = arrayArcs.getJSONObject(j);
                    String regulator = jsonArc.get("regulator").toString();
                    String regulatee = jsonArc.get("regulatee").toString();
                    
                    Map queryRegulator = new HashMap();
                    queryRegulator.put("schema",Feature.class.getCanonicalName());
                    queryRegulator.put("name", regulator);
                    Map regulatorResult = (Map)((JSONObject)clothoObject.queryOne(queryRegulator));
                    Feature regulatorFeature = mapToFeature(regulatorResult);
                    
                    Map queryRegulatee = new HashMap();
                    queryRegulatee.put("schema",Feature.class.getCanonicalName());
                    queryRegulatee.put("name", regulatee);
                    Map regulateeResult = (Map)((JSONObject)clothoObject.queryOne(queryRegulatee));
                    Feature regulateeFeature = mapToFeature(regulateeResult);
                    if(!featureListMap.containsKey(regulatorFeature.getName())){
                        featureListMap.put(regulatorFeature.getName(), regulatorFeature);
                    }
                    if(!featureListMap.containsKey(regulateeFeature.getName())){
                        featureListMap.put(regulateeFeature.getName(), regulateeFeature);
                    }
                    
                    
                    Arc arc = new Arc();
                    arc.setRole(ArcRole.valueOf((String)jsonArc.get("role")));
                    JSONArray arraySMs = (JSONArray) jsonArc.get("molecules");

                    for (int k = 0; k < arraySMs.size(); k++) {

                        SmallMolecule sm = new SmallMolecule();

                        //Get small molecule fields
                        JSONObject jsonSM = arraySMs.getJSONObject(k);
                        String smName = jsonSM.get("name").toString();
                        sm.setName(smName);

                        sm.setRole(SmallMolecule.SmallMoleculeRole.valueOf((String)jsonSM.get("role")));
                        arc.getMolecules().add(sm);

                    }
                    arc.setRegulatee(featureListMap.get(regulatee));
                    arc.setRegulator(featureListMap.get(regulator));
                    featureListMap.get(regulatee).getArcs().add(arc);
                    featureListMap.get(regulator).getArcs().add(arc);
                    
            }
        }
        
        return feature;
    }
    
    public static List<Feature> queryFeatures(Map map, Clotho clothoObject) {
        
        map.put("schema", Feature.class.getCanonicalName());
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;
        return convertJSONArrayToFeatures(array,clothoObject);
    }
    
    //Get assembly parameters
    public static List<AssemblyParameters> queryAssemblyParameters(Map map, Clotho clothoObject) {

        //Establish Clotho connection
        List<AssemblyParameters> aPs = new ArrayList<>();
        map.put("schema", AssemblyParameters.class.getCanonicalName());
        Object query = clothoObject.query(map);
        JSONArray arrayAP = (JSONArray) query;

        for (int i = 0; i < arrayAP.size(); i++) {

            JSONObject jsonAP = arrayAP.getJSONObject(i);
            AssemblyParameters candidate = new AssemblyParameters(jsonAP);
            aPs.add(candidate);
        }
        return aPs;
    }

    //Get all Clotho Polynucleotides.. Why man? Why do you want all the Polynucleotides???
    public static List<Polynucleotide> queryPolynucleotides(Map map, Clotho clothoObject) {

        map.put("schema", Polynucleotide.class.getCanonicalName());
        
        //Establish Clotho connection
        List<Polynucleotide> polynucs = new ArrayList<>();

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
            if (jsonPolynuc.containsKey("submissionDate")) {
                if (jsonPolynuc.get("submissionDate") != null) {
//                    pn.setSubmissionDate(new Date(Long.parseLong(jsonPolynuc.get("submissionDate").toString())));
                }
            }

            //Imbedded objects
            
            pn.setSequence(mapToNucSeq((Map) jsonPolynuc.get("sequence")));
            Map jsonPart = new HashMap();
            jsonPart = (Map) clothoObject.get((String) jsonPolynuc.get("part"));
            pn.setPart(mapToPart(jsonPart));
            Map jsonVec = new HashMap();
            jsonVec = (Map) clothoObject.get((String) jsonPolynuc.get("vector"));
            pn.setVector(mapToVector(jsonVec,clothoObject));
            
            pn.setClothoID(jsonPolynuc.get("id").toString());
            pn.setDV(Boolean.parseBoolean(jsonPolynuc.get("isDV").toString()));
            pn.setLevel(Integer.valueOf(jsonPolynuc.get("level").toString()));
            polynucs.add(pn);
        }

        return polynucs;
    }

    //Get all Clotho Cytometers
    public static List<Cytometer> queryCytometers(Map map, Clotho clothoObject) {

        //Establish Clotho connection
        map.put("schema", Cytometer.class.getCanonicalName());
        
        List<Cytometer> cytometers = new ArrayList<>();
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

        return cytometers;
    }

    //</editor-fold>
    
    //<editor-fold desc="Get Methods">
    
    public static Fluorophore getFluorophore(String fluorophoreId, Clotho clothoObject){
        Object object = clothoObject.get(fluorophoreId);
        return mapToFluorophore((Map)object);
    }
    
    public static Feature getFeature(String featureId,Clotho clothoObject){
        Object object = clothoObject.get(featureId);
        return mapToFeatureWithArcs((Map)object,clothoObject);
    }
    
    public static Vector getVector(String vectorId, Clotho clothoObject){
        Object object = clothoObject.get(vectorId);
        return mapToVector((Map)object,clothoObject);    
    }
    
    public static Part getPart(String partId, Clotho clothoObject){
        Object object = clothoObject.get(partId);
        //Do some check for map?
        return mapToPart((Map)object);
    }
    
    //Over-ride with module? Maybe get Module parameters and then clone?
    public static AssignedModule getAssignedModule(String assignedModuleId, Clotho clothoObject) {

        JSONObject amObj = new JSONObject();
        amObj = (JSONObject) clothoObject.get(assignedModuleId);
        
        
        AssignedModule amodule = new AssignedModule(mapToModule(amObj,clothoObject));
        for (Object expId : (JSONArray) amObj.get("experiments")) {
            amodule.getExperiments().add(getExperiment((String) expId, clothoObject));
        }
        if(amObj.containsKey("shortName")){
            amodule.setShortName((String)amObj.get("shortName"));
        }
        amodule.setClothoID(assignedModuleId);
        return amodule;

    }

    public static Experiment getExperiment(String experimentid, Clotho clothoObject) {

        JSONObject exptObj = new JSONObject();
        exptObj = (JSONObject) clothoObject.get(experimentid);
        
        JSONArray timeArray = new JSONArray();
        List<String> times = new ArrayList<String>();
        if (timeArray.size() > 0) {
            for (Object obj : timeArray) {
                times.add((String) obj);
            }
        }
            
        List<Medium> media = new ArrayList<Medium>();
        
        //Get Media Conditions
        for (Object mediaObj : (JSONArray) exptObj.get("mediaConditions")) {
            Map mediaMap = new HashMap();
            String mediaName = (String) mediaMap.get("name");
            MediaType mediaType = MediaType.valueOf((String) mediaMap.get("type"));
            Medium medium = new Medium(mediaName, mediaType);
            Map smoleculeMap = new HashMap();
            smoleculeMap = (Map) mediaMap.get("smallMolecule");
            SmallMolecule smolecule = new SmallMolecule();
            smolecule.setName((String) smoleculeMap.get("name"));
            smolecule.setRole(SmallMolecule.SmallMoleculeRole.valueOf((String) smoleculeMap.get("role")));
            smolecule.setConcentration(Double.valueOf((String) smoleculeMap.get("concentration")));
            medium.setSmallmolecule(smolecule);
            media.add(medium);
        }
        
        Experiment experiment = new Experiment(Experiment.ExperimentType.valueOf((String) exptObj.get("exType")), (String)exptObj.getString("name"),media,times );
        experiment.setClothoID((String) exptObj.get("id"));
        
        return experiment;
    }
    
    public static Module getModule(String rootModule, Clotho clothoObject) {
        Map map = new HashMap();
        map = (Map) clothoObject.get(rootModule);

        Module module = new Module(map.get("name").toString());
        //module.setName(map.get("name").toString()); //This seems redundant since the Module constructor sets the name of the module.
        module.setClothoID(map.get("id").toString());
        module.setRole(Module.ModuleRole.valueOf(map.get("role").toString()));
        module.setStage((int) map.get("stage"));
        module.setForward((boolean) map.get("isForward"));
        module.setRoot((boolean) map.get("isRoot"));

        JSONArray children = new JSONArray();
        children = (JSONArray) map.get("children");
        for (Object childObj : children) {
            Module childModule = getModule(childObj.toString(), clothoObject);
            childModule.getParents().add(module);
            module.getChildren().add(childModule);
        }

        for (Object amoduleId : (JSONArray) map.get("assignedModules")) {
            module.getAssignedModules().add(getAssignedModule((String) amoduleId, clothoObject));
        }

        return module;
    }
    
    public static Polynucleotide getPolynucleotide(String polynucleotideId, Clotho clothoObject) {
        Polynucleotide pn = new Polynucleotide();
        JSONObject jsonPolynuc = new JSONObject();
        jsonPolynuc = (JSONObject) clothoObject.get(polynucleotideId);
        pn.setName(jsonPolynuc.get("name").toString());
        pn.setAccession(jsonPolynuc.get("accession").toString());
        pn.setDescription(jsonPolynuc.get("description").toString());
        pn.setLinear(Boolean.parseBoolean(jsonPolynuc.get("isLinear").toString()));
        pn.setSingleStranded(Boolean.parseBoolean(jsonPolynuc.get("isSingleStranded").toString()));
        if (jsonPolynuc.containsKey("submissionDate")) {
            if (jsonPolynuc.get("submissionDate") != null) {
                pn.setSubmissionDate(new Date(jsonPolynuc.get("submissionDate").toString()));
            }
        }

        //Imbedded objects
        pn.setSequence(mapToNucSeq((Map)jsonPolynuc.get("sequence")));
        Map jsonPart = new HashMap();
        jsonPart = (Map) clothoObject.get((String) jsonPolynuc.get("part"));
        pn.setPart(mapToPart(jsonPart));
        Map jsonVec = new HashMap();
        jsonVec = (Map) clothoObject.get((String) jsonPolynuc.get("vector"));
        pn.setVector(mapToVector(jsonVec,clothoObject));

        pn.setClothoID(jsonPolynuc.get("id").toString());
        pn.setDV(Boolean.parseBoolean(jsonPolynuc.get("isDV").toString()));
        pn.setLevel(Integer.valueOf(jsonPolynuc.get("level").toString()));

        return pn;
    }
    
    public static AssemblyParameters getAssemblyParameters(String id, Clotho clothoObject) {
        AssemblyParameters aP = new AssemblyParameters();
        Map assmParamMap = new HashMap<String, String>();
        assmParamMap.put("schema", AssemblyParameters.class.getCanonicalName());
        assmParamMap.put("name", "default");

        JSONObject apObject = new JSONObject();
        //        apObject = (JSONObject) clothoObject.query(assmParamMap);
        Object query = clothoObject.query(assmParamMap);
        JSONArray arrayAP = (JSONArray) query;
        if (apObject != null) {
            aP = new AssemblyParameters(arrayAP.getJSONObject(0));
        }
        return aP;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Helper functions">
    //Get all Clotho Features
    public static List<Feature> convertJSONArrayToFeatures(JSONArray array,Clotho clothoObject) {

        HashMap<String,Feature> featureListMap = new HashMap<String,Feature>();
        
        for(Object obj:array){
            Map featureMap = new HashMap();
            featureMap = (Map)obj;
            Feature feature = mapToFeature(featureMap);
            if(!featureListMap.containsKey(feature.getName())){
                featureListMap.put(feature.getName(), feature);
            }
            JSONArray arrayArcs = (JSONArray)featureMap.get("arcs");
            if(arrayArcs!=null){
                for (int j = 0; j < arrayArcs.size(); j++) {
                    JSONObject jsonArc = arrayArcs.getJSONObject(j);
                    String regulator = jsonArc.get("regulator").toString();
                    String regulatee = jsonArc.get("regulatee").toString();

                    Map queryRegulator = new HashMap();
                    queryRegulator.put("schema", Feature.class.getCanonicalName());
                    queryRegulator.put("name", regulator);
                    Map regulatorResult = (Map) ((JSONObject) clothoObject.queryOne(queryRegulator));
                    Feature regulatorFeature = mapToFeature(regulatorResult);

                    Map queryRegulatee = new HashMap();
                    queryRegulatee.put("schema", Feature.class.getCanonicalName());
                    queryRegulatee.put("name", regulatee);
                    Map regulateeResult = (Map) ((JSONObject) clothoObject.queryOne(queryRegulatee));
                    Feature regulateeFeature = mapToFeature(regulateeResult);
                    if (!featureListMap.containsKey(regulatorFeature.getName())) {
                        featureListMap.put(regulatorFeature.getName(), regulatorFeature);
                    }
                    if (!featureListMap.containsKey(regulateeFeature.getName())) {
                        featureListMap.put(regulateeFeature.getName(), regulateeFeature);
                    }

                    Arc arc = new Arc();
                    arc.setRole(ArcRole.valueOf((String) jsonArc.get("role")));
                    JSONArray arraySMs = (JSONArray) jsonArc.get("molecules");

                    for (int k = 0; k < arraySMs.size(); k++) {

                        SmallMolecule sm = new SmallMolecule();

                        //Get small molecule fields
                        JSONObject jsonSM = arraySMs.getJSONObject(k);
                        String smName = jsonSM.get("name").toString();
                        sm.setName(smName);

                        sm.setRole(SmallMolecule.SmallMoleculeRole.valueOf((String) jsonSM.get("role")));
                        arc.getMolecules().add(sm);

                    }
                    arc.setRegulatee(featureListMap.get(regulatee));
                    arc.setRegulator(featureListMap.get(regulator));
                    featureListMap.get(regulatee).getArcs().add(arc);
                    featureListMap.get(regulator).getArcs().add(arc);

                }
            }

        }
        List<Feature> featureList = new ArrayList<>();
        featureList.addAll(featureListMap.values());
        return featureList;
    }
    
    //Remove parts from a set with duplicate sequence
    public static void removeDuplicateParts(HashSet<Polynucleotide> polyNucs, Clotho clothoObject) {

        HashMap<String, Part> sequencePartMap = new HashMap();
        HashMap<String, Vector> sequenceVectorMap = new HashMap();

        //Put all existing parts and vectors into their maps
        Map partQuery = new HashMap();
        partQuery.put("schema", Part.class.getCanonicalName());
        Map vectorQuery = new HashMap();
        vectorQuery.put("schema", Vector.class.getCanonicalName());
        List<Part> queryParts = queryParts(partQuery, clothoObject);
        List<Vector> queryVectors = queryVectors(vectorQuery, clothoObject);
        for (Part p : queryParts) {
            sequencePartMap.put(p.getSequence().getSeq(), p);
        }
        for (Vector v : queryVectors) {
            sequenceVectorMap.put(v.getSequence().getSeq(), v);
        }

        //Only add parts with new sequence to the output
        for (Polynucleotide pn : polyNucs) {

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
                } else {
                    sequencePartMap.put(partSeq, part);
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
                } else {
                    sequenceVectorMap.put(vecSeq, vector);
                }
            }
        }
    }

    //Annotate part and vector of a polynucleotide
    public static void annotateParts(List<Feature> features, HashSet<Polynucleotide> polyNucs) {

        for (Polynucleotide pn : polyNucs) {
            if (pn.getPart() != null && pn.getVector() != null) {
                annotate(features, pn.getPart().getSequence());
                annotate(features, pn.getVector().getSequence());
                pn.getVector().findOriRes(pn.getVector().getSequence());
            }
        }
    }

    //Automatically annotate a NucSeq with a feature library
    public static void annotate(List<Feature> features, NucSeq ns) {

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
    //</editor-fold>
    
    //<editor-fold desc="Commented out code" defaultstate="collapsed">
    
    //<editor-fold desc="Commenting out createSampleMethod" defaultstate="collapsed">
    /*
     public static String createSample(Sample sample, Clotho clothoObject) {
     String id = "";
     Map map = new HashMap();
     map.put("schema", "org.cidarlab.phoenix.core.dom.Sample");
     map.put("type", sample.getType());
     if (sample.getClothoID() != null) {
     map.put("id", sample.getClothoID());
     }
     if (sample.getName() != null) {
     map.put("name", sample.getName());
     }

     Map mediaMap = new HashMap();
     if (sample.getMedia() != null) {
     mediaMap = createMediumMap(sample.getMedia());
     }
     map.put("media", mediaMap);

     Map strainMap = new HashMap();
     if (sample.getStrain() != null) {
     strainMap = createStrainMap(sample.getStrain());
     }
     map.put("strain", strainMap);

     JSONArray polyIds = new JSONArray();
     if (sample.getPolynucleotides() != null) {
     for (Polynucleotide p : sample.getPolynucleotides()) {
     String polyId = createPolynucleotide(p, clothoObject);
     polyIds.add(polyId);
     }
     }
     map.put("polynucleotides", polyIds);
     map.put("time", sample.getTime());
     id = (String) clothoObject.create(map);
     sample.setClothoID(id);
     return id;
     }
     */
    //</editor-fold>
    
    //<editor-fold desc="Commenting out get Sample function" defaultstate="collapsed">
    /*
    public static Sample getSample(String sampleId, Clotho clothoObject) {
        JSONObject sampleObj = new JSONObject();
        sampleObj = (JSONObject) clothoObject.get(sampleId);
        //Get Sample Type
        SampleType sType = SampleType.valueOf((String) sampleObj.get("type"));

        //Get Media Map from Sample and convert it to a Medium object
        Map mediaMap = new HashMap();
        mediaMap = (Map) sampleObj.get("media");
        String mediaName = (String) mediaMap.get("name");
        Medium.MediaType mediaType = MediaType.valueOf((String) mediaMap.get("type"));
        Medium media = new Medium(mediaName, mediaType);
        //Get SmallMolecule Map from Media Map and convert it to a SmallMolecule Object
        Map smoleculeMap = new HashMap();
        smoleculeMap = (Map) mediaMap.get("smallMolecule");
        SmallMolecule smolecule = new SmallMolecule();
        smolecule.setName((String) smoleculeMap.get("name"));
        smolecule.setRole(SmallMolecule.SmallMoleculeRole.valueOf((String) smoleculeMap.get("role")));
        smolecule.setConcentration(Double.valueOf((String) smoleculeMap.get("concentration")));
        media.setSmallmolecule(smolecule);

        Map strainMap = new HashMap();
        strainMap = (Map) sampleObj.get("strain");
        Strain strain = new Strain((String) strainMap.get("name"));

        List<Polynucleotide> polynucleotides = new ArrayList<Polynucleotide>();
        JSONArray polyNucIds = new JSONArray();
        polyNucIds = (JSONArray) sampleObj.get("polynucleotides");
        for (Object obj : polyNucIds) {
            String polyNucId = (String) obj;
            polynucleotides.add(getPolynucleotide(polyNucId, clothoObject));
        }
        String time = (String) sampleObj.get("time");
        Sample sample = new Sample(sType, strain, polynucleotides, media, time);
        sample.setClothoID((String) sampleObj.get("id"));
        sample.setName((String) sampleObj.get("name"));

        return sample;
    }
    */
    //</editor-fold>
    
    //</editor-fold>
    
}
