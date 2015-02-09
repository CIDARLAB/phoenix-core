/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
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
        
        //Filter out FPs without spectra
        for (Fluorophore f : FPs) {
            if (f.getEm_spectrum().isEmpty() || f.getEx_spectrum().isEmpty()) {
                candidateList.remove(f);
            }
        }
        
        //Save the machine settings to a signal map
        HashSet<String> laserFilterStrings = getLaserFilterStrings(cytometer.getConfiguration());
        HashMap<Fluorophore, HashMap<String, Double>> fpFilterSignals = getFPFilterSignals(candidateList, laserFilterStrings);
        
        //First FP chosen should be gfp or some protien in the FITC channel on the 488nm laser in a 530/30 filter get MEFLs
        HashMap<Fluorophore, String> measurementFilters = new HashMap<>();
        HashMap<String, Double> filterNoise = new HashMap<>();
        for (String laserFilter : laserFilterStrings) {
            filterNoise.put(laserFilter, 0.0);
        }
        
        Fluorophore fitcFluorophore = selectFITCFluorophore(candidateList, measurementFilters, filterNoise, fpFilterSignals, false);
        solnList.add(fitcFluorophore);
        candidateList.remove(fitcFluorophore);

        //Then look for more proteins until the list is the desired size
        while (solnList.size() < n) {
            
            Fluorophore selectFluorophore = selectFluorophore(candidateList, measurementFilters, filterNoise, fpFilterSignals, false);
            solnList.add(selectFluorophore);
            candidateList.remove(selectFluorophore);
        }

        return solnList;

    }
    
    //From FP set, determine optimized configuration of lasers and filters for a machine
    public static Cytometer getOptimizedCytomterConfiguration (HashSet<Fluorophore> FPs, Cytometer cytometer) {
        return null;
    }    
    
    //Pick the Fluorophore on the FITC channel first
    private static Fluorophore selectFITCFluorophore (ArrayList<Fluorophore> candidateList, HashMap<Fluorophore, String> measurementFilters, HashMap<String, Double> filterNoise,  HashMap<Fluorophore, HashMap<String, Double>> fpFilterSignals, boolean oligomerization) {
        
        //SpheroTech RCP-30-5A beads used to obain MEFLs
        //Look at all FPs that emit in this laser/filter and pick the largest signal
        String laserFilter = "488.0:545.0:515.0";
        HashSet<String> ineligibleFilters = new HashSet<>(filterNoise.keySet());
        ineligibleFilters.remove(laserFilter);
        
        double maxDifferential = -100000000.0;
        Fluorophore fitcFP = null;

        for (int i = 0; i < candidateList.size(); i++) {

            //Initialize total signal            
            Fluorophore FP = candidateList.get(i);
            HashMap<String, Double> filterDiff = getSignalDifferential(ineligibleFilters, fpFilterSignals.get(FP), filterNoise);
            double signalDifferential = -100000000.0;
            for (Double diff : filterDiff.values()) {
                signalDifferential = diff;
            }

            //Compare to the best signal score so far or null case
            if (fitcFP == null) {
                fitcFP = FP;
                maxDifferential = signalDifferential;
                
            } else {

                //If oligomerization considered
                if (oligomerization) {
                    if (FP.getOligomerization() <= fitcFP.getOligomerization()) {

                        //Signal comparison        
                        if (signalDifferential > maxDifferential) {
                            maxDifferential = signalDifferential;
                            fitcFP = FP;
                        }
                    }
                } else {

                    //Signal comparison        
                    if (signalDifferential > maxDifferential) {
                        maxDifferential = signalDifferential;
                        fitcFP = FP;
                    }
                }
            }
        }
        
        //Add this FP to measurementFilters        
        measurementFilters.put(fitcFP, laserFilter);
        addNoise(fitcFP, fpFilterSignals, filterNoise);
        
        return fitcFP;
    }
    
    //Pick the Fluorophore on the FITC channel first
    private static Fluorophore selectFluorophore (ArrayList<Fluorophore> candidateList, HashMap<Fluorophore, String> measurementFilters, HashMap<String, Double> filterNoise, HashMap<Fluorophore, HashMap<String, Double>> fpFilterSignals, boolean oligomerization) {
        
        String laserFilter = "";
        HashSet<String> ineligibleFilters = new HashSet<>(measurementFilters.values());
        
        double maxDifferential = -100000000.0;
        Fluorophore nextFP = null;

        for (int i = 0; i < candidateList.size(); i++) {

            //Initialize total signal            
            Fluorophore FP = candidateList.get(i);
            HashMap<String, Double> filterDiff = getSignalDifferential(ineligibleFilters, fpFilterSignals.get(FP), filterNoise);
            double signalDifferential = -100000000.0;
            for (Double diff : filterDiff.values()) {
                signalDifferential = diff;
            }
            String candidateLF = "";
            for (String lf : filterDiff.keySet()) {
                candidateLF = lf;
            }

            //Compare to the best signal score so far or null case
            if (nextFP == null) {
                nextFP = FP;
                laserFilter = candidateLF;
                maxDifferential = signalDifferential;

            } else {

                //If oligomerization considered
                if (oligomerization) {
                    if (FP.getOligomerization() == nextFP.getOligomerization()) {

                        //Signal comparison        
                        if (signalDifferential > maxDifferential) {
                            maxDifferential = signalDifferential;
                            nextFP = FP;
                            laserFilter = candidateLF;
                        }
                    
                    } else if (FP.getOligomerization() < nextFP.getOligomerization()) {
                        maxDifferential = signalDifferential;
                        nextFP = FP;
                        laserFilter = candidateLF;
                    }
                } else {

                    //Signal comparison        
                    if (signalDifferential > maxDifferential) {
                        maxDifferential = signalDifferential;
                        nextFP = FP;
                        laserFilter = candidateLF;
                    }
                }
            }
        }
        
        //Add this FP to measurementFilters
        measurementFilters.put(nextFP, laserFilter);
        addNoise(nextFP, fpFilterSignals, filterNoise);
        
        return nextFP;
    }    
    
    //Get the signals
    private static HashMap<Fluorophore, HashMap<String, Double>> getFPFilterSignals (ArrayList<Fluorophore> FPs, HashSet<String> laserFilters) {
        
        //Initialize signals hash
        HashMap<Fluorophore, HashMap<String, Double>> fluorophoreSignals = new HashMap<>();
        
        //Loop through each Fluorophore and determine the signal it has in each laserFilter
        for (Fluorophore FP : FPs) {
            HashMap<String, Double> signals = new HashMap<>();
            
            for (String laserFilter : laserFilters) {
                
                //Get the signal in this laserFilter                
                Double[] wavelenghts = parseLaserFilterStrings(laserFilter);
                double laser = wavelenghts[0];
                double high = wavelenghts[1];
                double low = wavelenghts[2];
                double signal = getSignal(FP, laser, high, low);
                
                signals.put(laserFilter, signal);
            }
            
            fluorophoreSignals.put(FP, signals);
        }
        
        return fluorophoreSignals;
    }
    
    //Creat laser/filter strings
    private static HashSet<String> getLaserFilterStrings(HashMap<String, ArrayList<String[]>> cytometerConfig) {
        
        //Initialize laser/filter set
        HashSet<String> laserFilters = new HashSet<>();
        
        //Loop through each laser and its filters and convert the string
        for (String laser : cytometerConfig.keySet()) {
            ArrayList<String[]> filterSets = cytometerConfig.get(laser);
            String[] laserSplit = laser.split(":");
            
            //Convert filter pairs to low and high and create laserFilter strings
            //This assumes that each filterPair has a bandpass and an optional long pass
            for (String[] filterPair : filterSets) {
                                
                //Global assumed highs on all cytometers... this is temporary and could be improved
                double high = 1000;
                double low = 250;
                
                for (String filter : filterPair) {
                    String[] filterSplit = filter.split(":");
                    
                    //Bandpass filter
                    if (filterSplit.length == 2) {
                        high = Double.valueOf(filterSplit[0]) + Double.valueOf(filterSplit[1])/2;
                        low = Double.valueOf(filterSplit[0]) - Double.valueOf(filterSplit[1])/2;
                    }
                    
                    //Long pass filter
                    if (filterSplit.length == 1) {
                        double LP = Double.valueOf(filterSplit[0]);
                        if (LP > low) {
                            low = LP;
                        }
                    }
                }
                
                String laserFilter = Double.valueOf(laserSplit[0]) + ":" + high + ":" + low;
                laserFilters.add(laserFilter);
            }
        }
        
        return laserFilters;
    }
    
    //Parse laserFilter strings
    private static Double[] parseLaserFilterStrings(String laserFilter) {
        
        String[] split = laserFilter.split(":");
        
        //Assuming the split is size three, the first number is a laser, the next is high, then low, all wavelengths
        if (split.length == 3) {
            return new Double[]{Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2])};
        } else {
            return new Double[]{};
        }
    }
    
    //Get signal for an FP given laser, high and low
    private static Double getSignal(Fluorophore FP, double laser, double high, double low) {
        
        double signal = 0.0;

        //If this protein is excited by the laser, get the excitation multiplier
        if (FP.getEx_spectrum().containsKey(laser)) {
            double excitation = FP.getBrightness() * FP.getEx_spectrum().get(laser);

            //Get the filtered signal
            for (Double wavelength : FP.getEm_spectrum().keySet()) {

                if (wavelength >= low && wavelength <= high) {
                    signal = signal + (excitation * FP.getEm_spectrum().get(wavelength));
                }
            }
        }

        return signal;
    }
    
    //Get the signal differential -> max signal in one laserFilter for a Fluorophore - sum(signals in all other filters for that Fluorophore)
    private static HashMap<String, Double> getSignalDifferential(HashSet<String> ineligibleFilters, HashMap<String, Double> laserFilterSignals, HashMap<String, Double> laserFilterNoise) {
        
        HashSet<String> eligibleFilters = new HashSet<>(laserFilterSignals.keySet());
        eligibleFilters.removeAll(ineligibleFilters);
        
        double differential;
        double maxSingleFilter = 0.0;
        double sumOtherFilters = 0.0;
        String maxLaserFilter = "";
        
        //Find the max value, which key corresponds to that
        for (String laserFilter : eligibleFilters) {
            if (laserFilterSignals.get(laserFilter) > maxSingleFilter) {
                maxSingleFilter = laserFilterSignals.get(laserFilter) - laserFilterNoise.get(laserFilter);
//                maxSingleFilter = laserFilterSignals.get(laserFilter);
                maxLaserFilter = laserFilter;
            }
        }
        
        //Sum up the rest of the filters
        for (String laserFilter : laserFilterSignals.keySet()) {
            if (!laserFilter.equals(maxLaserFilter)) {
                sumOtherFilters = sumOtherFilters + laserFilterSignals.get(laserFilter);
            }
        }
        
        differential = maxSingleFilter - sumOtherFilters;
        HashMap<String, Double> filterDiff = new HashMap<>();
        filterDiff.put(maxLaserFilter, differential);
        return filterDiff;
    }
    
    //Add noise to all other laserFilters
    private static void addNoise (Fluorophore FP, HashMap<Fluorophore, HashMap<String, Double>> fpFilterSignals, HashMap<String, Double> filterNoises) {
        
        for (String laserFilter : filterNoises.keySet()) {
            if (fpFilterSignals.containsKey(FP)) {
                filterNoises.put(laserFilter, filterNoises.get(laserFilter) + fpFilterSignals.get(FP).get(laserFilter));
            }
        }        
    }
    
    //Hard-coded cytometer at BU CosBi
    public static Cytometer getConfiguredCytometer() {
        
        //Machine lasers
        HashSet<String> lasers = new HashSet<>();
        lasers.add("405:100");
        lasers.add("488:200");
        lasers.add("514:100");
        lasers.add("561:100");
        lasers.add("637:150");
        
        //Machine filters
        HashSet<String> filters = new HashSet<>();
        filters.add("450:50");
        filters.add("472:15");
        filters.add("488:10");
        filters.add("508:6");
        filters.add("515:20");
        filters.add("525:50");
        filters.add("535:20");
        filters.add("540:15");
        filters.add("540:30");      
        filters.add("582:15");                       
        filters.add("610:20");
        filters.add("670:30");
        filters.add("695:40");
        filters.add("710:50");
        filters.add("730:45");
        filters.add("780:60");
        filters.add("495");
        filters.add("505");
        filters.add("535");
        filters.add("525");
        filters.add("555");
        filters.add("595");
        filters.add("600");
        filters.add("635");
        filters.add("685");
        filters.add("690");
        filters.add("750");
                
        HashMap<String, ArrayList<String[]>> config = new HashMap<>();
        ArrayList<String[]> filters405 = new ArrayList<>();
        filters405.add(new String[]{"450:50"});
        filters405.add(new String[]{"525:50", "505"});
        filters405.add(new String[]{"540:30", "535"});
        filters405.add(new String[]{"610:20", "600"});       
        config.put("405:100", filters405);
        
        ArrayList<String[]> filters488 = new ArrayList<>();
        filters488.add(new String[]{"488:10"});
        filters488.add(new String[]{"530:30", "505"});
        filters488.add(new String[]{"582:15", "555"});
        filters488.add(new String[]{"695:40", "685"});
        config.put("488:200", filters488);
        
        ArrayList<String[]> filters514 = new ArrayList<>();
        filters514.add(new String[]{"508:6"});
        filters514.add(new String[]{"540:15", "525"});
        config.put("514:100", filters514);
        
        ArrayList<String[]> filters561 = new ArrayList<>();
        filters561.add(new String[]{"582:15"});
        filters561.add(new String[]{"610:20", "595"});
        filters561.add(new String[]{"670:30", "635"});
        filters561.add(new String[]{"695:40", "685"});
        filters561.add(new String[]{"780:60", "750"});
        config.put("561:100", filters561);
        
        ArrayList<String[]> filters637 = new ArrayList<>();
        filters637.add(new String[]{"670:30"});
        filters637.add(new String[]{"730:45", "685"});
        config.put("637:150", filters637);
        
        Cytometer cytometer = new Cytometer("BU_CosBi_Fortessa", lasers, filters, config);
        
        return cytometer;
    }
}
