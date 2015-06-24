/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.controller.Args;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.Annotation;
import org.cidarlab.phoenix.core.dom.Component;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.raven.algorithms.core.PrimerDesign;
import org.cidarlab.raven.datastructures.Vector;
import org.cidarlab.raven.javaapi.Raven;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 * This class has all methods for sending and receiving information to Raven
 * 
 * @author evanappleton
 */
public class RavenAdaptor {
    
    //Create assembly plans for given parts and return instructions file
    public static File generateAssemblyPlan(HashSet<Module> targetModules, String filePath) throws Exception {
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);
        
        
        //Get Phoenix data from Clotho
        JSONObject parameters = ClothoAdaptor.queryAssemblyParameters("default",clothoObject).toJSON();
        org.json.JSONObject rParameters = convertJSONs(parameters);
        
        Map polyNucQuery = new HashMap();
        polyNucQuery.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
        HashSet<Polynucleotide> polyNucs = ClothoAdaptor.queryPolynucleotides(polyNucQuery,clothoObject);
        
        Map featureQuery = new HashMap();
        featureQuery.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        HashSet<Feature> allFeatures = ClothoAdaptor.queryFeatures(featureQuery,clothoObject);
        
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        allFeatures.addAll(ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject));
        
        //Determine parts library
        HashSet<org.cidarlab.raven.datastructures.Part> partsLibR = new HashSet();
        HashSet<Vector> vectorsLibR = new HashSet();
        
        //Convert Phoenix Features to Raven Parts
        partsLibR.addAll(phoenixFeaturesToRavenParts(allFeatures));
                        
        //Convert Phoenix Polynucleotides to Raven Parts, Vectors and Plasmids
        HashMap<org.cidarlab.raven.datastructures.Part, Vector> libPairs = ravenPartVectorPairs(polyNucs, partsLibR, vectorsLibR);
        vectorsLibR.addAll(libPairs.values());
        partsLibR.addAll(libPairs.keySet());
        
        //Convert Phoenix Modules to Raven Plasmids
        HashSet<org.cidarlab.raven.datastructures.Part> targetParts = phoenixModulesToRavenParts(targetModules, partsLibR);
        
        //Run Raven to get assembly instructions
        Raven raven = new Raven();                
        File assemblyInstructions = raven.assemblyInstructions(targetParts, partsLibR, vectorsLibR, libPairs, new HashMap(), rParameters, filePath);
        
        conn.closeConnection();
        return assemblyInstructions;
    }
    
    //Convert Phoenix polynuclotides into their pairs
    public static HashMap<org.cidarlab.raven.datastructures.Part, Vector> ravenPartVectorPairs(HashSet<Polynucleotide> polyNucs, HashSet<org.cidarlab.raven.datastructures.Part> libParts, HashSet<Vector> vectorsLib) {
        
        HashMap<org.cidarlab.raven.datastructures.Part, Vector> plasmidPairs = new HashMap();
        
        //For each module, make a Raven part
        for (Polynucleotide pn : polyNucs) {
        
            //Special case for destination vector
            Vector vector;
            if (pn.isDV()) {
                int level = pn.getLevel();
                vector = phoenixPartToRavenVector(pn.getVector(), Integer.toString(level));
                vectorsLib.add(vector);
            } else {
                org.cidarlab.raven.datastructures.Part part = phoenixPartToRavenPart(pn.getPart(), libParts);
                vector = phoenixPartToRavenVector(pn.getVector(), null);
                plasmidPairs.put(part, vector);
            }            
        }
        
        return plasmidPairs;
    }
    
    //Convert Phoenix Parts to Raven Parts
    public static org.cidarlab.raven.datastructures.Part phoenixPartToRavenPart(Part pPart, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        Set<Annotation> annotations = pPart.getSequence().getAnnotations();
        
        String partSeq = pPart.getSequence().getSeq();
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        String moCloLO = moCloOHs.get(partSeq.substring(0, 4).toLowerCase());
        String moCloRO = moCloOHs.get(partSeq.substring(partSeq.length() - 4).toLowerCase());
        
        String name = pPart.getName();
        
        ArrayList<String> typeP = new ArrayList();
        typeP.add("plasmid");
        ArrayList<String> typeC = new ArrayList();
        typeC.add("composite");
        
        org.cidarlab.raven.datastructures.Part ravenPart;
        org.cidarlab.raven.datastructures.Part newPlasmid;
        ArrayList<org.cidarlab.raven.datastructures.Part> composition = new ArrayList();
        ArrayList<String> directions = new ArrayList();
        
        //Make Raven basic parts if only one annotation
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
                    sequence = Utilities.reverseComplement(sequence);
                }
                
                //Correct type
                type = a.getFeature().getRole().toString().toLowerCase();
                if (type.contains("cds")) {
                    type = "gene";
                } else if (type.contains("promoter")) {
                    type = "promoter";
                }
            }            
            
            ArrayList<String> bpDirectionL = new ArrayList();
            bpDirectionL.add(bpDirection);
            ArrayList<String> typeL = new ArrayList();
            typeL.add(type);
            
            ravenPart = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, null, new ArrayList(), bpDirectionL, moCloLO, moCloRO, typeL);
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            composition.add(ravenPart);
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, composition, new ArrayList(), bpDirectionL, moCloLO, moCloRO, typeP);
            newPlasmid.setTransientStatus(false);
            
        //Make Raven composite parts based on annotations
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
            
            //Composite version
            ravenPart = org.cidarlab.raven.datastructures.Part.generateComposite(name, composition, new ArrayList(), new ArrayList(), directions, moCloLO, moCloRO, typeC);
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            //Plasmid version
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(name, composition, new ArrayList(),  new ArrayList(), directions, moCloLO, moCloRO, typeP);
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
            newVector = Vector.generateVector(pPart.getName(), pPart.getSequence().getSeq(), "", "", "vector", "", "", resistance, -1);
            newVector.setTransientStatus(false);
        } else {
            newVector = Vector.generateVector(pPart.getName(), pPart.getSequence().getSeq(), moCloLO, moCloRO, "destination vector", pPart.getName(), "lacZ|" + moCloLO + "|" + moCloRO + "|+", resistance, -1);
            newVector.setTransientStatus(false);
        }

        return newVector;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixModulesToRavenParts(HashSet<Module> modules, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        int i = 1;
        
        //For each module, make a Raven part
        for (Module m : modules) {
            org.cidarlab.raven.datastructures.Part newPlasmid;
            ArrayList<org.cidarlab.raven.datastructures.Part> composition = new ArrayList();
            ArrayList<String> directions = new ArrayList();            
            
            //Make target plasmids by either finding existing parts or creating new ones
            for (PrimitiveModule pm : m.getSubmodules()) {
                
                //Determine direction
                ArrayList<String> aDir = new ArrayList();
                if (pm.getPrimitive().getOrientation().equals(Component.Orientation.FORWARD)) {
                    aDir.add("+");
                } else {
                    aDir.add("-");
                }
                
                //Regular parts with sequences
                if (!pm.getModuleFeatures().get(0).getSequence().getSequence().isEmpty()) {
                    for (Feature f : pm.getModuleFeatures()) {
                        String fName = f.getName().replaceAll(".ref", "");
                        
                        //Find Raven basic part for this composition
                        for (org.cidarlab.raven.datastructures.Part p : libParts) {
                            if (p.getName().equalsIgnoreCase(fName)) {
                                if (p.getLeftOverhang().isEmpty() && p.getRightOverhang().isEmpty() && p.getDirections().equals(aDir)) {
                                    composition.add(p);
                                    directions.addAll(aDir);
                                }
                            }
                        }
                    }
                    
                //Vector edge case
                } else if (pm.getPrimitiveRole() == FeatureRole.VECTOR) {
                    
                //Multiplex parts
                } else {
                    
                    String type = pm.getPrimitiveRole() + "_multiplex";
                    String name = pm.getPrimitiveRole() + "?";
                    ArrayList<String> typeM = new ArrayList();
                    typeM.add(type);                    
                    
                    org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, "", null, new ArrayList(), aDir, "", "", typeM);
                    newBasicPart.setTransientStatus(false);
                    libParts.add(newBasicPart);
                    
                    composition.add(newBasicPart);
                    directions.addAll(aDir);
                }
            }  
            
            ArrayList<String> typeP = new ArrayList();
            typeP.add("plasmid");            
            
            //Create blank polynucleotide as a placeholders
            //Here is where the colony picking math should be applied
//            HashSet<Polynucleotide> pnSet = new HashSet<>();
//            Polynucleotide pnPlaceholder = new Polynucleotide();
//            pnPlaceholder.setAccession(name + "_Polynucleotide");
//            pnSet.add(pnPlaceholder);
//            m.setPolynucleotides(pnSet);
            
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(m.getName(), composition, new ArrayList(), new ArrayList(), directions, "", "", typeP);
            newPlasmid.setTransientStatus(false);   
            
            ravenParts.add(newPlasmid);
        }        
        return ravenParts;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixFeaturesToRavenParts(HashSet<Feature> features) {
        
        HashSet<org.cidarlab.raven.datastructures.Part> ravenParts = new HashSet();
        
        //For each module, make a Raven blank basic part
        for (Feature f : features) {            
            if (!f.getRole().equals(Feature.FeatureRole.VECTOR)) {
                
                String name = f.getName().replaceAll(".ref", "");
                String type = f.getRole().toString().toLowerCase();
                if (type.contains("cds")) {
                    type = "gene";
                }
                String sequence = f.getSequence().getSequence();
                
                ArrayList<String> dirF = new ArrayList();
                dirF.add("+");
                ArrayList<String> dirR = new ArrayList();
                dirR.add("-");
                ArrayList<String> typeL = new ArrayList();
                typeL.add(type);
                
                //Forward version
                org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, sequence, null, new ArrayList(), dirF, "", "", typeL);
                newBasicPart.setTransientStatus(false);

                //Reverse version
                org.cidarlab.raven.datastructures.Part newReverseBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, PrimerDesign.reverseComplement(sequence), null, new ArrayList(), dirR, "", "", typeL);
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
    
    //Compare duplicates 
}
