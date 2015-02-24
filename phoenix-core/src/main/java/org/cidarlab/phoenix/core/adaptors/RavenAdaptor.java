/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.HashMap;
import java.util.HashSet;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.raven.datastructures.Vector;
import org.cidarlab.raven.javaapi.Raven;

/**
 * This class has all methods for sending and receiving information to Raven
 * 
 * @author evanappleton
 */
public class RavenAdaptor {
    
    //Create assembly plans for given parts and return instructions file
    public static String generateAssemblyPlan(HashSet<Module> targetModules) throws Exception {
        
        JSONObject parameters = ClothoAdaptor.queryAssemblyParameters("default").toJSON();
        org.json.JSONObject rParameters = convertJSONs(parameters);
        
        HashSet<Part> partsLib = ClothoAdaptor.queryParts();
        HashSet<org.cidarlab.raven.datastructures.Part> targetParts = phoenixModulesToRavenParts(targetModules);
        HashSet<org.cidarlab.raven.datastructures.Part> partsLibR = phoenixPartsToRavenParts(partsLib);
        HashSet<Vector> vectorsLibR = phoenixPartsToRavenVectors(partsLib);
        HashMap<org.cidarlab.raven.datastructures.Part, Vector> libPairs = new HashMap();
        
        
        Raven raven = new Raven();
        String assemblyInstructions = raven.assemblyInstructions(targetParts, partsLibR, vectorsLibR, libPairs, new HashMap(), rParameters);
        
        return assemblyInstructions;
    }
    
    //Convert Phoenix Parts to Raven Parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixPartsToRavenParts(HashSet<Part> pParts) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        
        //For each module, make a Raven part
        for (Part p : pParts) {
            
//            org.cidarlab.raven.datastructures.Part rPart = new Part();
            
        }
        
        return ravenParts;
    }
    
    //Convert Phoenix Parts to Raven Vectors
    public static HashSet<Vector> phoenixPartsToRavenVectors(HashSet<Part> pParts) {
        
        HashSet<Vector> ravenVectors = new HashSet();
        
        //For each module, make a Raven part
        for (Part p : pParts) {
            
            
        }
        
        return ravenVectors;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixModulesToRavenParts(HashSet<Module> modules) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        
        //For each module, make a Raven part
        for (Module m : modules) {
            
            
        }
        
        return ravenParts;
    }
    
    //Convert types of JSONObjects
    public static org.json.JSONObject convertJSONs(JSONObject old) {
        
        org.json.JSONObject newObj = new org.json.JSONObject();
        for (Object key : old.keySet()) {
            newObj.put(key.toString(), old.get(key));
        }
        
        return newObj;
    }
}
