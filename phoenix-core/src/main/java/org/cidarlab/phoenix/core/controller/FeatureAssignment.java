/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.clothocad.model.Feature;
import org.clothocad.model.Part;

/**
 *
 * @author evanappleton
 */
public class FeatureAssignment {    
    
    //Method for traverisng graphs performing a partial assignment
    public static void partialAssignment(List<Module> tesingModules) {
        
        //Add fluorescent proteins to each module
        addFPs(tesingModules);
        
        //Query promoters and regulator features, assign to abstract spots for EXPRESSORS and EXPRESSEES
        HashSet<Feature> features = ClothoAdaptor.queryFeatures(); 
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSim(List<Module> bestSim, List<Module> modules) {
        
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSeqResults(List<Module> modules) {
        
    }
    
    //Method for traverisng graphs, adding fluorescent proteins
    private static void addFPs(List<Module> testingModules) {
        
        //Recieve data from Clotho
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();
        
        Cytometer cytometer = new Cytometer();
        HashSet<Cytometer> allCytometers = ClothoAdaptor.queryCytometers();
        for (Cytometer c : allCytometers) {
            if (c.getName().startsWith("BU")) {
                cytometer = c;
            }
        }
        
        //For each module, determine how many FPs are needed
        for (Module root : testingModules) {            
            int count = 0;
            List<Module> children = root.getChildren();
            
            //Count the number of instance of CDS_FLUORESCENT_FUSION
            for (Module child : children) {
                List<PrimitiveModule> submodules = child.getSubmodules();                
                for (PrimitiveModule p : submodules) {
                    if (p.getPrimitiveRole().equals(PrimitiveModule.PrimitiveModuleRole.CDS_FLUORESCENT_FUSION)) {
                        count++;
                    }
                }
            }
            
            ArrayList<Fluorophore> bestSet = FluorescentProteinSelector.solve(FPs, cytometer, count);
            addFPsHelper(root, bestSet, children);
        }
    }
    
    //Helper method for traverisng graphs, adding fluorescent proteins
    private static void addFPsHelper(Module parent, ArrayList<Fluorophore> FPs, List<Module> children) {

        //Check to see if there are CDS_FLUORESCENT_FUSION in the parent
        List<Feature> parentFluorescentFusions = new ArrayList<>();
        parentFluorescentFusions.addAll(getFluorescentFusions(parent));       
        
        int count = 0;
        
        //Assign FPs to children
        for (Module child : children) {
//            List<PrimitiveModule> submodules = child.getSubmodules();
            for (PrimitiveModule p : child.getSubmodules()) {
                if (p.getPrimitiveRole().equals(PrimitiveModule.PrimitiveModuleRole.CDS_FLUORESCENT_FUSION)) {
                    
                    //If the parent has FPs already assigned, pull those down
                    if (parentFluorescentFusions.size() > 0) {                        
                        List<Feature> pFeatures = new ArrayList<>();
                        pFeatures.add(parentFluorescentFusions.get(count));
                        p.setModuleFeatures(pFeatures);         
                        count++;
                    } else {
                        List<Feature> pFeatures = new ArrayList<>();
                        pFeatures.add(FPs.get(count));
                        p.setModuleFeatures(pFeatures);
                        count++;
                    }
                    
                    
                }
            }
            
            addFPsHelper(child, FPs, child.getChildren());
        }
        
        parent.updateModuleFeatures();
    }
    
    //Check to see how many CDS_FLUORESCENT_FUSION a Module has
    private static List<Feature> getFluorescentFusions(Module m) {
        
        List<Feature> FPList = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(PrimitiveModule.PrimitiveModuleRole.CDS_FLUORESCENT_FUSION)) {
                FPList.addAll(pm.getModuleFeatures());
            }
        }
        
        return FPList;
    }
    
    //Gets a list of experiments and for all modules in the each experiment, it converts features to parts
    public static HashSet<Part> getExperimentParts(List<Experiment> experiments) {
        HashSet<Part> parts = ClothoAdaptor.queryParts();
        return null;
    }
    
    //Method for converting a module to a part
    private static Part moduleToPart(Module tesingModule) {
        return null;
    }
    
    //Checks to see whether there is a stage of partial assignments that still exist
    public static boolean isFullyAssigned(List<Module> modules) {
        return false;
    }
}
