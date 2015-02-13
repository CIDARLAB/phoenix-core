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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojava.bio.symbol.Location;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Person;
import org.cidarlab.phoenix.core.dom.Polynucleotide;

/**
 *
 * This class contains methods for reading a Benchling-produced Multi-part Genbank file
 *
 * @author evanappleton
 */
public class BenchlingAdaptor {

    /*
     * Creates a Polynucleotide from a Biojava sequence object
     */
    public static HashSet<Polynucleotide> getPolynucleotide(File input) throws NoSuchElementException, FileNotFoundException, BioException {
        
        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);
        HashSet<Polynucleotide> polyNucs = new HashSet<Polynucleotide>();
        
        //This loops for each entry in a multi-part GenBank file
        while (readGenbank.hasNext()) {
            Sequence seq = readGenbank.nextSequence();

            Polynucleotide polyNuc = new Polynucleotide();

            //Check for linearity
            if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
                polyNuc.setLinear(false);
            } else {
                polyNuc.setLinear(true);
            }

            //Check for strandedness
            if (seq.getAnnotation().containsProperty("CIRCULAR")) {
                if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
                    polyNuc.setSingleStranded(false);
                } else {
                    polyNuc.setSingleStranded(true);
                }
            } else {
                polyNuc.setSingleStranded(true);
            }

            //Basic information for polynucleotide
            polyNuc.setSequence(seq.seqString());
            polyNuc.setAccession(seq.getName() + "_Polynucleotide");
            
            if (seq.getAnnotation().containsProperty("COMMENT")) {
                polyNuc.setDescription(seq.getAnnotation().getProperty("COMMENT").toString());
            }
            if (seq.getAnnotation().containsProperty("MDAT")) {
                Date date = new Date(seq.getAnnotation().getProperty("MDAT").toString());
                polyNuc.setSubmissionDate(date);
            }
            
            polyNucs.add(polyNuc);
        }
        return polyNucs;
    }

    /*
     * Creates a Part set from a Biojava sequence object
     * This will create basic parts out of all incoming plasmids
     */
    public static HashSet<Part> getMoCloParts(File input) throws FileNotFoundException, NoSuchElementException, BioException {
        
        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);
        HashSet<Part> partSet = new HashSet<Part>();

        //This loops for each entry in a multi-part GenBank file
        while (readGenbank.hasNext()) {

            Sequence seq = readGenbank.nextSequence();

            //First we check for MoClo format... This is is a pretty hacky, inflexible way to do it
            //If MoClo sites are found, all features in between sites used to determine parts

            //Correct sequence for circular sequences by adding beginnning and end sequence
            String seqString = seq.seqString();
            String searchSeq = seq.seqString();
            if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
                searchSeq = seqString.substring(seqString.length() - 5) + seqString;
            }
            int start;
            int end;

            //Looks for flanking BbsI or BsaI sites if there are more than one, this method will break
            //This also assumes there are either exactly two of each site, not both or a mix
            if (searchSeq.contains(_BbsIfwd) && searchSeq.contains(_BbsIrev)) {
                
                //If there is only one and exactly one BbsI site in each direction does it conform to MoClo format
                if (searchSeq.indexOf(_BbsIfwd) == searchSeq.lastIndexOf(_BbsIfwd) && searchSeq.indexOf(_BbsIrev) == searchSeq.lastIndexOf(_BbsIrev)) {
                    start = searchSeq.indexOf(_BbsIfwd) + 8 - 5;
                    end = searchSeq.indexOf(_BbsIrev) - 2 - 5;
                } else {
                    continue;
                }

            } else if (searchSeq.contains(_BsaIfwd) && searchSeq.contains(_BsaIrev)) {

                //If there is only one and exactly one BbsI site in each direction does it conform to MoClo format
                if (searchSeq.indexOf(_BsaIfwd) == searchSeq.lastIndexOf(_BsaIfwd) && searchSeq.indexOf(_BsaIrev) == searchSeq.lastIndexOf(_BsaIrev)) {
                    start = searchSeq.indexOf(_BsaIfwd) + 7 - 5;
                    end = searchSeq.indexOf(_BsaIrev) - 1 - 5;
                } else {
                    continue;
                }
            
            } else {
                continue;
            }

            //Correct for indexing
            if (end <= 0) {
                end = end + seqString.length();
            }
            if (start >= seqString.length()) {
                start = seqString.length() - start;
            }

            //If the part range goes through index 0, the start index will be after the end index, so the sequence needs to be adjusted
            if (start > end) {
                seqString = seqString.concat(seqString);
                end = end + seqString.length();
            }

            String partSeq = seqString.substring(start, end + 1);
            Part part;
            if (seq.getAnnotation().containsProperty("COMMENT")) {
                part = Part.generateBasic(seq.getName(), seq.getAnnotation().getProperty("COMMENT").toString(), partSeq, null, null);
            } else {
                part = Part.generateBasic(seq.getName(), "", partSeq, null, null);
            }
            partSet.add(part);

        }
        return partSet;
    }
    
    /*
     * Gets all features for a given file
     * This method is intended to be used only on a reference set of features
     * 
     */
    public static HashSet<org.cidarlab.phoenix.core.dom.Feature> getFeatures(File input) throws FileNotFoundException, NoSuchElementException, BioException {

        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);
        HashSet<Feature> clothoFeatures = new HashSet<>();
        
        //This loops for each entry in a multi-part GenBank file
        while (readGenbank.hasNext()) {
            
            Sequence seq = readGenbank.nextSequence();

            //Look at all features within part boundary to get basic parts and order to make a composite part
            Iterator<org.biojava.bio.seq.Feature> features = seq.features();
            
            while (features.hasNext()) {
                
                //Get Biojava features
                org.biojava.bio.seq.Feature feature = features.next();
                Location locus = feature.getLocation();
                int startFeat = locus.getMin();
                int endFeat = locus.getMax();

                //Correct sequence for circular sequences by adding beginnning and end sequence
                String seqString = seq.seqString();
                seqString = seqString.concat(seqString);
                if (startFeat > endFeat) {
                    seqString = seqString.concat(seqString);
                    endFeat = endFeat + seqString.length();
                }

                //Check for linearity
                boolean linearity = true;
                if (seq.getAnnotation().containsProperty("DIVISION")) {
                    if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
                        linearity = false;
                    } else {
                        linearity = true;
                    }
                }

                //Check for strandedness
                boolean ss = true;
                if (seq.getAnnotation().containsProperty("CIRCULAR")) {
                    if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
                        ss = false;
                    } else {
                        ss = true;
                    }
                }

                //Get rest of feature information and apply it to the features
                NucSeq nucSeq = new NucSeq(seqString.substring(startFeat, endFeat + 1), ss, linearity);
                
                Color fwd = null;
                Color rev = null;
                String name = "";
                if (feature.getAnnotation().containsProperty("ApEinfo_fwdcolor") && feature.getAnnotation().containsProperty("ApEinfo_revcolor")) {
                    fwd = Color.decode(feature.getAnnotation().getProperty("ApEinfo_fwdcolor").toString());
                    rev = Color.decode(feature.getAnnotation().getProperty("ApEinfo_revcolor").toString());
                } 
                
                if (feature.getAnnotation().containsProperty("label")) {
                    name = feature.getAnnotation().getProperty("label").toString();
                }
                
                //If this sequence is a reference '.ref' part it should only have one feature
                //If so, we are going to assign feature roles
//                if (seq.countFeatures() == 1 && seq.getName().endsWith(".ref")) {
                if (seq.getName().endsWith(".ref")) {    
                
                    //Get tags from the part to create feature
                    if (seq.getAnnotation().containsProperty("KEYWORDS")) {
                        
                        //Get tags from Benchling
                        String tagstring = seq.getAnnotation().getProperty("KEYWORDS").toString();
                        HashMap<String, String> tags = new HashMap<>();
                        String[] tokens = tagstring.split("\" ");
                        for (String token : tokens) {
                            token.trim();
                            token = token.substring(token.indexOf("\"") + 1);
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
                                
                                Fluorophore fp = new Fluorophore();
                                fp.setName(seq.getName());
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
                                
                                clothoFeatures.add(fp);
                                
                            } else {
                                
                                org.cidarlab.phoenix.core.dom.Feature clothoFeature = new Feature();
                                clothoFeature.setName(seq.getName());
                                clothoFeature.setSequence(nucSeq);
                                clothoFeature.setForwardColor(fwd);
                                clothoFeature.setReverseColor(rev);
                                
                                if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("constitutive")) {
                                    clothoFeature.setRole(Feature.FeatureRole.PROMOTER_CONSTITUTIVE);
                                } else if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("repressible")) {
                                    clothoFeature.setRole(Feature.FeatureRole.PROMOTER_REPRESSIBLE);
                                } else if (type.equalsIgnoreCase("promoter") && subtype.equalsIgnoreCase("inducible")) {
                                    clothoFeature.setRole(Feature.FeatureRole.PROMOTER_INDUCIBLE);
                                } else if (type.equalsIgnoreCase("5'UTR")) {
                                    clothoFeature.setRole(Feature.FeatureRole.RBS);
                                } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("repressor")) {
                                    clothoFeature.setRole(Feature.FeatureRole.CDS_REPRESSOR);
                                } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("activator")) {
                                    clothoFeature.setRole(Feature.FeatureRole.CDS_REPRESSOR);
                                } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("linker")) {
                                    clothoFeature.setRole(Feature.FeatureRole.CDS_LINKER);
                                } else if (type.equalsIgnoreCase("CDS") && subtype.equalsIgnoreCase("resistance")) {
                                    clothoFeature.setRole(Feature.FeatureRole.CDS_RESISTANCE);
                                } else if (type.equalsIgnoreCase("CDS") && subtype.contains("Tag")) {
                                    clothoFeature.setRole(Feature.FeatureRole.CDS_TAG);
                                } else if (type.equalsIgnoreCase("terminator")) {
                                    clothoFeature.setRole(Feature.FeatureRole.TERMINATOR);
                                } else if (type.equalsIgnoreCase("origin")) {
                                    clothoFeature.setRole(Feature.FeatureRole.ORIGIN);
                                } 
                                
                                clothoFeatures.add(clothoFeature);
                            }
                        }
                    }
                    break;
                    
                } else {
                    
                    org.cidarlab.phoenix.core.dom.Feature clothoFeature = new Feature();
                    clothoFeature.setName(name);
                    clothoFeature.setSequence(nucSeq);
                    clothoFeature.setForwardColor(fwd);
                    clothoFeature.setReverseColor(rev);
                    clothoFeatures.add(clothoFeature);
                }
                
                
            }
        }
        return clothoFeatures;
    }
    
    public static HashSet<Fluorophore> getFluorophores(HashSet<Feature> features) {
        
        HashSet<Fluorophore> FPs = new HashSet<>();
        
        //Look through feature set to return only a set of the FPs
        for (Feature f : features) {
            if (f.getClass().equals(Fluorophore.class)) {
                Fluorophore fp = (Fluorophore) f;
                FPs.add(fp);
            }
        }
        
        return FPs;
    }
    
    /*
     * Gets all annotations for a given BioJava sequence
     */
    public static HashSet<Annotation> getAnnotations(Sequence seq) {

        HashSet<Annotation> annotations = new HashSet<Annotation>();

        //Look at all features within part boundary to get basic parts and order to make a composite part
        Iterator<org.biojava.bio.seq.Feature> features = seq.features();
        while (features.hasNext()) {

            //Get Biojava features
            org.biojava.bio.seq.StrandedFeature feature = (org.biojava.bio.seq.StrandedFeature) features.next();
            Location locus = feature.getLocation();
            int startFeat = locus.getMin();
            int endFeat = locus.getMax();

            //Correct sequence for circular sequences by adding beginnning and end sequence
            String seqString = seq.seqString();
            seqString = seqString.concat(seqString);
            if (startFeat > endFeat) {
                seqString = seqString.concat(seqString);
                endFeat = endFeat + seqString.length();
            }
            
            boolean plusStrand = true;
            if (!"+".equals(String.valueOf(feature.getStrand().getToken()))) {
                plusStrand = false;
            }

            //Check for linearity
            boolean linearity = true;
            if (seq.getAnnotation().containsProperty("DIVISION")) {
                if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
                    linearity = false;
                } else {
                    linearity = true;
                }
            }

            //Check for strandedness
            boolean ss = true;
            if (seq.getAnnotation().containsProperty("CIRCULAR")) {
                if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
                    ss = false;
                } else {
                    ss = true;
                }
            }

            //Get rest of feature information and apply it to the features
            NucSeq nucSeq = new NucSeq(seqString.substring(startFeat, endFeat + 1), ss, linearity);

            Color fwd = null;
            Color rev = null;
            String name = "";
            if (feature.getAnnotation().containsProperty("ApEinfo_fwdcolor") && feature.getAnnotation().containsProperty("ApEinfo_revcolor")) {
                fwd = Color.decode(feature.getAnnotation().getProperty("ApEinfo_fwdcolor").toString());
                rev = Color.decode(feature.getAnnotation().getProperty("ApEinfo_revcolor").toString());
            }

            if (feature.getAnnotation().containsProperty("label")) {
                name = feature.getAnnotation().getProperty("label").toString();
            }
                        
            org.cidarlab.phoenix.core.dom.Feature clothoFeature = new Feature();
            clothoFeature.setName(name);
            clothoFeature.setSequence(nucSeq);
            clothoFeature.setForwardColor(fwd);
            clothoFeature.setReverseColor(rev);

            //Create the annotation
            endFeat = locus.getMax();
            Annotation annotation = new Annotation(clothoFeature, nucSeq, fwd, rev, startFeat, endFeat, new Person(), plusStrand, null);
            annotations.add(annotation);
        }

        return annotations;
    }
    
    /*
     * Creates an annotation set
     */
    public static HashSet<NucSeq> getNucSeq(File input) throws FileNotFoundException, NoSuchElementException, BioException {

        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);
        HashSet<NucSeq> nucSeqs = new HashSet<>();

        //This loops for each entry in a multi-part GenBank file
        while (readGenbank.hasNext()) {
            
            Sequence seq = readGenbank.nextSequence();

            //Check for linearity
            boolean linearity = true;
            if (seq.getAnnotation().containsProperty("DIVISION")) {
                if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
                    linearity = false;
                } else {
                    linearity = true;
                }
            }

            //Check for strandedness
            boolean ss = true;
            if (seq.getAnnotation().containsProperty("CIRCULAR")) {
                if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
                    ss = false;
                } else {
                    ss = true;
                }
            }

            //Get rest of feature information and apply it to the features
            String seqString = seq.seqString();
            NucSeq nucSeq = new NucSeq(seqString, ss, linearity);
            nucSeq.setName(seq.getName() + "_NucSeq");
            HashSet<Annotation> annotations = getAnnotations(seq);
            for (Annotation ann : annotations) {
                nucSeq.addAnnotation(ann);
            }
            
            nucSeqs.add(nucSeq);
        }
        
        return nucSeqs;
    }
    
    private static String _BbsIfwd = "gaagac";
    private static String _BbsIrev = "gtcttc";
    private static String _BsaIfwd = "ggtctc";
    private static String _BsaIrev = "gagacc";
}
