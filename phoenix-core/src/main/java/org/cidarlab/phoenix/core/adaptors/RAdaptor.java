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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.AssignedModule;

/**
 *
 * @author prash
 */
public class RAdaptor {
    
    
    public static void directoryWalk(String path, String resultsRoot, Map<String,String> nameMap, Map<String,AssignedModule> expexe) throws IOException{
        File root = new File( path );
        File[] list = root.listFiles();
        
        if (list == null) return;
        
        for ( File f : list ) {
            if ( f.isDirectory() ) {
                directoryWalk(f.getAbsolutePath(), resultsRoot, nameMap, expexe);
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                if(f.getName().equals("timeSeriesPlotPoints.csv")){
                    String pieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                    if(nameMap.containsKey(pieces[0])){
                        String amoduleShortName = nameMap.get(pieces[0]);
                        AssignedModule amodule = expexe.get(amoduleShortName);
                        
                        
                    }
                    //String relFilepath = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(resultsRoot) + resultsRoot.length());
                    //System.out.println( "File:" + relFilepath);
                }
                else if(f.getName().equals("mediaTitrationPlotPoints.csv")){
                    //System.out.println( "File:" + f.getAbsoluteFile() );
                }
                else if(f.getName().equals("regulationPlotPoints.csv")){
                    //System.out.println( "File:" + f.getAbsoluteFile() );
                }
                
                
            }
        }
    }
    
    public static String[] filepathPieces(String filepath, String rootFilepath){
        String relativeFilepath = filepath.substring(filepath.lastIndexOf(rootFilepath) + rootFilepath.length());
        return relativeFilepath.split("/");
    }
    
    public static Map<String,String> parseKeyMapFiles(String keyFile, String mapFile){
        Map<String,String> nameMap = new HashMap<>();
        
        Map<String,String> customNameMap = new HashMap<>();
        Set<String> shortNames = new HashSet<>();
        File key = new File(keyFile);
        File map = new File(mapFile);
        try {
            BufferedReader keyReader = new BufferedReader(new FileReader(key));
            BufferedReader mapReader = new BufferedReader(new FileReader(map));
            
            String mapLine;
            while((mapLine = mapReader.readLine())!=null){
                if(mapLine.equals("Short Name,Features,Custom Name"))
                    continue;
                String names[] = mapLine.split(",");
                shortNames.add(names[0]);
                if(names.length == 3){
                    if(!names[2].trim().equals(""))
                        customNameMap.put(names[2], names[0]);
                }
            }
            
            String keyLine;
            while((keyLine = keyReader.readLine())!=null){
                if(keyLine.equals("FILENAME,PART,CONTROL,MEDIA,TIME,REGULATION"))   
                    continue;
                String pieces[] = keyLine.split(",");
                if(!pieces[0].trim().equals("")){
                    if(shortNames.contains(pieces[1])){
                        nameMap.put(pieces[0], pieces[1]);
                    }
                    else{
                        if(customNameMap.containsKey(pieces[1])){
                            nameMap.put(pieces[0], customNameMap.get(pieces[1]));
                        }
                    }
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(nameMap);
        return nameMap;
    }
    
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
