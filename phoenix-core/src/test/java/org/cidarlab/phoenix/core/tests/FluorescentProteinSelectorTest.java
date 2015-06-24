/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.controller.Args;
import org.cidarlab.phoenix.core.controller.FluorescentProteinSelector;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author evanappleton
 */
public class FluorescentProteinSelectorTest {   
    
    @BeforeClass
    public static void setUpBeforeClass()
            throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass()
            throws Exception {
    }

    @Before
    public void setUp()
            throws Exception {
    }

    @After
    public void tearDown()
            throws Exception {
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
        
        Cytometer cytometer = new Cytometer("BU_CosBi_Fortessa_Unconfigured", lasers, filters, new HashMap<String, ArrayList<String[]>>());
        
        return cytometer;
    }
    
    //Runs test on FP Selector algorithm to get 1 FP with configured cytometer
//    @Test
    public void getOneFPTest() {        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject);
        
        Map cytometerQuery = new HashMap();
        cytometerQuery.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        HashSet<Cytometer> cytometers = ClothoAdaptor.queryCytometers(cytometerQuery,clothoObject);
        for (Cytometer c : cytometers) {
            FluorescentProteinSelector.solve(FPs, c, 1); 
        }
        
        conn.closeConnection();
    }
    
    //Runs test on FP Selector algorithm to get n FPs with configured cytometer
//    @Test
    public void getMultiFPTest() {        
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        HashSet<Fluorophore> FPs = ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject);
        
        Map cytometerQuery = new HashMap();
        cytometerQuery.put("schema", "org.cidarlab.phoenix.core.dom.Cytometer");
        HashSet<Cytometer> cytometers = ClothoAdaptor.queryCytometers(cytometerQuery,clothoObject);
        for (Cytometer c : cytometers) {
            FluorescentProteinSelector.solve(FPs, c, 5); 
        }
        
        conn.closeConnection();
    }
    
//    //Main testing class
//    public static void main(String[] args) {
//        
//        FluorescentProteinSelectorTest fpst = new FluorescentProteinSelectorTest();
//        fpst.getOneFPTest();
//    }
//    
//    //Constructor
//    public FluorescentProteinSelectorTest() {
//        
//    }    
}
