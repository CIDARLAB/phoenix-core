/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;

/**
 *
 * @author prash
 */
public class SBMLAdaptorTest {
    
    public SBMLAdaptorTest() {
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

    @Test
    public void testReplaceParamsLocalToGlobal(){
        String sbmlDocOld = Utilities.getResourcesFilepath() + "sbmlTest/sbmlTest.xml";
        String sbmlDocNew = Utilities.getResourcesFilepath() + "sbmlTest/sbmlTestResult.xml";
        SBMLDocument newDoc = SBMLAdaptor.convertParamsLocalToGlobal(sbmlDocOld);
        SBMLWriter writer = new SBMLWriter();
        try {
            writer.write(newDoc, sbmlDocNew);
            System.out.println(writer.writeSBMLToString(newDoc));
        } catch (XMLStreamException ex) {
            Logger.getLogger(SBMLAdaptorTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SBMLAdaptorTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SBMLException ex) {
            Logger.getLogger(SBMLAdaptorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        System.out.println("New Doc is ::\n");
    }
    
}
