/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
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
        
        FPs.add(new Fluorophore("CyPet", 2, 53.0, 435.0, 477.0, new HashMap<Double, Double>()));
        FPs.add(new Fluorophore("wtGFP", 2, 100.0, 501.0, 511.0, new HashMap<Double, Double>()));
        FPs.add(new Fluorophore("DsRed", 4, 176.0, 558.0, 583.0, new HashMap<Double, Double>()));
        FPs.add(new Fluorophore("mRFP1", 1, 44.0, 584.0, 607.0, new HashMap<Double, Double>()));
        FPs.add(new Fluorophore("E2-Crimson", 4, 59.0, 611.0, 646.0, new HashMap<Double, Double>()));
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
                
        HashMap<String, String[]> config = new HashMap<>();
        config.put("405:100", new String[]{"450:50"});
        config.put("405:100", new String[]{"525:50", "505"});
        config.put("405:100", new String[]{"540:30", "535"});        
        config.put("405:100", new String[]{"610:20", "600"});
        
        config.put("488:200", new String[]{"488:10"});
        config.put("488:200", new String[]{"540:30", "535"});
        config.put("488:200", new String[]{"582:15", "555"});
        config.put("488:200", new String[]{"695:40", "685"});
        
        config.put("514:100", new String[]{"508:6"});
        config.put("514:100", new String[]{"540:15", "525"});
        
        config.put("561:100", new String[]{"582:15"});
        config.put("561:100", new String[]{"610:20", "595"});
        config.put("561:100", new String[]{"670:30", "635"});
        config.put("561:100", new String[]{"695:40", "685"});
        config.put("561:100", new String[]{"780:60", "750"});
        
        config.put("637:150", new String[]{"670:30"});
        config.put("637:150", new String[]{"730:45", "685"});
        
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
        
        Cytometer cytometer = new Cytometer("BU_CosBi_Fortessa_Unconfigured", lasers, filters, new HashMap<String, String[]>());
        
        return cytometer;
    }
    
    //Runs test on FP Selector algorithm to get 1 FP with configured cytometer
    public void getOneFPTest() {        
        
        //        HashSet<Fluorophore> FPs = getFluorophoreSet();
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();
        Cytometer cytometer = getConfiguredCytometer();
        ArrayList<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, cytometer, 2);
        
    }
    
    //Runs test on FP Selector algorithm to get 2 FPs with configured cytometer
    public void getTwoFPTest() {        
        
        //        HashSet<Fluorophore> FPs = getFluorophoreSet();
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();
        Cytometer cytometer = getConfiguredCytometer();
        ArrayList<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, cytometer, 2);
        
    }
    
    //Runs test on FP Selector algorithm to get 3 FPs with configured cytometer
    public void getThreeFPTest() {        
        
        //        HashSet<Fluorophore> FPs = getFluorophoreSet();
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores();
        Cytometer cytometer = getConfiguredCytometer();
        ArrayList<Fluorophore> solve = FluorescentProteinSelector.solve(FPs, cytometer, 3);
        
    }
    
    //Main testing class
    public static void main(String[] args) {
        
        FluorescentProteinSelectorTest fpst = new FluorescentProteinSelectorTest();
        fpst.getOneFPTest();
    }
    
}
