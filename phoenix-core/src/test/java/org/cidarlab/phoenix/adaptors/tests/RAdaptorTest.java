/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.IOException;
import org.cidarlab.phoenix.core.adaptors.RAdaptor;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class RAdaptorTest {
    
    //@Test
    public void getAllDirectories() throws IOException{
        String directory = "/home/prash/cidar/phoenixData/data/degradation/Degradation_102215";
        //String blank = "";
        RAdaptor.getAllData(directory);
    }
    
    //@Test
    public void runRTest(){
        System.out.println(Utilities.getFilepath());
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        String filepathR = resourceFilepath + "run.R";
        RAdaptor.runR(filepathR);
    }
    
    @Test
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
