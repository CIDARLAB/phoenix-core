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
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.SmallMolecule;
import org.cidarlab.phoenix.core.grammars.FailureModeGrammar;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
import org.cidarlab.phoenix.core.grammars.StructuralGrammar;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;


/**
 * This is the primary class for managing the workflow of tools within Phoenix
 * 
 * @author evanappleton
 * @author prash
 * 
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
        ClothoAdaptor.uploadCytometerData(cytometer,clothoObject);
        
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
    //FILES IN, Module OUT
    public static Module initializeDesign (File structuralSpecification, File functionalSpecification) throws Exception {

        //STL function decomposition
        
        //Map STL decomposition to structure contstraint libraries

        //Create target modules with miniEugene        
        String path = structuralSpecification.getAbsolutePath();        
        String miniEugeneFileName = path.substring(path.lastIndexOf(Utilities.getSeparater()) + 1, path.length() - 4);    
        
        List<Module> eugeneModules = EugeneAdaptor.getStructures(structuralSpecification, null, miniEugeneFileName);
        
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
        
        bestModule.assignTreeModuleStage();
        bestModule.printTree();

        //At this point, I have a Module tree, which has Assigned Modules for Expressors and Expressees  and a Many to Many relationship between modules and Assigned Modules. 
        //I just want to create Control Modules for AssignedModules & Create Experiment Objects for AssignedModules
        TestingStructures.createExperiments(bestModule);
        
        assignShortName(bestModule);
        
        
        
//        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
//        Clotho clothoObject = new Clotho(conn);
//        
//        conn.closeConnection();
        return bestModule;
        
    }
    
    //FILES IN, Module OUT
    public static Module initializeDesign (File structuralSpecification, File functionalSpecification, NoClotho nc) throws Exception {

        //STL function decomposition
        
        //Map STL decomposition to structure contstraint libraries

        //Create target modules with miniEugene        
        String path = structuralSpecification.getAbsolutePath();        
        String miniEugeneFileName = path.substring(path.lastIndexOf(Utilities.getSeparater()) + 1, path.length() - 4);    
        
        List<Module> eugeneModules = EugeneAdaptor.getStructures(structuralSpecification, null, miniEugeneFileName);
        
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
//        TestingStructures.addTestingPrimitives(bestModule,nc); // COMMENTED OUT TO TEST WITH OTHER COLLECTIONS OF PARTS
        
        //Perform partial part assignments given the feature library
//        FeatureAssignment.partialAssignment(bestModule, 0.5, nc); // COMMENTED OUT TO TEST WITH OTHER COLLECTIONS OF PARTS
        
        bestModule.assignTreeModuleStage();
        bestModule.printTree();

        //At this point, I have a Module tree, which has Assigned Modules for Expressors and Expressees  and a Many to Many relationship between modules and Assigned Modules. 
        //I just want to create Control Modules for AssignedModules & Create Experiment Objects for AssignedModules
//        TestingStructures.createExperiments(bestModule); // COMMENTED OUT TO TEST WITH OTHER COLLECTIONS OF PARTS
        
        assignShortName(bestModule);
        
        
        
//        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
//        Clotho clothoObject = new Clotho(conn);
//        
//        conn.closeConnection();
        return bestModule;
        
    }
    
     
    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //Root MODULE IN, FILES OUT
    public static List<File> createExperimentInstructions (Module module, String filePath) throws Exception {
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        List<AssignedModule> amodulesToTestList = new ArrayList<AssignedModule>();
        Set<AssignedModule> amodulesToTest = new HashSet<AssignedModule>();
        amodulesToTestList = getModuleTreeAssignedModules(module);
        amodulesToTest.addAll(amodulesToTestList);
        List<Experiment> currentExperiments = new ArrayList<>();
        for (AssignedModule m : amodulesToTest) {
            currentExperiments.addAll(m.getExperiments());
        }
        System.out.println("amodulesToTest size " + amodulesToTest.size());
        //Create assembly and testing plans
        File testingInstructions = PhoenixInstructions.generateTestingInstructions(amodulesToTestList, filePath);
        File mapFile = PhoenixInstructions.generateNameMapFile(amodulesToTestList, filePath);
        
        File assemblyInstructions = RavenAdaptor.generateAssemblyPlan(amodulesToTest, filePath);
        
        //Save these strings to files and return them from this method
        List<File> assmTestFiles = new ArrayList<>();
        assmTestFiles.add(testingInstructions);
        assmTestFiles.add(assemblyInstructions);
        assmTestFiles.add(mapFile);
        return assmTestFiles;
    }
    
     
    //Create assembly and testing instructions from a set of Modules that need to be built and tested
    //Root MODULE IN, FILES OUT
    public static List<File> createExperimentInstructions (Module module, String filePath, NoClotho nc) throws Exception {
        
        //Determine experiments from current module assignment state
        //Create expreriment objects based upon the modules being tested
        List<AssignedModule> amodulesToTestList = new ArrayList<AssignedModule>();
        Set<AssignedModule> amodulesToTest = new HashSet<AssignedModule>();
        amodulesToTestList = getModuleTreeAssignedModules(module);
        amodulesToTest.addAll(amodulesToTestList);
        List<Experiment> currentExperiments = new ArrayList<>();
        for (AssignedModule m : amodulesToTest) {
            currentExperiments.addAll(m.getExperiments());
        }
        System.out.println("amodulesToTest size " + amodulesToTest.size());
        //Create assembly and testing plans
        File testingInstructions = PhoenixInstructions.generateTestingInstructions(amodulesToTestList, filePath);
        File mapFile = PhoenixInstructions.generateNameMapFile(amodulesToTestList, filePath);
        
        //File assemblyInstructions = RavenAdaptor.generateAssemblyPlan(amodulesToTest, filePath, nc);
        
        //Save these strings to files and return them from this method
        List<File> assmTestFiles = new ArrayList<>();
        assmTestFiles.add(testingInstructions);
        //assmTestFiles.add(assemblyInstructions);
        assmTestFiles.add(mapFile);
        return assmTestFiles;
    }
    
    private static Feature getExpressorFeature(AssignedModule amodule){
        for(PrimitiveModule pm:amodule.getSubmodules()){
            if(pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_CONSTITUTIVE) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || pm.getModuleFeature().getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)){
                return pm.getModuleFeature();
            }
        }
        return null;
    }
    
    public static Feature getExpresseeFeature(AssignedModule amodule){
        for(PrimitiveModule pm:amodule.getSubmodules()){
            if(pm.getModuleFeature().getRole().equals(FeatureRole.CDS) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_ACTIVATOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_REPRESSOR)){
                return pm.getModuleFeature();
            }
        }
        return null;
    }
    
    private static Feature getFPFeature(AssignedModule amodule){
        for(PrimitiveModule pm:amodule.getSubmodules()){
            if(pm.getModuleFeature().getRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)){
                return pm.getModuleFeature();
            }
        }
        return null;
    }
    
    private static SmallMolecule getFeatureSmallMolecule(AssignedModule amodule){
        for(PrimitiveModule pm:amodule.getSubmodules()){
            if(pm.getModuleFeature().getRole().equals(FeatureRole.CDS) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_ACTIVATOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR) || pm.getModuleFeature().getRole().equals(FeatureRole.CDS_REPRESSOR)){
                for(Arc arc:pm.getModuleFeature().getArcs()){
                    for(SmallMolecule sm:arc.getMolecules()){
                        return sm;
                    }
                }
            }
        }
        return null;
    }
    
    
    public static void createAllSBMLfiles(Module module, String filepath){
        
        for(AssignedModule amodule:module.getAssignedModules()){
            if(!amodule.getSBMLDocument().isEmpty()){
                SBMLAdaptor.createSBMLfiles(amodule, filepath);
            }
        }
        for(Module child:module.getChildren()){
            createAllSBMLfiles(child,filepath);
        }
    }
    
    //This is currently a little funky. Needs to change for small molecules. Not sure how to handle it. Multiple SBML documents per sm?
    public static void assignSBMLDocuments(Module module){
        
        //COPASIAdaptor sbmlfunctions = new COPASIAdaptor();
        if(module.getRole().equals(ModuleRole.EXPRESSEE)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getExpresseeFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName())); //name and id of expressee
            }
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getExpresseeFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName())); //name and id of expressee
                amodule.getSBMLDocument().add(SBMLAdaptor.createActivationModel("_"+getExpresseeFeature(amodule).getClothoID(),"_"+getFPFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of the expressee and id of the FP, name of the expressee (cds), name of the FP
            }
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getExpresseeFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName())); //name and id of expressee
                amodule.getSBMLDocument().add(SBMLAdaptor.createActivationModel("_"+getExpresseeFeature(amodule).getClothoID(),"_"+getFPFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of the expressee and id of the FP, name of the expressee (cds), name of the FP
                amodule.getSBMLDocument().add(SBMLAdaptor.createInductionActivationModel("_"+(getFeatureSmallMolecule(amodule).getName().replaceAll(".", "_")), "_"+getExpresseeFeature(amodule).getClothoID(), "_"+getFPFeature(amodule).getClothoID(),getFeatureSmallMolecule(amodule).getName(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of inducer (small molecule), id of the expressee and id of the FP, name of the inducer, name of the expressee (cds), name of the FP
            }
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getExpresseeFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName())); //name and id of expressee
                amodule.getSBMLDocument().add(SBMLAdaptor.createRepressionModel("_"+getExpresseeFeature(amodule).getClothoID(),"_"+getFPFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of the expressee and id of the FP, name of the expressee (cds), name of the FP
            }
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getExpresseeFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName())); //name and id of expressee
                amodule.getSBMLDocument().add(SBMLAdaptor.createRepressionModel("_"+getExpresseeFeature(amodule).getClothoID(),"_"+getFPFeature(amodule).getClothoID(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of the expressee and id of the FP, name of the expressee (cds), name of the FP
                amodule.getSBMLDocument().add(SBMLAdaptor.createInductionRepressionModel("_"+(getFeatureSmallMolecule(amodule).getName().replaceAll(".", "_")), "_"+getExpresseeFeature(amodule).getClothoID(), "_"+getFPFeature(amodule).getClothoID(),getFeatureSmallMolecule(amodule).getName(), getExpresseeFeature(amodule).getName(),getFPFeature(amodule).getName(),Utilities.getCooperativity(getExpresseeFeature(amodule).getName()))); //id of inducer (small molecule), id of the expressee and id of the FP, name of the inducer, name of the expressee (cds), name of the FP
            }
            
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                amodule.getSBMLDocument().add(SBMLAdaptor.createExpressionModel("_"+getExpressorFeature(amodule).getClothoID(), getExpressorFeature(amodule).getName())); //
                amodule.getSBMLDocument().add(SBMLAdaptor.createDegradationModel("_"+getFPFeature(amodule).getClothoID(), getFPFeature(amodule).getName())); //id and name of expressee       
            }
        }
        
        for(Module child:module.getChildren()){
            assignSBMLDocuments(child);
        }
        
    }
    
    public static List<AssignedModule> getModuleTreeAssignedModules(Module module){
        List<AssignedModule> modulesToTest = new ArrayList<AssignedModule>();
        for (AssignedModule amodule : module.getAssignedModules()) {
            if (!modulesToTest.contains(amodule)) {
                modulesToTest.add(amodule);
            }
        }
        for(Module child:module.getChildren()){
            for(AssignedModule amodule:getModuleTreeAssignedModules(child)){
                if(!modulesToTest.contains(amodule)){
                    modulesToTest.add(amodule);
                }
            }
        }
        return modulesToTest;
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
        //List<Module> bestCombinedModules = COPASIAdaptor.runSimulations(modules);

        //Update module graphs based upon simulations
        //HashSet<Module> modulesToTest = FeatureAssignment.completeAssignmentSim(bestCombinedModules, modules);
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
    
        Map<String, List<AssignedModule>> amap = getAssignedModulesMap(module);
        removeDuplicateAssignedModules(module,amap);
        
    }
    
    public static Map<String, AssignedModule> getShortNameEXPEXEMap(Module module){
        Map<String, AssignedModule> map = new HashMap();
        getShortNameEXPEXEMap(module, map);
        
        return map;
    }
    
    private static void getShortNameEXPEXEMap(Module module, Map<String, AssignedModule> map){
        switch(module.getRole()){
            case EXPRESSOR:
            case EXPRESSEE:
            case EXPRESSEE_REPRESSOR:
            case EXPRESSEE_REPRESSIBLE_REPRESSOR:
            case EXPRESSEE_ACTIVATOR:
            case EXPRESSEE_ACTIVATIBLE_ACTIVATOR: 
                for(AssignedModule am:module.getAssignedModules()){
                    map.put(am.getShortName(), am);
                }
                break;
            default:
                break;
        }
        for(Module child:module.getChildren()){
            getShortNameEXPEXEMap(child,map);
        }
    }
    
    public static Map<String, List<AssignedModule>> getAssignedModulesMap(Module module){
        Map<String, List<AssignedModule>> amap = new HashMap<>();
        String featureString = PigeonAdaptor.generatePigeonString(module, true);
        //String featureString = module.getFeatureShortString();
        
        if (!module.getAssignedModules().isEmpty()) {
            amap.put(featureString, module.getAssignedModules());
        }
        for(Module child:module.getChildren()){
            amap.putAll(getAssignedModulesMap(child));
        }
        
        return amap;
    }
    public static void removeDuplicateAssignedModules(Module module, Map<String, List<AssignedModule>> amap) {
        
         String featureString = PigeonAdaptor.generatePigeonString(module, true);
        //String featureString = module.getFeatureShortString();
        
        if(!module.getAssignedModules().isEmpty()){
            module.setAssignedModules(amap.get(featureString));
        }

        for (Module child : module.getChildren()) {
            removeDuplicateAssignedModules(child, amap);
        }
        
        
    }
    
}
