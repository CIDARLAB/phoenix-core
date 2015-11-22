/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cidarlab.phoenix.core.adaptors.*;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.grammars.FailureModeGrammar;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
import org.cidarlab.phoenix.core.grammars.StructuralGrammar;
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
        System.out.println("\n\nTHIS IS THE FILEPATH: " + filepath + "\n\n");
        filepath = filepath.substring(0,filepath.indexOf("WEB-INF/classes/"));
        filepath += "flare.json";
        return filepath;
    }
    
    //Data upload method
    //FILE IN, NOTHING OUT
    public static void preliminaryDataUpload (File featureLib, File plasmidLib, File fluorophoreSpectra, File cytometer) throws FileNotFoundException, Exception {
        
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);        
        
        ClothoAdaptor.uploadSequences(featureLib, true,clothoObject);
        ClothoAdaptor.uploadFluorescenceSpectrums(fluorophoreSpectra,clothoObject);
        ClothoAdaptor.uploadSequences(plasmidLib, false,clothoObject);
        ClothoAdaptor.uploadCytometer(cytometer,clothoObject);
        
        conn.closeConnection();
    }
    
    //Add additional plasminds only, no further processing
    //FILE IN, NOTHING OUT
    public static void addPlasmids (File plasmidLib) throws Exception {
        
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        ClothoAdaptor.uploadSequences(plasmidLib, false,clothoObject);
        
        conn.closeConnection();
    }
    
        //Add additional plasminds only, no further processing
    //FILE IN, NOTHING OUT
    public static void addFeatures (File featureLib) throws Exception {
        
        //Import data from Benchling multi-part Genbank files to Clotho
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        ClothoAdaptor.uploadSequences(featureLib, true,clothoObject);
        
        conn.closeConnection();
    }
    
    //Main Phoenix design decomposition method
    //Remember to start Clotho before this initializeDesign
    //FILES IN, NOTHING OUT
    public static Module initializeDesign (File structuralSpecification, File functionalSpecification) throws Exception {

        //STL function decomposition
        
        //Map STL decomposition to structure contstraint libraries

        //Create target modules with miniEugene        
        String path = structuralSpecification.getAbsolutePath();        
        String miniEugeneFileName;
        if (System.getProperty("os.name").contains("Mac")) {
            miniEugeneFileName = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
        } else if (System.getProperty("os.name").contains("Linux")) {
            miniEugeneFileName = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
        } else {
            miniEugeneFileName = path.substring(path.lastIndexOf("\\") + 1, path.length() - 4);
        }        
        
        List<Module> eugeneModules = EugeneAdaptor.getStructures(structuralSpecification, 1, miniEugeneFileName);
        
        //Check the validity of the Module's structure
        List<Module> rootModules = new ArrayList<Module>();
        for(Module module:eugeneModules){
            if(StructuralGrammar.validStructure(module)){
                rootModules.add(module);
            }
        }
        
        //Pick Module with least number of failure modes
        for(Module module:rootModules){
            FailureModeGrammar.assignFailureModes(module);
        }
        List<Module> sortedModules = new ArrayList<Module>();
        sortedModules = FailureModeGrammar.sortByFailureModes(rootModules);
        
        //Best Module :: sortedModules.get(0);
        Module bestModule = sortedModules.get(0);
        
        //Decompose Best Module with PhoenixGrammar to get a module graph
        PhoenixGrammar.decompose(bestModule);
        
        //Adds Testing primitives to The Module Tree. 
        TestingStructures.addTestingPrimitives(bestModule);
        
        //Perform partial part assignments given the feature library
        FeatureAssignment.partialAssignment(bestModule, 0.5);
        removeDuplicateAssignedModules(bestModule);
        
        //At this point, I have a Module tree, which has Assigned Modules for Expressors and Expressees  and a Many to Many relationship between modules and Assigned Modules. 
        //I just want to create Control Modules for AssignedModules & Create Experiment Objects for AssignedModules
        TestingStructures.createExperiments(bestModule);
        
        
        
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        conn.closeConnection();
        return bestModule;
        
    }
    
     
    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //Root MODULE IN, FILES OUT
    public static List<File> createExperimentInstructions (Module module, String filePath) throws Exception {
        
        
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        Set<AssignedModule> amodulesToTest = new HashSet<AssignedModule>();
        amodulesToTest = getAllAssignedModules(module);
        List<Experiment> currentExperiments = new ArrayList<>();
        for (AssignedModule m : amodulesToTest) {
            currentExperiments.addAll(m.getExperiments());
        }
        
        //Create assembly and testing plans
        File assemblyInstructions = RavenAdaptor.generateAssemblyPlan(amodulesToTest, filePath);
        File testingInstructions = PhoenixInstructions.generateTestingInstructions(currentExperiments, filePath);

        //Save these strings to files and return them from this method
        List<File> assmTestFiles = new ArrayList<>();
        assmTestFiles.add(testingInstructions);
        assmTestFiles.add(assemblyInstructions);
        return assmTestFiles;
    }
    
    public static Set<AssignedModule> getAllAssignedModules(Module module){
        Set<AssignedModule> modulesToTest = new HashSet<AssignedModule>();
        if(module.isRoot() || modulesToTest == null){
            modulesToTest = new HashSet<AssignedModule>();
        }
        modulesToTest.addAll(module.getAssignedModules());
        for(Module child:module.getChildren()){
            modulesToTest.addAll(getAllAssignedModules(child));
        }
        return modulesToTest;
    }
            
    
    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //MODULES IN, FILES OUT
    public static List<File> createExperimentInstructions (HashSet<AssignedModule> modulesToTest, String filePath) throws Exception {
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        List<Experiment> currentExperiments = new ArrayList<>();
        for (AssignedModule m : modulesToTest) {
            currentExperiments.addAll(m.getExperiments());
        }
        
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
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);        
        
        List<Experiment> currentExperiments = new ArrayList<>();
        
        //Import data from experiments
        ClothoAdaptor.uploadSequences(plasmidsCreated, false,clothoObject);
        FeatureAssignment.completeAssignmentSeqResults(modules);
        ClothoAdaptor.uploadCytometryData(fcsFiles, currentExperiments);

        //Run analytics to produce graphs, parameters
        AnalyticsAdaptor.runAnalytics(currentExperiments);

        //Make owl data sheets
        //OwlAdaptor.makeDatasheets(currentExperiments);
        currentExperiments.clear();

        //Run simulations to produce candidate part/feature matches
        List<Module> bestCombinedModules = COPASIAdaptor.runSimulations(modules);

        //Update module graphs based upon simulations
        HashSet<Module> modulesToTest = FeatureAssignment.completeAssignmentSim(bestCombinedModules, modules);
        //ceateExperimentInstructions (modulesToTest);
        
        conn.closeConnection();
    }
    
    public static void assignShortName(Module module){
        if(module.isRoot()){
            module.setIndex("0");
        }
        int mod_count =0;
        for(Module child:module.getChildren()){
            child.setIndex(module.getIndex() + "_"+mod_count);
            mod_count++;
            int amod_count=0;
            for(AssignedModule amod:child.getAssignedModules()){
                amod.setIndex(module.getIndex() +"_"+amod_count);
                amod_count++;
                amod.setShortName(Module.getShortModuleRole(amod.getRole())+"_"+amod.getIndex());
                for(Experiment experiment:amod.getExperiments()){
                    experiment.setAmName(amod.getName());
                    experiment.setAmShortName(amod.getShortName());
                }
            }
            assignShortName(child);
        }
    }
    
    public static void removeDuplicateAssignedModules(Module module) {
        removeDuplicateAssignedModules(module,new HashMap<String, List<AssignedModule>>());
    }
    
    public static void removeDuplicateAssignedModules(Module module, Map<String, List<AssignedModule>> amap) {
        
        if (module.isRoot()) {
            amap = new HashMap<String, List<AssignedModule>>();
        }
        
        String featureString = module.getFeatureShortString();
        
        if (amap.containsKey(featureString)) {
            module.setAssignedModules(amap.get(featureString));
        } else {
            amap.put(featureString, module.getAssignedModules());
        }

        for (Module child : module.getChildren()) {
            removeDuplicateAssignedModules(child, amap);
        }
    }
    
}
