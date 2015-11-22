/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Component;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Experiment.ExperimentType;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Medium;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.Sample;
import org.cidarlab.phoenix.core.dom.Sample.SampleType;
import org.cidarlab.phoenix.core.dom.Strain;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 *
 * @author evanappleton
 */
public class TestingStructures {

    //Traverse module graphs and add testing primitives
    public static void addTestingPrimitives(Module rootModule) {

        //For each module, traverse graph
        initializeTestingPrimitiveModules();
        addTestingPrimitivesHelper(rootModule);
       
    }

    //Adding testing primitive module helper
    private static void addTestingPrimitivesHelper(Module module) {

        //For each of the children, add testing peices if they are stage 0+
        //if (module.getStage() >= 0) {
        if (module.getRole().equals(ModuleRole.EXPRESSEE) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            addTestExpressee(module);
        } else if (module.getRole().equals(ModuleRole.EXPRESSOR)) {
            addTestExpressor(module);
        } else if (module.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            addTestTU(module);
        } else {
            addTestHighFunction(module);
        }
        //}
        for(Module child:module.getChildren()){
            addTestingPrimitivesHelper(child);    
        }
    }

    //Add testing peices to an EXPRESSEE
    private static void addTestExpressee(Module m) {

        //Add testing promoter, rbs, terminator, vector
        //These checks are purely precautionary now, this method should never be called if these things aren't true
        if (m.getSubmodules().size() == 1) {
            FeatureRole pR = m.getSubmodules().get(0).getPrimitiveRole();
            if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                
                //Check to see if this regulator is the regulator of the first testing promoter
                //If true, use the second controllable testing promoter
                PrimitiveModule testControlPromoter;
                if (m.getSubmodules().get(0).getModuleFeature().equals(testControllablePromoter1.getModuleFeature().getArcs().get(0).getRegulatee())) {
                    testControlPromoter = testControllablePromoter2;
                } else {
                    testControlPromoter = testControllablePromoter1;
                }
                
                List<PrimitiveModule> testSubmodules = new ArrayList<>();
                testSubmodules.add(testControlPromoter);
                testSubmodules.add(testRBS);
                testSubmodules.add(m.getSubmodules().get(0));
                testSubmodules.add(testLinker);
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, Feature.FeatureRole.CDS_FLUORESCENT_FUSION)));
                testSubmodules.add(testTerminator);
                testSubmodules.add(testVector1);
                m.setSubmodules(testSubmodules);
                m.updateModuleFeatures();
            }
        }
    }

    //Add testing peices to an EXPRESSOR
    private static void addTestExpressor(Module m) {

        //Look for testing slots, add new CDS_FLUORESCENT
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.TESTING)) {
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT)));
            } else {
                testSubmodules.add(pm);
            }
        }

        testSubmodules.add(testVector1);
        m.setSubmodules(testSubmodules);
        m.updateModuleFeatures();
    }

    //Add testing peices to a TRANSCRIPTIONAL_UNIT
    private static void addTestTU(Module m) {

        //Look for testing slots, add new CDS_FLUORESCENT_FUSION
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            FeatureRole pR = pm.getPrimitiveRole();
            if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT_FUSION)));
            } else {
                testSubmodules.add(pm);
            }
        }

        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
        m.updateModuleFeatures();
    }

    //Add testing peices to a HIGHER_FUNCTION
    private static void addTestHighFunction(Module m) {

        //Look for testing slots, add new CDS_FLUORESCENT_FUSION
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            FeatureRole pR = pm.getPrimitiveRole();
            if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT_FUSION)));
            } else {
                testSubmodules.add(pm);
            }
        }

        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
        m.updateModuleFeatures();
    }

    //Add testing Features in WILDCARD 
    public static void wildcardAssign(Module m) {

        //For each WILDCARD, add the appropriate testing primitive in reverse orientation 
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(Feature.FeatureRole.WILDCARD)) {

                //Assumes all primitive modules have exactly one module feature
                String type = pm.getPrimitive().getType().getName();
                if (type.equals("p") || type.equals("ip") || type.equals("rp") || type.equals("cp")) {
                    pm.setPrimitive(testConstitutivePromoter.getPrimitive().clone());
                    pm.setModuleFeature(testConstitutivePromoter.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("r")) {
                    pm.setPrimitive(testRBS.getPrimitive().clone());
                    pm.setModuleFeature(testRBS.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("c") || type.equals("rc") || type.equals("fc")) {
                    pm.setPrimitive(testFP2.getPrimitive().clone());
                    pm.setModuleFeature(testFP2.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("t")) {
                    pm.setPrimitive(testTerminator.getPrimitive().clone());
                    pm.setModuleFeature(testTerminator.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                }
            }
        }

        m.updateModuleFeatures();
    }
    
    private static class ControlsMap{
        private Map<String, String> colorNameMap;
        private Map<String, String> regNameMap;
        private Map<String, AssignedModule> aModuleControlMap;
        private ControlsMap(){
            colorNameMap = new HashMap<>();
            regNameMap = new HashMap<>();
            aModuleControlMap = new HashMap<>();
        }
    }
    
    public static void createExperiments(Module rootModule){
        ControlsMap expMap = new ControlsMap();
        expMap = getAllControls(rootModule);
        List<Medium> defaultMedia = new ArrayList<>();
        defaultMedia.add(new Medium("LB", Medium.MediaType.RICH));
        List<String> defaultTime = Arrays.asList(new String[]{"0 min", "10 min", "20 min", "30 min", "40 min", "50 min", "60 min"});
        assignAllControls(rootModule,expMap.colorNameMap,expMap.regNameMap,expMap.aModuleControlMap,defaultMedia,defaultTime);
        
    }
    
    public static void assignAllControls(Module module,Map<String, String> colorNameMap, Map<String, String> regNameMap,Map<String, AssignedModule> aModuleControlMap,List<Medium> defaultMedia,List<String> defaultTime){
        
        for(AssignedModule aModule:module.getAssignedModules()){
            List<Experiment> experimentList = new ArrayList<>();
            List<AssignedModule> controlModules = new ArrayList<>();
            
            controlModules.add(aModuleControlMap.get("EXPRESSION_DEGRADATION_CONTROL"));
            controlModules.add(aModuleControlMap.get("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL"));
                
            
            //Experiments for EXPRESSEE
            if (aModule.getRole().equals(ModuleRole.EXPRESSOR)) {

                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.EXPRESSION, "EXPRESSION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));
                aModule.getExperiments().addAll(experimentList);
                for (PrimitiveModule pm : aModule.getSubmodules()) {
                    if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                        controlModules.add(aModuleControlMap.get(colorNameMap.get(pm.getModuleFeature().getName())));
                    }
                }
                aModule.setControlModules(controlModules);
                
            } else {
                for (PrimitiveModule pm : aModule.getSubmodules()) {
                    FeatureRole pR = pm.getPrimitiveRole();
                    if (pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                        for (Arc a : pm.getModuleFeature().getArcs()) {
                            Feature regulatee = a.getRegulatee();
                            controlModules.add(aModuleControlMap.get(regNameMap.get(regulatee.getName())));
                        }
                    }
                }
                for (PrimitiveModule pm : aModule.getSubmodules()) {
                    if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                        controlModules.add(aModuleControlMap.get(colorNameMap.get(pm.getModuleFeature().getName())));
                    }
                }
                if (aModule.getRole().equals(ModuleRole.EXPRESSEE)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));
                    experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));

                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_ACTIVATOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));

                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));
                    experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));

                    aModule.getExperiments().addAll(experimentList);

                } //Experiments for EXPRESSEE_REPRESSOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));

                    aModule.getExperiments().addAll(experimentList);
                }
                aModule.setControlModules(controlModules);
            }
            
        }
        for(Module child:module.getChildren()){
            assignAllControls(child,colorNameMap,regNameMap,aModuleControlMap,defaultMedia,defaultTime);
        }
        
            //Experiments for EXPRESSOR
            //Experiments for TRANSCRIPTIONAL_UNIT
            /*
            else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.RBS_CONTEXT, "RBS_CONTEXT_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));                
                aModule.getExperiments().addAll(experimentList);

                //Experiments for HIGHER_FUNCTION
            } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
                //Initialize experiment object
                experimentList.add(new Experiment(ExperimentType.SPECIFICATION, "SPECIFICATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
            }
            */
        
        
        
    }
    
    
    
    
    
    
    
    /*
    //Add controls and samples
    private static void addSamples(List<Experiment> experiments, List<AssignedModule> controlModulesAll, HashMap<String, Sample> sampleHash, AssignedModule aM, boolean regControls) {

        //Add control constructs 
        List<AssignedModule> controlsThisModule = new ArrayList<>();
        controlsThisModule.add(createExpDegControl());
        //controlsThisModule.addAll(createColorControls(controlsThisModule, aM));
        if (regControls) {
            //controlsThisModule.addAll(createRegControls(aM));
        }

        removeDuplicateModules(controlsThisModule, controlModulesAll, aM);

        //Add samples
        for (Experiment e : experiments) {
            createControlSamples(e, controlsThisModule, aM, sampleHash, 3);
            createExperimentSamples(e, aM, sampleHash, 3);
        }

        aM.setControlModules(controlsThisModule);
    }
    */
    
    
    
    private static ControlsMap getAllControls(Module module){
        ControlsMap map = new ControlsMap();
        if(module.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT) || module.getRole().equals(ModuleRole.EXPRESSOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                getAmoduleControlsMap(amodule,map.colorNameMap,map.regNameMap,map.aModuleControlMap,false);
            }
        }
        else if(module.getRole().equals(ModuleRole.EXPRESSEE) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)){
            for(AssignedModule amodule:module.getAssignedModules()){
                getAmoduleControlsMap(amodule,map.colorNameMap,map.regNameMap,map.aModuleControlMap,true);
            }
        }
        System.out.println("Module Role ::  " +module.getRole());
        for(Module child:module.getChildren()){
            
            ControlsMap tempMap = getAllControls(child);
            map.aModuleControlMap.putAll(tempMap.aModuleControlMap);
            map.colorNameMap.putAll(tempMap.colorNameMap);
            map.regNameMap.putAll(tempMap.regNameMap);
        }
        return map;
    }
    
    private static void getAmoduleControlsMap(AssignedModule amodule, Map<String, String> colorNameMap,Map<String, String> regNameMap, Map<String, AssignedModule> aModuleMap,boolean regControl) {

        //Exp Deg Control
        if (!aModuleMap.containsKey("EXPRESSION_DEGRADATION_CONTROL")) {
            aModuleMap.put("EXPRESSION_DEGRADATION_CONTROL", createExpDegControl());
        }
        
        if(!aModuleMap.containsKey("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL")){
            aModuleMap.put("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL", createColorControl(testFP1,"EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL"));
        }
        
        //Color Control
        for (PrimitiveModule pm : amodule.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                String controlName = pm.getModuleFeature().getName().replaceAll(".ref", "") + "_COLOR_CONTROL";
                if (!colorNameMap.containsKey(pm.getModuleFeature().getName())) {
                    colorNameMap.put(pm.getModuleFeature().getName(), controlName);
                    aModuleMap.put(controlName, createColorControl(pm, controlName));
                }
            }
        }
     
        //Reg Control
        if (regControl) {
            for (PrimitiveModule pm : amodule.getSubmodules()) {
                FeatureRole pR = pm.getPrimitiveRole();
                if (pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {

                    for (Arc a : pm.getModuleFeature().getArcs()) {

                        Feature regulatee = a.getRegulatee();
                        PrimitiveModule regPromoter = new PrimitiveModule(regulatee.getRole(), new Primitive(new ComponentType("p"), regulatee.getName()), regulatee);
                        String regControlName = regulatee.getName().replaceAll(".ref", "") + "_REGULATION_CONTROL";
                        if (regNameMap.containsKey(regulatee.getName())) {
                            regNameMap.put(regulatee.getName(), regControlName);
                            aModuleMap.put(regControlName, createRegControl(regPromoter, regControlName));
                        }
                    }
                }
            }
        }
    }

    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static AssignedModule createExpDegControl() {

        //Expression control
        Module expDegControlModule = new Module("EXPRESSION_DEGRADATION_CONTROL");
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        testSubmodules.add(testConstitutivePromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(testFP1);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector1);
        expDegControlModule.setSubmodules(testSubmodules);
        expDegControlModule.updateModuleFeatures();
        expDegControlModule.setRole(ModuleRole.EXPRESSION_DEGRATATION_CONTROL);
        AssignedModule expDegContAModule = new AssignedModule(expDegControlModule);
        return expDegContAModule;
    }

    private static AssignedModule createColorControl(PrimitiveModule pm, String controlName) {
        
        Module colorControlModule = new Module(controlName);
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        testSubmodules.add(testConstitutivePromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(pm);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector1);
        colorControlModule.setSubmodules(testSubmodules);
        colorControlModule.updateModuleFeatures();
        colorControlModule.setRole(ModuleRole.COLOR_CONTROL);
        return new AssignedModule(colorControlModule);
    }
    
    private static AssignedModule createRegControl(PrimitiveModule regPromoter, String controlName) {
        Module regControlModule = new Module(controlName);
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        
        PrimitiveModule testControlPromoter;
        FeatureRole testRegulatorRole;
        ComponentType cT;
        String primitiveName;
        
        //If this promoter is the same as the first testing controllable promoter, pick the second testing promoter
        if (regPromoter.getModuleFeature().equals(testControllablePromoter1.getModuleFeature())) {
            testControlPromoter = testControllablePromoter2;
            testRegulatorRole = FeatureRole.CDS_REPRESSIBLE_REPRESSOR;
            cT = new ComponentType("rc");
            primitiveName = "rcTEST";
        } else {
            testControlPromoter = testControllablePromoter1;
            testRegulatorRole = FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR;
            cT = new ComponentType("fc");
            primitiveName = "fcTEST";
        }
        
        testSubmodules.add(regPromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(testFP2);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testConstitutivePromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(new PrimitiveModule(testRegulatorRole, new Primitive(cT, primitiveName), testControlPromoter.getModuleFeature().getArcs().get(0).getRegulator()));
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector2);
        regControlModule.setSubmodules(testSubmodules);
        regControlModule.updateModuleFeatures();
        regControlModule.setRole(ModuleRole.REGULATION_CONTROL);
        AssignedModule regControl = new AssignedModule(regControlModule);
        return regControl;
    }
    
    //Remove duplicate modules based on PrimitiveModules
    private static void removeDuplicateModules(List<AssignedModule> controlsThisModule, List<AssignedModule> controlModulesAll, AssignedModule m) {

        for (AssignedModule cM : controlsThisModule) {
            for (AssignedModule cMA : controlModulesAll) {
                if (cM.getSubmodules().equals(cMA.getSubmodules())) {
                    cM = cMA;
                }
            }

            controlModulesAll.add(cM);
            if (m.getControlModules() == null) {
                List<AssignedModule> cMs = new ArrayList<>();
                cMs.add(cM);
                m.setControlModules(cMs);
            } else {
                m.getControlModules().add(cM);
            }
        }
    }

    //Method for forming an experiment from a module which has partial part assignment
    private static void createExperimentSamples(Experiment experiment, Module m, HashMap<String, Sample> sampleHash, Integer replicates) {

        Strain defaultStrain = new Strain("E. coli DH5a");

        //Create regulation control sample with test sample for each media condition
        List<Polynucleotide> pNs = new ArrayList<>();
        pNs.add(makePolynucleotide(m));
        List<Sample> experimentSamples = new ArrayList<>();
//        experimentSamples.addAll(experiment.getExpDegControls());

        //EXPRESSION EXPERIMENT
        if (experiment.getExType().equals(ExperimentType.EXPRESSION)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (times.isEmpty()) {
                    Sample expTest = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(expTest.getClothoID()) == null) {
                        sampleHash.put(expTest.getClothoID(), expTest);
                        experimentSamples.add(expTest);
                    } else {
                        experimentSamples.add(sampleHash.get(expTest.getClothoID()));
                    }
                }
            }

            //DEGRADATION EXPERIMENT
        } else if (experiment.getExType().equals(ExperimentType.DEGRADATION)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    for (String i : times) {
                        Sample degTest = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, i);
                        if (sampleHash.get(degTest.getClothoID()) == null) {
                            sampleHash.put(degTest.getClothoID(), degTest);
                            experimentSamples.add(degTest);
                        } else {
                            experimentSamples.add(sampleHash.get(degTest.getClothoID()));
                        }
                    }
                }
            }

            //REGULATION EXPERIMENT
        } else if (experiment.getExType().equals(ExperimentType.REGULATION)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample regTest = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(regTest.getClothoID()) == null) {
                        sampleHash.put(regTest.getClothoID(), regTest);
                        experimentSamples.add(regTest);
                    } else {
                        experimentSamples.add(sampleHash.get(regTest.getClothoID()));
                    }
                }
            }

            //SMALL MOLECULE EXPERIMENT    
        } else if (experiment.getExType().equals(ExperimentType.SMALL_MOLECULE)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample smTest = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(smTest.getClothoID()) == null) {
                        sampleHash.put(smTest.getClothoID(), smTest);
                        experimentSamples.add(smTest);
                    } else {
                        experimentSamples.add(sampleHash.get(smTest.getClothoID()));
                    }
                }
            }

            //RBS CONTEXT     
        } else if (experiment.getExType().equals(ExperimentType.RBS_CONTEXT)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample rbsContextTest = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(rbsContextTest.getClothoID()) == null) {
                        sampleHash.put(rbsContextTest.getClothoID(), rbsContextTest);
                        experimentSamples.add(rbsContextTest);
                    } else {
                        experimentSamples.add(sampleHash.get(rbsContextTest.getClothoID()));
                    }
                }
            }

            //SPECIFICATION
        } else if (experiment.getExType().equals(ExperimentType.SPECIFICATION)) {

            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<String> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(SampleType.EXPERIMENT, defaultStrain, pNs, media, "0 min");
                    experimentSamples.add(degControl);
                }
            }

        }

        //Add sample replicates
        List<Sample> replicateExperimentSamples = new ArrayList();
        for (Sample s : experimentSamples) {
            List<Sample> createReplicates = createReplicates(s, replicates);
            replicateExperimentSamples.addAll(createReplicates);
        }

        //experiment.setExperimentSamples(experimentSamples);
        //experiment.setExperimentSamples(replicateExperimentSamples);
    }

    //Method for forming an experiment from a module which has partial part assignment
    private static void createControlSamples(Experiment experiment, List<AssignedModule> controlModules, Module testModule, HashMap<String, Sample> sampleHash, Integer replicates) {

        Strain defaultStrain = new Strain("E. coli DH5a");

        //Initialize control samples
        Sample beadControl = new Sample(SampleType.BEADS, null, null, null, "0 min");
        Sample negativeControl = new Sample(SampleType.NEGATIVE, defaultStrain, null, experiment.getMediaConditions().get(0), "0 min");

        if (sampleHash.get(beadControl.getClothoID()) == null) {
            sampleHash.put(beadControl.getClothoID(), beadControl);
            //experiment.setBeadControl(beadControl);
        } else {
            //experiment.setBeadControl(sampleHash.get(beadControl.getClothoID()));
        }

        if (sampleHash.get(negativeControl.getClothoID()) == null) {
            sampleHash.put(negativeControl.getClothoID(), negativeControl);
        //    experiment.setNegativeControl(negativeControl);
        } else {
        //    experiment.setNegativeControl(sampleHash.get(negativeControl.getClothoID()));
        }

        //Loop through modules to create samples
        for (Module m : controlModules) {

            //Regulation controls
            if (m.getRole().equals(ModuleRole.REGULATION_CONTROL)) {

                //Get exisiting regulation controls if they exist
                List<Sample> regulationControls = new ArrayList();

                //Create regulation control sample with test sample for each media condition
                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(testModule));
                pNs.add(makePolynucleotide(m));
                for (Medium media : experiment.getMediaConditions()) {
                    Sample regulationControl = new Sample(SampleType.REGULATION, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(regulationControl.getClothoID()) == null) {
                        sampleHash.put(regulationControl.getClothoID(), regulationControl);
                        regulationControls.add(regulationControl);
                    } else {
                        regulationControls.add(sampleHash.get(regulationControl.getClothoID()));
                    }
                }

                //Create regulation control sample in the absence of the test sample for each media condition
                List<Polynucleotide> pNsUR = new ArrayList<>();
                pNsUR.add(makePolynucleotide(m));
                for (Medium media : experiment.getMediaConditions()) {
                    Sample regulationControl = new Sample(SampleType.REGULATION, defaultStrain, pNsUR, media, "0 min");
                    if (sampleHash.get(regulationControl.getClothoID()) == null) {
                        sampleHash.put(regulationControl.getClothoID(), regulationControl);
                        regulationControls.add(regulationControl);
                    } else {
                        regulationControls.add(sampleHash.get(regulationControl.getClothoID()));
                    }
                }

                //Add sample replicates
                List<Sample> replicateControlSamples = new ArrayList();
                for (Sample s : regulationControls) {
                    List<Sample> createReplicates = createReplicates(s, replicates);
                    replicateControlSamples.addAll(createReplicates);
                }

                //experiment.setRegulationControls(replicateControlSamples);

                //Color controls
            } else if (m.getRole().equals(ModuleRole.COLOR_CONTROL)) {

                //Get existing color controls if they exist
                List<Sample> colorControls = new ArrayList<>(); 
                //colorControls = experiment.getColorControls();

                //Create color control sample for each media condition
                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(m));
                for (Medium media : experiment.getMediaConditions()) {
                    Sample colorControl = new Sample(SampleType.FLUORESCENT, defaultStrain, pNs, media, "0 min");
                    if (sampleHash.get(colorControl.getClothoID()) == null) {
                        sampleHash.put(colorControl.getClothoID(), colorControl);
                        colorControls.add(colorControl);
                    } else {
                        colorControls.add(sampleHash.get(colorControl.getClothoID()));
                    }
                    colorControls.add(colorControl);
                }

                //Expression and degration control
            } else if (m.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL)) {

                List<Sample> expDegControls = new ArrayList();

                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(m));

                //Add degradation control samples if this experiment is a degradation experiment
                if (experiment.getExType().equals(ExperimentType.DEGRADATION)) {

                    //Add time series of samples for each media condition
                    for (Medium media : experiment.getMediaConditions()) {
                        List<String> times = experiment.getTimes();
                        if (!times.isEmpty()) {
                            for (String i : times) {
                                Sample degControl = new Sample(SampleType.EXPRESSION_DEGRATATION, defaultStrain, pNs, media, i);
                                if (sampleHash.get(degControl.getClothoID()) == null) {
                                    sampleHash.put(degControl.getClothoID(), degControl);
                                    expDegControls.add(degControl);
                                } else {
                                    expDegControls.add(sampleHash.get(degControl.getClothoID()));
                                }
                            }
                        }
                    }

                    //Add expression control if this is an expression experiment    
                } else if (experiment.getExType().equals(ExperimentType.EXPRESSION)) {

                    for (Medium media : experiment.getMediaConditions()) {
                        Sample expControl = new Sample(SampleType.EXPRESSION_DEGRATATION, defaultStrain, pNs, media, "0 min");
                        if (sampleHash.get(expControl.getClothoID()) == null) {
                            sampleHash.put(expControl.getClothoID(), expControl);
                            expDegControls.add(expControl);
                        } else {
                            expDegControls.add(sampleHash.get(expControl.getClothoID()));
                        }
                    }
                }

                //Add sample replicates
                List<Sample> replicateControlSamples = new ArrayList();
                for (Sample s : expDegControls) {
                    List<Sample> createReplicates = createReplicates(s, replicates);
                    replicateControlSamples.addAll(createReplicates);
                }

                //experiment.setExpDegControls(replicateControlSamples);
            }
        }
    }

    //Make a new polynucleotide with the same name as the module, but no other information
    private static Polynucleotide makePolynucleotide(Module m) {
        Polynucleotide pn = new Polynucleotide(m.getName() + "_Polynucleotide");
        return pn;
    }
    
    
    
    //Initialize testing primitive modules
    private static void initializeTestingPrimitiveModules() {

        //Initiate hashes for existing polynucleotides
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation, Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);

        Map J23104 = new HashMap();
        J23104.put("schema", Feature.class.getCanonicalName());
        J23104.put("name", "J23104.ref");
        testConstitutivePromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(J23104, clothoObject).get(0));

        Map pBAD = new HashMap();
        pBAD.put("schema", Feature.class.getCanonicalName());
        pBAD.put("name", "para-1.ref");
        testControllablePromoter1 = new PrimitiveModule(FeatureRole.PROMOTER_INDUCIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(pBAD, clothoObject).get(0));
        
        Map pLtetO1 = new HashMap();
        pLtetO1.put("schema", Feature.class.getCanonicalName());
        pLtetO1.put("name", "pLtetO-1.ref");
        testControllablePromoter2 = new PrimitiveModule(FeatureRole.PROMOTER_REPRESSIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(pLtetO1, clothoObject).get(0));
        
        Map BCD8 = new HashMap();
        BCD8.put("schema", Feature.class.getCanonicalName());
        BCD8.put("name", "BCD8.ref");
        testRBS = new PrimitiveModule(FeatureRole.RBS, new Primitive(new ComponentType("r"), "rTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(BCD8)).iterator().next());

        Map HelicalLinker = new HashMap();
        HelicalLinker.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        HelicalLinker.put("name", "HelicalLinker.ref");
        testLinker = new PrimitiveModule(FeatureRole.CDS_LINKER, new Primitive(new ComponentType("l"), "linkerTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(HelicalLinker)).iterator().next());

        Map GFPm = new HashMap();
        GFPm.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        GFPm.put("name", "EGFPm.ref");
        testFP1 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fl"), "cTEST1"), ClothoAdaptor.queryFluorophores(GFPm, clothoObject).iterator().next());

        Map EBFP2 = new HashMap();
        EBFP2.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        EBFP2.put("name", "EBFP2.ref");
        testFP2 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fl"), "cTEST2"), ClothoAdaptor.queryFluorophores(EBFP2, clothoObject).iterator().next());

        Map B0015 = new HashMap();
        B0015.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        B0015.put("name", "B0015.ref");
        testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(B0015)).iterator().next());

        Map ColE1 = new HashMap();
        ColE1.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        ColE1.put("name", "ColE1.ref");
        testVector1 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST1"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(ColE1)).iterator().next());
        
        Map p15A = new HashMap();
        p15A.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        p15A.put("name", "p15A.ref");
        testVector2 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST2"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(p15A)).iterator().next());
        
        finalVector = testVector1;

        conn.closeConnection();
    }

    //Method for creating replicate samples
    private static List<Sample> createReplicates(Sample s, int n) {

        List<Sample> replicates = new ArrayList();
        for (int i = 0; i < n; i++) {
            Sample clone = s.clone();
            replicates.add(clone);
        }

        return replicates;
    }

    //FIELDS
    private static PrimitiveModule testConstitutivePromoter;
    private static PrimitiveModule testControllablePromoter1;
    private static PrimitiveModule testControllablePromoter2;
    private static PrimitiveModule testRBS;
    private static PrimitiveModule testLinker;
    private static PrimitiveModule testFP1;
    private static PrimitiveModule testFP2;
    private static PrimitiveModule testTerminator;
    private static PrimitiveModule testVector1;
    private static PrimitiveModule testVector2;
    private static PrimitiveModule finalVector;
}
