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
import org.cidarlab.phoenix.core.dom.Component.Orientation;
import org.cidarlab.minieugene.exception.MiniEugeneException;
import org.cidarlab.minieugene.util.FileUtil;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Person;

/**
 * This class has all methods for sending and receiving information to Eugene and miniEugene
 * 
 * @author evanappleton
 */
public class EugeneAdaptor {
    
    //This method returns all structures for a given miniEugene file    
    public static List<Module> getStructures(File input, Integer numSolutions, String nameRoot) throws IOException, MiniEugeneException {
        
        //Split text into rules list
        String eugFileString = FileUtil.readFile(input);
        String[] split = eugFileString.split("\\r?\\n");
        
        //Somewhat hacky way to get the size of the design out of the file
        //Requires a specific convention in the first line of the input file -> "// Size=N"
        String firstLine = split[0];
        int size = 0;
        if (firstLine.contains("Size=")) {
            size = Integer.parseInt(firstLine.substring(firstLine.indexOf("Size=") + 5));
        }
        
        //Remove lines that are comments or empty lines
        ArrayList<String> rulesList = new ArrayList<>();
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
        List<Module> phoenixModules = new ArrayList<>();
        
        //Return number of solutions based upon input
        //If numSolutions is negative, return all, if it is greater than solution size, also return all
        if (numSolutions != null) {
            
            mE.solve(rules, size, numSolutions);
            List<Component[]> solutions = mE.getSolutions();
            phoenixModules = componentToModule(solutions, nameRoot);
        } else {
            
            mE.solve(rules, size);
            List<Component[]> solutions = mE.getSolutions();
            phoenixModules = componentToModule(solutions, nameRoot);
            for(Module root:phoenixModules){
                System.out.println(PigeonAdaptor.generatePigeonString(root, true));
            }
        }
        
        return phoenixModules;
    }
    
    //This method converts Eugene components to Clotho modules
    public static List<Module> componentToModule(List<Component[]> eugeneDevices, String nameRoot) {
        
        List<Module> phoenixModules = new ArrayList<>();
        int count = 0;
        
        //For each device, translate all components to features
        //These will be features without a sequence, only a type and direction
        for (Component[] eugeneDevice : eugeneDevices) {
            
            Module phoenixModule = new Module(nameRoot + "_" + count++);
            phoenixModule.setForward(true);
            
            ArrayList<Feature> moduleFeatures = new ArrayList<>();
            ArrayList<PrimitiveModule> primitiveModules = new ArrayList<>();
            
            for (Component c : eugeneDevice) {
                
                //Determine if this component is a coding sequence
                String type = c.getType().getName();
                boolean isCDS = false;
                if (type.equalsIgnoreCase("c") || type.equalsIgnoreCase("fc") || type.equalsIgnoreCase("rc")) {
                    isCDS = true;
                }
                
                //Create a new feature and add it to module features
                ComponentType ctype = new ComponentType(type);
                
                Feature f = Feature.generateFeature(c.getName(), "", new Person(), isCDS);
                f.setRole(findRole(ctype));
                moduleFeatures.add(f);
                
                
                //Create a new primitive module
                
                Primitive primitive = new Primitive(ctype,c.getName());
                
                //Primitive orientation                
                if (c.isForward()) {
                    primitive.setOrientation(Orientation.FORWARD);
                } else {
                    primitive.setOrientation(Orientation.REVERSE);
                }
                
                PrimitiveModule pm = new PrimitiveModule(findRole(ctype),primitive,f);
                pm.setForward(c.isForward());
                primitiveModules.add(pm);
            }
            
            phoenixModule.setRole(ModuleRole.HIGHER_FUNCTION);
            phoenixModule.setSubmodules(primitiveModules); 
            phoenixModule.setRoot(true);
            phoenixModules.add(phoenixModule);             
        }
        
        return phoenixModules;
    }

    
    //Determine primitive role from Eugene component types
    public static Feature.FeatureRole findRole(ComponentType type) {
        
        Feature.FeatureRole role = Feature.FeatureRole.WILDCARD;
        if (type.getName().startsWith("p")) {
            role = Feature.FeatureRole.PROMOTER;
        } else if (type.getName().startsWith("ip")) {
            role = Feature.FeatureRole.PROMOTER_INDUCIBLE;
        } else if (type.getName().startsWith("rp")) {
            role = Feature.FeatureRole.PROMOTER_REPRESSIBLE;
        } else if (type.getName().startsWith("cp")) {
            role = Feature.FeatureRole.PROMOTER_CONSTITUTIVE;
        } else if (type.getName().startsWith("rc")) {
            role = Feature.FeatureRole.CDS_REPRESSIBLE_REPRESSOR;
        } else if (type.getName().startsWith("fc")) {
            role = Feature.FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR;
        } else if (type.getName().startsWith("r")) {
            role = Feature.FeatureRole.RBS;
        } else if (type.getName().startsWith("c")) {
            role = Feature.FeatureRole.CDS_ACTIVATOR;
        } else if (type.getName().startsWith("g")) {
            role = Feature.FeatureRole.CDS_REPRESSOR;
        } else if (type.getName().startsWith("t")) {
            role = Feature.FeatureRole.TERMINATOR;
        } else if (type.getName().startsWith("unk")) {
            role = Feature.FeatureRole.CDS;
        } else if (type.getName().startsWith("fl")) {
            role = Feature.FeatureRole.CDS_FLUORESCENT;
        } else if (type.getName().startsWith("l")) {
            role = Feature.FeatureRole.CDS_LINKER;
        } 
         
        
        return role;
    }
    
    

}
