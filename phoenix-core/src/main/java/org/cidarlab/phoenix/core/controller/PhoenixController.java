/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.AnalyticsAdaptor;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.adaptors.EugeneAdaptor;
import org.cidarlab.phoenix.core.adaptors.OwlAdaptor;
import org.cidarlab.phoenix.core.adaptors.RavenAdaptor;
import org.cidarlab.phoenix.core.adaptors.iBioSimAdaptor;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
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
    public static void run (File featureLib, File plasmidLib, File structureFile, File fluorophoreSpectra, File cytometer, List<File> fcsFiles, File plasmidsCreated) throws Exception {
        
        //Import data from Benchling multi-part Genbank files to Clotho
//        ClothoAdaptor.uploadSequences(plasmidLib, false);
//        ClothoAdaptor.uploadSequences(featureLib, true);
//        ClothoAdaptor.uploadFluorescenceSpectrums(fluorophoreSpectra);
        
        //LTL function decomposition
        
        //Map LTL decomposition to structure contstraint libraries
        
        //Create target modules with miniEugene
        List<Module> modules = EugeneAdaptor.getStructures(structureFile, 1);
        
        //Decompose target modules with PhoenixGrammar to get module graphs
        PhoenixGrammar.decomposeAll(modules);
        
        //Extend the modules for testing
        TestingStructures.addTestingPrimitives(modules);
          
        //Perform partial part assignments given the feature library
        FeatureAssignment.partialAssignment(modules);
                
        List<Experiment> currentExperiments = new ArrayList<>();
                
        //Repeat until all module graphs are fully assigned
        while (!FeatureAssignment.isFullyAssigned(modules)) {
            
            //Determine experiments from current module assignment state
            currentExperiments.addAll(TestingStructures.createExperiments(modules));
            
            //Convert modules to parts to get target parts
            HashSet<Part> experimentParts = FeatureAssignment.getExperimentParts(currentExperiments);
            
            //Create instruction files            
            String assemblyInstructions = RavenAdaptor.generateAssemblyPlan(experimentParts);
            System.out.println(assemblyInstructions);
            String testingInstructions = PhoenixInstructions.generateTestingInstructions(currentExperiments);            
            System.out.println(testingInstructions);
            
            //Import data from experiments
            ClothoAdaptor.uploadSequences(plasmidsCreated, false);
            FeatureAssignment.completeAssignmentSeqResults(modules);
            ClothoAdaptor.uploadCytometryData(fcsFiles, currentExperiments);
            
            //Run analytics to produce graphs, parameters
            AnalyticsAdaptor.runAnalytics(currentExperiments);
            
            //Make owl data sheets
            OwlAdaptor.makeDatasheets(currentExperiments);
            currentExperiments.clear();
            
            //Run simulations to produce candidate part/feature matches
            List<Module> bestCombinedModules = iBioSimAdaptor.runSimulations(modules);
            
            //Update module graphs based upon simulations
            FeatureAssignment.completeAssignmentSim(bestCombinedModules, modules);
        }
    }
    
}
