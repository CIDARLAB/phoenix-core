/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import org.cidarlab.phoenix.core.adaptors.CopasiAdaptor;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prash
 */
public class CopasiAdaptorTest {
    
    public CopasiAdaptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of importSBML method, of class CopasiAdaptor.
     */
    @Test
    public void testImportSBML() {
        System.out.println("importSBML");
        String filepath = Utilities.getFilepath() + "/src/main/resources/copasiTest/exp_0_0_0_2_1.xml";
        CopasiAdaptor.importSBML(filepath);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
