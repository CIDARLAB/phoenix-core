/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

/**
 * This is the primary class for managing the workflow of tools within Phoenix
 * 
 * @author evanappleton
 */
public class PhoenixController {
    
    //Main Phoenix run method
    //Will begin as pure server-side, so might be called from a main method initially
    public void run () {
        
        //Import data from Benchling to Clotho
        
        //Recieve data from Clotho
        
        //LTL function decomposition
        
        //Map LTL decomposition to structure contstraint libraries
        
        //Use feature library to create target modules
        
        //Decompose target modules with DecompositionGrammar to get feature graphs
        
        //REPEAT
        //Pass feature graphs to measurement and experiment grammars to get ExperimentDesign
        
        //Form part graph from feature graph via Raven optimizations
        
        //Create instruction files
        
        //Recieve data back from Clotho, match up to instructions
        
        //Run analytics to produce graphs, parameters
        
        //Make owl data sheets
        
        //Run simulations to produce candidate part/feature matches
        
        //Update feature graphs based upon simulations
        
        //Repeat from REPEAT point
    }
    
}
