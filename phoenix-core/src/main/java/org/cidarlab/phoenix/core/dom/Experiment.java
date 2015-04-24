/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.io.File;
import java.util.Date;
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
    public void createExperimentFile(List<Experiment> experiments) {
        
    }
    
    //Read an experiment csv file and create experiment objects
    public List<Experiment> parseExperimentFile() {
        return null;
    }
    
    //Module measured
    @Setter
    @Getter
    private Module module;
    
    //Part measured
    @Setter
    @Getter
    private Part part;
    
    //Environmental Conditions
    @Setter
    @Getter
    private Medium environment;
    
    //If this file is a special type of control
    @Setter
    @Getter
    private boolean isControl;
    
    //Polynucleotide measured
    @Setter
    @Getter
    private Polynucleotide polynucleotide;
    
    //Test strain
    @Setter
    @Getter
    private Strain strain;
    
    //Time of experiment (for dynamic measurements)
    @Setter
    @Getter
    private Date time;
    
    //Experiment type
    @Setter
    @Getter
    private ExperimentType type;
    
    //Control type
    @Setter
    @Getter
    private ControlType controlType;
    
    //fcs files
    @Setter
    @Getter
    private File fcsFile;
    
    //Experiment types
    public enum ExperimentType {
        EXPRESSION,
        DEGRADATION,
        REGULATION,
        SMALL_MOLECULE;
    }
    
    //Experiment types
    public enum ControlType {
        BEADS,
        NEGATIVE,
        FLUORESCENT;
    }
}