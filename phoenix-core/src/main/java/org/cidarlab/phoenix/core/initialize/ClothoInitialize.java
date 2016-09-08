/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.initialize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.ClothoAdaptor;
import org.cidarlab.phoenix.core.controller.Args;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.dom.Feature;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 *
 * @author prash
 */
public class ClothoInitialize {
    
    public String getFilepath()
    {
        String filepath="";       
        filepath = PhoenixController.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("/target/"));
        return filepath;
    }
    
    public void featureUpload() {
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);
        
        String filePath = getFilepath() + "/src/main/resources/BenchlingGenbankFiles/phoenix_feature_lib.gb";
        File toLoad = new File(filePath);
        try {
//            ClothoAdaptor.queryFeatures();
            Map featureQuery = new HashMap();
            featureQuery.put("schema", Feature.class.getCanonicalName());
        
            ClothoAdaptor.uploadSequences(toLoad, true,clothoObject);
            ClothoAdaptor.queryFeatures(featureQuery,clothoObject);
        } catch (Exception ex) {
            Logger.getLogger(ClothoInitialize.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        String fluorfilePath = getFilepath() + "/src/main/resources/FluorescentProteins/fp_spectra.csv";
        File toFluorLoad = new File(fluorfilePath);
        try {
            ClothoAdaptor.uploadFluorescenceSpectrums(toFluorLoad,clothoObject);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClothoInitialize.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClothoInitialize.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        conn.closeConnection();
    }
    
    
    public static void main(String[] args) {
        ClothoInitialize init = new ClothoInitialize();
        init.featureUpload();
        
    }
    
}
