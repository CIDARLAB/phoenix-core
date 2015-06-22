/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.io.FileNotFoundException;
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
    
    //Data upload method
    //FILE IN, NOTHING OUT
    public static void preliminaryDataUpload (File featureLib, File plasmidLib, File fluorophoreSpectra, File cytometer) throws FileNotFoundException, Exception {
     
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoAdaptor.uploadSequences(featureLib, true);
        ClothoAdaptor.uploadFluorescenceSpectrums(fluorophoreSpectra);
        ClothoAdaptor.uploadSequences(plasmidLib, false);
        ClothoAdaptor.uploadCytometer(cytometer);        
    }
    
    //Main Phoenix design decomposition method
    //Remember to start Clotho before this initializeDesign
    //FILES IN, NOTHING OUT
    public static HashSet<Module> initializeDesign (File structuralSpecification, File functionalSpecification) throws Exception {

        //STL function decomposition
        
        //Map STL decomposition to structure contstraint libraries

        //Create target modules with miniEugene
        List<Module> modules = EugeneAdaptor.getStructures(structuralSpecification, 1, "toggle");

        //Decompose target modules with PhoenixGrammar to get module graphs
        PhoenixGrammar.decomposeAll(modules);

        //Extend the modules for testing
        TestingStructures.addTestingPrimitives(modules);

        //Perform partial part assignments given the feature library
        HashSet<Module> modulesToTest = new HashSet<Module>();
        for (Module m : modules) {
            HashSet<Module> testableModules = FeatureAssignment.partialAssignment(modules);
            modulesToTest.addAll(testableModules);
        }
        
        //Remove this once you've got it working.
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        for (Module module : modulesToTest) {
            ClothoAdaptor.createModule(module, clothoObject);
        }
     
        return modulesToTest;
    }

    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //MODULES IN, FILES OUT
    public static List<File> createExperimentInstructions (HashSet<Module> modulesToTest) throws Exception {
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        List<Experiment> currentExperiments = new ArrayList<>();
        currentExperiments.addAll(TestingStructures.createExperiments(modulesToTest));

        //Create assembly and testing plans
        File assemblyInstructions = RavenAdaptor.generateAssemblyPlan(modulesToTest);
        File testingInstructions = PhoenixInstructions.generateTestingInstructions(currentExperiments);

        //Save these strings to files and return them from this method
        List<File> assmTestFiles = new ArrayList<>();
        assmTestFiles.add(testingInstructions);
        assmTestFiles.add(assemblyInstructions);
        return assmTestFiles;
    }
    
    //Take a plasmid library back in, interpret data, run simulations for parents, verify, make part assignments
    //FILES IN, NOTHING OUT
    public static void interpretData (List<File> fcsFiles, File plasmidsCreated, List<Module> modules) throws Exception {
        
        List<Experiment> currentExperiments = new ArrayList<>();
        
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
        HashSet<Module> modulesToTest = FeatureAssignment.completeAssignmentSim(bestCombinedModules, modules);
//        createExperimentInstructions (modulesToTest);
    }    
}
