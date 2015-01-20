/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.util.HashSet;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.clothocad.model.Feature;
import org.clothocad.model.Part;

/**
 * This is the primary class for managing the workflow of tools within Phoenix
 * 
 * @author evanappleton
 */
public class PhoenixController {
    
    //Main Phoenix run method
    //Remember to start Clotho before this run
    //Will begin as pure server-side, so might be called from a main method initially
    public void run (File featureLib, File plasmidLib) throws Exception {
        
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoAdaptor.uploadSequences(plasmidLib, false);
        ClothoAdaptor.uploadSequences(featureLib, true);
        
        //Recieve data from Clotho
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();
        
        //LTL function decomposition
        
        //Map LTL decomposition to structure contstraint libraries
        
        //Create target modules with miniEugene
        
        //Decompose target modules with PhoenixGrammar to get module graphs
        
        //Determine which fluorophores to use and assign to placeholders in module graph
        
        //REPEAT
        //Pass feature graphs to experiment grammars to get ExperimentDesign
        HashSet<Feature> features = ClothoAdaptor.queryFeatures();
        
        //Form part graph from module graph via Raven optimizations
        HashSet<Part> parts = ClothoAdaptor.queryParts();
        
        //Create instruction files
        
        //Recieve data back from Clotho, match up to instructions
        
        //Run analytics to produce graphs, parameters
        
        //Make owl data sheets
        
        //Run simulations to produce candidate part/feature matches
        
        //Update feature graphs based upon simulations
        
        //Repeat from REPEAT point
    }
    
}
