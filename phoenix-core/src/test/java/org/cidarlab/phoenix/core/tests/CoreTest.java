/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.io.File;
import java.util.HashSet;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.dom.Module;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author evanappleton
 */
public class CoreTest {
    
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
    
    public String getFilepath()
    {
        String filepath = PhoenixController.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    //Test with existing feature library, plasmid library, spectra file and specification
//    @Test
    public void coreTest() throws Exception {
        
        //Upload data from back end
        File featureLib = new File(getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb");
        File plasmidLib = new File(getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_6315.gb");        
        File fluorophoreSpectra = new File(getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv");
        File cytometer = new File(getFilepath() + "/src/main/resources/FluorescentProteins/cosbi_fortessa_bd.csv");
//        PhoenixController.preliminaryDataUpload (featureLib, plasmidLib, fluorophoreSpectra, cytometer);
        
        //Run a design decomposition
        File structureFile = new File(getFilepath() + "/src/main/resources/miniEugeneFiles/toggle-switch-full.eug");
        HashSet<Module> modulesToTest = PhoenixController.initializeDesign(structureFile, null);
        PhoenixController.createExperimentInstructions(modulesToTest, getFilepath() + "/src/main/resources/InstructionFiles");
    }
    
    //Main testing class
    public static void main(String[] args) throws Exception {
        CoreTest cT = new CoreTest();
        cT.coreTest();
    }

    //Constructor
    public CoreTest() {
    }
    
}
