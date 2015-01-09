/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.util.HashMap;
import java.util.HashSet;
import org.cidarlab.phoenix.core.controller.FluorescentProteinSelector;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Fluorophore;

/**
 *
 * @author evanappleton
 */
public class FluorescentProteinSelectorTest {
    
    //Constructor
    public FluorescentProteinSelectorTest() {
        
    }
    
    //Hard-coded FP set for testing
    public HashSet<Fluorophore> getFluorophoreSet() {
        
        //Test set of FPs
        HashSet<Fluorophore> FPs = new HashSet<>();
        FPs.add(new Fluorophore("CyPet", 2, 53.0, 435.0, 477.0));
        FPs.add(new Fluorophore("wtGFP", 2, 100.0, 501.0, 511.0));
        FPs.add(new Fluorophore("DsRed", 4, 176.0, 558.0, 583.0));
        FPs.add(new Fluorophore("mRFP1", 1, 44.0, 584.0, 607.0));
        FPs.add(new Fluorophore("E2-Crimson", 4, 59.0, 611.0, 646.0));
        
        return FPs;
    }
    
    //Hard-coded cytometer at BU CosBi
    public Cytometer getConfiguredCytometer() {
        
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
        filters.add("508:10");
        filters.add("525:50");
        filters.add("540:60");
        filters.add("488:10");
        filters.add("530:30");
        filters.add("582:12");
        filters.add("695:30");
        filters.add("540:20");
        filters.add("512:12");
        filters.add("610:20");
        filters.add("670:30");
        filters.add("695:40");
        filters.add("780:60");
        filters.add("710:50");
        
        HashMap<String, String> config = new HashMap<>();
        config.put("405:100", "450:50");
        config.put("405:100", "508:10");
        config.put("405:100", "525:50");
        config.put("405:100", "540:60");
        config.put("488:200", "488:10");
        config.put("488:200", "530:30");
        config.put("488:200", "582:12");
        config.put("488:200", "695:30");
        config.put("514:100", "508:10");
        config.put("514:100", "540:20");
        config.put("561:100", "582:12");
        config.put("561:100", "610:20");
        config.put("561:100", "670:30");
        config.put("561:100", "695:40");
        config.put("561:100", "780:60");
        config.put("637:150", "670:30");
        config.put("637:150", "710:50");
        
        Cytometer cytometer = new Cytometer("BU_CosBi_Fortessa", lasers, filters, config);
        
        return cytometer;
    }
    
    //Hard-coded BU CosBi cytometer with filter-laser configuration unsolved
    public Cytometer getUnconfiguredCytometer() {
        
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
        filters.add("508:10");
        filters.add("525:50");
        filters.add("540:60");
        filters.add("488:10");
        filters.add("530:30");
        filters.add("582:12");
        filters.add("695:30");
        filters.add("540:20");
        filters.add("512:12");
        filters.add("610:20");
        filters.add("670:30");
        filters.add("695:40");
        filters.add("780:60");
        filters.add("710:50");
        
        Cytometer cytometer = new Cytometer("BU_CosBi_Fortessa_Unconfigured", lasers, filters, new HashMap<String, String>());
        
        return cytometer;
    }
    
    //Runs test on FP Selector algorithm to get 2 FPs with configured cytometer
    public void getTwoFPTest() {        
        
        HashSet<Fluorophore> FPs = getFluorophoreSet();
        Cytometer cytometer = getConfiguredCytometer();
        HashSet<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, cytometer, 2);
        
    }
    
    //Runs test on FP Selector algorithm to get 3 FPs with configured cytometer
    public void getThreeFPTest() {        
        
        HashSet<Fluorophore> FPs = getFluorophoreSet();
        Cytometer cytometer = getConfiguredCytometer();
        HashSet<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, cytometer, 3);
        
    }
    
    //Main testing class
    public static void main(String[] args) {
        
        FluorescentProteinSelectorTest fpst = new FluorescentProteinSelectorTest();
        fpst.getTwoFPTest();
    }
    
}
