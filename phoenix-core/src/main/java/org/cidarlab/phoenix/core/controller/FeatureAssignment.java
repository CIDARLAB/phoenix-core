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
import org.clothocad.model.Feature.FeatureRole;
import org.clothocad.model.Part;

/**
 *
 * @author evanappleton
 */
public class FeatureAssignment {    
    
    //Method for traverisng graphs performing a partial assignment
    //This method will be hacky until we have a real part assignment algorithm based on simulation
    public static void partialAssignment(List<Module> testingModules) {
        
        //Add fluorescent proteins to each module
        addFPs(testingModules);
        
        //Query promoters and regulator features, assign to abstract spots for EXPRESSORS and EXPRESSEES
        HashSet<Feature> features = ClothoAdaptor.queryFeatures(); 
        HashSet<Module> exp = getExpressorsExpressees(testingModules);
        featureMatchAssign(exp, features);
        
        for (Module m : exp) {
            
            //If the EXPRESSORS and EXPRESSEES are not fully assigned
            if (!isAssigned(m)) {
                promoterRegulatorAssign(m, features);
            }
        }                  
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
                    if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
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
            
            for (PrimitiveModule p : child.getSubmodules()) {
                
                //If a fused FP is encountered
                if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                    
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
                
                //If an EXPRESSEE is encountered
                } else if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) && !FPs.isEmpty()) {
                    
                    List<Feature> pFeatures = new ArrayList<>();
                    pFeatures.add(FPs.get(0));
                    p.setModuleFeatures(pFeatures);
                }
            }
            
            addFPsHelper(child, FPs, child.getChildren());
        }
        
        parent.updateModuleFeatures();
    }
    
    //If the structural design has any Feature names that match exactly to the Feature library, assign these
    private static void featureMatchAssign(HashSet<Module> modules, HashSet<Feature> features) {
  
        for (Module m : modules) {
            
            //Look for regulators that are abstract
            for (int i = 0; i < m.getSubmodules().size(); i++) {
                PrimitiveModule pm = m.getSubmodules().get(i);
                if (!pm.getModuleFeatures().isEmpty()) {
                    for (Feature f : pm.getModuleFeatures()) {
                        for (Feature libF : getAllFeaturesOfRole(features, f.getRole())) {
                            if (f.getName().equalsIgnoreCase(libF.getName())) {
                                List<Feature> mf = new ArrayList<>();
                                mf.add(libF);
                                pm.setModuleFeatures(mf);
                            }
                        }
                    }
                }
            }
            
            m.updateModuleFeatures();
        }
    }
    
    //Clone module, assign promoters and regulators from feature library
    private static void promoterRegulatorAssign(Module m, HashSet<Feature> features) {

        List<Module> assignedModules = m.getAssignedModules();

        //Make assigned modules for EXPRESSEES
        if (m.getRole().equals(Module.ModuleRole.EXPRESSEE)) {

            //Look for regulators that are abstract
            for (int i = 0; i < m.getSubmodules().size(); i++) {
                PrimitiveModule pm = m.getSubmodules().get(i);
                for (Feature f : pm.getModuleFeatures()) {
                    if (pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_ACTIVATOR) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_REPRESSOR)) {
                        if (f.getSequence().getSequence().isEmpty()) {

                            //Assign a regulator from the feature library
                            HashSet<Feature> featuresOfRole = getAllFeaturesOfRole(features, pm.getPrimitiveRole());
                            for (Feature fR : featuresOfRole) {
                                Module clone = m.clone();
                                List<Feature> mfClone = new ArrayList<>();
                                mfClone.add(fR);
                                clone.getSubmodules().get(i).setModuleFeatures(mfClone);
                                clone.updateModuleFeatures();
                                assignedModules.add(clone);
                            }
                        }
                    }
                }
            }
        }

        //Make assigned modules for EXPRESSORS
        if (m.getRole().equals(Module.ModuleRole.EXPRESSOR)) {

            //Keep track of already assigned promoters in the case of multiple promoters... no duplicates
            //This needs a more robust long-term solution
            ArrayList<Module> clonesThisModule = new ArrayList<>();
            ArrayList<Integer> promoterIndicies = new ArrayList<>();
            
            //Look for promoters that are abstract
            for (int i = 0; i < m.getSubmodules().size(); i++) {
                PrimitiveModule pm = m.getSubmodules().get(i);
                for (Feature f : pm.getModuleFeatures()) {
                    if (pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_CONSTITUTIVE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_REPRESSIBLE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_INDUCIBLE)) {

                        if (f.getSequence().getSequence().isEmpty()) {

                            //If there are already clones with a promoter that was assigned
                            if (clonesThisModule.isEmpty()) {

                                //Assign a promoter from the feature library
                                HashSet<Feature> featuresOfRole = new HashSet<>();
                                featuresOfRole.addAll(getAllFeaturesOfRole(features, pm.getPrimitiveRole()));
                                for (Feature fR : featuresOfRole) {
                                    Module clone = m.clone();
                                    List<Feature> mfClone = new ArrayList<>();
                                    mfClone.add(fR);
                                    clone.getSubmodules().get(i).setModuleFeatures(mfClone);
                                    clone.updateModuleFeatures();
                                    clonesThisModule.add(clone);
                                }

                            //Assign additional promoters
                            } else {

                                //Look through the indicies where promoters have been assigned already
                                boolean differentPromoter = true;
                                for (int index : promoterIndicies) {

                                    //If the Primitive modules at both positions are the same in the abstract module, assign the same final feature
                                    if (m.getSubmodules().get(i).equals(m.getSubmodules().get(index))) {
                                        for (Module clone : clonesThisModule) {
                                            clone.getSubmodules().get(i).setModuleFeatures(clone.getSubmodules().get(index).getModuleFeatures());
                                            clone.updateModuleFeatures();
                                            differentPromoter = false;
                                        }
                                        break;
                                    }
                                }
                                
                                //If a new promoter needs to be assigned, more clones will be made from the existing one
                                if (differentPromoter) {
                                    for (Module clone : clonesThisModule) {
                                        
                                        //Find promoters that should not be assigned again
                                        List<Feature> usedPromoters = new ArrayList<>();
                                        for (int index : promoterIndicies) {
                                            usedPromoters.addAll(clone.getSubmodules().get(index).getModuleFeatures());
                                        }
                                        HashSet<Feature> featuresOfRole = new HashSet<>();
                                        featuresOfRole.addAll(getAllFeaturesOfRole(features, pm.getPrimitiveRole()));
                                        featuresOfRole.removeAll(usedPromoters);
                                        
                                        //Make new clones for all non-duplicate possibilities
                                        for (Feature fR : featuresOfRole) {
                                            Module newClone = m.clone();
                                            List<Feature> mfClone = new ArrayList<>();
                                            mfClone.add(fR);
                                            newClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                                            newClone.updateModuleFeatures();
                                            clonesThisModule.add(newClone);
                                        }
                                    }                                    
                                }
                            }

                            promoterIndicies.add(i);
                        }
                    }

                    m.setAssignedModules(clonesThisModule);
                }
            }
        }
    }
    
    //Check to see how many CDS_FLUORESCENT_FUSION a Module has
    private static List<Feature> getFluorescentFusions(Module m) {
        
        List<Feature> FPList = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                FPList.addAll(pm.getModuleFeatures());
            }
        }
        
        return FPList;
    }
    
    //Traverse a list of Module graphs to find all modules of a specific stage
    private static HashSet<Module> getExpressorsExpressees (List<Module> modules) {
        
        HashSet<Module> stageModules = new HashSet<>();
        
        //Traverse each module for modules of a particular stage
        for (Module m : modules) {
            
            ArrayList<Module> queue = new ArrayList<>();
            HashSet<Module> seenModules = new HashSet<>();
            queue.add(m);
            
            //Queue traversal of module graphs to get all nodes of a certain stage
            while (!queue.isEmpty()) {
                Module currentModule = queue.get(0);
                seenModules.add(currentModule);
                queue.remove(0);

                if (currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE) || currentModule.getRole().equals(Module.ModuleRole.EXPRESSOR)) {
                    stageModules.add(currentModule);
                } 

                for (Module neighbor : currentModule.getAllNeighbors()) {
                    if (!seenModules.contains(neighbor)) {
                        if (!queue.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        
        return stageModules;
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSim(List<Module> bestSim, List<Module> modules) {
        
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSeqResults(List<Module> modules) {
        
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
        return true;
    }
    
    //Checks to see whether there is a stage of partial assignments that still exist
    public static boolean isAssigned(Module module) {
        
        //If a null sequence for a feature is found, not fully assigned
        for (PrimitiveModule pm : module.getSubmodules()) {
            for (Feature f : pm.getModuleFeatures()) {
                if (f.getSequence().getSequence().isEmpty()) {
                    return false;
                }
            }
        }        
        return true;
    }
    
    //Get all features of a particular FeatureRole
    private static HashSet<Feature> getAllFeaturesOfRole (HashSet<Feature> allFeatures, FeatureRole role) {

        HashSet<Feature> featuresOfRole = new HashSet<>();
        for (Feature f : allFeatures) {
            if (f.getRole().equals(role)) {
                featuresOfRole.add(f);
            }
        }
        
        return featuresOfRole;
    }
}
