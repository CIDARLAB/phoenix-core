/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.PrimitiveModule.PrimitiveModuleRole;

/**
 *
 * @author evanappleton
 */
public class TestingStructures {
    
    //Traverse module graphs and add testing primitives
    public static void addTestingPrimitives (List<Module> modules) {
        
        //For each module, traverse graph
        for (Module m : modules) {
            addTestingPrimitivesHelper(m, m.getChildren());            
        } 
    }
    
    //Adding testing primitive module helper
    private static void addTestingPrimitivesHelper (Module parent, List<Module> children) {
        
        //For each of the children, add testing peices if they are stage 0+
        for (Module child : children) {
            if (child.getStage() >= 0) {

                //Add testing peices to the root
                if (child.getRole().equals(ModuleRole.EXPRESSEE)) {
                    addTestExpressee(child);
                } else if (child.getRole().equals(ModuleRole.EXPRESSOR)) {
                    addTestExpressor(child);
                } else if (child.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
                    addTestTU(child);
                } else {
                    addTestHighFunction(child);
                }

                addTestingPrimitivesHelper(child, child.getChildren());
            }
        }
    }
    
    //Add testing peices to an EXPRESSEE
    private static void addTestExpressee (Module m) {
        
        //Add testing promoter, rbs, terminator, vector
        if (m.getSubmodules().size() == 1) {
            PrimitiveModuleRole pR = m.getSubmodules().get(0).getPrimitiveRole();
            if (pR.equals(PrimitiveModuleRole.CDS) || pR.equals(PrimitiveModuleRole.CDS_ACTIVATOR) || pR.equals(PrimitiveModuleRole.CDS_REPRESSOR)) {
                List<PrimitiveModule> testSubmodules = new ArrayList<>();
                testSubmodules.add(testPromoter);
                testSubmodules.add(testRBS);
                testSubmodules.add(new PrimitiveModule(PrimitiveModuleRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP")));
                testSubmodules.add(testTerminator);
                testSubmodules.add(testVector);
                m.setSubmodules(testSubmodules);
            }
        }
    }
    
    //Add testing peices to an EXPRESSOR
    private static void addTestExpressor (Module m) {
        
        //Look for testing slots, add new CDS_FLUORESCENT
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {            
            if (pm.getPrimitiveRole().equals(PrimitiveModuleRole.TESTING)) {                
                testSubmodules.add(new PrimitiveModule(PrimitiveModuleRole.CDS_FLUORESCENT, new Primitive(new ComponentType("fc"), "FP")));
            } else {
                testSubmodules.add(pm);
            }
        }
        
        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
    }
    
    //Add testing peices to a TRANSCRIPTIONAL_UNIT
    private static void addTestTU (Module m) {
        
        //Look for testing slots, add new CDS_FLUORESCENT_FUSION
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {    
            PrimitiveModuleRole pR = pm.getPrimitiveRole();
            if (pR.equals(PrimitiveModuleRole.CDS) || pR.equals(PrimitiveModuleRole.CDS_ACTIVATOR) || pR.equals(PrimitiveModuleRole.CDS_REPRESSOR)) {                
                testSubmodules.add(new PrimitiveModule(PrimitiveModuleRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP")));
            } else {
                testSubmodules.add(pm);
            }
        }
        
        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
    }
    
    //Add testing peices to a HIGHER_FUNCTION
    private static void addTestHighFunction (Module m) {
        
        //Look for testing slots, add new CDS_FLUORESCENT_FUSION
        List<PrimitiveModule> testSubmodules = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {            
            PrimitiveModuleRole pR = pm.getPrimitiveRole();
            if (pR.equals(PrimitiveModuleRole.CDS) || pR.equals(PrimitiveModuleRole.CDS_ACTIVATOR) || pR.equals(PrimitiveModuleRole.CDS_REPRESSOR)) {               
                testSubmodules.add(new PrimitiveModule(PrimitiveModuleRole.CDS_FLUORESCENT_FUSION, new Primitive(new ComponentType("fc"), "FP")));
            } else {
                testSubmodules.add(pm);
            }
        }
        
        testSubmodules.add(finalVector);
        m.setSubmodules(testSubmodules);
    }
    
    //Method for forming sets of experiments given a paritally assigned module graph
    public static List<Experiment> createExperiments(List<Module> modules) {
        return null;
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static Experiment createMultiplexExperiment(Module module) {
        return null;
    }
    
    //Method for forming an experiment from a module which has partial part assignment
    private static Experiment createAssignedExperiment(Module module) {
        return null;
    }
     
    //FIELDS
    private static PrimitiveModule testPromoter = new PrimitiveModule(PrimitiveModuleRole.PROMOTER_CONSTITUTIVE, new Primitive(new ComponentType("p"), "pTEST"));
    private static PrimitiveModule testRBS = new PrimitiveModule(PrimitiveModuleRole.RBS, new Primitive(new ComponentType("r"), "rTEST"));
    private static PrimitiveModule testTerminator = new PrimitiveModule(PrimitiveModuleRole.TERMINATOR, new Primitive(new ComponentType("t"), "tTEST"));
    private static PrimitiveModule testVector = new PrimitiveModule(PrimitiveModuleRole.VECTOR, new Primitive(new ComponentType("v"), "vTEST"));
    private static PrimitiveModule finalVector = new PrimitiveModule(PrimitiveModuleRole.VECTOR, new Primitive(new ComponentType("v"), "v"));
}
