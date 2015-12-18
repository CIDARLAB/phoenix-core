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
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;

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
                        getDegradationMetaData(amodule,f.getAbsolutePath());
                    }
                    //String relFilepath = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(resultsRoot) + resultsRoot.length());
                    //System.out.println( "File:" + relFilepath);
                }
                else if(f.getName().equals("mediaTitrationPlotPoints.csv")){
                    String pieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                    if(nameMap.containsKey(pieces[0])){
                        String amoduleShortName = nameMap.get(pieces[0]);
                        AssignedModule amodule = expexe.get(amoduleShortName);
                        getSmallMoleculeMetaData(amodule,f.getAbsolutePath());
                    }
                    //System.out.println( "File:" + f.getAbsoluteFile() );
                }
                else if(f.getName().equals("regulationPlotPoints.csv")){
                     String pieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                    if(nameMap.containsKey(pieces[0])){
                        String amoduleShortName = nameMap.get(pieces[0]);
                        AssignedModule amodule = expexe.get(amoduleShortName);
                        getRegulationMetaData(amodule,f.getAbsolutePath());
                    }
                    //System.out.println( "File:" + f.getAbsoluteFile() );
                }
                
                
            }
        }
    }
    
    
    private static void getRegulationMetaData(AssignedModule amodule, String filepath) {
        if (amodule == null) {
            return;
        }

        String expChannelName = "";
        for (PrimitiveModule pm : amodule.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                String fpName = pm.getModuleFeature().getName();
                expChannelName = Utilities.getChannelsMap().get(fpName);
                if (expChannelName == null) {
                    System.out.println("Can't find channel in the map for Expressee FP");
                    return;
                }
                if (expChannelName.equals("")) {
                    System.out.println("Need to fill out channel from cytometer in Utilities");
                    return;
                }
                expChannelName = expChannelName.replaceAll("-", ".");
            }
        }
        
        String regChannelName = "";
        for(AssignedModule control:amodule.getControlModules()){
            if(control.getRole().equals(ModuleRole.REGULATION_CONTROL)){
                for(PrimitiveModule pm:control.getSubmodules()){
                    if(pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)){
                        String fpName = pm.getModuleFeature().getName();
                        regChannelName = Utilities.getChannelsMap().get(fpName);
                        if (regChannelName == null) {
                            System.out.println("Can't find channel in the map for Regulator FP ");
                            return;
                        }
                        if (regChannelName.equals("")) {
                            System.out.println("Need to fill out channel from cytometer in Utilities");
                            return;
                        }
                        regChannelName = regChannelName.replaceAll("-", ".");
                    }
                }
            }
        }
        
        Experiment regExp = null;
        for (Experiment exp : amodule.getExperiments()) {
            if (exp.getExType().equals(ExperimentType.REGULATION)) {
                regExp = exp;
                break;
            }
        }
        if (regExp == null) {
            System.out.println("Couldn't find Regulation experiment in Assigned Module");
            return;
        }

        File file = new File(filepath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int exeRow = 0;
            int regRow = 0;
            while ((line = reader.readLine()) != null) {
                String pieces[] = line.split(",");
                if (line.toLowerCase().contains(expChannelName.toLowerCase())) {
                    for (int i = 0; i < pieces.length; i++) {
                        if (pieces[i].trim().toLowerCase().contains(expChannelName.trim().toLowerCase())) {
                            exeRow = i;
                        }
                        if (pieces[i].trim().toLowerCase().contains(regChannelName.trim().toLowerCase())) {
                            regRow = i;
                        }
                    }
                } else {
                    double exeVal = Double.parseDouble(pieces[exeRow].trim());
                    double regVal = Double.parseDouble(pieces[regRow].trim());
                    regExp.getResults().getRegulation().put(exeVal, regVal);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private static void getSmallMoleculeMetaData(AssignedModule amodule, String filepath) {
        if (amodule == null) {
            return;
        }

        String regChannelName = "";
        for(AssignedModule control:amodule.getControlModules()){
            if(control.getRole().equals(ModuleRole.REGULATION_CONTROL)){
                for(PrimitiveModule pm:control.getSubmodules()){
                    if(pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)){
                        String fpName = pm.getModuleFeature().getName();
                        regChannelName = Utilities.getChannelsMap().get(fpName);
                        if (regChannelName == null) {
                            System.out.println("Can't find channel in the map for Regulator FP ");
                            return;
                        }
                        if (regChannelName.equals("")) {
                            System.out.println("Need to fill out channel from cytometer in Utilities");
                            return;
                        }
                        regChannelName = regChannelName.replaceAll("-", ".");
                    }
                }
            }
        }
        Experiment smExp = null;
        for (Experiment exp : amodule.getExperiments()) {
            if (exp.getExType().equals(ExperimentType.SMALL_MOLECULE)) {
                smExp = exp;
                break;
            }
        }
        if (smExp == null) {
            System.out.println("Couldn't find Small Molecule experiment in Assigned Module");
            return;
        }

        List<String> lines = new ArrayList<>();
        File file = new File(filepath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int meanRow = 0;
            int stdRow = 0;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                String pieces[] = line.split(",");
                if (line.toLowerCase().contains(regChannelName.toLowerCase())) {
                    for (int i = 0; i < pieces.length; i++) {
                        if (pieces[i].trim().toLowerCase().contains(regChannelName.trim().toLowerCase()) && pieces[i].trim().toLowerCase().contains("mean")) {
                            meanRow = i;
                        }
                        if (pieces[i].trim().toLowerCase().contains(regChannelName.trim().toLowerCase()) && pieces[i].trim().toLowerCase().contains("std")) {
                            stdRow = i;
                        }
                    }
                } else {
                    int concVal = Integer.parseInt(pieces[0].trim());
                    double meanVal = Double.parseDouble(pieces[meanRow].trim());
                    double stdVal = Double.parseDouble(pieces[stdRow].trim());
                    smExp.getResults().getMeanInduction().put((double) concVal, meanVal);
                    smExp.getResults().getStdInduction().put((double) concVal, stdVal);
                    
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private static void getDegradationMetaData(AssignedModule amodule, String filepath) {
        if (amodule == null) {
            return;
        }

        String channelName = "";
        for (PrimitiveModule pm : amodule.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION)) {
                String fpName = pm.getModuleFeature().getName();
                channelName = Utilities.getChannelsMap().get(fpName);
                if (channelName == null) {
                    System.out.println("Can't find channel in the map");
                    return;
                }
                if (channelName.equals("")) {
                    System.out.println("Need to fill out channel from cytometer in Utilities");
                    return;
                }
                channelName = channelName.replaceAll("-", ".");
            }
        }
        Experiment degExp = null;
        for (Experiment exp : amodule.getExperiments()) {
            if (exp.getExType().equals(ExperimentType.DEGRADATION)) {
                degExp = exp;
                break;
            }
        }
        if (degExp == null) {
            System.out.println("Couldn't find Degradation experiment in Assigned Module");
            return;
        }

        File file = new File(filepath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int meanRow = 0;
            int stdRow = 0;
            while ((line = reader.readLine()) != null) {
                String pieces[] = line.split(",");
                if (line.toLowerCase().contains(channelName.toLowerCase())) {
                    for (int i = 0; i < pieces.length; i++) {
                        if (pieces[i].trim().toLowerCase().contains(channelName.trim().toLowerCase()) && pieces[i].trim().toLowerCase().contains("mean")) {
                            meanRow = i;
                        }
                        if (pieces[i].trim().toLowerCase().contains(channelName.trim().toLowerCase()) && pieces[i].trim().toLowerCase().contains("std")) {
                            stdRow = i;
                        }
                    }
                } else {
                    int timeVal = Integer.parseInt(pieces[0].trim());
                    double meanVal = Double.parseDouble(pieces[meanRow].trim());
                    double stdVal = Double.parseDouble(pieces[stdRow].trim());
                    degExp.getResults().getMeanTimeSeries().put((double) timeVal, meanVal);
                    degExp.getResults().getStdTimeSeries().put((double)timeVal, stdVal);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String[] filepathPieces(String filepath, String rootFilepath){
        String relativeFilepath = filepath.substring(filepath.lastIndexOf(rootFilepath) + rootFilepath.length());
        return relativeFilepath.split("/");
    }
    
    public static Map<String,String> parseKeyMapFiles(String mapFile){
        Map<String,String> nameMap = new HashMap<>();
        
        Map<String,String> customNameMap = new HashMap<>();
        Set<String> shortNames = new HashSet<>();
        File map = new File(mapFile);
        try {
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
                else{
                    customNameMap.put(names[0], names[0]);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(customNameMap);
        return customNameMap;
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
