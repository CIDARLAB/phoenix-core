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
 * @author prash
 * 
 */
public class Experiment {
    
    //No args constructor
    public Experiment(ExperimentType exptType, String _name, List<Medium> _media, List<String> _times) {
        exType = exptType;
        results = new ExperimentResults(exptType);
        name = _name;
        //clothoID = _name;
        mediaConditions = _media;
        times = _times;
    }   
    
    //Experiment name
    @Setter
    @Getter
    private String name;
    
    @Getter
    @Setter
    private String amShortName;
    
    @Getter
    @Setter
    private String amName;
    
    
    //Experiment clothoID
    @Setter
    @Getter
    private String clothoID;
    
    //Experiment type
    @Getter
    @Setter
    private ExperimentType exType;
        
    //Time series for measurements
    @Setter
    @Getter
    private List<String> times;
    
    //Time series for measurements
    @Setter
    @Getter
    private List<Medium> mediaConditions;
    
    //Experiment results object
    @Setter
    @Getter
    private ExperimentResults results;
    
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