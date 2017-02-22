/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dataprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.phoenix.core.adaptors.RAdaptor;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;

/**
 *
 * @author prash
 */
public class AnalyzeData {
    
    public static void directoryWalk(String path, String resultsRoot, String sbmldirectory, Map<String,String> nameMap, Map<String,AssignedModule> expexe) throws IOException{
        File root = new File( path );
        File[] list = root.listFiles();
        
        if (list == null) return;
        
        for ( File f : list ) {
            if ( f.isDirectory() ) {
                directoryWalk(f.getAbsolutePath(), resultsRoot,sbmldirectory, nameMap, expexe);
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                if(f.getName().equals("timeSeriesPlotPoints.csv")){
                    String pieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                    //System.out.println("Pieces :: " + pieces[0] );
                    for(String nameMapKey : nameMap.keySet()){
                        if(pieces[0].toLowerCase().contains(nameMapKey.toLowerCase())){
                            
                            //System.out.println("f abs path :: " + f.getAbsolutePath());
                            System.out.println("pieces[0] :: " + pieces[0].toLowerCase());
                            System.out.println("nameMapKey :: " + nameMapKey.toLowerCase());
                            
                            String amoduleShortName = nameMap.get(nameMapKey);
                            System.out.println("A Module Short Name :: " + amoduleShortName);
                            AssignedModule amodule = expexe.get(amoduleShortName);
                            List<String> csvLines = getDegradationMetaData(amodule,f.getAbsolutePath());
                            //ParameterEstimation.estimateDegradationParameters(amodule,sbmldirectory, csvLines);
                            
                        }
                    }
                    if(nameMap.containsKey(pieces[0])){
                        //Ideally, this is how it should be. The above method adds additional complexity. need to figure out naming convention.
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
            if (pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT_FUSION)) {
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
            if(control.getRole().equals(Module.ModuleRole.REGULATION_CONTROL)){
                for(PrimitiveModule pm:control.getSubmodules()){
                    if(pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT_FUSION)){
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
            if (exp.getExType().equals(Experiment.ExperimentType.REGULATION)) {
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
            if(control.getRole().equals(Module.ModuleRole.REGULATION_CONTROL)){
                for(PrimitiveModule pm:control.getSubmodules()){
                    if(pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT) || pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT_FUSION)){
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
            if (exp.getExType().equals(Experiment.ExperimentType.SMALL_MOLECULE)) {
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
    
    
    private static List<String> getDegradationMetaData(AssignedModule amodule, String filepath) {
        List<String> csvLines = new ArrayList<String>();
        //System.out.println("Going to get Degradation Data");
        if (amodule == null) {
            return null;
        }
        //System.out.println("Reached here?");
        
        String channelName = "";
        for (PrimitiveModule pm : amodule.getSubmodules()) {
            if (pm.getPrimitiveRole().equals(Feature.FeatureRole.CDS_FLUORESCENT_FUSION)) {
                String fpName = pm.getModuleFeature().getName();
                channelName = Utilities.getChannelsMap().get(fpName);
                if (channelName == null) {
                    System.out.println("Can't find channel in the map");
                    return null;
                }
                if (channelName.equals("")) {
                    System.out.println("Need to fill out channel from cytometer in Utilities");
                    return null;
                }
                channelName = channelName.replaceAll("-", ".");
                //System.out.println("Channel name :: " + channelName);
            }
        }
        Experiment degExp = null;
        for (Experiment exp : amodule.getExperiments()) {
            if (exp.getExType().equals(Experiment.ExperimentType.DEGRADATION)) {
                degExp = exp;
                break;
            }
        }
        if (degExp == null) {
            System.out.println("Couldn't find Degradation experiment in Assigned Module");
            return null;
        }
        
        int colIndex = 0;
        
        
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
                    //System.out.println("Time Val :: " + timeVal);
                    double meanVal = Double.parseDouble(pieces[meanRow].trim());
                    double stdVal = Double.parseDouble(pieces[stdRow].trim());
                    //System.out.println("meanVal :: " + meanVal);
                    //System.out.println("stdVal :: " + stdVal);
                    degExp.getResults().getMeanTimeSeries().put((double) timeVal, meanVal);
                    degExp.getResults().getStdTimeSeries().put((double)timeVal, stdVal);
                    csvLines.add(timeVal + "," + meanVal);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return csvLines;
    }
    
    private static String[] filepathPieces(String filepath, String rootFilepath){
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
        //System.out.println(customNameMap);
        return customNameMap;
    }
    
    public static void fillOutNameMap(String fileFrom, String fileTo){
        File from = new File(fileFrom);
        File to = new File(fileTo);
        Map<String, List<String>> map = new HashMap<String,List<String>>();
        
        try {
            BufferedReader brFrom = new BufferedReader(new FileReader(from));
            String line = "";
            while((line = brFrom.readLine()) != null){
                if(line.trim().equals("Short Name,Features,Custom Name")){
                    continue;
                }
                String pieces[] = line.trim().split(",");
                
                if(pieces.length == 3){
                    if(!map.containsKey(pieces[1])){
                        map.put(pieces[1], new ArrayList());
                    }
                    map.get(pieces[1]).add(pieces[2]);
                }
            }
            brFrom.close();
            Map<String,Integer> namecount = new HashMap();
            List<String> lines = new ArrayList<String>();
            BufferedReader brTo = new BufferedReader(new FileReader(to));
            boolean added = false;
            while((line = brTo.readLine()) != null){
                String pieces[] = line.trim().split(",");
                added = false;
                if(map.containsKey(pieces[1])){
                    if(!namecount.containsKey(pieces[1])){
                        namecount.put(pieces[1], 0);
                    }
                    
                    int val = namecount.get(pieces[1]);
                    
                    for(String thirdcol:map.get(pieces[1])){
                        String colpieces[] = thirdcol.split("_");
                        int colval = 0;
                        try{
                            colval = Integer.parseInt(colpieces[2].trim());
                            if( (val+1) == Integer.valueOf(colpieces[2].trim())){
                            lines.add(line + thirdcol);
                            added = true;
                        }
                        }catch(Exception e){
                            lines.add(line + thirdcol);
                        } 
                        
                        
                    }
                    namecount.put(pieces[1], ++val);
                }
                if(!added){
                    lines.add(line);
                }
            }
            brTo.close();
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(to));
            for(String newLine:lines){
                writer.write(newLine);
                writer.newLine();
            }
            writer.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalyzeData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalyzeData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
    public static String getChannel(String filepathpiece){
        String pieces[] = filepathpiece.split("_");
        switch(pieces[1]){
            case "bfp":
                return "Pacific_Blue-A";
            case "rfp":
                return "PE-Texas_Red-A";
            case "gfp":
                return "FITC-A";
//            case "yfp":
//                break;
                
        }
        
        
        return "";
    }
    
}
