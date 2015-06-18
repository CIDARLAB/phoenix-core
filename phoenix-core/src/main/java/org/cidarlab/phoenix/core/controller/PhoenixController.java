/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.*;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

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
//        ClothoAdaptor.uploadSequences(featureLib, true);
//        ClothoAdaptor.uploadFluorescenceSpectrums(fluorophoreSpectra);
//        ClothoAdaptor.uploadSequences(plasmidLib, false);
        
        //LTL function decomposition
        
        //Map LTL decomposition to structure contstraint libraries
        
        //Create target modules with miniEugene
        List<Module> modules = EugeneAdaptor.getStructures(structureFile, 1, "toggle");
        
        //Decompose target modules with PhoenixGrammar to get module graphs
        PhoenixGrammar.decomposeAll(modules);
        
        //Extend the modules for testing
        TestingStructures.addTestingPrimitives(modules);
        
        //Perform partial part assignments given the feature library
        HashSet<Module> modulesToTest = FeatureAssignment.partialAssignment(modules);
                
        //Create expreriment objects based upon the modules being tested
        List<Experiment> currentExperiments = new ArrayList<>();
                
        //Repeat until all module graphs are fully assigned
//        while (!FeatureAssignment.isFullyAssigned(modules)) {
            
        //Remove this once you've got it working.
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        for(Module module:modulesToTest){
            ClothoAdaptor.createModule(module, clothoObject);
        }
        
        
            //Determine experiments from current module assignment state
            currentExperiments.addAll(TestingStructures.createExperiments(modulesToTest));
            
            //Create assembly and testing plans
            
            String assemblyInstructions = RavenAdaptor.generateAssemblyPlan(modulesToTest);
            String testingInstructions = PhoenixInstructions.generateTestingInstructions(currentExperiments);
            
//            //Import data from experiments
//            ClothoAdaptor.uploadSequences(plasmidsCreated, false);
//            FeatureAssignment.completeAssignmentSeqResults(modules);
//            ClothoAdaptor.uploadCytometryData(fcsFiles, currentExperiments);
//            
//            //Run analytics to produce graphs, parameters
//            AnalyticsAdaptor.runAnalytics(currentExperiments);
//            
//            //Make owl data sheets
//            OwlAdaptor.makeDatasheets(currentExperiments);
//            currentExperiments.clear();
//            
//            //Run simulations to produce candidate part/feature matches
//            List<Module> bestCombinedModules = iBioSimAdaptor.runSimulations(modules);
//            
//            //Update module graphs based upon simulations
//            modulesToTest = FeatureAssignment.completeAssignmentSim(bestCombinedModules, modules);
//        }
    }
    
}
