/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.HashSet;
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
        
    //Experiment type
    @Setter
    @Getter
    private HashSet<Sample> experimentSamples;
    
    //Experiment type
    @Setter
    @Getter
    private Sample beadControl;
    
    //Experiment type
    @Setter
    @Getter
    private Sample negativeControl;
    
    //Experiment type
    @Setter
    @Getter
    private HashSet<Sample> colorControls;
    
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