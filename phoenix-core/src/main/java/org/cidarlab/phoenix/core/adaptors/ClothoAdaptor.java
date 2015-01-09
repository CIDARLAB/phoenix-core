/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public static Clotho clothoUpload(File input, boolean featureLib) throws Exception {

        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        //If this file is a feature library, only get features and save those to Clotho
        if (featureLib) {
            HashSet<Feature> features = BenchlingAdaptor.getFeatures(input);
            HashSet<Fluorophore> fluorophores = BenchlingAdaptor.getFluorophores(features);
            features.removeAll(fluorophores);
            createClothoFeatures(features, clothoObject);
            createClothoFluorophores(fluorophores, clothoObject);
            
        } else {
 
            //Get polynucleotides, nucseqs and parts from a multi-part genbank file     
            HashSet<Polynucleotide> polyNucs = BenchlingAdaptor.getPolynucleotide(input);
            HashSet<NucSeq> nucSeqs = BenchlingAdaptor.getNucSeq(input);
            HashSet<Part> parts = BenchlingAdaptor.getMoCloParts(input);

            //Save all polynucleotides, nucseqs and parts to Clotho
            createClothoPolynucleotides(polyNucs, clothoObject);
            createClothoParts(parts, clothoObject);
            createClothoNucSeqs(nucSeqs, clothoObject);
        }
        
        conn.closeConnection();
        return clothoObject;
    }

    //Add polynucleotides to Clotho via Clotho Server API
    public static void createClothoPolynucleotides(HashSet<Polynucleotide> polyNucs, Clotho clothoObject) {

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
    public static void createClothoFeatures(HashSet<Feature> features, Clotho clothoObject) {

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
    public static void createClothoFluorophores(HashSet<Fluorophore> flourophores, Clotho clothoObject) {

        for (Fluorophore f : flourophores) {

            //Feature schema
            Map createFluorophore = new HashMap();
            createFluorophore.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
            createFluorophore.put("name", f.getName());
//            createFluorophore.put("forwardColor", f.getForwardColor().toString());
//            createFluorophore.put("reverseColor", f.getReverseColor().toString());
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
    public static void createClothoParts(HashSet<Part> parts, Clotho clothoObject) {
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
    public static void createClothoNucSeqs(HashSet<NucSeq> nucSeqs, Clotho clothoObject) {

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

                //Feature schema
                Feature f = annotation.getFeature();
                Map createFeature = new HashMap();
                createFeature.put("schema", "org.clothocad.model.Feature");
                createFeature.put("forwardColor", f.getForwardColor().toString());
                createFeature.put("reverseColor", f.getReverseColor().toString());

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
    public static HashSet<Feature> queryClothoFeatures() {
        
        //Establish Clotho connection
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        Map map = new HashMap();
        map.put("schema", "org.clothocad.model.Feature");
        Object query = clothoObject.query(map);
        
        conn.closeConnection();
        
        return null;
    }
    
    //Get all Clotho Fluorophores
    public static HashSet<Fluorophore> queryClothoFluorophores() {
        return null;
    }
    
    //Get all Clotho NucSeqs
    public static HashSet<NucSeq> queryClothoNucSeqs() {
        return null;
    }
    
    //Get all Clotho Parts
    public static HashSet<Part> queryClothoParts() {
        return null;
    }
    
    //Get all Clotho Polynucleotides
    public static HashSet<Polynucleotide> queryClothoPolyNucleotides() {
        return null;
    }
}
