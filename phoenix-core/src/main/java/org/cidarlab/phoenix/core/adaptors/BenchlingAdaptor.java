/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import org.biojava.bio.seq.Feature;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojava.bio.symbol.Location;
import org.clothocad.model.Annotation;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Part;
import org.clothocad.model.Polynucleotide;

/**
 *
 * This class contains methods for reading a Benchling-produced Multi-part Genbank file
 *
 * @author evanappleton
 */
public class BenchlingAdaptor {

    /*
     * This method is for reading a Benchling-produced Multipart Genbank file with Biojava 1.9.0
     * It creates Clotho Polynucleotides, Parts, Sequences, Annotations and Features
     */
    public static void readGenbankFileBiojava(File input) throws Exception {

        //Import file, begin reading
        BufferedReader reader = new BufferedReader(new FileReader(input.getAbsolutePath()));
        SequenceIterator readGenbank = SeqIOTools.readGenbank(reader);

        //This loops for each entry in a multi-part GenBank file
        while (readGenbank.hasNext()) {

            Sequence seq = readGenbank.nextSequence();
            Polynucleotide polyNuc = getPolynucleotide(seq);
            HashSet<Part> parts = getMoCloParts(seq);
        }
    }

    /*
     * Creates a Polynucleotide from a Biojava sequence object
     */
    public static Polynucleotide getPolynucleotide(Sequence seq) {

        Polynucleotide polyNuc = new Polynucleotide();

        //Check for linearity
        if (seq.getAnnotation().getProperty("DIVISION").equals("circular")) {
            polyNuc.setLinear(false);
        } else {
            polyNuc.setLinear(true);
        }

        //Check for strandedness
        if (seq.getAnnotation().getProperty("CIRCULAR").equals("ds-DNA")) {
            polyNuc.setSingleStranded(false);
        } else {
            polyNuc.setSingleStranded(true);
        }

        //Basic information for polynucleotide
        polyNuc.setSequence(seq.seqString());
        polyNuc.setAccession(seq.getName());
        String description = seq.getAnnotation().getProperty("COMMENT").toString();
        polyNuc.setDescription(description);
        Date date = new Date(seq.getAnnotation().getProperty("MDAT").toString());
        polyNuc.setSubmissionDate(date);

        return polyNuc;
    }

    /*
     * Creates a Part set from a Biojava sequence object
     */
    public static HashSet<Part> getMoCloParts(Sequence seq) {
        
        HashSet<Part> partSet = new HashSet<Part>();
        
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
            start = searchSeq.indexOf(_BbsIfwd) + 8 - 5;
            end = searchSeq.indexOf(_BbsIrev) - 2 - 5;
        } else if (searchSeq.contains(_BsaIfwd) && searchSeq.contains(_BsaIrev)) {
            start = searchSeq.indexOf(_BsaIfwd) + 7 - 5;
            end = searchSeq.indexOf(_BsaIrev) - 1 - 5;
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

        //Loop through features to make basic parts
        int count = 0;
        ArrayList<Part> partOrder = new ArrayList<Part>();
        ArrayList<Feature> featureOrder = new ArrayList<Feature>();
        
        //If the part range goes through index 0, the start index will be after the end index, so the sequence needs to be adjusted
        if (start > end) {
            seqString = seqString.concat(seqString);
            end = end + seqString.length();
        }
        
        //Look at all features within part boundary to get basic parts and order to make a composite part
        Iterator<Feature> features = seq.features();
        while (features.hasNext()) {
            count++;
            
            Feature feature = features.next();
            Location locus = feature.getLocation();
            int startFeat = locus.getMin();
            int endFeat = locus.getMax();
            
            //If they fit in the range of part start and end
            if (endFeat < startFeat) {
                endFeat = endFeat + seqString.length();
            }
   
            //If this feature is within the part boundaries
            if (startFeat > start && end > endFeat) {
                String bPartSeq = seqString.substring(startFeat - 1, endFeat);
                String name = feature.getAnnotation().getProperty("label").toString();
                Part bPart = Part.generateBasic(name, "", bPartSeq, null, null);
                partSet.add(bPart);

                //Order parts for composite part
                boolean found = false;
                for (int i = 0; i < featureOrder.size(); i++) {
                    Feature feat = featureOrder.get(i);
                    
                    //Correct for wrap-around sequence edge cases
                    int startThisFeat = feat.getLocation().getMin();
                    if (feat.getLocation().getMin() > feat.getLocation().getMax()) {
                        startThisFeat = startThisFeat + seqString.length();
                    }
                    
                    //Place this part in the proper order of the composite part
                    if (startFeat < startThisFeat) {
                        featureOrder.add(i, feature);
                        partOrder.add(i, bPart);
                        found = true;
                        break;
                    }
                }
                
                //If the feature was not before any particular feature
                if (!found) {
                    featureOrder.add(feature);
                    partOrder.add(bPart);
                }
                
                //If part oder empty, initialize
                if (featureOrder.isEmpty()) {
                    partOrder.add(bPart);
                    featureOrder.add(feature);
                }
            }

        }
        
        //Add basic parts and composite to output if applicable
        if (count > 1) {
            Part composite = Part.generateComposite(partOrder, null, null, null, seq.getName(), seq.getAnnotation().getProperty("COMMENT").toString());
            partSet.add(composite);
        }
        
        return partSet;
    }
    
    /*
     * Creates a Feature set
     */
    public static HashSet<Feature> getFeatures() {
        return null;
    }
    
    /*
     * Creates an annotation set
     */
    public static HashSet<Annotation> getAnnotations() {
        return null;
    }
    
    /*
     * Creates an annotation set
     */
    public static HashSet<NucSeq> getSequences() {
        return null;
    }
    
    private static String _BbsIfwd = "gaagac";
    private static String _BbsIrev = "gtcttc";
    private static String _BsaIfwd = "ggtctc";
    private static String _BsaIrev = "gagacc";
}
