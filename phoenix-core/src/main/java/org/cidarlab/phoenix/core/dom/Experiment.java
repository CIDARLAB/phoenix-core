/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * This file is for the Experiment object
 * 
 * @author evanappleton
 */
public class Experiment {
    
    //No args constructor
    public Experiment() {  
    }
    
    //No args constructor
    public Experiment(ExperimentType exptType, String _name, List<Medium> _media, List<Integer> _times) {
        exType = exptType;
        name = _name;
        clothoID = _name;
        mediaConditions = _media;
        times = _times;
        experimentSamples = new ArrayList<>();
        colorControls = new ArrayList<>();
        regulationControls = new ArrayList<>();
        expDegControls = new ArrayList<>();
    }   
    
    //Get all samples in an experiment
    public List<Sample> getAllSamples() {
        
        List<Sample> allSamples = new ArrayList<>();
        allSamples.add(this.beadControl);
        allSamples.add(this.negativeControl);
        allSamples.addAll(this.colorControls);
        allSamples.addAll(this.expDegControls);
        allSamples.addAll(this.regulationControls);
        allSamples.addAll(this.experimentSamples);
        
        return allSamples;
    }
    
    //Experiment name
    @Setter
    @Getter
    private String name;
    
    //Experiment clothoID
    @Setter
    @Getter
    private String clothoID;
    
    //Experiment type
    @Setter
    @Getter
    private ExperimentType exType;
        
    //Experiment testing samples
    @Setter
    @Getter
    private List<Sample> experimentSamples;
    
    //Bead control sample
    @Setter
    @Getter
    private Sample beadControl;
    
    //Negative control sample
    @Setter
    @Getter
    private Sample negativeControl;
    
    //Color control samples
    @Setter
    @Getter
    private List<Sample> colorControls;
    
    //Regulation control samples
    @Setter
    @Getter
    private List<Sample> regulationControls;
    
    //Regulation control samples
    @Setter
    @Getter
    private List<Sample> expDegControls;
    
    //Time series for measurements
    @Setter
    @Getter
    private List<Integer> times;
    
    //Time series for measurements
    @Setter
    @Getter
    private List<Medium> mediaConditions;
    
    //Experiment types
    public enum ExperimentType {
        EXPRESSION,
        DEGRADATION,
        REGULATION,
        SMALL_MOLECULE,
        SIGNAL_MISMATCH,
        PROMOTER_CONTEXT,
        RBS_CONTEXT,
        READ_THROUGH,
        ORTHOGONALITY,
        RECOMBINATION,
        TRANSCRIPTIONAL_INTERFERENCE,
        SPECIFICATION;
    }
}