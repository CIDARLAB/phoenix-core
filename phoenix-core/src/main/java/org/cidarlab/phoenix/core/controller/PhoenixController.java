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
import net.sf.json.JSONObject;
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
    
    public static String getJSONFilepath()
    {
        String filepath = PhoenixController.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("WEB-INF/classes/"));
        filepath += "test.json";
        return filepath;
    }
    
    //Data upload method
    //FILE IN, NOTHING OUT
    public static void preliminaryDataUpload (File featureLib, File plasmidLib, File fluorophoreSpectra, File cytometer) throws FileNotFoundException, Exception {
     
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        
        ClothoAdaptor.uploadSequences(featureLib, true,clothoObject);
        ClothoAdaptor.uploadFluorescenceSpectrums(fluorophoreSpectra,clothoObject);
        ClothoAdaptor.uploadSequences(plasmidLib, false,clothoObject);
        ClothoAdaptor.uploadCytometer(cytometer,clothoObject);
        
        conn.closeConnection();
    }
    
    //Main Phoenix design decomposition method
    //Remember to start Clotho before this initializeDesign
    //FILES IN, NOTHING OUT
    public static List<Module> initializeDesign (File structuralSpecification, File functionalSpecification) throws Exception {

        //STL function decomposition
        
        //Map STL decomposition to structure contstraint libraries

        //Create target modules with miniEugene
        List<Module> modules = EugeneAdaptor.getStructures(structuralSpecification, 1, "toggle");

        //Decompose target modules with PhoenixGrammar to get module graphs
        PhoenixGrammar.decomposeAll(modules);

        //Extend the modules for testing
        TestingStructures.addTestingPrimitives(modules);

        //Perform partial part assignments given the feature library
        FeatureAssignment.partialAssignment(modules);
        
        JSONObject flareValue = new JSONObject();
        flareValue = ClientSideAdaptor.convertModuleToJSON(modules.get(0));
        
        String JSONFilePath = getJSONFilepath();
        
        ClientSideAdaptor.createFlareFile(JSONFilePath,flareValue);
        //Remove this once you've got it working.
//        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
//        Clotho clothoObject = new Clotho(conn);
//        for (Module module : modulesToTest) {
//            ClothoAdaptor.createModule(module, clothoObject);
//        }
     
        return modules;
    }

    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //MODULES IN, FILES OUT
    public static List<File> createExperimentInstructions (HashSet<Module> modulesToTest, String filePath) throws Exception {
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        List<Experiment> currentExperiments = new ArrayList<>();
        currentExperiments.addAll(TestingStructures.createExperiments(modulesToTest));

        //Create assembly and testing plans
        File assemblyInstructions = RavenAdaptor.generateAssemblyPlan(modulesToTest, filePath);
        File testingInstructions = PhoenixInstructions.generateTestingInstructions(currentExperiments, filePath);

        //Save these strings to files and return them from this method
        List<File> assmTestFiles = new ArrayList<>();
        assmTestFiles.add(testingInstructions);
        assmTestFiles.add(assemblyInstructions);
        return assmTestFiles;
    }
    
    //Take a plasmid library back in, interpret data, run simulations for parents, verify, make part assignments
    //FILES IN, NOTHING OUT
    public static void interpretData (List<File> fcsFiles, File plasmidsCreated, List<Module> modules) throws Exception {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        
        List<Experiment> currentExperiments = new ArrayList<>();
        
        //Import data from experiments
        ClothoAdaptor.uploadSequences(plasmidsCreated, false,clothoObject);
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
        
        conn.closeConnection();
    }    
}
