/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.Component;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.raven.algorithms.core.PrimerDesign;
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
        
        //Get Phoenix data from Clotho
        JSONObject parameters = ClothoAdaptor.queryAssemblyParameters("default").toJSON();
        org.json.JSONObject rParameters = convertJSONs(parameters);
        HashSet<Polynucleotide> polyNucs = ClothoAdaptor.queryPolynucleotides();
        HashSet<Feature> allFeatures = ClothoAdaptor.queryFeatures();
        allFeatures.addAll(ClothoAdaptor.queryFluorophores());
        
        //Determine parts library
        HashSet<org.cidarlab.raven.datastructures.Part> partsLibR = new HashSet();
        HashSet<Vector> vectorsLibR = new HashSet();
        
        partsLibR.addAll(phoenixFeaturesToRavenParts(allFeatures));
                        
        HashMap<org.cidarlab.raven.datastructures.Part, Vector> libPairs = ravenPartVectorPairs(polyNucs, partsLibR);
        vectorsLibR.addAll(libPairs.values());
        partsLibR.addAll(libPairs.keySet());
        
        HashSet<org.cidarlab.raven.datastructures.Part> targetParts = phoenixModulesToRavenParts(targetModules, partsLibR);
        
        Raven raven = new Raven();
        String assemblyInstructions = raven.assemblyInstructions(targetParts, partsLibR, vectorsLibR, libPairs, new HashMap(), rParameters);
        
        return assemblyInstructions;
    }
    
    //Convert Phoenix polynuclotides into their pairs
    public static HashMap<org.cidarlab.raven.datastructures.Part, Vector> ravenPartVectorPairs(HashSet<Polynucleotide> polyNucs, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        HashMap<org.cidarlab.raven.datastructures.Part, Vector> plasmidPairs = new HashMap();
        
        //For each module, make a Raven part
        for (Polynucleotide pn : polyNucs) {
        
            //Special case for destination vector
            org.cidarlab.raven.datastructures.Part part = phoenixPartToRavenPart(pn.getPart(), libParts);
            Vector vector;
            if (pn.isDV()) {
                int level = pn.getLevel();
                vector = phoenixPartToRavenVector(pn.getVector(), Integer.toString(level));
            } else {
                vector = phoenixPartToRavenVector(pn.getVector(), null);
            }
            
            plasmidPairs.put(part, vector);
        }
        
        return plasmidPairs;
    }
    
    //Convert Phoenix Parts to Raven Parts
    public static org.cidarlab.raven.datastructures.Part phoenixPartToRavenPart(Part pPart, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        Set<Annotation> annotations = pPart.getSequence().getAnnotations();
        
        String partSeq = pPart.getSequence().getSeq();
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        String moCloLO = moCloOHs.get(partSeq.substring(partSeq.length() - 4).toLowerCase());
        String moCloRO = moCloOHs.get(partSeq.substring(0, 4).toLowerCase());
        String name = pPart.getName();
        
        org.cidarlab.raven.datastructures.Part ravenPart;
        org.cidarlab.raven.datastructures.Part newPlasmid;
        ArrayList<org.cidarlab.raven.datastructures.Part> composition = new ArrayList();
        ArrayList<String> directions = new ArrayList();
        
        if (annotations.size() == 1) {
            
            
            String basicPartName = "";
            String bpDirection = "+";
            String sequence = "";
            String type = "";
            
            for (Annotation a : annotations) {

                basicPartName = a.getFeature().getName();
                sequence = a.getFeature().getSequence().getSequence();
                if (!a.isForwardStrand()) {
                    bpDirection = "-";
                }
                
                //Correct type
                type = a.getFeature().getRole().toString().toLowerCase();
                if (type.contains("cds")) {
                    type = "gene";
                } else if (type.contains("promoter")) {
                    type = "promoter";
                }
            }            
            
            ravenPart = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, null);
            ravenPart.addSearchTag("Type: " + type);
            ravenPart.addSearchTag("Direction: [" + bpDirection + "]");
            ravenPart.addSearchTag("LO: " + moCloLO);
            ravenPart.addSearchTag("RO: " + moCloRO);
            ravenPart.addSearchTag("Scars: []");
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            composition.add(ravenPart);
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, composition);
            newPlasmid.addSearchTag("Direction: [" + bpDirection + "]");
            newPlasmid.addSearchTag("LO: " + moCloLO);
            newPlasmid.addSearchTag("RO: " + moCloRO);
            newPlasmid.addSearchTag("Type: plasmid");
            newPlasmid.addSearchTag("Scars: []");
            newPlasmid.setTransientStatus(false);
            
        } else {
            
            //Organize annotations by start order
            HashMap<Integer, Annotation> annotationOrderMap = new HashMap();            
            for (Annotation a : annotations) {
                annotationOrderMap.put(a.getStart(), a);                
            }
            ArrayList<Integer> startOrder = new ArrayList(annotationOrderMap.keySet());
            Collections.sort(startOrder);
            
            //Get composition and direction
            for (Integer start : startOrder) {
                Annotation a = annotationOrderMap.get(start);
                ArrayList<String> aDir = new ArrayList();
                
                //Directions
                if (a.isForwardStrand()) {
                    directions.add("+");
                    aDir.add("+");
                } else {
                    directions.add("-");
                    aDir.add("-");
                }
                
                //Find Raven basic part for this composition
                for (org.cidarlab.raven.datastructures.Part p : libParts) {
                    if (p.getName().equalsIgnoreCase(a.getFeature().getName())) {
                        if (p.getLeftOverhang().isEmpty() && p.getRightOverhang().isEmpty() && p.getDirections().equals(aDir)) {
                            composition.add(p);
                        }
                    }
                }
            }
            
            ravenPart = org.cidarlab.raven.datastructures.Part.generateComposite(composition, new ArrayList(), name);
            ravenPart.addSearchTag("Direction: " + directions);
            ravenPart.addSearchTag("LO: " + moCloLO);
            ravenPart.addSearchTag("RO: " + moCloRO);
            ravenPart.addSearchTag("Type: composite");
            ravenPart.addSearchTag("Scars: []");
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(composition, new ArrayList(), name);
            newPlasmid.addSearchTag("Direction: " + directions);
            newPlasmid.addSearchTag("LO: " + moCloLO);
            newPlasmid.addSearchTag("RO: " + moCloRO);
            newPlasmid.addSearchTag("Type: plasmid");
            newPlasmid.addSearchTag("Scars: []");
            newPlasmid.setTransientStatus(false);
        }       
        
        return newPlasmid;
    }
    
    //Convert Phoenix Parts to Raven Vectors
    public static Vector phoenixPartToRavenVector(Part pPart, String level) {
        
        //Get parameters for Raven
        String vecSeq = pPart.getSequence().getSeq();
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        String moCloLO = "";
        String moCloRO = "";
        String resistance = "";
        
        //If both ends match MoClo overhangs, add them as overhangs
        if (moCloOHs.containsKey(vecSeq.substring(vecSeq.length() - 4).toLowerCase()) && moCloOHs.containsKey(vecSeq.substring(vecSeq.length() - 4).toLowerCase())) {
            moCloLO = moCloOHs.get(vecSeq.substring(vecSeq.length() - 4).toLowerCase());
            moCloRO = moCloOHs.get(vecSeq.substring(0, 4).toLowerCase());
        }        
        
        //Get the resistance
        Set<Annotation> annotations = pPart.getSequence().getAnnotations();
        for (Annotation a : annotations) {
            if (a.getFeature().getRole().equals(Feature.FeatureRole.CDS_RESISTANCE)) {
                resistance = a.getFeature().getName().replaceAll(".ref", "");
            }
        }
         
        Vector newVector;
        if (level == null) {
            newVector = Vector.generateVector(pPart.getName(), pPart.getSequence().getSeq());
            newVector.addSearchTag("LO: ");
            newVector.addSearchTag("RO: ");
            newVector.addSearchTag("Type: vector");
            newVector.addSearchTag("Resistance: " + resistance);
            newVector.setTransientStatus(false);
        } else {
            newVector = Vector.generateVector(pPart.getName(), pPart.getSequence().getSeq());
            newVector.addSearchTag("LO: " + moCloLO);
            newVector.addSearchTag("RO: " + moCloRO);
            newVector.addSearchTag("Level: " + level);
            newVector.addSearchTag("Type: destination vector");
            newVector.addSearchTag("Resistance: " + resistance);
            newVector.addSearchTag("Vector: " + pPart.getName());
            newVector.addSearchTag("Composition: lacZ|" + moCloLO + "|" + moCloRO + "|+");
            newVector.setTransientStatus(false);
        }

        return newVector;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixModulesToRavenParts(HashSet<Module> modules, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        
        //For each module, make a Raven part
        for (Module m : modules) {
            org.cidarlab.raven.datastructures.Part newPlasmid;
            ArrayList<org.cidarlab.raven.datastructures.Part> composition = new ArrayList();
            ArrayList<String> directions = new ArrayList();
            ArrayList<String> aDir = new ArrayList();
            
            for (PrimitiveModule pm : m.getSubmodules()) {
                
                //Determine direction
                if (pm.getPrimitive().getOrientation().equals(Component.Orientation.FORWARD)) {
                    directions.add("+");
                    aDir.add("+");
                } else {
                    directions.add("-");
                    aDir.add("-");
                }
                
                if (!pm.getModuleFeatures().isEmpty()) {
                    for (Feature f : pm.getModuleFeatures()) {

                        //Find Raven basic part for this composition
                        for (org.cidarlab.raven.datastructures.Part p : libParts) {
                            if (p.getName().equalsIgnoreCase(f.getName())) {
                                if (p.getLeftOverhang().isEmpty() && p.getRightOverhang().isEmpty() && p.getDirections().equals(aDir)) {
                                    composition.add(p);
                                }
                            }
                        }
                    }
                } else {
                    
                    String type = pm.getPrimitive().getName() + "_multiplex";
                    String sequence = "";
                    org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(pm.getPrimitive().getName(), sequence, null);
                    newBasicPart.addSearchTag("Type: " + type);
                    newBasicPart.addSearchTag("Direction: " + aDir);
                    newBasicPart.addSearchTag("LO: ");
                    newBasicPart.addSearchTag("RO: ");
                    newBasicPart.addSearchTag("Scars: []");
                    newBasicPart.setTransientStatus(false);
                }
            }  
            
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(composition, new ArrayList(), "");
            newPlasmid.addSearchTag("Direction: " + directions);
            newPlasmid.addSearchTag("LO: ");
            newPlasmid.addSearchTag("RO: ");
            newPlasmid.addSearchTag("Type: plasmid");
            newPlasmid.addSearchTag("Scars: []");
            newPlasmid.setTransientStatus(false);   
            
            ravenParts.add(newPlasmid);
        }        
        return ravenParts;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixFeaturesToRavenParts(HashSet<Feature> features) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        
        //For each module, make a Raven part
        for (Feature f : features) {            
            if (!f.getRole().equals(Feature.FeatureRole.VECTOR)) {
                
                String name = f.getName().replaceAll(".ref", "");
                String type = f.getRole().toString().toLowerCase();
                String sequence = f.getSequence().getSequence();
                
                org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, sequence, null);
                newBasicPart.addSearchTag("LO: ");
                newBasicPart.addSearchTag("RO: ");
                newBasicPart.addSearchTag("Type: " + type);
                newBasicPart.addSearchTag("Direction: [+]");
                newBasicPart.addSearchTag("Scars: []");
                newBasicPart.setTransientStatus(false);

                org.cidarlab.raven.datastructures.Part newReverseBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, PrimerDesign.reverseComplement(sequence), null);
                newReverseBasicPart.addSearchTag("LO: ");
                newReverseBasicPart.addSearchTag("RO: ");
                newReverseBasicPart.addSearchTag("Type: " + type);
                newReverseBasicPart.addSearchTag("Direction: [-]");
                newReverseBasicPart.addSearchTag("Scars: []");
                newReverseBasicPart.setTransientStatus(false);
                
                ravenParts.add(newBasicPart);
                ravenParts.add(newReverseBasicPart);
            }
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
    
    //Switch keys and values for a Map of String to String
    public static HashMap<String, String> reverseKeysVals(HashMap<String, String> initialMap) {
        
        HashMap<String, String> finalMap = new HashMap();
        for (String key : initialMap.keySet()) {
            finalMap.put(initialMap.get(key), key);
        }
        return finalMap;
    }
}
