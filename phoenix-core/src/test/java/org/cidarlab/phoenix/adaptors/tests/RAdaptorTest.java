/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.IOException;
import org.cidarlab.phoenix.core.adaptors.RAdaptor;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class RAdaptorTest {
    
    @Test
    public void getAllDirectories() throws IOException{
        String directory = "/home/prash/cidar/phoenixData/data/degradation/Degradation_102215";
        //String blank = "";
        RAdaptor.getAllData(directory);
    }
    
}
