/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Sample {
   
    //Constructor
    public Sample(SampleType _type, Strain _strain, List<Polynucleotide> _polynucleotides, Medium _media, String _time) {
        
        type = _type;
        strain = _strain;
        polynucleotides = _polynucleotides;
        media = _media;
        time = _time;
        
        String pnNames = "";
        if (_polynucleotides != null) {
            for (Polynucleotide p : _polynucleotides) {
                if (pnNames.isEmpty()) {
                    pnNames = p.getClothoID();
                } else {
                    pnNames = pnNames + "_" + p.getClothoID();
                }
            }
        }
        
        if (_strain != null && _media != null) {
            if (pnNames.isEmpty()) {
                clothoID = _type.toString() + "_" + _strain.getName() + "_" + _media.getName() + "_" + _time;
            } else {
                clothoID = pnNames + "_" + _type.toString() + "_" + _strain.getName() + "_" + _media.getName() + "_" + _time;
            }
        } else {
            clothoID = _type.toString();
        }
    }    
    
    //Module measured
    @Setter
    @Getter
    private String name;
    
    //Module measured
    @Setter
    @Getter
    private String clothoID;
    
    //Module measured
    @Setter
    @Getter
    private Medium media;
    
    //Polynucleotide measured
    @Setter
    @Getter
    private List<Polynucleotide> polynucleotides;
    
    //Test strain
    @Setter
    @Getter
    private Strain strain;
    
    //Time of experiment (for dynamic measurements)
    @Setter
    @Getter
    private String time;
    
    //Control type
    @Setter
    @Getter
    private SampleType type;
    
    //Results from cytometry experiment
    @Setter
    @Getter
    private HashMap<Detector, Double> results;
    
    //Experiment types
    public enum SampleType {
        EXPERIMENT,
        BEADS,
        NEGATIVE,
        FLUORESCENT,
        EXPRESSION_DEGRATATION,
        REGULATION;
    }
}
