/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.cidarlab.phoenix.core.adaptors.IBioSimAdaptor;
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
public class IBioSimAdaptorTest {
    
    public IBioSimAdaptorTest() {
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
    //@Test
    public void testParameterEstimation() throws IOException, XMLStreamException {
        System.out.println("parameterEstimation");
        String sbml = Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degrade.xml";
	List<String> params = new ArrayList<String>();
	params.add("deg_degradation___y");
	params.add("deg_degradation___K_d");
	List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degrade.csv");
	String results = IBioSimAdaptor.estimateParameters(sbml, params, experimentFiles);
        System.out.println(results);
    }
    
}
