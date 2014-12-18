/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import static org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor.*;
import org.clothocad.model.Feature;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Part;
import org.clothocad.model.Polynucleotide;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

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
        
        //Add all features, polynucleotides, nucseqs and parts to Clotho via Clotho Server API
        //Add polynucleotides
        int count = 0;
        for (Polynucleotide pn : polyNucs) {
            count++;
            
            //Polynucleotide schema
            Map createPolynucleotide = new HashMap();
            createPolynucleotide.put("schema","org.clothocad.model.Polynucleotide");
            createPolynucleotide.put("name",pn.getAccession());
            createPolynucleotide.put("id",count);
            createPolynucleotide.put("accession",pn.getAccession());
            createPolynucleotide.put("description",pn.getDescription());
            createPolynucleotide.put("sequence",pn.getSequence());
            createPolynucleotide.put("isLinear",pn.isLinear());
            createPolynucleotide.put("isSingleStranded",pn.isSingleStranded());
            createPolynucleotide.put("submissionDate",pn.getSubmissionDate().toString());
            
            clothoObject.create(createPolynucleotide);
        }
        
        //Add features
        for (Feature f : features) {
            count++;
            
            //Feature schema
            Map createFeature = new HashMap();
            createFeature.put("schema","org.clothocad.model.Feature");
            createFeature.put("name",f.getName());
            createFeature.put("id",count);
            createFeature.put("forwardColor",f.getForwardColor().toString());
            createFeature.put("reverseColor",f.getReverseColor().toString());
            
            //NucSeq sub-schema
            Map createNucSeq = new HashMap();
            createNucSeq.put("schema","org.clothocad.model.NucSeq");
            createNucSeq.put("sequence", f.getSequence().getSeq());
            createNucSeq.put("isCircular", f.getSequence().isCircular());
            createNucSeq.put("isSingleStranded", f.getSequence().isSingleStranded());
            
            createFeature.put("sequence", createNucSeq);
            
            clothoObject.create(createFeature);
        }
        
        //Add parts
        for (Part p : parts) {
            count++;
            
            //Part schema
            Map createPart = new HashMap();
            createPart.put("schema","org.clothocad.model.Part");
            createPart.put("name",p.getName());
            createPart.put("id",count);
            
            //NucSeq sub-schema
            Map createNucSeq = new HashMap();
            createNucSeq.put("schema","org.clothocad.model.NucSeq");
            createNucSeq.put("sequence", p.getSequence().getSeq());
            createNucSeq.put("isCircular", p.getSequence().isCircular());
            createNucSeq.put("isSingleStranded", p.getSequence().isSingleStranded());
            
            createPart.put("sequence", createNucSeq);
            
            clothoObject.create(createPart);            
        }
        
        //Add nucseqs
        for (NucSeq ns : nucSeqs) {
            count++;
            
            //NucSeq schema
            Map createNucSeqMain = new HashMap();
            createNucSeqMain.put("schema","org.clothocad.model.NucSeq");
            createNucSeqMain.put("name",ns.getName());
            createNucSeqMain.put("id",count);
            createNucSeqMain.put("sequence", ns.getSeq());
            createNucSeqMain.put("isCircular", ns.isCircular());
            createNucSeqMain.put("isSingleStranded", ns.isSingleStranded());
            
        }
        
        conn.closeConnection();
    }
    
}
