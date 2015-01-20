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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.*;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.clothocad.model.Feature;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Part;
import org.clothocad.model.Polynucleotide;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.Annotation;
import org.clothocad.model.Person;

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
    public static Clotho uploadSequences(File input, boolean featureLib) throws Exception {

        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        //If this file is a feature library, only get features and save those to Clotho
        if (featureLib) {
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Fluorophore> fluorophores = BenchlingAdaptor.getFluorophores(features);
            features.removeAll(fluorophores);
            createFeatures(features, clothoObject);
            createFluorophores(fluorophores, clothoObject);
            
        } else {
 
            //Get polynucleotides, nucseqs and parts from a multi-part genbank file     
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            HashSet<NucSeq> nucSeqs = BenchlingAdaptor.getNucSeq(input);
            HashSet<Part> parts = BenchlingAdaptor.getMoCloParts(input);

            //Save all polynucleotides, nucseqs and parts to Clotho
            createPolynucleotides(polyNucs, clothoObject);
            createParts(parts, clothoObject);
            createNucSeqs(nucSeqs, clothoObject);
        }
        
        conn.closeConnection();
        return clothoObject;
    }
    
    /*
     * This method is for uploading fluorescence spectrum data to be associated with Fluorphore objects
     */
    public static Clotho uploadFluorescenceSpectrums (File input) throws FileNotFoundException, IOException {
        
        //Establish Clotho connection
//        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
//        Clotho clothoObject = new Clotho(conn);
        
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
//        HashSet<Fluorophore> queryFluorophores = queryFluorophores();
        
        String t = "";
        
//        conn.closeConnection();
//        return clothoObject;
        return null;
    }

    //Add polynucleotides to Clotho via Clotho Server API
    public static void createPolynucleotides(HashSet<Polynucleotide> polyNucs, Clotho clothoObject) {

        for (Polynucleotide pn : polyNucs) {

            //Polynucleotide schema
            Map createPolynucleotide = new HashMap();
            createPolynucleotide.put("schema", "org.clothocad.model.Polynucleotide");
            createPolynucleotide.put("name", pn.getAccession());
            createPolynucleotide.put("accession", pn.getAccession().substring(0, pn.getAccession().length() - 15));
            createPolynucleotide.put("description", pn.getDescription());
            createPolynucleotide.put("sequence", pn.getSequence());
            createPolynucleotide.put("isLinear", pn.isLinear());
            createPolynucleotide.put("isSingleStranded", pn.isSingleStranded());
            createPolynucleotide.put("submissionDate", pn.getSubmissionDate().toString());

            Clotho.create(createPolynucleotide);
        }
    }

    //Add features to Clotho via Clotho Server API
    public static void createFeatures(HashSet<Feature> features, Clotho clothoObject) {

        for (Feature f : features) {

            //Feature schema
            Map createFeature = new HashMap();
            createFeature.put("schema", "org.clothocad.model.Feature");
            createFeature.put("name", f.getName());
            createFeature.put("forwardColor", f.getForwardColor().toString());
            createFeature.put("reverseColor", f.getReverseColor().toString());

            //NucSeq sub-schema
            Map createSequence = new HashMap();
            createSequence.put("schema", "org.clothocad.model.Sequence");
            createSequence.put("sequence", f.getSequence().getSequence());
            createFeature.put("sequence", createSequence);

            Clotho.create(createFeature);
        }
    }
    
    //Add features to Clotho via Clotho Server API
    public static void createFluorophores(HashSet<Fluorophore> flourophores, Clotho clothoObject) {

        for (Fluorophore f : flourophores) {

            //Feature schema
            Map createFluorophore = new HashMap();
            createFluorophore.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
            createFluorophore.put("name", f.getName());
            createFluorophore.put("forwardColor", f.getForwardColor().toString());
            createFluorophore.put("reverseColor", f.getReverseColor().toString());
            createFluorophore.put("brightness", f.getBrightness());
            createFluorophore.put("emission_max", f.getEmission_max());
            createFluorophore.put("excitation_max", f.getExcitation_max());
            createFluorophore.put("oligomerization", f.getOligomerization());

            //NucSeq sub-schema
            Map createSequence = new HashMap();
            createSequence.put("schema", "org.clothocad.model.Sequence");
            createSequence.put("sequence", f.getSequence().getSequence());
            createFluorophore.put("sequence", createSequence);

            Clotho.create(createFluorophore);
        }
    }

    //Add parts to Clotho via Clotho Server API
    public static void createParts(HashSet<Part> parts, Clotho clothoObject) {
        for (Part p : parts) {

            //Part schema
            Map createPart = new HashMap();
            createPart.put("schema", "org.clothocad.model.Part");
            createPart.put("name", p.getName());

            //NucSeq sub-schema
            Map createNucSeq = new HashMap();
            createNucSeq.put("schema", "org.clothocad.model.NucSeq");
            createNucSeq.put("sequence", p.getSequence().getSeq());
            createNucSeq.put("isCircular", p.getSequence().isCircular());
            createNucSeq.put("isSingleStranded", p.getSequence().isSingleStranded());

            createPart.put("sequence", createNucSeq);

            clothoObject.create(createPart);
        }
    }

    //Add nucseqs to Clotho via Clotho Server API
    public static void createNucSeqs(HashSet<NucSeq> nucSeqs, Clotho clothoObject) {

        for (NucSeq ns : nucSeqs) {

            //NucSeq schema
            Map createNucSeqMain = new HashMap();
            createNucSeqMain.put("schema", "org.clothocad.model.NucSeq");
            createNucSeqMain.put("name", ns.getName());
            createNucSeqMain.put("sequence", ns.getSeq());
            createNucSeqMain.put("isCircular", ns.isCircular());
            createNucSeqMain.put("isSingleStranded", ns.isSingleStranded());

            Set<Annotation> annotations = ns.getAnnotations();
            List<Map> annotationList = new ArrayList<>();

            //Get all annotations
            for (Annotation annotation : annotations) {

                Map createAnnotation = new HashMap();
                createAnnotation.put("schema", "org.clothocad.model.Annotation");
                createAnnotation.put("start", annotation.getStart());
                createAnnotation.put("end", annotation.getEnd());
                createAnnotation.put("forwardColor", annotation.getForwardColor().toString());
                createAnnotation.put("reverseColor", annotation.getReverseColor().toString());
                createAnnotation.put("isForwardStrand", annotation.isForwardStrand());

                //Feature schema - assumed one feature per annotation
                Feature f = annotation.getFeature();
                Map createFeature = new HashMap();
                createFeature.put("schema", "org.clothocad.model.Feature");
                createFeature.put("forwardColor", f.getForwardColor().toString());
                createFeature.put("reverseColor", f.getReverseColor().toString());
                createFeature.put("name", f.getName());

                //NucSeq sub-schema
                Map createNucSeqSub = new HashMap();
                createNucSeqSub.put("schema", "org.clothocad.model.Sequence");
                createNucSeqSub.put("sequence", f.getSequence().getSequence());

                createFeature.put("sequence", createNucSeqSub);

                //Get this feature's Person
                Person author = annotation.getAuthor();
                Map createPerson = new HashMap();
                createPerson.put("schema", "org.clothocad.model.Person");
                createPerson.put("givenName", author.getGivenName());
                createPerson.put("surName", author.getSurName());
                createPerson.put("emailAddress", author.getEmailAddress());

                createAnnotation.put("feature", createFeature);
                createAnnotation.put("author", createPerson);

                annotationList.add(createAnnotation);
            }

            createNucSeqMain.put("annotations", annotationList);

            Clotho.create(createNucSeqMain);
        }
    }
    
    //Get all Clotho Features
    public static HashSet<Feature> queryFeatures() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        HashSet<Feature> features = new HashSet<>();
        
        Map map = new HashMap();
        map.put("schema", "org.clothocad.model.Feature");
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
            
            feature.setForwardColor(fwdColor);
            feature.setReverseColor(revColor);
            feature.setName(name);
            feature.setSequence(sequence);
            
            features.add(feature);
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
            
            //Get sequence object and fields
            JSONObject jsonSequence = (JSONObject) jsonFluorophore.get("sequence");
            String seq = jsonSequence.get("sequence").toString();            
            NucSeq sequence = new NucSeq(seq);
            
            fluorophore.setForwardColor(fwdColor);
            fluorophore.setReverseColor(revColor);
            fluorophore.setName(name);
            fluorophore.setSequence(sequence);
            fluorophore.setOligomerization(oligo);
            fluorophore.setBrightness(brightness);
            fluorophore.setEmission_max(em);
            fluorophore.setExcitation_max(ex);
            
            fluorophores.add(fluorophore);
        } 
        
        conn.closeConnection();
        
        return fluorophores;
    }
    
    //Get all Clotho NucSeqs
    public static HashSet<NucSeq> queryNucSeqs() {

        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        HashSet<NucSeq> nucSeqs = new HashSet<>();

        Map map = new HashMap();
        map.put("schema", "org.clothocad.model.NucSeq");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;

        for (int i = 0; i < array.size(); i++) {
                        
            //Get NucSeq fields
            JSONObject jsonNucSeq = array.getJSONObject(i);
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
            nucSeqs.add(ns);
        }

        conn.closeConnection();

        return nucSeqs;
    }

    
    //Get all Clotho Parts
    public static HashSet<Part> queryParts() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);

        HashSet<Part> parts = new HashSet<>();

        Map map = new HashMap();
        map.put("schema", "org.clothocad.model.Part");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;

        for (int i = 0; i < array.size(); i++) {
                        
            //Get Part fields
            JSONObject jsonPart = array.getJSONObject(i);
            String name = jsonPart.get("name").toString();
            
            //Get NucSeq fields
            JSONObject jsonNucSeq = (JSONObject) jsonPart.get("sequence");
            String seq = jsonNucSeq.get("sequence").toString();
            boolean circular = Boolean.parseBoolean(jsonNucSeq.get("isCircular").toString());
            boolean ss = Boolean.parseBoolean(jsonNucSeq.get("isSingleStranded").toString());
            
            NucSeq ns = new NucSeq(seq, ss, circular);
            ns.setName(jsonPart.get("name").toString());
            
            Part p = Part.generateBasic(name, "", seq, null, null);
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
        map.put("schema", "org.clothocad.model.Polynucleotide");
        Object query = clothoObject.query(map);
        JSONArray array = (JSONArray) query;

        for (int i = 0; i < array.size(); i++) {
                        
            Polynucleotide polynuc = new Polynucleotide();
            
            //Get Polynucleotide fields
            JSONObject jsonPolynuc = array.getJSONObject(i);
            polynuc.setName(jsonPolynuc.get("name").toString());
            polynuc.setAccession(jsonPolynuc.get("accession").toString());
            polynuc.setDescription(jsonPolynuc.get("description").toString());
            polynuc.setSequence(jsonPolynuc.get("sequence").toString());
            polynuc.setLinear(Boolean.parseBoolean(jsonPolynuc.get("isLinear").toString()));
            polynuc.setSingleStranded(Boolean.parseBoolean(jsonPolynuc.get("isSingleStranded").toString()));
            polynuc.setSubmissionDate(new Date(jsonPolynuc.get("submissionDate").toString()));
            
            polynucs.add(polynuc);
        }
        conn.closeConnection();
        
        return polynucs;
    }
}
