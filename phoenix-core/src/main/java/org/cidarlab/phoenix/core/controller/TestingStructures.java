/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Component;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Person;

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
    public static HashSet<Experiment> createExperiments(HashSet<Module> modules) {
        
        //Initialize experiment set - some types of modules require multiple experiments
        HashSet<Experiment> experimentSet = new HashSet<>();
        HashSet<Module> controlModulesAll = new HashSet<>(); 
        
        for (Module m : modules) {

            HashSet<Module> controlsThisModule = new HashSet<>();
            
            //Experiments for EXPRESSEE
            if (m.getRole().equals(ModuleRole.EXPRESSEE)) {

                //Add control constructs                    
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for EXPRESSEE_ACTIVATIBLE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR)) {

                //Add control constructs                   
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for EXPRESSEE_ACTIVATOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR)) {

                //Add control constructs                    
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for EXPRESSEE_REPRESSIBLE_REPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR)) {

                //Add control constructs                   
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for EXPRESSEE_REPRESSOR    
            } else if (m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {

                //Add control constructs                  
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for EXPRESSOR
            } else if (m.getRole().equals(ModuleRole.EXPRESSOR)) {

                //Add control constructs                  
                controlsThisModule.add(createExpDegControl());
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for TRANSCRIPTIONAL_UNIT
            } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {

                //Add control constructs                  
                controlsThisModule.add(createExpDegControl());
                controlsThisModule.add(createRegControl(m));
                
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
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);

            //Experiments for HIGHER_FUNCTION
            } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {

                //Add control constructs
//                Module controlModule = new Module();
//                controlModulesAll.add(controlModule);
                
                //Initialize experiment object
                Experiment ex = new Experiment();
                experimentSet.add(ex);
            }
        }
        
        //Add control modules
        modules.addAll(controlModulesAll);
        
        return experimentSet;
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static Module createExpDegControl() {
        
        //Expression control
        Module expDegControl = new Module();
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        testSubmodules.add(testPromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(testCDS1);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector1);
        expDegControl.setSubmodules(testSubmodules);
        expDegControl.updateModuleFeatures();
        expDegControl.setRole(ModuleRole.TESTING_CONTROL);
        
        return expDegControl;
    }
    
    //Make a standard expression/degradation control for EXPRESSORs and all types of EXPRESSEEs
    private static Module createRegControl(Module expressee) {
        
        //Expression control
        Module regControl = new Module();
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        testSubmodules.add(testPromoter);
        testSubmodules.add(testRBS);
        testSubmodules.add(testCDS2);
        testSubmodules.add(testTerminator);
        testSubmodules.add(testVector2);
        regControl.setSubmodules(testSubmodules);
        regControl.updateModuleFeatures();
        regControl.setRole(ModuleRole.TESTING_CONTROL);
        
        return regControl;
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static Experiment createAssignedExperiment(Module module) {
        return null;
    }
     
    //FIELDS
    private static final PrimitiveModule testPromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"), new Feature("pTEST", new NucSeq("ttgacggctagctcagtcctaggtacagtgctagc"), new Person(), FeatureRole.PROMOTER_CONSTITUTIVE));
    private static final PrimitiveModule testRBS = new PrimitiveModule(FeatureRole.RBS, new Primitive(new ComponentType("r"), "rTEST"), new Feature("rTEST", new NucSeq("gggcccaagttcacttaaaaaggagatcaacaatgaaagcaattttcgtactgaaacatcttaatcatgctaaggaggttttct"), new Person(), FeatureRole.RBS));
    private static final PrimitiveModule testCDS1 = new PrimitiveModule(FeatureRole.CDS, new Primitive(new ComponentType("c"), "cTEST1"), new Feature("cTEST1", new NucSeq("atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagatcacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa"), new Person(), FeatureRole.CDS));
    private static final PrimitiveModule testCDS2 = new PrimitiveModule(FeatureRole.CDS, new Primitive(new ComponentType("c"), "cTEST2"), new Feature("cTEST2", new NucSeq("atgcggggttctcatcatcatcatcatcatggtatggctagcatgactggtggacagcaaatgggtcgggatctgtacgagaacctgtacttccagggctcgagcatggtgagcaagggcgaggagctgttcaccggggtggtgcccatcctggtcgagctggacggcgacgtaaacggccacaagttcagcgtgaggggcgagggcgagggcgatgccaccaacggcaagctgaccctgaagttcatctgcaccaccggcaagctgcccgtgccctggcccaccctcgtgaccaccctgagccacggcgtgcagtgcttcgcccgctaccccgaccacatgaagcagcacgacttcttcaagtccgccatgcccgaaggctacgtccaggagcgcaccatcttcttcaaggacgacggcacctacaagacccgcgccgaggtgaagttcgagggcgacaccctggtgaaccgcatcgagctgaagggcgtcgacttcaaggaggacggcaacatcctggggcacaagctggagtacaacttcaacagccacaacatctatatcatggccgtcaagcagaagaacggcatcaaggtgaacttcaagatccgccacaacgtggaggacggcagcgtgcagctcgccgaccactaccagcagaacacccccatcggcgacggccccgtgctgctgcccgacagccactacctgagcacccagtccgtgctgagcaaagaccccaacgagaagcgcgatcacatggtcctgctggagttccgcaccgccgcctaa"), new Person(), FeatureRole.CDS));
    private static final PrimitiveModule testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"), new Feature("tTEST", new NucSeq("ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctctactagagtcacactggctcaccttcgggtgggcctttctgcgtttata"), new Person(), FeatureRole.TERMINATOR));
    private static final PrimitiveModule testVector1 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST1"), new Feature("vTEST1", new NucSeq(""), new Person(), FeatureRole.VECTOR));
    private static final PrimitiveModule testVector2 = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST2"), new Feature("vTEST2", new NucSeq(""), new Person(), FeatureRole.VECTOR));
    private static final PrimitiveModule finalVector = new PrimitiveModule(FeatureRole.VECTOR, new Primitive(new ComponentType("v"), "vFINAL"), new Feature("vFINAL", new NucSeq(""), new Person(), FeatureRole.VECTOR));
}
