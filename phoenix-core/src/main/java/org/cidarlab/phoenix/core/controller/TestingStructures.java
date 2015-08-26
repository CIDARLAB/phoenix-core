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
    public static void addTestingPrimitives (List<Module> modules) {
        
        //For each module, traverse graph
        initializeTestingPrimitiveModules();
        for (Module m : modules) {
            addTestingPrimitivesHelper(m.getChildren());            
        } 
    }
    
    //Adding testing primitive module helper
    private static void addTestingPrimitivesHelper (List<Module> children) {
        
        //For each of the children, add testing peices if they are stage 0+
        for (Module child : children) {
            if (child.getStage() >= 0) {

                //Add testing peices to the root
                if (child.getRole().equals(ModuleRole.EXPRESSEE) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || child.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                    addTestExpressee(child);
                } else if (child.getRole().equals(ModuleRole.EXPRESSOR)) {
                    addTestExpressor(child);
                } else if (child.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                    addTestTU(child);
                } else {
                    addTestHighFunction(child);
                }

                addTestingPrimitivesHelper(child.getChildren());
            }
        }
    }
    
    //Add testing peices to an EXPRESSEE
    private static void addTestExpressee (Module m) {
        
        //Add testing promoter, rbs, terminator, vector
        if (m.getSubmodules().size() == 1) {
            FeatureRole pR = m.getSubmodules().get(0).getPrimitiveRole();
            if (pR.equals(FeatureRole.CDS) || pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                List<PrimitiveModule> testSubmodules = new ArrayList<>();
                testSubmodules.add(testPromoter);
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
    private static void addTestExpressor (Module m) {
        
        //Look for testing slots, add new CDS_FLUORESCENT
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {            
            if (pm.getPrimitiveRole().equals(FeatureRole.TESTING)) {                
                testSubmodules.add(new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fc"), "FP"), new Feature("EXPRESSEE", new NucSeq(), null, FeatureRole.CDS_FLUORESCENT)));
            } else {
                testSubmodules.add(pm);
            }
        }
        
        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
        m.updateModuleFeatures();
    }
    
    //Add testing peices to a TRANSCRIPTIONAL_UNIT
    private static void addTestTU (Module m) {
        
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
    private static void addTestHighFunction (Module m) {
        
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
                    pm.setPrimitive(testPromoter.getPrimitive().clone());
                    pm.setModuleFeature(testPromoter.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("r")) {
                    pm.setPrimitive(testRBS.getPrimitive().clone());
                    pm.setModuleFeature(testRBS.getModuleFeature());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("c") || type.equals("rc") || type.equals("fc")) {
                    pm.setPrimitive(testCDS2.getPrimitive().clone());
                    pm.setModuleFeature(testCDS2.getModuleFeature());
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
    
    //Method for forming sets of experiments given a paritally assigned module graph
    public static List<Experiment> createExperiments(HashSet<AssignedModule> modules) {
        
        //Initialize experiment set - some types of modules require multiple experiments
        ArrayList<Module> controlModulesAll = new ArrayList<>();
        List<Medium> defaultMedia = new ArrayList<>();
        defaultMedia.add(new Medium("LB", Medium.MediaType.RICH));        
        List<String> defaultTime = Arrays.asList(new String[]{"0 min", "10 min", "20 min", "30 min", "40 min", "50 min", "60 min"});
        HashMap<String, Sample> sampleHash = new HashMap<>();
        
        for (AssignedModule m : modules) {

            List<Experiment> experimentList = new ArrayList<>();
            
            //Experiments for EXPRESSEE
            if (m.getRole().equals(ModuleRole.EXPRESSEE)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                addSamples(experimentList, controlModulesAll, sampleHash, m, true);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {             
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));
                experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));                
                addSamples(experimentList, controlModulesAll, sampleHash, m, true);               
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));            
                addSamples(experimentList, controlModulesAll, sampleHash, m, true); 
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {

                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));
                experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));                
                addSamples(experimentList, controlModulesAll, sampleHash, m, true);

                m.getExperiments().addAll(experimentList);              

            //Experiments for EXPRESSEE_REPRESSOR    
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));            
                addSamples(experimentList, controlModulesAll, sampleHash, m, true);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.EXPRESSION, "EXPRESSION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<String>()));          
                addSamples(experimentList, controlModulesAll, sampleHash, m, false);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for TRANSCRIPTIONAL_UNIT
            } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.RBS_CONTEXT, "RBS_CONTEXT_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));          
                addSamples(experimentList, controlModulesAll, sampleHash, m, false);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for HIGHER_FUNCTION
            } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {

                //Initialize experiment object
                experimentList.add(new Experiment(ExperimentType.SPECIFICATION, "SPECIFICATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
            }
        }
        
        //Make sure all duplicate samples are merged
        List<Experiment> allExperiments = new ArrayList<>();
        for (AssignedModule m : modules) {
            allExperiments.addAll(m.getExperiments());
        }
        removeDuplicateSamplesPolynucleotides(allExperiments);
        
        return allExperiments;
    }
    
    //Add controls and samples
    private static void addSamples(List<Experiment> experiments, ArrayList<Module> controlModulesAll, HashMap<String, Sample> sampleHash, AssignedModule aM, boolean regControls) {

        //Add control constructs 
        ArrayList<Module> controlsThisModule = new ArrayList<>();
        controlsThisModule.add(createExpDegControl());
        controlsThisModule.addAll(createColorControls(controlsThisModule, aM));
        if (regControls) {
            controlsThisModule.addAll(createRegControls(aM));
        }
        
        removeDuplicateModules(controlsThisModule, controlModulesAll, aM);

        //Add samples
        for (Experiment e : experiments) {            
            createControlSamples(e, controlsThisModule, aM, sampleHash, 3);
            createExperimentSamples(e, aM, sampleHash, 3);
        }
        
        aM.setControlModules(controlsThisModule);
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static Module createExpDegControl() {
        
        //Expression control
        Module expDegControl = new Module("EXPRESSION_DEGRADATION_CONTROL");
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        testSubmodules.add(testPromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(testCDS1);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector1);
        expDegControl.setSubmodules(testSubmodules);
        expDegControl.updateModuleFeatures();
        expDegControl.setRole(ModuleRole.EXPRESSION_DEGRATATION_CONTROL);
        
        return expDegControl;
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static HashSet<Module> createRegControls(Module expressee) {
        
        //Expression control
        HashSet<Module> regControls = new HashSet<>();
        int count = 0;
        
        //Find the promoters that correspond to this regulatory gene 
        for (PrimitiveModule pm : expressee.getSubmodules()) {
            FeatureRole pR = pm.getPrimitiveRole();
            if (pR.equals(FeatureRole.CDS_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSOR) || pR.equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pR.equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                
                //Feature f = pm.getModuleFeatures().get(0);
                for (Arc a : pm.getModuleFeature().getArcs()) {

                    Feature regulatee = a.getRegulatee();
                    PrimitiveModule regPromoter = new PrimitiveModule(regulatee.getRole(), new Primitive(new ComponentType("p"), regulatee.getName()), regulatee);

                    Module regControl = new Module(regulatee.getName().replaceAll(".ref", "") + "_REGULATION_CONTROL");
                    count++;
                    List<PrimitiveModule> testSubmodules = new ArrayList<>();

                    testSubmodules.add(regPromoter);
                    testSubmodules.add(testRBS);
                    testSubmodules.add(testCDS2);
                    testSubmodules.add(testTerminator);
                    testSubmodules.add(testVector2);
                    regControl.setSubmodules(testSubmodules);
                    regControl.updateModuleFeatures();
                    regControl.setRole(ModuleRole.REGULATION_CONTROL);

                    regControls.add(regControl);
                }

            }
        }
        return regControls;
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static ArrayList<Module> createColorControls(ArrayList<Module> controlModules, Module m) {
        
        HashSet<Module> colorControls = new HashSet<>();
        
        HashSet<Module> allModulesNeedingColorControl = new HashSet<>();
        allModulesNeedingColorControl.add(m);
        allModulesNeedingColorControl.addAll(controlModules);
        
        for (Module cm : allModulesNeedingColorControl) {
            for (PrimitiveModule pm : cm.getSubmodules()) {
                if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {

                    //Expression control
                    Module colorControl = new Module(pm.getModuleFeature().getName().replaceAll(".ref", "") + "_COLOR_CONTROL");
                    List<PrimitiveModule> testSubmodules = new ArrayList<>();
                    testSubmodules.add(testPromoter);
                    testSubmodules.add(testRBS);
                    testSubmodules.add(pm);
                    testSubmodules.add(testTerminator);
                    testSubmodules.add(testVector2);
                    colorControl.setSubmodules(testSubmodules);
                    colorControl.updateModuleFeatures();
                    colorControl.setRole(ModuleRole.COLOR_CONTROL);
                    colorControls.add(colorControl);
                }
            }
        }

        controlModules.addAll(colorControls);
        return controlModules;
    }
    
    //Remove duplicate modules based on PrimitiveModules
    private static void removeDuplicateModules(ArrayList<Module> controlsThisModule, ArrayList<Module> controlModulesAll, AssignedModule m) {
        
        for (Module cM : controlsThisModule) {
            for (Module cMA : controlModulesAll) {
                if (cM.getSubmodules().equals(cMA.getSubmodules())) {
                    cM = cMA;
                }
            }

            controlModulesAll.add(cM);
            if (m.getControlModules() == null) {
                ArrayList<Module> cMs = new ArrayList<>();
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
        
//        experiment.setExperimentSamples(experimentSamples);
        experiment.setExperimentSamples(replicateExperimentSamples);
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static void createControlSamples(Experiment experiment, ArrayList<Module> controlModules, Module testModule, HashMap<String, Sample> sampleHash, Integer replicates) {
        
        Strain defaultStrain = new Strain("E. coli DH5a");
        
        //Initialize control samples
        Sample beadControl = new Sample(SampleType.BEADS, null, null, null, "0 min");
        Sample negativeControl = new Sample(SampleType.NEGATIVE, defaultStrain, null, experiment.getMediaConditions().get(0), "0 min");
        
        if (sampleHash.get(beadControl.getClothoID()) == null) {
            sampleHash.put(beadControl.getClothoID(), beadControl);
            experiment.setBeadControl(beadControl);
        } else {
            experiment.setBeadControl(sampleHash.get(beadControl.getClothoID()));
        }
        
        if (sampleHash.get(negativeControl.getClothoID()) == null) {
            sampleHash.put(negativeControl.getClothoID(), negativeControl);
            experiment.setNegativeControl(negativeControl);
        } else {
            experiment.setNegativeControl(sampleHash.get(negativeControl.getClothoID()));
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

                experiment.setRegulationControls(replicateControlSamples);
                
            //Color controls
            } else if (m.getRole().equals(ModuleRole.COLOR_CONTROL)) {
                
                //Get existing color controls if they exist
                List<Sample> colorControls = experiment.getColorControls();
                
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

                experiment.setExpDegControls(replicateControlSamples);
            }
        }
    }
    
    //Make a new polynucleotide with the same name as the module, but no other information
    private static Polynucleotide makePolynucleotide(Module m) {
        
        Polynucleotide pn = new Polynucleotide(m.getName()+"_Polynucleotide");        
        return pn;
    }
    
    //Remove merge an duplicate samples across experiments
    private static void removeDuplicateSamplesPolynucleotides(List<Experiment> experiments) {
        
        //Initiate hashes for existing polynucleotides
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        
        Map polyNucQuery = new HashMap();
        polyNucQuery.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
        HashSet<Polynucleotide> polyNucs = ClothoAdaptor.queryPolynucleotides(polyNucQuery,clothoObject);
        HashMap<String, Polynucleotide> pnNameHash = new HashMap<>(); //key: clothoID, value: polynucleotide with that clothoID
        for (Polynucleotide pn : polyNucs) {
            pnNameHash.put(pn.getClothoID(), pn);
        }
        
        //Look through each experiment for duplicated samples
        List<Sample> allSamples = new ArrayList<>();
        List<Sample> sampleList = new ArrayList<>();
        
        for (Experiment ex : experiments) {                        
            allSamples.addAll(ex.getAllSamples());
        }
            
        //Remove duplicate polynucleotides
        for (Sample s : allSamples) {

            //Remove duplicate polynucleotides
            if (s.getPolynucleotides() != null) {
                List<Polynucleotide> polynucleotides = s.getPolynucleotides();
                for (Polynucleotide pn : polynucleotides) {
                    if (pnNameHash.containsKey(pn.getClothoID())) {
                        pn = pnNameHash.get(pn.getClothoID());
                    } else {
                        pnNameHash.put(pn.getClothoID(), pn);
                    }
                }
            }
        }
        
        conn.closeConnection();
    }
    
    //Initialize testing primitive modules
    private static void initializeTestingPrimitiveModules() {

        //Initiate hashes for existing polynucleotides
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);        
                
        Map J23104 = new HashMap();
        J23104.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        J23104.put("name", "J23104.ref");
        testPromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(J23104)).iterator().next());
        
        Map BCD2 = new HashMap();
        BCD2.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        BCD2.put("name", "BCD2.ref");
        testRBS = new PrimitiveModule(FeatureRole.RBS, new Primitive(new ComponentType("r"), "rTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(BCD2)).iterator().next());
        
        Map HelicalLinker = new HashMap();
        HelicalLinker.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        HelicalLinker.put("name", "HelicalLinker.ref");
        testLinker = new PrimitiveModule(FeatureRole.CDS_LINKER, new Primitive(new ComponentType("c"), "linkerTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(HelicalLinker)).iterator().next());
        
        Map GFPm = new HashMap();
        GFPm.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        GFPm.put("name", "EGFPm.ref");
        testCDS1 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("c"), "cTEST1"), ClothoAdaptor.convertJSONArrayToFluorophores((JSONArray) clothoObject.query(GFPm)).iterator().next());
        
        Map EBFP2 = new HashMap();
        EBFP2.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        EBFP2.put("name", "EBFP2.ref");
        testCDS2 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("c"), "cTEST2"), ClothoAdaptor.convertJSONArrayToFluorophores((JSONArray) clothoObject.query(EBFP2)).iterator().next());
        
        Map B0015 = new HashMap();
        B0015.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        B0015.put("name", "B0015.ref");
        testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(B0015)).iterator().next());
        
        Map ColE1 = new HashMap();
        ColE1.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        ColE1.put("name", "ColE1.ref");
        testVector1 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST1"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(ColE1)).iterator().next());        
        testVector2 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST2"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(ColE1)).iterator().next());        
        finalVector = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vFINAL"), ClothoAdaptor.convertJSONArrayToFeatures((JSONArray) clothoObject.query(ColE1)).iterator().next());
        
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
    private static PrimitiveModule testPromoter;
    private static PrimitiveModule testRBS;
    private static PrimitiveModule testLinker;
    private static PrimitiveModule testCDS1;
    private static PrimitiveModule testCDS2;
    private static PrimitiveModule testTerminator;
    private static PrimitiveModule testVector1;
    private static PrimitiveModule testVector2;
    private static PrimitiveModule finalVector;
}
