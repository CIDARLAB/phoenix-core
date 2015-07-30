/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.servlets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author zchapasko
 */
public class ServletIO {
    
    public static String getJSONFilepath()
    {
        String filepath = ServletIO.class.getClassLoader().getResource(".").getPath();
        filepath = filepath.substring(0,filepath.indexOf("WEB-INF/classes/"));
        filepath += "flare.json";
        return filepath;
    }
    
    public static void writeUpdatedJSON(String content) {
	try { 
            File file = new File(getJSONFilepath());
 
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
