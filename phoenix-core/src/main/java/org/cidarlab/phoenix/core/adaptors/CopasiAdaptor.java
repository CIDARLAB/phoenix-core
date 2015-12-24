/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.COPASI.*;
import org.cidarlab.phoenix.core.controller.Utilities;

/**
 *
 * @author prash
 */
public class CopasiAdaptor {
    
    public static void importSBML(String filename){
    
        try {
            
            System.load("/home/prash/cidar/phoenix-core/phoenix-core/src/main/resources/lib/libCopasiJava.so");
            //System.loadLibrary("CopasiJava");
            /*String property = System.getProperty("java.library.path");
            StringTokenizer parser = new StringTokenizer(property, ";");
            while (parser.hasMoreTokens()) {
                System.err.println(parser.nextToken());
            }*/
            System.out.println("now this");
            CCopasiRootContainer.init();
            System.out.println("Doesn't even reach here");
            if (CCopasiRootContainer.getRoot() != null) {
                System.out.println("NOT NULL!");
            }
            CCopasiDataModel model = CCopasiRootContainer.addDatamodel();
            boolean imported = false;
            try {
                imported = model.importSBML(filename);
            } catch (Exception ex) {
                Logger.getLogger(CopasiAdaptor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (imported) {
                System.out.println("SUCCESS!!");
            } else {
                System.out.println(" -.- ");
            }

        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e.getLocalizedMessage());
            System.exit(1);
        }
        //String libFilepath = Utilities.getFilepath() + "/src/main/resources/ExternalDependencies/libCopasiJava.so";
    }
    
    
}
