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
import java.util.Set;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.adaptors.PigeonAdaptor;
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
    public static void partialAssignment(Module rootModule, Double percentage) {
                
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);        
        
        HashSet<AssignedModule> modulesToTest = new HashSet<>();
        HashSet<List<Feature>> assignedFeatureLists = new HashSet<>();
        
        //Add fluorescent proteins to each module
        addFPs(rootModule,clothoObject);
        
        //Query promoters and regulator features, assign to abstract spots for EXPRESSORS and EXPRESSEES
        Map featureQuery = new HashMap();
        List<Feature> features = ClothoAdaptor.queryFeatures(featureQuery,clothoObject); 
        System.out.println("==========================================");
        System.out.println("All Features");
        for(Feature feature:features){
            System.out.println(feature.toString());
        }
        
        List<Module> exe = getExpressees(rootModule);
        List<Module> exp = getExpressors(rootModule);
        
        System.out.println("==========================================");
        System.out.println("Number of Exe ::" + exe.size());
        System.out.println("Number of Exp ::" + exp.size());
        
        System.out.println("==========================================");
        System.out.println("Feature Match Assign for Expressees");
        featureMatchAssign(exe, features);
        
        System.out.println("Feature Match Assign for Expressors");
        featureMatchAssign(exp, features);
        
        List<Feature> assignedRegulators = new ArrayList<>();
        
        
        for(Module m:exe){
            if(!isAssigned(m)){
                assignedRegulators.addAll(regulatorAssign(m, features));
            }
        }
        System.out.println("==========================================");
        System.out.println("Assigned Regulators ::");
        for(Feature f:assignedRegulators){
            System.out.println(f.toString());
        }
        
        int countexp=0;
        for(Module m:exp){
            if(!isAssigned(m)){
                System.out.println("Run "+countexp);
                countexp++;
                promoterAssign(m, features, assignedRegulators);
            }
        }
        List<Module> expexe = new ArrayList<Module>();
        expexe.addAll(exe);
        expexe.addAll(exp);
        
        //CHECK FROM THIS PART ONWARDS!!!
        for (Module m : expexe) {
            Set<AssignedModule> multiplexModules = new HashSet<>();
            boolean multiplexed=false;
            List<AssignedModule> multiplexedModulesList = new ArrayList<>();
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
                    multiplexed = true;
                    multiplexModules = addMultiplexModules(assignedM, percentage, features);
                    for(AssignedModule amoduleMplx:multiplexModules){
                        if(!multiplexedModulesList.contains(amoduleMplx))
                            multiplexedModulesList.add(amoduleMplx);
                    }
                    //multiplexedModulesList.addAll(multiplexModules);
                    modulesToTest.addAll(multiplexModules);
                }
            }
            if(multiplexed){
                System.out.println("Set AssignedModules::Size of Multiplexed Modules List::"+multiplexedModulesList.size());
                m.setAssignedModules(multiplexedModulesList);
            }
        }
        
        //Assign WILDCARDs for assigned modules
        for (AssignedModule m : modulesToTest) {
            TestingStructures.wildcardAssign(m);
        }
        conn.closeConnection();
        //return modulesToTest;
    }
    
    //Method for traverisng graphs, adding fluorescent proteins
    private static void addFPs(Module rootModule,Clotho clothoObject) {
                
        //Recieve data from Clotho
        List<Fluorophore> FPs = new ArrayList<Fluorophore>();
        Map fluorophoreQuery = new HashMap();
        //fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        FPs = ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject);
        
        Cytometer cytometer = new Cytometer();
        Map cytometerQuery = new HashMap();
        //cytometerQuery.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        List<Cytometer> allCytometers = ClothoAdaptor.queryCytometers(cytometerQuery,clothoObject);
        for (Cytometer c : allCytometers) {
            if (c.getName().startsWith("BU")) {
                cytometer = c;
            }
        }
        
        //Determine how many FPs are needed
        int count = 0;
        for (PrimitiveModule p : rootModule.getSubmodules()) {
            //System.out.println("PM: " + p.getName());
            //System.out.println("PM PrimitiveRole: " + p.getPrimitiveRole());
            //System.out.println("PM Role: " + p.getRole());
            if (p.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                count++;
            }
        }
        System.out.println("Count: " + count);
        List<Fluorophore> bestSet = FluorescentProteinSelector.solve(FPs, cytometer, count);
        addFPsHelper(rootModule, bestSet);
    }
    
    
    //Helper method for traverisng graphs, adding fluorescent proteins
    private static void addFPsHelper(Module module, List<Fluorophore> FPs) {

        //Check to see if there are CDS_FLUORESCENT_FUSION in the parent
        List<Feature> parentFluorescentFusions = new ArrayList<>();
        parentFluorescentFusions.addAll(getFluorescentFusions(module));       
        
        int count = 0;
        int cdsFlCount = 0;
        
        //Assign FPs to children
        for (Module child : module.getChildren()) {
            
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
                    
                    /*List<Feature> pFeatures = new ArrayList<>();
                    pFeatures.add(FPs.get(cdsFlCount));
                    p.setModuleFeatures(pFeatures);*/
                    p.setModuleFeature(FPs.get(cdsFlCount));
                    cdsFlCount++;
                }
            }
            addFPsHelper(child, FPs);
        }
        
        module.updateModuleFeatures();
    }
    
    //If the structural design has any Feature names that match exactly to the Feature library, assign these
    private static void featureMatchAssign(List<Module> modules, List<Feature> features) {
  
        int count =0;
        for (Module m : modules) {
            
            System.out.println("==========================================");
            System.out.println("Module :: "+count);
            System.out.println(m.toString());
            count++;
            //Look for regulators that are abstract
            for(PrimitiveModule pm:m.getSubmodules()){
                for (Feature libF : getAllFeaturesOfRole(features, pm.getModuleFeature().getRole())) {
                    if (pm.getModuleFeature().getName().equalsIgnoreCase(libF.getName())) {
                        System.out.println("In Feature Match Assign :: Module Feature Set ::"+libF.getName());
                        pm.setModuleFeature(libF);
                    }
                }
            }
            m.updateModuleFeatures();
        }
    }
    
    //Clone module, assign regulators from feature library
    private static List<Feature> regulatorAssign(Module m, List<Feature> features) {

        List<Feature> assignedRegulators = new ArrayList<>();
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
                                //assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                                assignedClone.getSubmodules().get(i).setModuleFeature(fR);
                                assignedClone.updateModuleFeatures();
                                m.getAssignedModules().add(assignedClone);
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
        List<AssignedModule> clonesThisModule = new ArrayList<>();
        List<Integer> promoterIndicies = new ArrayList<>();
        int count = 0;

        //Look for promoters that are abstract
        for (int i = 0; i < m.getSubmodules().size(); i++) {
            PrimitiveModule pm = m.getSubmodules().get(i);
            //for (Feature f : pm.getModuleFeatures()) {
                if (pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_CONSTITUTIVE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_REPRESSIBLE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_INDUCIBLE)) {

                    if (pm.getModuleFeature().getSequence().getSequence().isEmpty()) {
                        System.out.println("Primitive Module ::"+pm.getName()+" Feature ::"+pm.getModuleFeature().getName() + " has empty sequence. PM Role is "+pm.getPrimitiveRole());
                        //If there are already clones with a promoter that was assigned
                        if (clonesThisModule.isEmpty()) {
                            System.out.println("ClonesThisModule list is empty");
                            //Assign a promoter from the feature library
                            List<Feature> featuresOfRole = getAllFeaturesOfRole(features, pm.getPrimitiveRole());
                            count = makeAssignedClones(featuresOfRole, assignedRegulators, m, pm, clonesThisModule, count, i);
                            System.out.println("Clones Created ::");
                            for(AssignedModule clone:clonesThisModule){
                                System.out.println(clone.toString());
                            }
                            //Assign additional promoters
                        } else {
                            System.out.println("ClonesThisModule is not empty anymore.");
                            //Look through the indicies where promoters have been assigned already
                            boolean differentPromoter = true;
                            for (int index : promoterIndicies) {

                                //If the Primitive modules at both positions are the same in the abstract module, assign the same final feature
                                if (m.getSubmodules().get(i).equals(m.getSubmodules().get(index))) {
                                    for (Module clone : clonesThisModule) {
                                        clone.getSubmodules().get(i).setModuleFeature(clone.getSubmodules().get(index).getModuleFeature());
                                        //clone.getSubmodules().get(i).setModuleFeatures(clone.getSubmodules().get(index).getModuleFeatures());
                                        clone.updateModuleFeatures();
                                        differentPromoter = false;
                                    }
                                    break;
                                }
                            }

                            //If a new promoter needs to be assigned, more clones will be made from the existing one
                            if (differentPromoter) {
                                System.out.println("Different Promoter indeed! Index is "+i+". Pm Role ::"+pm.getPrimitiveRole());
                                List<Module> incompleteClones = new ArrayList<>();
                                incompleteClones.addAll(clonesThisModule);
                                for (Module clone : incompleteClones) {

                                    clone.setName(clone.getName().substring(0, clone.getName().length()-2));
                                    
                                    //Find promoters that should not be assigned again
                                    List<Feature> usedPromoters = new ArrayList<>();
                                    for (int index : promoterIndicies) {
                                        usedPromoters.add(clone.getSubmodules().get(index).getModuleFeature());
                                    }
                                    List<Feature> featuresOfRole = new ArrayList<>();
                                    featuresOfRole.addAll(getAllFeaturesOfRole(features, pm.getPrimitiveRole()));
                                    featuresOfRole.removeAll(usedPromoters);
                                    System.out.println("Remaining Roles::");
                                    for(Feature foR:featuresOfRole){
                                        System.out.println(foR.getName());
                                    }
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
    
    
    
    //Clone module, assign promoters from feature library
    /*
    private static void promoterAssign(Module m, List<Feature> features, List<Feature> assignedRegulators) {
        
        //Keep track of already assigned promoters in the case of multiple promoters... no duplicates
        //This needs a more robust long-term solution - Double-dutch??
        List<AssignedModule> clonesThisModule = new ArrayList<>();
        List<Integer> promoterIndicies = new ArrayList<>();
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
    */
    
    
    
    private static Integer makeAssignedClones(List<Feature> featuresOfRole, List<Feature> assignedRegulators, Module m, PrimitiveModule pm, List<AssignedModule> clonesThisModule, int count, int i) {
        
        for (Feature fR : featuresOfRole) {

            //If a regulated promoter, make sure regulator was assigned already
            if (pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_REPRESSIBLE) || pm.getPrimitiveRole().equals(Feature.FeatureRole.PROMOTER_INDUCIBLE)) {
                boolean regulatorAssigned = false;

                //Check to see if this promoter is regulated by an assigned regulator
                for (Arc a : fR.getArcs()) {
                    if (assignedRegulators.contains(a.getRegulator())) {
                        regulatorAssigned = true;
                        break;
                    }
                }

                //Get rid of features that were saved when the module was saved that are only placeholders
                if (!fR.getSequence().getSequence().isEmpty() && regulatorAssigned) {
                    Module clone = m.clone(m.getName() + "_" + count);
                    AssignedModule assignedClone = new AssignedModule(clone);
                    count++;
                    List<Feature> mfClone = new ArrayList<>();
                    mfClone.add(fR);
                    //assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                    assignedClone.getSubmodules().get(i).setModuleFeature(fR);
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
                    //assignedClone.getSubmodules().get(i).setModuleFeatures(mfClone);
                    assignedClone.getSubmodules().get(i).setModuleFeature(fR);
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
    
    
    
    private static List<Module> getExpressees(Module module){
        List<Module> expressees = new ArrayList<Module>();
        
        if(module.getRole().equals(ModuleRole.EXPRESSEE) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || module.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)){
            expressees.add(module);
        }
        for(Module child:module.getChildren()){
            expressees.addAll(getExpressees(child));
        }
        
        return expressees;
    }
    
    private static List<Module> getExpressors(Module module){
        List<Module> expressors = new ArrayList<Module>();
        
        if(module.getRole().equals(ModuleRole.EXPRESSOR)){
            expressors.add(module);
        }
        for(Module child:module.getChildren()){
            expressors.addAll(getExpressors(child));
        }
        
        return expressors;
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
        //partQuery.put("schema", "org.cidarlab.phoenix.core.dom.Part");
        List<Part> parts = ClothoAdaptor.queryParts(partQuery,clothoObject);
        
        conn.closeConnection();
        return parts;
    }
    
    //Based upon the desired percentage of the module space returned make clones of assigned modules
    private static Set<AssignedModule> addMultiplexModules(AssignedModule aM, double percentage, List<Feature> features) {
        
        Set<AssignedModule> multiplexedAM = new HashSet(); 
        
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
