/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import org.cidarlab.phoenix.core.adaptors.BenchlingAdaptor;

/**
 *
 * @author evanappleton
 */
public class BenchlingAdaptorTest {
    
    //Testing object initializations
    public BenchlingAdaptorTest() {
        // some initializations may come
        // along the way
    }

    //Run Genbank Parsing Test
    public void genbankParseTest() throws Exception {
        String filePath = "/Users/evanappleton/phoenix/phoenix-core/phoenix-core/src/main/resources/BenchlingGenbankFiles/benchling_export_120514_multi.gb";
        File toLoad = new File(filePath);
        BenchlingAdaptor.readGenbankFileBiojava(toLoad); 
    }

    /**
     * MAIN CLASS
     */
    public static void main(String[] args) throws Exception {

        BenchlingAdaptorTest bat = new BenchlingAdaptorTest();
        bat.genbankParseTest();
        
    }
    
    
    
}
