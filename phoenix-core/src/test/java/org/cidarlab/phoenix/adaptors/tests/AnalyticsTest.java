/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

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
public class AnalyticsTest {
    
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
    
//    @Test
    public void testUploadKeyFile() {
        
    }
    
//    @Test
    public void testCreateKeyFile() {
        
    }
    
    public static void main (String[] args) {
        AnalyticsTest at = new AnalyticsTest();
        at.testUploadKeyFile();
    }
}
