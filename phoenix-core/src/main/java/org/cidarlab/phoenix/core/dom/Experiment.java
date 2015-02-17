/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.io.File;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * This file is for the Experiment object
 * 
 * @author evanappleton
 */
public class Experiment {
    
    public Experiment() {
        
    }
    
    //Module to be measured
    @Setter
    @Getter
    private Module measurementModule;
    
    //Part measured
    @Setter
    @Getter
    private Part measurementPart;
    
    //Modules for controls
    @Setter
    @Getter
    private List<Module> controlModules;
    
    //Parts for controls
    @Setter
    @Getter
    private List<Part> controlPart;
    
    //Test strain
    @Setter
    @Getter
    private Strain strain;
    
    //Experiment type
    @Setter
    @Getter
    private ExperimentType type;
    
    //fcs files
    @Setter
    @Getter
    private List<File> fcsFiles;
    
    //Experiment types
    public enum ExperimentType {
        EXPRESSION,
        DEGRADATION,
        REGULATION,
        SMALL_MOLECULE;
    }
}