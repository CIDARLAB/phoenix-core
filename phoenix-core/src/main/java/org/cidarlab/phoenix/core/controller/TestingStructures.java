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
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Arc;
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
import org.cidarlab.phoenix.core.dom.Person;
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
                    pm.setModuleFeatures(testPromoter.getModuleFeatures());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("r")) {
                    pm.setPrimitive(testRBS.getPrimitive().clone());
                    pm.setModuleFeatures(testRBS.getModuleFeatures());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("c") || type.equals("rc") || type.equals("fc")) {
                    pm.setPrimitive(testCDS1.getPrimitive().clone());
                    pm.setModuleFeatures(testCDS1.getModuleFeatures());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } else if (type.equals("t")) {
                    pm.setPrimitive(testTerminator.getPrimitive().clone());
                    pm.setModuleFeatures(testTerminator.getModuleFeatures());
                    pm.getPrimitive().setOrientation(Component.Orientation.REVERSE);
                } 
            }            
        }
        
        m.updateModuleFeatures();
    }
    
    //Method for forming sets of experiments given a paritally assigned module graph
    public static List<Experiment> createExperiments(HashSet<Module> modules) {
        
        //Initialize experiment set - some types of modules require multiple experiments
        HashSet<Module> controlModulesAll = new HashSet<>();
        List<Medium> defaultMedia = new ArrayList<>();
        defaultMedia.add(new Medium("LB", Medium.MediaType.RICH));        
        List<Integer> defaultTime = Arrays.asList(new Integer[]{0, 10, 20, 30, 40, 50, 60});
        
        for (Module m : modules) {

            List<Experiment> experimentList = new ArrayList<>();
            
            //Experiments for EXPRESSEE
            if (m.getRole().equals(ModuleRole.EXPRESSEE)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                addSamples(experimentList, controlModulesAll, m, true);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {             
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));
                experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));                
                addSamples(experimentList, controlModulesAll, m, true);               
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));            
                addSamples(experimentList, controlModulesAll, m, true); 
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {

                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));
                experimentList.add(new Experiment(ExperimentType.SMALL_MOLECULE, "SMALL_MOLECULE_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));                
                addSamples(experimentList, controlModulesAll, m, true);

                m.getExperiments().addAll(experimentList);              

            //Experiments for EXPRESSEE_REPRESSOR    
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.DEGRADATION, "DEGRADATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
                experimentList.add(new Experiment(ExperimentType.REGULATION, "REGULATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));            
                addSamples(experimentList, controlModulesAll, m, true);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for EXPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSOR)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.EXPRESSION, "EXPRESSION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, new ArrayList<Integer>()));          
                addSamples(experimentList, controlModulesAll, m, false);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for TRANSCRIPTIONAL_UNIT
            } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                
                //Add these experiments to the returned experiment set
                experimentList.add(new Experiment(ExperimentType.RBS_CONTEXT, "RBS_CONTEXT_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));          
                addSamples(experimentList, controlModulesAll, m, false);
                
                m.getExperiments().addAll(experimentList);

            //Experiments for HIGHER_FUNCTION
            } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {

                //Initialize experiment object
                experimentList.add(new Experiment(ExperimentType.SPECIFICATION, "SPECIFICATION_" + m.getName() + "_" + new Date().toString().replaceAll(" ", "_"), defaultMedia, defaultTime));
            }
        }
        
        //Make sure all duplicate samples are merged
        List<Experiment> allExperiments = new ArrayList<>();
        for (Module m : modules) {
            allExperiments.addAll(m.getExperiments());
        }
        removeDuplicatePolynucleotides(allExperiments);
        
        return allExperiments;
    }
    
    //Add controls and samples
    private static void addSamples(List<Experiment> experiments, HashSet<Module> controlModulesAll, Module m, boolean regControls) {

        //Add control constructs 
        HashSet<Module> controlsThisModule = new HashSet<>();
        controlsThisModule.add(createExpDegControl());
        controlsThisModule.addAll(createColorControls(controlsThisModule, m));
        if (regControls) {
            controlsThisModule.addAll(createRegControls(m));
        }
        
        removeDuplicateModules(controlsThisModule, controlModulesAll, m);

        //Add samples
        for (Experiment e : experiments) {            
            createControlSamples(e, controlsThisModule, m);
            createExperimentSamples(e, m);
        }
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
                
                Feature f = pm.getModuleFeatures().get(0);
                if (f.getArcs() != null) {                    
                    for (Arc a : f.getArcs()) {
                        
                        Feature regulatee = a.getRegulatee();
                        PrimitiveModule regPromoter = new PrimitiveModule(regulatee.getRole(), new Primitive(new ComponentType("p"), regulatee.getName()), regulatee);
                        
                        Module regControl = new Module(regulatee.getClothoID().replaceAll(".ref", "") + "_REGULATION_CONTROL_" + count);
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
        }
        
        
        
        return regControls;
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static HashSet<Module> createColorControls(HashSet<Module> controlModules, Module m) {
        
        HashSet<Module> colorControls = new HashSet<>();
        
        HashSet<Module> allModulesNeedingColorControl = new HashSet<>();
        allModulesNeedingColorControl.add(m);
        allModulesNeedingColorControl.addAll(controlModules);
        
        for (Module cm : allModulesNeedingColorControl) {
            for (PrimitiveModule pm : cm.getSubmodules()) {
                if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {

                    //Expression control
                    Module colorControl = new Module(pm.getModuleFeatures().get(0).getName().replaceAll(".ref", "") + "_COLOR_CONTROL");
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
    private static void removeDuplicateModules(HashSet<Module> controlsThisModule, HashSet<Module> controlModulesAll, Module m) {
        
        for (Module cM : controlsThisModule) {
            for (Module cMA : controlModulesAll) {
                if (cM.getSubmodules().equals(cMA.getSubmodules())) {
                    cM = cMA;
                }
            }

            controlModulesAll.add(cM);
            if (m.getControlModules() == null) {
                HashSet<Module> cMs = new HashSet<>();
                cMs.add(cM);
                m.setControlModules(cMs);
            } else {
                m.getControlModules().add(cM);
            }
        }
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static void createExperimentSamples(Experiment experiment, Module m) {

        Strain defaultStrain = new Strain("E. coli DH5a");
        
        //Create regulation control sample with test sample for each media condition
        List<Polynucleotide> pNs = new ArrayList<>();
        pNs.add(makePolynucleotide(m));
        List<Sample> experimentSamples = new ArrayList<>();
        experimentSamples.addAll(experiment.getExpDegControls());
        
        //EXPRESSION EXPERIMENT
        if (experiment.getExType().equals(ExperimentType.EXPRESSION)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, 0);
                    experimentSamples.add(degControl);
                }
            }
            
        //DEGRADATION EXPERIMENT
        } else if (experiment.getExType().equals(ExperimentType.DEGRADATION)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    for (Integer i : times) {
                        Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, i);
                        experimentSamples.add(degControl);
                    }
                }
            }
            
        //REGULATION EXPERIMENT
        } else if (experiment.getExType().equals(ExperimentType.REGULATION)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, 0);
                    experimentSamples.add(degControl);
                }
            }
            
        //SMALL MOLECULE EXPERIMENT    
        } else if (experiment.getExType().equals(ExperimentType.SMALL_MOLECULE)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, 0);
                    experimentSamples.add(degControl);
                }
            }
            
        //RBS CONTEXT     
        } else if (experiment.getExType().equals(ExperimentType.RBS_CONTEXT)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, 0);
                    experimentSamples.add(degControl);
                }
            }
            
        //SPECIFICATION
        } else if (experiment.getExType().equals(ExperimentType.SPECIFICATION)) {
            
            //Add time series of samples for each media condition
            for (Medium media : experiment.getMediaConditions()) {
                List<Integer> times = experiment.getTimes();
                if (!times.isEmpty()) {
                    Sample degControl = new Sample(m.getName(), SampleType.EXPERIMENT, defaultStrain, pNs, media, 0);
                    experimentSamples.add(degControl);
                }
            }
            
        }
        
        experiment.setExperimentSamples(experimentSamples);
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static void createControlSamples(Experiment experiment, HashSet<Module> controlModules, Module testModule) {
        
        Strain defaultStrain = new Strain("E. coli DH5a");
        
        //Initialize control samples
        Sample beadControl = new Sample("RaibowBeads", SampleType.BEADS, null, null, null, 0);
        Sample negativeControl = new Sample("Negative", SampleType.NEGATIVE, defaultStrain, null, experiment.getMediaConditions().get(0), 0);
        
        experiment.setNegativeControl(negativeControl);
        experiment.setBeadControl(beadControl);
        
        //Loop through modules to create samples
        for (Module m : controlModules) {
            
            //Regulation controls
            if (m.getRole().equals(ModuleRole.REGULATION_CONTROL)) {
                
                //Get exisiting regulation controls if they exist
                List<Sample> regulationControls = experiment.getRegulationControls();
                
                //Create regulation control sample with test sample for each media condition
                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(testModule));
                pNs.add(makePolynucleotide(m));                
                for (Medium media : experiment.getMediaConditions()) {
                    Sample regulationControl = new Sample(m.getName(), SampleType.REGULATION, defaultStrain, pNs, media, 0);
                    regulationControls.add(regulationControl);
                }
                
                //Create regulation control sample in the absence of the test sample for each media condition
                List<Polynucleotide> pNsUR = new ArrayList<>();
                pNsUR.add(makePolynucleotide(m));
                for (Medium media : experiment.getMediaConditions()) {
                    Sample unregulatedRegulationControl = new Sample(m.getName(), SampleType.REGULATION, defaultStrain, pNsUR, media, 0);
                    regulationControls.add(unregulatedRegulationControl);
                }
                
            //Color controls
            } else if (m.getRole().equals(ModuleRole.COLOR_CONTROL)) {
                
                //Get existing color controls if they exist
                List<Sample> colorControls = experiment.getColorControls();
                
                //Create color control sample for each media condition
                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(testModule));               
                for (Medium media : experiment.getMediaConditions()) {
                    Sample colorControl = new Sample(m.getName(), SampleType.FLUORESCENT, defaultStrain, pNs, media, 0);
                    colorControls.add(colorControl);
                }
                
            //Expression and degration control
            } else if (m.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL)) {
        
                List<Sample> expDegControls = experiment.getExpDegControls();
                
                List<Polynucleotide> pNs = new ArrayList<>();
                pNs.add(makePolynucleotide(testModule));
                
                //Add degradation control samples if this experiment is a degradation experiment
                if (experiment.getExType().equals(ExperimentType.DEGRADATION)) {
                    
                    //Add time series of samples for each media condition
                    for (Medium media : experiment.getMediaConditions()) {
                        List<Integer> times = experiment.getTimes();
                        if (!times.isEmpty()) {
                            for (Integer i : times) {
                                Sample degControl = new Sample(m.getName(), SampleType.EXPRESSION_DEGRATATION, defaultStrain, pNs, media, i);
                                expDegControls.add(degControl);
                            }
                        }
                    }
                
                //Add expression control if this is an expression experiment    
                } else if (experiment.getExType().equals(ExperimentType.EXPRESSION)) {
                    
                    for (Medium media : experiment.getMediaConditions()) {
                        Sample expControl = new Sample(m.getName(), SampleType.EXPRESSION_DEGRATATION, defaultStrain, pNs, media, 0);
                        expDegControls.add(expControl);
                    }
                }
            }
        }
    }
    
    //Make a new polynucleotide with the same name as the module, but no other information
    private static Polynucleotide makePolynucleotide(Module m) {
        
        Polynucleotide pn = new Polynucleotide(m.getName());        
        return pn;
    }
    
    //Remove merge an duplicate samples across experiments
    private static void removeDuplicatePolynucleotides(List<Experiment> experiments) {
        
        //Initiate hashes for existing polynucleotides
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        
        Map polyNucQuery = new HashMap();
        polyNucQuery.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
        HashSet<Polynucleotide> polyNucs = ClothoAdaptor.queryPolynucleotides(polyNucQuery,clothoObject);
        HashMap<String, Polynucleotide> pnNameHash = new HashMap<>(); //key: clothoID, value: polynucleotide with that clothoID
        for (Polynucleotide pn : polyNucs) {
            pnNameHash.put(pn.getClothoID(), pn);
        }
        
        //Look through each experiment for duplicated samples
        for (Experiment ex : experiments) {
            
            HashSet<Sample> allSamples = new HashSet<>();
//            allSamples.add(ex.getBeadControl());
//            allSamples.add(ex.getNegativeControl());
            allSamples.addAll(ex.getColorControls());
            allSamples.addAll(ex.getExpDegControls());
            allSamples.addAll(ex.getExperimentSamples());
            allSamples.addAll(ex.getRegulationControls());
            
            //Remove duplicate polynucleotides
            for (Sample s : allSamples) {
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
     
    //FIELDS
    private static final PrimitiveModule testPromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), new Feature("pTEST", new NucSeq("ttgacggctagctcagtcctaggtacagtgctagc"), new Person(), FeatureRole.PROMOTER_CONSTITUTIVE));
    private static final PrimitiveModule testRBS = new PrimitiveModule(FeatureRole.RBS, new Primitive(new ComponentType("r"), "rTEST"), new Feature("rTEST", new NucSeq("gggcccaagttcacttaaaaaggagatcaacaatgaaagcaattttcgtactgaaacatcttaatcatgctaaggaggttttct"), new Person(), FeatureRole.RBS));
    private static final PrimitiveModule testCDS1 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("c"), "cTEST1"), new Feature("cTEST1", new NucSeq("atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagatcacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa"), new Person(), FeatureRole.CDS_FLUORESCENT));
    private static final PrimitiveModule testCDS2 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT, new Primitive(new ComponentType("c"), "cTEST2"), new Feature("cTEST2", new NucSeq("atgcggggttctcatcatcatcatcatcatggtatggctagcatgactggtggacagcaaatgggtcgggatctgtacgagaacctgtacttccagggctcgagcatggtgagcaagggcgaggagctgttcaccggggtggtgcccatcctggtcgagctggacggcgacgtaaacggccacaagttcagcgtgaggggcgagggcgagggcgatgccaccaacggcaagctgaccctgaagttcatctgcaccaccggcaagctgcccgtgccctggcccaccctcgtgaccaccctgagccacggcgtgcagtgcttcgcccgctaccccgaccacatgaagcagcacgacttcttcaagtccgccatgcccgaaggctacgtccaggagcgcaccatcttcttcaaggacgacggcacctacaagacccgcgccgaggtgaagttcgagggcgacaccctggtgaaccgcatcgagctgaagggcgtcgacttcaaggaggacggcaacatcctggggcacaagctggagtacaacttcaacagccacaacatctatatcatggccgtcaagcagaagaacggcatcaaggtgaacttcaagatccgccacaacgtggaggacggcagcgtgcagctcgccgaccactaccagcagaacacccccatcggcgacggccccgtgctgctgcccgacagccactacctgagcacccagtccgtgctgagcaaagaccccaacgagaagcgcgatcacatggtcctgctggagttccgcaccgccgcctaa"), new Person(), FeatureRole.CDS_FLUORESCENT));
    private static final PrimitiveModule testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"), new Feature("tTEST", new NucSeq("ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata"), new Person(), FeatureRole.TERMINATOR));
    private static final PrimitiveModule testVector1 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST1"), new Feature("vTEST1", new NucSeq(""), new Person(), FeatureRole.VECTOR));
    private static final PrimitiveModule testVector2 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST2"), new Feature("vTEST2", new NucSeq(""), new Person(), FeatureRole.VECTOR));
    private static final PrimitiveModule finalVector = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vFINAL"), new Feature("vFINAL", new NucSeq(""), new Person(), FeatureRole.VECTOR));
}
