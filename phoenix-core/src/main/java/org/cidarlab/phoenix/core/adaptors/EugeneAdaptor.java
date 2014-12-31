/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.minieugene.MiniEugene;
import org.cidarlab.minieugene.dom.Component;
import org.cidarlab.minieugene.exception.MiniEugeneException;
import org.cidarlab.minieugene.util.FileUtil;
import org.clothocad.model.Feature;
import org.clothocad.model.NucSeq;
import org.clothocad.model.Person;

/**
 * This class has all methods for sending and receiving information to Eugene and miniEugene
 * 
 * @author evanappleton
 */
public class EugeneAdaptor {
    
    //This method returns all structures for a given miniEugene file    
    public static List<List<Feature>> getStructures(File input, Integer numSolutions) throws IOException, MiniEugeneException {
        
        //Split text into rules list
        String eugFileString = FileUtil.readFile(input);
        String[] split = eugFileString.split(System.getProperty("line.separator"));
        
        //Somewhat hacky way to get the size of the design out of the file
        //Requires a specific convention in the first line of the input file -> "// Size=N"
        String firstLine = split[0];
        int size = 0;
        if (firstLine.contains("Size=")) {
            size = Integer.parseInt(firstLine.substring(firstLine.indexOf("Size=") + 5));
        }
        
        //Remove lines that are comments or empty lines
        ArrayList<String> rulesList = new ArrayList<String>();
        for (String line : split) {
            line = line.trim();
            
            //Add lines to rules list
            if (!(line.startsWith("//") || line.isEmpty())) {
                rulesList.add(line);
            }
        }
        String[] rules = rulesList.toArray(new String[rulesList.size()]);
        
        //Make miniEugene object and find solutions
        MiniEugene mE = new MiniEugene();
        List<List<Feature>> phoenixModules = new ArrayList<List<Feature>>();
        
        //Return number of solutions based upon input
        //If numSolutions is negative, return all, if it is greater than solution size, also return all
        if (numSolutions != null) {
            mE.solve(rules, size, numSolutions);
            List<Component[]> solutions = mE.getSolutions();
            phoenixModules = componentToModule(solutions);
        } else {
            mE.solve(rules, size);
            List<Component[]> solutions = mE.getSolutions();
            phoenixModules = componentToModule(solutions);
        }
        
        return phoenixModules;
    }
    
    //This method converts Eugene components to Clotho modules
    public static List<List<Feature>> componentToModule(List<Component[]> eugeneDevices) {
        
        List<List<Feature>> phoenixModules = new ArrayList<List<Feature>>();
        
        //For each device, translate all components to features
        //These will be features without a sequence, only a type and direction
        for (Component[] eugeneDevice : eugeneDevices) {
            
            ArrayList<Feature> phoenixModule = new ArrayList<Feature>();
            for (Component c : eugeneDevice) {
                
                String type = c.getType().getName();
                boolean isCDS = false;
                if (type.equalsIgnoreCase("c") || type.equalsIgnoreCase("fc") || type.equalsIgnoreCase("rc")) {
                    isCDS = true;
                }
                c.isForward();
                
                Feature f = Feature.generateFeature(c.getName(), "", new Person(), isCDS);
                phoenixModule.add(f);
            }
            
            phoenixModules.add(phoenixModule);            
        }
        
        return phoenixModules;
    }
}
