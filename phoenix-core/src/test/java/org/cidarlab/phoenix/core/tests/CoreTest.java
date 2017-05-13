/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dataprocessing.AnalyzeData;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Module;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        File plasmidLib = new File(getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_72715.gb");        
        File fluorophoreSpectra = new File(getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv");
        File cytometer = new File(getFilepath() + "/src/main/resources/FluorescentProteins/cosbi_fortessa_bd.csv");
//        PhoenixController.preliminaryDataUpload (featureLib, plasmidLib, fluorophoreSpectra, cytometer);
        
        //Run a design decomposition
        File structureFile = new File(getFilepath() + "/src/main/resources/miniEugeneFiles/inverter.eug");
        Module bestModule = PhoenixController.initializeDesign(structureFile, null);
        
        PhoenixController.createExperimentInstructions(bestModule, getFilepath() + "/src/main/resources/InstructionFiles");
        
        
        //Assign SBML Documents
//        PhoenixController.assignSBMLDocuments(bestModule);
//        
//        String filepathSBML = getFilepath() + "/src/main/resources/sbmlDocs";
//        //Create SBML Documents
//        PhoenixController.createAllSBMLfiles(bestModule, filepathSBML);
        
    }
    
    public void completeCoreTestSimulation() throws Exception{
        File featureLib = new File(getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb");
        File plasmidLib = new File(getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_plasmid_lib_72715.gb");        
        File fluorophoreSpectra = new File(getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv");
        File cytometer = new File(getFilepath() + "/src/main/resources/FluorescentProteins/cosbi_fortessa_bd.csv");
        
        File structureFile = new File(getFilepath() + "/src/main/resources/miniEugeneFiles/inverter.eug");
        Module bestModule = PhoenixController.initializeDesign(structureFile, null);
        
        PhoenixController.createExperimentInstructions(bestModule, getFilepath() + "/src/main/resources/InstructionFiles");
        
        
        //Assign SBML Documents
//        PhoenixController.assignSBMLDocuments(bestModule);
        
        String filepathSBML = getFilepath() + "/src/main/resources/sbmlDocs";
        //Create SBML Documents
        //PhoenixController.createAllSBMLfiles(bestModule, filepathSBML);
        
        //-------Part 1 is complete.
        //-------Part 2 gets values for SBML Files from Results and a Filled out Name map.
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        String directory = resourceFilepath + "results/";
        String filepath = Utilities.getFilepath() + "/src/main/resources/InstructionFiles/";
        String keyFile = filepath + "testingInstructionsTest.csv";
        String mapFile = filepath + "nameMapFileTest.csv";
        String mapFileFilled = filepath + "nameMapFileTest_ea_filled.csv";
        AnalyzeData.fillOutNameMap(mapFileFilled, mapFile);
        Map<String,String> nameMap = AnalyzeData.parseKeyMapFiles(mapFile);
        
        Map<String,AssignedModule> expexe = new HashMap<String,AssignedModule>();
        expexe = PhoenixController.getShortNameEXPEXEMap(bestModule);
        System.out.println(expexe);
        System.out.println("=======================================\n\n");
        
        
        System.out.println(nameMap);
        System.out.println("=======================================\n\n");
        
        AnalyzeData.directoryWalk_old(directory,directory,filepathSBML,nameMap,expexe);
        
        
        
    }
    
    
    //@Test
    public void testFileCopy(){
        String filepath = Utilities.getFilepath() + "/src/main/resources/InstructionFiles/";
        String mapFileFilled = filepath + "nameMapFileTest_ea_filled.csv";
        String mapFileEmpty = filepath + "nameMapFileTestBackUP.csv";
        AnalyzeData.fillOutNameMap(mapFileFilled, mapFileEmpty);
        
    }
    
    //Main testing class
    public static void main(String[] args) throws Exception {
        CoreTest cT = new CoreTest();
        //cT.coreTest();
        cT.completeCoreTestSimulation();
    }

    //Constructor
    public CoreTest() {
    }
    
}
