/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Polynucleotide;

/**
 * This class is responsible for producing and interpreting experimental instructions in Phoenix
 * This includes part assembly and determination of testing points
 * 
 * @author evanappleton
 */
public class PhoenixInstructions {
    
    //Create experiment objects for module and it's respective polynucleotide
    public HashSet<Experiment> createTestingExperiments(Module m, HashSet<Polynucleotide> plasmids) {
        
        //Initialize experiment set - some types of modules require multiple experiments
        HashSet<Experiment> experimentSet = new HashSet<>();
        
        //Experiments for EXPRESSEE
        if (m.getRole().equals(ModuleRole.EXPRESSEE)) {
          
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
        } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {
           
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for EXPRESSEE_ACTIVATOR
        } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {
            
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
        } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {
        
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for EXPRESSEE_REPRESSOR    
        } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for EXPRESSOR
        } else if (m.getRole().equals(ModuleRole.EXPRESSOR)) {
            
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for TRANSCRIPTIONAL_UNIT
        } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        //Experiments for HIGHER_FUNCTION
        } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
            
            //Initialize experiment object
            Experiment ex = new Experiment();
            experimentSet.add(ex);
            
        }                        
        
        return experimentSet;
    }
    
    //Method for producing testing instructions from Experiments
    public static String generateTestingInstructions(List<Experiment> experiments) {
        return "";
    }
    
}
