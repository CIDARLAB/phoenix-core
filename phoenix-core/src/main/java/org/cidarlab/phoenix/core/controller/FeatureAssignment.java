/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.cidarlab.phoenix.core.dom.Module;
import org.clothocad.model.Feature;
import org.clothocad.model.Part;

/**
 *
 * @author evanappleton
 */
public class FeatureAssignment {    
    
    //Method for traverisng graphs performing a partial assignment
    public static void partialAssignment(List<Module> tesingModules) {
        
        HashSet<Feature> features = ClothoAdaptor.queryFeatures();
        addFPs(tesingModules);
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSim(List<Module> bestSim, List<Module> modules) {
        
    }
    
    //Method for making a complete assignment based on best module simulations
    public static void completeAssignmentSeqResults(List<Module> modules) {
        
    }
    
    //Method for traverisng graphs, adding fluorescent proteins
    private static void addFPs(List<Module> tesingModules) {
        
        //Recieve data from Clotho
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();        
        ArrayList<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, FluorescentProteinSelector.getConfiguredCytometer(), 2);
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
