/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.HashSet;
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
    
    //Convert an experiment list to an output csv
    public void createExperimentFile(Experiment experiments) {
        
    }
    
    //Read an experiment csv file and create experiment objects
    public List<Experiment> parseExperimentFile() {
        return null;
    }    
    
    //Experiment type
    @Setter
    @Getter
    private ExperimentType type;
        
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
        SMALL_MOLECULE;
    }
}