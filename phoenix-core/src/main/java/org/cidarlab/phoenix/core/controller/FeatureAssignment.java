/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Part;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 *
 * @author evanappleton
 */
public class FeatureAssignment {    
    
    //Method for traverisng graphs performing a partial assignment
    //This method will be hacky until we have a real part assignment algorithm based on simulation
    public static HashSet<AssignedModule> partialAssignment(List<Module> testingModules, Double percentage) {
                
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);        
        
        HashSet<AssignedModule> modulesToTest = new HashSet<>();
        HashSet<List<Feature>> assignedFeatureLists = new HashSet<>();
        
        //Add fluorescent proteins to each module
        addFPs(testingModules,clothoObject);
        
        //Query promoters and regulator features, assign to abstract spots for EXPRESSORS and EXPRESSEES
        Map featureQuery = new HashMap();
        featureQuery.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        List<Feature> features = ClothoAdaptor.queryFeatures(featureQuery,clothoObject); 
        ArrayList<Module> exp = getExpressorsExpressees(testingModules);
        featureMatchAssign(exp, features);
        List<Feature> assignedRegulators = new ArrayList<>();
        
        for (Module m : exp) {
            
            //If the EXPRESSORS and EXPRESSEES are not fully assigned
            if (!isAssigned(m)) {
                if (m.getRole().equals(Module.ModuleRole.EXPRESSEE) || m.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || m.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATOR) || m.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || m.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSOR)) {
                    assignedRegulators.addAll(regulatorAssign(m, features));
                } else if (m.getRole().equals(Module.ModuleRole.EXPRESSOR)) {
                    promoterAssign(m, features, assignedRegulators);
                }
            }
            
            //Add each of the assigned modules to the `modules to test' collection
            for (AssignedModule assignedM : m.getAssignedModules()) {
                
                //Check the features to remove any duplicate assignments
                List<Feature> assignedFeatureList = new ArrayList<>();
                for (PrimitiveModule pm : assignedM.getSubmodules()) {
                    if(!(pm.getModuleFeature().getSequence() == null)){
                        assignedFeatureList.add(pm.getModuleFeature());
                    }
                }
                
                //If this is a unique feature assignment, add assigned modules and multiplex copies to the modules to test
                if (assignedFeatureLists.add(assignedFeatureList)) {
                    
                    HashSet<AssignedModule> multiplexModules = addMultiplexModules(assignedM, percentage, features);
                    modulesToTest.addAll(multiplexModules);
                }
            }
        }
        
        //Assign WILDCARDs for assigned modules
        for (Module m : modulesToTest) {
            TestingStructures.wildcardAssign(m);
        }
        conn.closeConnection();
        return modulesToTest;
    }
    
    //Method for traverisng graphs, adding fluorescent proteins
    private static void addFPs(List<Module> testingModules,Clotho clothoObject) {
                
        //Recieve data from Clotho
        List<Fluorophore> FPs = new ArrayList<Fluorophore>();
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        FPs = ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject);
        
        Cytometer cytometer = new Cytometer();
        Map cytometerQuery = new HashMap();
        cytometerQuery.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        List<Cytometer> allCytometers = ClothoAdaptor.queryCytometers(cytometerQuery,clothoObject);
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
        int cdsFlCount = 0;
        
        //Assign FPs to children
        for (Module child : children) {
            
            for (PrimitiveModule p : child.getSubmodules()) {
                
                //If a fused FP is encountered
                if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                    
                    //If the parent has FPs already assigned, pull those down
                    if (parentFluorescentFusions.size() > 0) {
                        p.setModuleFeature(parentFluorescentFusions.get(count));
                        //List<Feature> pFeatures = new ArrayList<>();
                        //pFeatures.add(parentFluorescentFusions.get(count));
                        //p.setModuleFeatures(pFeatures);         
                        count++;
                    } else {
                        
                        p.setModuleFeature(FPs.get(count));
                        //List<Feature> pFeatures = new ArrayList<>();
                        //pFeatures.add(FPs.get(count));
                        //p.setModuleFeatures(pFeatures);
                        
                        count++;
                    }                    
                
                //If an EXPRESSOR is encountered
                } else if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) && !FPs.isEmpty()) {
                    
                    List<Feature> pFeatures = new ArrayList<>();
                    pFeatures.add(FPs.get(cdsFlCount));
                    p.setModuleFeatures(pFeatures);
                    cdsFlCount++;
                }
            }
            
            addFPsHelper(child, FPs, child.getChildren());
        }
        
        parent.updateModuleFeatures();
    }
    
    //If the structural design has any Feature names that match exactly to the Feature library, assign these
    private static void featureMatchAssign(ArrayList<Module> modules, List<Feature> features) {
  
        for (Module m : modules) {
            
            //Look for regulators that are abstract
            for (int i = 0; i < m.getSubmodules().size(); i++) {
                PrimitiveModule pm = m.getSubmodules().get(i);
                for (Feature libF : getAllFeaturesOfRole(features, pm.getModuleFeature().getRole())) {
                    if (pm.getModuleFeature().getName().equalsIgnoreCase(libF.getName())) {
                        pm.setModuleFeature(libF);
                    }
                }

                /*
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
                }*/
            }
            
            m.updateModuleFeatures();
        }
    }
    
    //Clone module, assign regulators from feature library
    private static List<Feature> regulatorAssign(Module m, List<Feature> features) {

        List<Feature> assignedRegulators = new ArrayList<>();
        List<AssignedModule> assignedModules = m.getAssignedModules();
        int count = 0;

        //Look for regulators that are abstract
        for (int i = 0; i < m.getSubmodules().size(); i++) {
            PrimitiveModule pm = m.getSubmodules().get(i);
            //for (Feature f : pm.getModuleFeatures()) {
                if (pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_ACTIVATOR) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_REPRESSOR) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_REPRESSIBLE_REPRESSOR)) {
                    if (pm.getModuleFeature().getSequence().getSequence().isEmpty()) {

                        //Assign a regulator from the feature library
                        List<Feature> featuresOfRole = getAllFeaturesOfRole(features, pm.getPrimitiveRole());
                        for (Feature fR : featuresOfRole) {
                            assignedRegulators.add(fR);

                            //Get rid of features that were saved when the module was saved that are only placeholders
                            if (!fR.getSequence().getSequence().isEmpty()) {
                                Module clone = m.clone(m.getName() + "_" + count);
                                AssignedModule assignedClone = new AssignedModule(clone);
                                count++;
                                List<Feature> mfClone = new ArrayList<>();
                                mfClone.add(fR);
                                assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                                assignedClone.updateModuleFeatures();
                                assignedModules.add(assignedClone);
                            }
                        }
                    }
                }
            //}
        }
        return assignedRegulators;
    }
    
    //Clone module, assign promoters from feature library
    private static void promoterAssign(Module m, List<Feature> features, List<Feature> assignedRegulators) {
        
        //Keep track of already assigned promoters in the case of multiple promoters... no duplicates
        //This needs a more robust long-term solution - Double-dutch??
        ArrayList<AssignedModule> clonesThisModule = new ArrayList<>();
        ArrayList<Integer> promoterIndicies = new ArrayList<>();
        int count = 0;

        //Look for promoters that are abstract
        for (int i = 0; i < m.getSubmodules().size(); i++) {
            PrimitiveModule pm = m.getSubmodules().get(i);
            //for (Feature f : pm.getModuleFeatures()) {
                if (pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_CONSTITUTIVE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_REPRESSIBLE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_INDUCIBLE)) {

                    if (pm.getModuleFeature().getSequence().getSequence().isEmpty()) {

                        //If there are already clones with a promoter that was assigned
                        if (clonesThisModule.isEmpty()) {

                            //Assign a promoter from the feature library
                            List<Feature> featuresOfRole = getAllFeaturesOfRole(features, pm.getPrimitiveRole());
                            count = makeAssignedClones(featuresOfRole, assignedRegulators, m, pm, clonesThisModule, count, i);

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
                                
                                List<Module> incompleteClones = new ArrayList<>();
                                incompleteClones.addAll(clonesThisModule);
                                for (Module clone : incompleteClones) {

                                    clone.setName(clone.getName().substring(0, clone.getName().length()-2));
                                    
                                    //Find promoters that should not be assigned again
                                    List<Feature> usedPromoters = new ArrayList<>();
                                    for (int index : promoterIndicies) {
                                        usedPromoters.addAll(clone.getSubmodules().get(index).getModuleFeatures());
                                    }
                                    List<Feature> featuresOfRole = new ArrayList<>();
                                    featuresOfRole.addAll(getAllFeaturesOfRole(features, pm.getPrimitiveRole()));
                                    featuresOfRole.removeAll(usedPromoters);

                                    //Make new clones for all non-duplicate possibilities
                                    count = makeAssignedClones(featuresOfRole, assignedRegulators, clone, pm, clonesThisModule, count, i);
                                }
                                clonesThisModule.removeAll(incompleteClones);
                            }
                        }

                        promoterIndicies.add(i);
                    }
                }

                m.setAssignedModules(clonesThisModule);
            //}
        }
    }
    
    private static Integer makeAssignedClones(List<Feature> featuresOfRole, List<Feature> assignedRegulators, Module m, PrimitiveModule pm, ArrayList<AssignedModule> clonesThisModule, int count, int i) {
        
        for (Feature fR : featuresOfRole) {

            //If a regulated promoter, make sure regulator was assigned already
            if (pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_REPRESSIBLE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_INDUCIBLE)) {
                boolean regulatorAssigned = false;

                //Check to see if this promoter is regulated by an assigned regulator
                if (!fR.getArcs().isEmpty()) {
                    for (Arc a : fR.getArcs()) {
                        if (assignedRegulators.contains(a.getRegulator())) {
                            regulatorAssigned = true;
                            break;
                        }
                    }
                }

                //Get rid of features that were saved when the module was saved that are only placeholders
                if (!fR.getSequence().getSequence().isEmpty() && regulatorAssigned) {
                    Module clone = m.clone(m.getName() + "_" + count);
                    AssignedModule assignedClone = new AssignedModule(clone);
                    count++;
                    List<Feature> mfClone = new ArrayList<>();
                    mfClone.add(fR);
                    assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                    assignedClone.updateModuleFeatures();
                    clonesThisModule.add(assignedClone);                    
                }

            } else {

                //Get rid of features that were saved when the module was saved that are only placeholders
                if (!fR.getSequence().getSequence().isEmpty()) {
                    Module clone = m.clone(m.getName() + "_" + count);
                    AssignedModule assignedClone = new AssignedModule(clone);
                    count++;
                    List<Feature> mfClone = new ArrayList<>();
                    mfClone.add(fR);
                    assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                    assignedClone.updateModuleFeatures();
                    clonesThisModule.add(assignedClone);  
                }
            }
        }
        
        return count;
    }
    
    //Check to see how many CDS_FLUORESCENT_FUSION a Module has
    private static List<Feature> getFluorescentFusions(Module m) {
        
        List<Feature> FPList = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                FPList.add(pm.getModuleFeature());
            }
        }
        
        return FPList;
    }
    
    //Traverse a list of Module graphs to find all modules of a specific stage
    private static ArrayList<Module> getExpressorsExpressees (List<Module> modules) {
        
        ArrayList<Module> expressors = new ArrayList();
        ArrayList<Module> expressees = new ArrayList();
        ArrayList<Module> stageModules = new ArrayList();
        
        //Traverse each module for modules of a particular stage
        for (Module m : modules) {
            
            ArrayList<Module> queue = new ArrayList();
            HashSet<Module> seenModules = new HashSet();
            queue.add(m);
            
            //Queue traversal of module graphs to get all nodes of a certain stage
            while (!queue.isEmpty()) {
                Module currentModule = queue.get(0);
                seenModules.add(currentModule);
                queue.remove(0);

                if (currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE) || currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE_ACTIVATOR) || currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || currentModule.getRole().equals(Module.ModuleRole.EXPRESSEE_REPRESSOR)) {
                    expressees.add(currentModule);
                } else if (currentModule.getRole().equals(Module.ModuleRole.EXPRESSOR)) {
                    expressors.add(currentModule);
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
        
        stageModules.addAll(expressees);
        stageModules.addAll(expressors);
        return stageModules;
    }
    
    //Method for making a complete assignment based on best module simulations
    public static HashSet<Module> completeAssignmentSim(List<Module> bestSim, List<Module> modules) {
        return null;
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSeqResults(List<Module> modules) {
        
    }
    
    //Gets a list of experiments and for all modules in the each experiment, it converts features to parts
    public static List<Part> getExperimentParts(List<Experiment> experiments) {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        Map partQuery = new HashMap();
        partQuery.put("schema", "org.cidarlab.phoenix.core.dom.Part");
        List<Part> parts = ClothoAdaptor.queryParts(partQuery,clothoObject);
        
        conn.closeConnection();
        return parts;
    }
    
    //Based upon the desired percentage of the module space returned make clones of assigned modules
    private static HashSet<AssignedModule> addMultiplexModules(AssignedModule aM, double percentage, List<Feature> features) {
        
        HashSet<AssignedModule> multiplexedAM = new HashSet(); 
        
//        for (Module aM : m.getAssignedModules()) {
            int numVariants = getNumVariants(aM, features);
            int multiplexNum = getMultiplexNumber(aM, numVariants, percentage);
            
            if (multiplexNum > 1) {
                for (int i = 0; i < multiplexNum; i++) {
                    AssignedModule clone = aM.clone(aM.getName() + "_" + i);
                    multiplexedAM.add(clone);
                }
            } else {
                multiplexedAM.add(aM);
            }
//        }
        
        return multiplexedAM;
    }
    
    //Method for converting a module to a part
    private static Integer getMultiplexNumber(Module m, int numVariants, double percentage) {
        if (m.getRole().equals(ModuleRole.EXPRESSOR)) {
            return 12;
        } else {
            return 1;
        }
    }
    
    //Method for converting a module to a part
    private static Integer getNumVariants(Module m, List<Feature> features) {
        
        int numVariants = 1;
        
        //Look for regulators that are abstract
        for (int i = 0; i < m.getSubmodules().size(); i++) {
            PrimitiveModule pm = m.getSubmodules().get(i);
            //for (Feature f : pm.getModuleFeatures()) {
                if ((pm.getModuleFeature().getSequence() == null) && !pm.getPrimitiveRole().equals(FeatureRole.WILDCARD)) {

                    //Assign a regulator from the feature library
                    List<Feature> featuresOfRole = getAllFeaturesOfRole(features, pm.getPrimitiveRole());
                    numVariants = numVariants * featuresOfRole.size();
                }
            //}
        }
        
        return numVariants;
    }
    
    //Checks to see whether there is a stage of partial assignments that still exist
    public static boolean isFullyAssigned(List<Module> modules) {
        return true;
    }
    
    //Checks to see whether there is a stage of partial assignments that still exist
    public static boolean isAssigned(Module module) {
        
        //If a null sequence for a feature is found, not fully assigned
        for (PrimitiveModule pm : module.getSubmodules()) {
            //for (Feature f : pm.getModuleFeatures()) {
                if (pm.getModuleFeature().getSequence().getSequence().isEmpty()) {
                    return false;
                }
            //}
        }        
        return true;
    }
    
    //Get all features of a particular FeatureRole
    private static List<Feature> getAllFeaturesOfRole (List<Feature> allFeatures, FeatureRole role) {

        List<Feature> featuresOfRole = new ArrayList<>();
        for (Feature f : allFeatures) {
            if (f.getRole().equals(role) && !f.getSequence().getSequence().isEmpty()) {
                featuresOfRole.add(f);
            }
        }
        
        return featuresOfRole;
    }
}
