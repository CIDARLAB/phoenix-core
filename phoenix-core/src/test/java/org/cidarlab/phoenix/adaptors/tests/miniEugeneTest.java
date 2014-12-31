/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.io.IOException;
import org.cidarlab.minieugene.exception.MiniEugeneException;
import org.cidarlab.phoenix.core.adaptors.EugeneAdaptor;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author evanappleton
 */
public class miniEugeneTest {
    
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
        String filepath="";
        
        filepath = PhoenixController.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    
    //Test for oriented TUs example on miniEugene site
    @Test
    public void orientedTUTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = getFilepath() + "/src/main/resources/miniEugeneFiles/oriented-tus.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, null);
        EugeneAdaptor.getStructures(input, 1);
    }
    
    //Test for toggle switches example on miniEugene site
    @Test
    public void toggleTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = getFilepath() + "/src/main/resources/miniEugeneFiles/toggle-switch.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, null);
        EugeneAdaptor.getStructures(input, 1);        
    }
    
    
    //Test for NOR gate example on miniEugene site
    @Test
    public void norGateTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = getFilepath() + "/src/main/resources/miniEugeneFiles/nor-gate-templates.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, null);
        EugeneAdaptor.getStructures(input, 1);
    }
    
    //Test for priority encoder example on miniEugene site
    //This test is fairly extensive, comment out now for memory concerns
//    @Test
    public void priorityEncoderTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = getFilepath() + "/src/main/resources/miniEugeneFiles/priority-encoder.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, 1);        
    }
}
