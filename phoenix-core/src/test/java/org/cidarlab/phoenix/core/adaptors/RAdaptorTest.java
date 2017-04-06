/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.IOException;
import java.util.Map;
import org.cidarlab.phoenix.core.adaptors.RAdaptor;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dataprocessing.AnalyzeData;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class RAdaptorTest {
    
    @Test
    public void getAllDirectories() throws IOException{
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        String directory = resourceFilepath + "results/";
        String filepath = Utilities.getFilepath() + "/src/main/resources/InstructionFiles/";
        String keyFile = filepath + "testingInstructionsTest.csv";
        String mapFile = filepath + "nameMapFileTest.csv";
        Map<String,String> nameMap = AnalyzeData.parseKeyMapFiles(mapFile);
        
        AnalyzeData.directoryWalk(directory,directory,"",nameMap,null);
        
    }
    
    //@Test
    public void runRTest(){
        System.out.println(Utilities.getFilepath());
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        String filepathR = resourceFilepath + "run.R";
        RAdaptor.runR(filepathR);
    }
    
    //@Test
    public void parseKeyMapFilesTest(){
       String filepath = Utilities.getFilepath() + "/src/main/resources/InstructionFiles/";
       String keyFile = filepath + "testingInstructionsTest.csv";
       String mapFile = filepath + "nameMapFileTest_ea_filled.csv";
       AnalyzeData.parseKeyMapFiles(mapFile);
       
    }
    
    
    
    
    
    //@Test
    public void createRunRTest(){
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        String filepathR = resourceFilepath + "run.R";
        String keyfilepath = resourceFilepath + "key_EXPRESSEE_regulation_degradation_111715_edit.csv";
        //String keyfilepath = "key_EXPRESSEE_regulation_degradation_111715_edit.csv";
        //String dataFilepath = resourceFilepath + "data";
        String dataFilepath = resourceFilepath + "data";

        String cytometryFilepath = Utilities.getCytometryFilepath();
        String resultsFilepath = resourceFilepath + "results";
        cytometryFilepath += "/cytometry_analysis/master_analytics_scripts/analyze.R";
        
        RAdaptor.createRunRScript(filepathR, keyfilepath, cytometryFilepath, dataFilepath,resultsFilepath,1000);
        System.out.println("End of Run Script");
        RAdaptor.runR(filepathR);
    }
    
}
