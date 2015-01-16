/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.ArrayList;
import java.util.HashSet;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Fluorophore;

/**
 * This class contains the methods for determining where to place fluorescent proteins in a phoenix
 * It inputs a module graph, a cytometer with settings, and fluorophore set and returns a modified modified graph
 * 
 * @author evanappleton
 */
public class FluorescentProteinSelector {
    
    //Choose n FPs given a machine with lasers and filters, but no configuration
    //This algorithm is heuristic, mapping to a constraint solver would get the optimal solution
    //The hueristic soltion will probably give near-optimal solutions since the protein sets considered here are small
    //This alrogithm is on the basis of maximum emission and excitation spectrums, it would be better to do this for the whole range
    public static ArrayList<Fluorophore> solve(HashSet<Fluorophore> FPs, Cytometer cytometer, Integer n){
        
        //Initialize return variable
        ArrayList<Fluorophore> solnList = new ArrayList<>();
        ArrayList<Fluorophore> candidateList = new ArrayList<>(FPs);
        
        //First FP chosen should be gfp or some protien in the FITC channel on the 488nm laser in a 530/30 filter get MEFLs
        //SpheroTech RCP-30-5A beads used to obain MEFLs
        //First look for all FPs that satisfy these ranges
        ArrayList<Fluorophore> FITC_channel_FPs = new ArrayList<>();
        double laser = 488.0;
        double low = 515.0;
        double high = 545.0;
        
        for (Fluorophore FP : FPs) {
            
            //The general way of finding such proteins based on maximums
            if (FP.getExcitation_max() > laser && FP.getEmission_max() > low && FP.getEmission_max() < high) {
                FITC_channel_FPs.add(FP);
            
            //The hackier way of finding FITC-spectrum proteins by name resembling GFP if the set is empty
            } else if (FITC_channel_FPs.isEmpty() && FP.getExcitation_max() > laser && FP.getName().contains("GFP")) {
                FITC_channel_FPs.add(FP);
            }
        }
        
        //Loop through the FITC_channel_FPs if there is more than one prioritize oligomerization, then brightness
        if (!FITC_channel_FPs.isEmpty()) {
            Fluorophore fitcFP = FITC_channel_FPs.get(0);
            FITC_channel_FPs.remove(0);
            
            for (int i = 1; i < FITC_channel_FPs.size(); i++) {
                Fluorophore candidate = FITC_channel_FPs.get(i);
                
                //If this FP has a smaller oligomerization, pick this one
                if (candidate.getOligomerization() < fitcFP.getOligomerization()) {
                    fitcFP = candidate;
                    
                } else if (candidate.getOligomerization() == fitcFP.getOligomerization()) {
                    
                    //If the oligomerization is the same, compare brightness
                    if (candidate.getBrightness() > fitcFP.getBrightness()) {
                        fitcFP = candidate;
                    }
                }
            }
            
            solnList.add(fitcFP);
            candidateList.remove(fitcFP);
        }
        
        
        //Then look for more proteins until the list is the desired size
        while (solnList.size() < n) {
            
        }
        
        return solnList;
    }
    
    //From FP set, determine optimized configuration of lasers and filters for a machine
    public static Cytometer getOptimizedCytomterConfiguration (HashSet<Fluorophore> FPs, Cytometer cytometer) {
        return null;
    }    
}
