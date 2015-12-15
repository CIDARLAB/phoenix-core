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
import org.cidarlab.phoenix.core.dom.Fluorophore;
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
 * @author prash
 * 
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

        //For each child of the imput module, add testing peices
        if (module.getRole().equals(ModuleRole.EXPRESSEE) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            addTestExpressee(module);
        } else if (module.getRole().equals(ModuleRole.EXPRESSOR)) {
            addTestExpressor(module);
        } else if (module.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            addTestTU(module);
        } else {
            addTestHighFunction(module);
        }

        for (Module child : module.getChildren()) {
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
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT_FUSION)));
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_TAG,new Primitive(new ComponentType("tg"),"TAG"),new Feature("TAG", new NucSeq(""),null,FeatureRole.CDS_TAG)));
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
                PrimitiveModule fpPM = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT_FUSION));
//                fpPM.setForward(true);
                testSubmodules.add(fpPM);

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

    private static class ControlsMap {

        private Map<String, String> colorNameMap;
        private Map<String, String> regNameMap;
        private Map<String, AssignedModule> aModuleControlMap;

        private ControlsMap() {
            colorNameMap = new HashMap<>();
            regNameMap = new HashMap<>();
            aModuleControlMap = new HashMap<>();
        }
    }

    public static void createExperiments(Module rootModule) {
        ControlsMap expMap = new ControlsMap();
        expMap = getAllControls(rootModule);
        List<Medium> defaultMedia = new ArrayList<>();
        defaultMedia.add(new Medium("M9_glucose", Medium.MediaType.RICH));
        assignAllControls(rootModule, expMap.colorNameMap, expMap.regNameMap, expMap.aModuleControlMap, defaultMedia, Utilities.getDefaultTimeMap());

    }

    public static void assignAllControls(Module module, Map<String, String> colorNameMap, Map<String, String> regNameMap, Map<String, AssignedModule> aModuleControlMap, List<Medium> defaultMedia, Map<ExperimentType,List<String>> defaultTimeMaps) {

        for (AssignedModule aModule : module.getAssignedModules()) {
            List<Experiment> experimentList = new ArrayList<>();
            List<AssignedModule> controlModules = new ArrayList<>();

            controlModules.add(aModuleControlMap.get("EXPRESSION_DEGRADATION_CONTROL"));
            controlModules.add(aModuleControlMap.get("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL"));

            //Experiments for EXPRESSEE
            if (aModule.getRole().equals(ModuleRole.EXPRESSOR)) {

                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.EXPRESSION, "EXPRESSION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.EXPRESSION)));
                aModule.getExperiments().addAll(experimentList);
                for (PrimitiveModule pm : aModule.getSubmodules()) {
                    if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                        controlModules.add(aModuleControlMap.get(colorNameMap.get(pm.getModuleFeature().getName())));
                    }
                }
                aModule.setControlModules(controlModules);

            } else {
                
                if (aModule.getRole().equals(ModuleRole.EXPRESSEE) || aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                    System.out.println("Assign Regulation Control");
                    for (PrimitiveModule pm : aModule.getSubmodules()) {
                        FeatureRole pR = pm.getPrimitiveRole();
                        if (pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                            for (Arc a : pm.getModuleFeature().getArcs()) {
                                Feature regulatee = a.getRegulatee();
                                System.out.println("Regulatee Name :: "+regulatee.getName());
                                controlModules.add(aModuleControlMap.get(regNameMap.get(regulatee.getName())));
                            }
                        }
                    }
                    System.out.println("Assign Color Control");
                    for (PrimitiveModule pm : aModule.getSubmodules()) {
                        if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                            System.out.println("pm Module Feature Name :: " + pm.getModuleFeature().getName());
                            controlModules.add(aModuleControlMap.get(colorNameMap.get(pm.getModuleFeature().getName())));
                        }
                    }
                }
                
                if (aModule.getRole().equals(ModuleRole.EXPRESSEE)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.DEGRADATION)));
                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.DEGRADATION)));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.REGULATION)));
                    experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.SMALL_MOLECULE)));

                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_ACTIVATOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.DEGRADATION)));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.REGULATION)));

                    aModule.getExperiments().addAll(experimentList);
                } //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.DEGRADATION)));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.REGULATION)));
                    experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.SMALL_MOLECULE)));

                    aModule.getExperiments().addAll(experimentList);

                } //Experiments for EXPRESSEE_REPRESSOR
                else if (aModule.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {

                    //Add these experiments to the returned experiment set
                    experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.DEGRADATION)));
                    experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + aModule.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTimeMaps.get(ExperimentType.REGULATION)));

                    aModule.getExperiments().addAll(experimentList);
                }
                aModule.setControlModules(controlModules);
            }

        }
        for (Module child : module.getChildren()) {
            assignAllControls(child, colorNameMap, regNameMap, aModuleControlMap, defaultMedia, defaultTimeMaps);
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
     }
     */
    private static ControlsMap getAllControls(Module module) {
        ControlsMap map = new ControlsMap();
        if (module.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT) || module.getRole().equals(ModuleRole.EXPRESSOR)) {
            for (AssignedModule amodule : module.getAssignedModules()) {
                getAmoduleControlsMap(amodule, map.colorNameMap, map.regNameMap, map.aModuleControlMap, false);
            }
        } else if (module.getRole().equals(ModuleRole.EXPRESSEE) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            for (AssignedModule amodule : module.getAssignedModules()) {
                getAmoduleControlsMap(amodule, map.colorNameMap, map.regNameMap, map.aModuleControlMap, true);
            }
        }
        System.out.println("Module Role ::  " + module.getRole());
        for (Module child : module.getChildren()) {

            ControlsMap tempMap = getAllControls(child);
            map.aModuleControlMap.putAll(tempMap.aModuleControlMap);
            map.colorNameMap.putAll(tempMap.colorNameMap);
            map.regNameMap.putAll(tempMap.regNameMap);
        }
        return map;
    }

    private static void getAmoduleControlsMap(AssignedModule amodule, Map<String, String> colorNameMap, Map<String, String> regNameMap, Map<String, AssignedModule> aModuleMap, boolean regControl) {

        //Exp Deg Control
        if (!aModuleMap.containsKey("EXPRESSION_DEGRADATION_CONTROL")) {
            aModuleMap.put("EXPRESSION_DEGRADATION_CONTROL", createExpDegControl());
        }

        if (!aModuleMap.containsKey("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL")) {
            aModuleMap.put("EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL", createColorControl(testFP1, "EXPRESSION_DEGRADATION_CONTROL_COLOR_CONTROL"));
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
            PrimitiveModule fpModule = null;
            for(PrimitiveModule pm: amodule.getSubmodules()){
                if(pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)){
                    fpModule = pm;
                }
            }
            for (PrimitiveModule pm : amodule.getSubmodules()) {
                FeatureRole pR = pm.getPrimitiveRole();
                if (pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {

                    for (Arc a : pm.getModuleFeature().getArcs()) {

                        Feature regulatee = a.getRegulatee();
                        PrimitiveModule regPromoter = new PrimitiveModule(regulatee.getRole(), new Primitive(new ComponentType("p"), regulatee.getName()), regulatee);
                        String regControlName = regulatee.getName().replaceAll(".ref", "") + "_REGULATION_CONTROL";
                        if (!regNameMap.containsKey(regulatee.getName())) {
                            regNameMap.put(regulatee.getName(), regControlName);
                            aModuleMap.put(regControlName, createRegControl(regPromoter, fpModule, regControlName));
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

    private static AssignedModule createRegControl(PrimitiveModule regPromoter, PrimitiveModule fpModule, String controlName) {
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
        if(fpModule!= null){
            if(testFP2.getModuleFeature().getName().equals(fpModule.getModuleFeature().getName())){
                testSubmodules.add(testFP1);
            }
            else{
                testSubmodules.add(testFP2);
            }
        }
        else{
            testSubmodules.add(testFP2);
        }
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

    //Initialize testing primitive modules
    private static void initializeTestingPrimitiveModules() {

        //Initiate hashes for existing polynucleotides
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation, Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);

        Map J23104 = new HashMap();
        J23104.put("schema", Feature.class.getCanonicalName());
        J23104.put("name", "J23104.ref");
        testConstitutivePromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(J23104, clothoObject).get(0));
        //testConstitutivePromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.querySingleFeature(J23104, clothoObject));

        Map pBAD = new HashMap();
        pBAD.put("schema", Feature.class.getCanonicalName());
        pBAD.put("name", "para-1.ref");
        //testControllablePromoter1 = new PrimitiveModule(FeatureRole.PROMOTER_INDUCIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(pBAD, clothoObject).get(0));
        testControllablePromoter1 = new PrimitiveModule(FeatureRole.PROMOTER_INDUCIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.querySingleFeature(pBAD, clothoObject));

        Map pLtetO1 = new HashMap();
        pLtetO1.put("schema", Feature.class.getCanonicalName());
        pLtetO1.put("name", "pLtetO-1.ref");
        //testControllablePromoter2 = new PrimitiveModule(FeatureRole.PROMOTER_REPRESSIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.queryFeatures(pLtetO1, clothoObject).get(0));
        testControllablePromoter2 = new PrimitiveModule(FeatureRole.PROMOTER_REPRESSIBLE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.querySingleFeature(pLtetO1, clothoObject));

        Map BCD8 = new HashMap();
        BCD8.put("schema", Feature.class.getCanonicalName());
        BCD8.put("name", "BCD8.ref");
        testRBS = new PrimitiveModule(FeatureRole.RBS, new Primitive(new ComponentType("r"), "rTEST"), ClothoAdaptor.querySingleFeature(BCD8, clothoObject));

        Map HelicalLinker = new HashMap();
        HelicalLinker.put("schema", Feature.class.getCanonicalName());
        HelicalLinker.put("name", "HelicalLinker.ref");
        testLinker = new PrimitiveModule(FeatureRole.CDS_LINKER, new Primitive(new ComponentType("l"), "linkerTEST"), ClothoAdaptor.querySingleFeature(HelicalLinker, clothoObject));

        Map GFPm = new HashMap();
        GFPm.put("schema", Fluorophore.class.getCanonicalName());
        GFPm.put("name", "EGFPm.ref");
        testFP1 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fl"), "cTEST1"), ClothoAdaptor.queryFluorophores(GFPm, clothoObject).iterator().next());

        Map EBFP2 = new HashMap();
        EBFP2.put("schema", Fluorophore.class.getCanonicalName());
        EBFP2.put("name", "EBFP2.ref");
        testFP2 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fl"), "cTEST2"), ClothoAdaptor.queryFluorophores(EBFP2, clothoObject).iterator().next());

        Map B0015 = new HashMap();
        B0015.put("schema", Feature.class.getCanonicalName());
        B0015.put("name", "B0015.ref");
        testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"), ClothoAdaptor.querySingleFeature(B0015, clothoObject));

        Map ColE1 = new HashMap();
        ColE1.put("schema", Feature.class.getCanonicalName());
        ColE1.put("name", "ColE1.ref");
        testVector1 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST1"), ClothoAdaptor.querySingleFeature(ColE1, clothoObject));

        Map p15A = new HashMap();
        p15A.put("schema", Feature.class.getCanonicalName());
        p15A.put("name", "p15A.ref");
        testVector2 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST2"), ClothoAdaptor.querySingleFeature(p15A, clothoObject));

        finalVector = testVector1;

        conn.closeConnection();
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
