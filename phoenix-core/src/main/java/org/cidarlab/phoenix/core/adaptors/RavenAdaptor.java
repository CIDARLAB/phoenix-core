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
import java.util.List;
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
import org.cidarlab.raven.accessibility.ClothoWriter;
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
    public static File generateAssemblyPlan(HashSet<Module> modulesToTest, String filePath) throws Exception {
        
        //Add testing modules to target modules
        HashSet<Module> targetModules = new HashSet<>();
        HashSet<List<Feature>> moduleFeatureHash = new HashSet<>();
        
        for (Module targetModule : modulesToTest) {
            if (!moduleFeatureHash.contains(targetModule.getModuleFeatures())) {
                targetModules.add(targetModule);
                moduleFeatureHash.add(targetModule.getModuleFeatures());
            }

            HashSet<Module> controlModules = targetModule.getControlModules();
            for (Module controlModule : controlModules) {
                if (!moduleFeatureHash.contains(controlModule.getModuleFeatures())) {
                    targetModules.add(controlModule);
                    moduleFeatureHash.add(controlModule.getModuleFeatures());
                }
            }
        }
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation);
        Clotho clothoObject = new Clotho(conn);        
        
        //Get Phoenix data from Clotho
        JSONObject parameters = ClothoAdaptor.getAssemblyParameters("default",clothoObject).toJSON();
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
        
        //Get MoClo overhangs, including a search for linkers        
        String moCloLO;
        String moCloRO;
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        
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
            int annotationStartIndex = 0;
            int annotationEndIndex = 0;
            
            for (Annotation a : annotations) {

                basicPartName = a.getFeature().getName();
                sequence = a.getFeature().getSequence().getSequence();
                if (!a.isForwardStrand()) {
                    bpDirection = "-";
                    sequence = Utilities.reverseComplement(sequence);
                }
                
                annotationStartIndex = a.getStart();
                annotationEndIndex = a.getEnd();
                
                //Correct type
                type = a.getFeature().getRole().toString().toLowerCase();
                if (type.contains("cds")) {
                    type = "gene";
                } else if (type.contains("promoter")) {
                    type = "promoter";
                }
            }                
                        
            //Determine MoClo overhangs, searching for 
            moCloLO = getMoCloOHs(partSeq.substring(0, annotationStartIndex).toLowerCase(), true, libParts);           
            moCloRO = getMoCloOHs(partSeq.substring(annotationEndIndex).toLowerCase(), false, libParts);    

            ArrayList<String> bpDirectionL = new ArrayList();
            bpDirectionL.add(bpDirection);
            ArrayList<String> typeL = new ArrayList();
            typeL.add(type);
            
            ravenPart = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, null, typeL, bpDirectionL, moCloLO, moCloRO);
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            composition.add(ravenPart);
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateBasic(basicPartName, sequence, composition, typeP, bpDirectionL, moCloLO, moCloRO);
            newPlasmid.setTransientStatus(false);
            
        //Make Raven composite parts based on annotations
        } else {
            
            //Scars and linkers
            ArrayList<String> scars = new ArrayList<>();
            ArrayList<String> linkers = new ArrayList<>();
            ArrayList<String> scarSeqs = new ArrayList<>();
            
            int lastAnnotationEndIndex = 0;
            int annotationStartIndex = 0;
            int annotationEndIndex = 0;
            
            //Organize annotations by start order
            HashMap<Integer, Annotation> annotationOrderMap = new HashMap();            
            for (Annotation a : annotations) {
                annotationOrderMap.put(a.getStart(), a);                
            }
            ArrayList<Integer> startOrder = new ArrayList(annotationOrderMap.keySet());
            Collections.sort(startOrder);
            
            boolean previousIsLinker = false;
            
            //Get composition and direction
            for (int i = 0; i < startOrder.size(); i++) {
                                
                Annotation a = annotationOrderMap.get(startOrder.get(i));
                
                //Directions
                ArrayList<String> aDir = new ArrayList();
                if (a.isForwardStrand()) {
                    directions.add("+");
                    aDir.add("+");
                } else {
                    directions.add("-");
                    aDir.add("-");
                }
                
                //Scars and linkers
                if (i > 0) {
              
                    String scar = "_";
                    String scarSeq = "";
                    String linker = "_";
                    
                    //Linkers
                    if (a.getFeature().getRole() == FeatureRole.CDS_LINKER) {
                       
                        //Consecutive linkers edge case
                        if (!linkers.get(linkers.size()-1).equals("_")) {
                            linker = linkers.get(linkers.size()-1) + "|" + a.getFeature().getName(); 
                            linkers.remove(linkers.size()-1);
                        } else {
                            linker = a.getFeature().getName();
                        }
                        
                        linkers.add(linker);
                        scarSeq = partSeq.substring(a.getStart(), a.getEnd() + 1);
                        previousIsLinker = true;

                    } else {
                        if (!previousIsLinker) {
                            linkers.add(linker);
                        } else {
                            previousIsLinker = false;
                        }
                        
                        scarSeq = partSeq.substring(lastAnnotationEndIndex, a.getStart());
                        scar = reverseScarLookup(scarSeq);
                    }

                    scarSeqs.add(scarSeq);
                    scars.add(scar);
                }
                
                //Find Raven basic part for this composition
                for (org.cidarlab.raven.datastructures.Part p : libParts) {
                    if (p.getName().equalsIgnoreCase(a.getFeature().getName())) {
                        if (p.getLeftOverhang().isEmpty() && p.getRightOverhang().isEmpty() && p.getDirections().equals(aDir)) {
                            composition.add(p);
                        }
                    }
                }
                
                if (i == 0) {
                    annotationStartIndex = a.getStart();
                    lastAnnotationEndIndex = a.getEnd();
                } else if (i == startOrder.size() - 1) {
                    annotationEndIndex = a.getEnd();
                } else {
                    lastAnnotationEndIndex = a.getEnd();
                }                
            }
            
            //Determine MoClo overhangs, searching for 
            if (partSeq.substring(0, annotationStartIndex).length() < 4 || partSeq.substring(annotationEndIndex).length() < 4) {
                String flag = "";
            }            
            
            moCloLO = getMoCloOHs(partSeq.substring(0, annotationStartIndex).toLowerCase(), true, libParts);           
            moCloRO = getMoCloOHs(partSeq.substring(annotationEndIndex).toLowerCase(), false, libParts);
            
            //Composite version
            ravenPart = org.cidarlab.raven.datastructures.Part.generateComposite(name, composition, scars, scarSeqs, linkers, directions, moCloLO, moCloRO, typeC);
            ravenPart.setTransientStatus(false);
            libParts.add(ravenPart);
            
            //Plasmid version
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(name, composition, scars, scarSeqs, linkers, directions, moCloLO, moCloRO, typeP);
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
        moCloLO = moCloOHs.get(vecSeq.substring(vecSeq.length() - 4).toLowerCase());
        moCloRO = moCloOHs.get(vecSeq.substring(0, 4).toLowerCase());     
        
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
        
        //For each module, make a Raven part
        for (Module m : modules) {
            org.cidarlab.raven.datastructures.Part newPlasmid;
            ArrayList<org.cidarlab.raven.datastructures.Part> composition = new ArrayList();
            ArrayList<String> directions = new ArrayList();            
           
            //Scars and linkers
            ArrayList<String> scars = new ArrayList<>();
            ArrayList<String> linkers = new ArrayList<>();
            boolean previousIsLinker = false;
            
            //Make target plasmids by either finding existing parts or creating new ones
            for (int i = 0; i < m.getSubmodules().size(); i++) {
                
                PrimitiveModule pm = m.getSubmodules().get(i);
                
                String scar = "_";                
                String linker = "_";
                
                //Determine direction
                ArrayList<String> aDir = new ArrayList();
                if (pm.getPrimitive().getOrientation().equals(Component.Orientation.FORWARD)) {
                    aDir.add("+");
                } else {
                    aDir.add("-");
                }
                
                //Regular parts with sequences
                if (!pm.getModuleFeatures().get(0).getSequence().getSequence().isEmpty() && pm.getPrimitiveRole() != FeatureRole.VECTOR) {
                    
                    if (pm.getPrimitiveRole() == FeatureRole.CDS_LINKER) {
                        String fName = pm.getModuleFeatures().get(0).getName().replaceAll(".ref", "");
                        
                        //Consecutive linkers edge case
                        if (!linkers.get(linkers.size()-1).equals("_")) {
                            linker = linkers.get(linkers.size()-1) + "|" + fName; 
                            linkers.remove(linkers.size()-1);
                        } else {
                            linker = fName;
                        }

//                        isLinker = true;
                        previousIsLinker = true;
                        linkers.add(linker);
                        
                    } else {

                        if (i > 0) {
                            if (!previousIsLinker) {
                                linkers.add(linker);
                            } else {
                                previousIsLinker = false;
                            }
                            //Scars and linkers
                            scars.add(scar);
                        }
                        
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
                    }
                    
                //Vector edge case
                } else if (pm.getPrimitiveRole() == FeatureRole.VECTOR) {
                    
                    String flag = "";
                    
                //Multiplex parts
                } else {
                    
                    if (i > 0) {
                        if (!previousIsLinker) {
                            linkers.add(linker);
                        } else {
                            previousIsLinker = false;
                        }
                        //Scars and linkers
                        scars.add(scar);
                    }
                    
                    String type = pm.getPrimitiveRole() + "_multiplex";
                    String name = pm.getPrimitiveRole() + "?";
                    ArrayList<String> typeM = new ArrayList();
                    typeM.add(type);                    
                    
                    org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, "", null, typeM, aDir, "", "");
                    newBasicPart.setTransientStatus(false);
                    libParts.add(newBasicPart);
                    
                    composition.add(newBasicPart);
                    directions.addAll(aDir);
                }
            }  
            
            ArrayList<String> typeP = new ArrayList();
            typeP.add("plasmid");            
            
            ArrayList<String> scarSeqs = ClothoWriter.scarsToSeqs(scars, null);
            
            //Create blank polynucleotide as a placeholders
            //Here is where the colony picking math should be applied
//            HashSet<Polynucleotide> pnSet = new HashSet<>();
//            Polynucleotide pnPlaceholder = new Polynucleotide();
//            pnPlaceholder.setAccession(name + "_Polynucleotide");
//            pnSet.add(pnPlaceholder);
//            m.setPolynucleotides(pnSet);
            
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(m.getName(), composition, scars, scarSeqs, linkers, directions, "", "", typeP);
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
                org.cidarlab.raven.datastructures.Part newBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, sequence, null, typeL, dirF, "", "");
                newBasicPart.setTransientStatus(false);

                //Reverse version
                org.cidarlab.raven.datastructures.Part newReverseBasicPart = org.cidarlab.raven.datastructures.Part.generateBasic(name, PrimerDesign.reverseComplement(sequence), null, typeL, dirR, "", "");
                newReverseBasicPart.setTransientStatus(false);
                
                ravenParts.add(newBasicPart);
                ravenParts.add(newReverseBasicPart);
            }
        }
        
        return ravenParts;
    }
    
    //Get MoClo overhangs, including a search for linker sequence
    private static String getMoCloOHs(String seq, boolean left, HashSet<org.cidarlab.raven.datastructures.Part> libParts) {
        
        String OH;
        
        //Loop through the part libary to see in the input sequence is contained in any of these parts
        org.cidarlab.raven.datastructures.Part linkerPart = null;
        for (org.cidarlab.raven.datastructures.Part libPart : libParts) {
            if (libPart.getSeq().contains(seq) || libPart.getSeq().contains(Utilities.reverseComplement(seq))) {
                linkerPart = libPart;
            }
        }
        
        //Check to see if requesting a left (TRUE) or right (FALSE) overhang
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());        
        if (left) {
            OH = moCloOHs.get(seq.substring(0,4).toLowerCase());
            String linkFragSeq = seq.substring(4);
            if (linkFragSeq.length() > 6) {
                for (org.cidarlab.raven.datastructures.Part libPart : libParts) {
                    if (libPart.getSeq().toLowerCase().contains(linkFragSeq.toLowerCase()) || libPart.getSeq().toLowerCase().contains(Utilities.reverseComplement(linkFragSeq).toLowerCase())) {
                        if (libPart.getType().contains("gene")) {
                            linkerPart = libPart;
                            break;
                        }
                    }
                }
            }
            
        } else {
            
            OH = moCloOHs.get(seq.substring(seq.length() - 4).toLowerCase());            
            String linkFragSeq = seq.substring(0, seq.length() - 4);
            if (linkFragSeq.length() > 6) {
                for (org.cidarlab.raven.datastructures.Part libPart : libParts) {
                    if (libPart.getSeq().toLowerCase().contains(linkFragSeq.toLowerCase()) || libPart.getSeq().toLowerCase().contains(Utilities.reverseComplement(linkFragSeq).toLowerCase())) {
                        if (libPart.getType().contains("gene")) {
                            linkerPart = libPart;
                            break;
                        }
                    }
                }
            }
        }

        if (linkerPart != null) {
            OH = "(" + linkerPart.getName() + ")" + OH;
        }
        
        return OH;
    }
    
    private static String reverseScarLookup(String scarSeq) {
        
        String scar = "_";
        
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        if (moCloOHs.containsKey(scarSeq.toLowerCase())) {
            scar = moCloOHs.get(scarSeq.toLowerCase());
        }
        
        return scar;
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
