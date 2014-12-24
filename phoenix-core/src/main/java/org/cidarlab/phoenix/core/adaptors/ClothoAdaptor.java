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
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.*;
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
    public static void clothoGenbankUpload(File input) throws Exception {

        //Get features, polynucleotides, nucseqs and parts from a multi-part genbank file
        HashSet<Feature> features = getFeatures(input);
        HashSet<Polynucleotide> polyNucs = getPolynucleotide(input);
        ArrayList<NucSeq> nucSeqs = getNucSeq(input);
        HashSet<Part> parts = getMoCloParts(input);
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        //Save all features, polynucleotides, nucseqs and parts to Clotho
        createClothoPolynucleotides(polyNucs, clothoObject);
        createClothoFeatures(features, clothoObject);
        createClothoParts(parts, clothoObject);
        createClothoNucSeqs(nucSeqs, clothoObject);
        
        conn.closeConnection();
        
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
            Map createNucSeq = new HashMap();
            createNucSeq.put("schema", "org.clothocad.model.NucSeq");
            createNucSeq.put("sequence", f.getSequence().getSeq());
            createNucSeq.put("isCircular", f.getSequence().isCircular());
            createNucSeq.put("isSingleStranded", f.getSequence().isSingleStranded());

            createFeature.put("sequence", createNucSeq);

            Clotho.create(createFeature);
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

            Clotho.create(createPart);
        }
    }

    //Add nucseqs to Clotho via Clotho Server API
    public static void createClothoNucSeqs(ArrayList<NucSeq> nucSeqs, Clotho clothoObject) {

        for (NucSeq ns : nucSeqs) {

            //NucSeq schema
            Map createNucSeqMain = new HashMap();
            createNucSeqMain.put("schema", "org.clothocad.model.NucSeq");
            createNucSeqMain.put("name", ns.getName());
            createNucSeqMain.put("sequence", ns.getSeq());
            createNucSeqMain.put("isCircular", ns.isCircular());
            createNucSeqMain.put("isSingleStranded", ns.isSingleStranded());

            Set<Annotation> annotations = ns.getAnnotations();
            List<Map> annotationList = new ArrayList<Map>();

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
                createNucSeqSub.put("schema", "org.clothocad.model.NucSeq");
                createNucSeqSub.put("sequence", f.getSequence().getSeq());
                createNucSeqSub.put("isCircular", f.getSequence().isCircular());
                createNucSeqSub.put("isSingleStranded", f.getSequence().isSingleStranded());

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
}
