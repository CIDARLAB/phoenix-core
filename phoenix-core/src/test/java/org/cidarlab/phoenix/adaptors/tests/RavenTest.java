/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.util.Arrays;
import java.util.List;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.controller.Args;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.dom.AssemblyParameters;
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
public class RavenTest {
    
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
    
    @Test
    public void testParameterUpload() {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        
        String[] efficiency = new String[]{"1.0", "1.0", "1.0", "1.0", "1.0"};
        List<String> effArray = Arrays.asList(efficiency);
        AssemblyParameters assmP = new AssemblyParameters();
        assmP.setEfficiency(effArray);
        assmP.setMethod("moclo");
        assmP.setOligoNameRoot("phoenix");
        assmP.setName("default");
        
        ClothoAdaptor.createAssemblyParameters(assmP,clothoObject);
        conn.closeConnection();
    }
//    
//    public static void main (String[] args) {
//        RavenTest rt = new RavenTest();
//        rt.testParameterUpload();
//    }
}
