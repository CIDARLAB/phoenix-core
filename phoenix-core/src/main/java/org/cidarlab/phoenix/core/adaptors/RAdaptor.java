/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Experiment.ExperimentType;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;

/**
 *
 * @author prash
 */
public class RAdaptor {
    
    
    public static void createRunRScript(String runRFilepath, String keyFilepath, String AnalyzeRFilepath, String dataFilepath, String wd, int minEvents){
        
        File runFile = new File(runRFilepath);
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(runFile);
            writer = new BufferedWriter(fw);
            
            writer.write("#!/usr/bin/Rscript");
            writer.newLine();
            writer.write("minEvents <- "+minEvents);
            writer.newLine();
            writer.write("superWD <- \""+wd+"\"");
            writer.newLine();
            writer.write("wd <- superWD");
            writer.newLine();
            writer.write("dataPath <- \""+dataFilepath+"\"");
            writer.newLine();
            writer.write("key <- read.csv(\""+keyFilepath+"\", header = TRUE)");
            writer.newLine();
            writer.write("source(\""+AnalyzeRFilepath+"\", chdir=T)");
            writer.close();
            //Convert file to an executable
            StringBuilder commandBuilder = new StringBuilder("chmod 755 "+ runRFilepath);       
            String command = commandBuilder.toString();
        
            Runtime runtime = Runtime.getRuntime();
            Process proc = null;
            proc = runtime.exec(command);
            proc.waitFor();
            
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void runR(String filepath){
        
        System.out.println("Starting Run R");
        System.out.println("Filepath :: "+filepath);
        StringBuilder commandBuilder = null;
        
        //commandBuilder = new StringBuilder("R CMD BATCH " +filepath);
        //commandBuilder = new StringBuilder("Rscript "+filepath);
        //commandBuilder = new StringBuilder("R < " +filepath+ " --no-save");
        commandBuilder = new StringBuilder(filepath);
        
        String command = commandBuilder.toString();
        String resourceFilepath = Utilities.getFilepath() + "/src/main/resources/RTest/";
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        System.out.println("Command ::\n"+command);
        try {
            
            proc = runtime.exec(command);
            proc.waitFor();
            InputStream in = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*try {
            InputStream in = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
    
}
