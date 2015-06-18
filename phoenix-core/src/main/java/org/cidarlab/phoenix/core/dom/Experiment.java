/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

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
    public Experiment(ExperimentType exptType) {
        exType = exptType;
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
        TRANSCRIPTIONAL_INTERFERENCE;
    }
}