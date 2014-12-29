/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.cidarlab.minieugene.dom.Component;
import org.cidarlab.minieugene.exception.MiniEugeneException;
import org.cidarlab.phoenix.core.adaptors.EugeneAdaptor;

/**
 *
 * @author evanappleton
 */
public class miniEugeneTest {
    
    //Initialize miniEugene test object
    public void miniEugeneTest() {
        
    }
    
    //Test for oriented TUs example on miniEugene site
    public void orientedTUTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/miniEugeneFiles/oriented-tus.eug";
        File input = new File(filePath);
        
        List<Component[]> allStructures = EugeneAdaptor.getStructures(input, null);
        List<Component[]> oneStructure = EugeneAdaptor.getStructures(input, 1);
        
    }
    
    //Test for toggle switches example on miniEugene site
    public void toggleTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/miniEugeneFiles/toggle-switch.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, null);
        
    }
    
    
    //Test for NOR gate example on miniEugene site
    public void norGateTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/miniEugeneFiles/nor-gate-templates.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, null);
        
    }
    
    //Test for priority encoder example on miniEugene site
    public void priorityEncoderTest() throws IOException, MiniEugeneException {
        
        //Upload test file
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/miniEugeneFiles/priority-encoder.eug";
        File input = new File(filePath);
        
        EugeneAdaptor.getStructures(input, 1);
        
    }
    
    //Run miniEugene tests
    public static void main (String[] args) throws IOException, MiniEugeneException {
        
        miniEugeneTest test = new miniEugeneTest();
        test.orientedTUTest();
        test.toggleTest();
        test.norGateTest();
        test.priorityEncoderTest();
        
        String stop = "";
    } 
    
}
