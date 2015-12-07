/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.controller.Utilities;

/**
 *
 * @author prash
 */
public class RAdaptor {
    
    
    public static void walk(String path) throws IOException{
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                if(f.getName().equals("timeSeriesPlotPoints.csv")){
                    System.out.println( "File:" + f.getAbsoluteFile() );
                }
                else if(f.getName().equals("mediaTitrationPlotPoints.csv")){
                    System.out.println( "File:" + f.getAbsoluteFile() );
                }
                else if(f.getName().equals("regulationPlotPoints.csv")){
                    System.out.println( "File:" + f.getAbsoluteFile() );
                }
                
                
            }
        }
    }
    
    /*public static String findTimeSeriesPlotPointsCSV(String directoryPath) throws IOException{
        File node = new File(directoryPath);
        String filePath = "";
        File[] fileList = node.listFiles();
        
        for(int i=0;i<fileList.length;i++){
            //System.out.println("FLiFTSPP :: " + fileList[i].isDirectory()+ " :: " + fileList[i].getName());
            if(fileList[i].getName().equals("timeSeriesPlotPoints.csv")){
                //System.out.println("Found it :: " + fileList[i].getCanonicalPath());
                return fileList[i].getCanonicalPath();
            }
            else{
                if(fileList[i].isDirectory()){
                    return findTimeSeriesPlotPointsCSV(fileList[i].getAbsolutePath());
                }
            }           
        }
        return "";
    }*/
    
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
