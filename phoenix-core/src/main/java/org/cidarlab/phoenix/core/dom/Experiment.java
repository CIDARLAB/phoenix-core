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
    
    //Module to be measured
    @Setter
    @Getter
    private List<Module> controlModules;
    
    //Part measured
    @Setter
    @Getter
    private List<Part> controlPart;
    
    
}