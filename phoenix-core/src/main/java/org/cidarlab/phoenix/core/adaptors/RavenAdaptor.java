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
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Component;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Part;
import org.cidarlab.phoenix.core.dom.Vector;
import org.cidarlab.phoenix.core.dom.Polynucleotide;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.raven.accessibility.ClothoWriter;
import org.cidarlab.raven.algorithms.core.PrimerDesign;
import org.cidarlab.raven.communication.RavenController;
import org.cidarlab.raven.communication.WeyekinPoster;
import org.cidarlab.raven.datastructures.RGraph;
import org.cidarlab.raven.datastructures.RNode;
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
    public static File generateAssemblyPlan(Set<AssignedModule> modulesToTest, String filePath) throws Exception {
        
        //Add testing modules to target modules
        HashSet<Module> allModules = new HashSet<>();
        HashSet<List<Feature>> moduleFeatureHash = new HashSet<>();
        
        for (AssignedModule targetModule : modulesToTest) {
            if (!moduleFeatureHash.contains(targetModule.getModuleFeatures())) {
                allModules.add(targetModule);
                moduleFeatureHash.add(targetModule.getModuleFeatures());
            }

            List<AssignedModule> controlModules = targetModule.getControlModules();
            for (AssignedModule controlModule : controlModules) {
                if (!moduleFeatureHash.contains(controlModule.getModuleFeatures())) {
                    allModules.add(controlModule);
                    moduleFeatureHash.add(controlModule.getModuleFeatures());
                }
            }
        }
        
        ClothoConnection conn = new ClothoConnection(Args.clothoLocation,Args.maxTimeOut);
        Clotho clothoObject = new Clotho(conn);        
        
        //Get Phoenix data from Clotho
        JSONObject parameters = ClothoAdaptor.getAssemblyParameters("default",clothoObject).toJSON();
        org.json.JSONObject rParameters = convertJSONs(parameters);
        
        Map polyNucQuery = new HashMap();
        polyNucQuery.put("schema", "org.cidarlab.phoenix.core.dom.Polynucleotide");
        HashSet<Polynucleotide> polyNucs = new HashSet<>(ClothoAdaptor.queryPolynucleotides(polyNucQuery,clothoObject));
        
        Map featureQuery = new HashMap();
        featureQuery.put("schema", "org.cidarlab.phoenix.core.dom.Feature");
        List<Feature> allFeatures = ClothoAdaptor.queryFeatures(featureQuery,clothoObject);
        
        Map fluorophoreQuery = new HashMap();
        fluorophoreQuery.put("schema", "org.cidarlab.phoenix.core.dom.Fluorophore");
        allFeatures.addAll(ClothoAdaptor.queryFluorophores(fluorophoreQuery,clothoObject));
        
        //Determine parts library
        HashSet<org.cidarlab.raven.datastructures.Part> partsLibR = new HashSet();
        HashSet<org.cidarlab.raven.datastructures.Vector> vectorsLibR = new HashSet();
        
        //Convert Phoenix Features to Raven Parts
        partsLibR.addAll(phoenixFeaturesToRavenParts(allFeatures));
                        
        //Convert Phoenix Polynucleotides to Raven Parts, Vectors and Plasmids
        HashMap<org.cidarlab.raven.datastructures.Part, org.cidarlab.raven.datastructures.Vector> libPairs = ravenPartVectorPairs(polyNucs, partsLibR, vectorsLibR);
        vectorsLibR.addAll(libPairs.values());
        partsLibR.addAll(libPairs.keySet());
        
        //Convert Phoenix Modules to Raven Plasmids
        ArrayList<HashSet<org.cidarlab.raven.datastructures.Part>> listTargetSets = new ArrayList();
        HashSet<Module> expressees = new HashSet<>();
        HashSet<Module> expressors = new HashSet<>();
        for (Module m : allModules) {
            if (m.getRole() == ModuleRole.EXPRESSEE || m.getRole() == ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR || m.getRole() == ModuleRole.EXPRESSEE_ACTIVATOR || m.getRole() == ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR || m.getRole() == ModuleRole.EXPRESSEE_REPRESSOR) {
                expressees.add(m);
            } else if (m.getRole() == ModuleRole.EXPRESSOR) {
                expressors.add(m);
            }
        }
        allModules.removeAll(expressees);
        allModules.removeAll(expressors);
             
        listTargetSets.add(phoenixModulesToRavenParts(expressees, partsLibR));
        listTargetSets.add(phoenixModulesToRavenParts(expressors, partsLibR));
        listTargetSets.add(phoenixModulesToRavenParts(allModules, partsLibR));        
     
        //Run Raven to get assembly instructions
        Raven raven = new Raven();         
        RavenController assemblyObj = raven.assemblyObject(listTargetSets, partsLibR, vectorsLibR, libPairs, new HashMap(), rParameters, filePath);
        
        //This is the information to be saved into Clotho and grabbed for the Owl datasheets
        //This information applies to all polynucleotides currently made in Phoenix
        String assemblyMethod = "MoClo (GoldenGate)"; //Right now everythig in Phoenix requires MoClo RFC 94 - can be extended in future, but this is what it is right now
        String assemblyRFC = "BBa_94";
        String chassis = "E. coli"; //Also always the case for Phoenix right now
        String supplementalComments = ""; //Nothing for now, perhaps this can be searched upon plasmid re-enrty?
        
        //This information is specific to each Polynucleotide
        for (HashSet<org.cidarlab.raven.datastructures.Part> partSet : listTargetSets) {
            for (org.cidarlab.raven.datastructures.Part p : partSet) {
                
                ArrayList<String> neighborNames = new ArrayList<>();
                String pigeonCode = "";
                                               
                //This assumes part name and rootNode name are the same - I think this is true, but there might be a bug here
                for (RGraph aG : assemblyObj.getAssemblyGraphs()) {
                    if (aG.getRootNode().getName().equalsIgnoreCase(p.getName())) {
                        
                        //Unclear if we want to use this information... The PrimitiveModules already have feature and direction, but these lists place the scars between those features
                        ArrayList<String> composition = aG.getRootNode().getComposition();
                        ArrayList<String> direction = aG.getRootNode().getDirection();
                        ArrayList<String> linkers = aG.getRootNode().getLinkers();
                        ArrayList<String> scars = aG.getRootNode().getScars();
                        ArrayList<String> type = aG.getRootNode().getType();
                        
                        //Neighbor names - Assembly components should be the neighbors of the root node - the parts put together in the last cloning reaction for this polynucleotide
                        ArrayList<RNode> neighbors = aG.getRootNode().getNeighbors();
                        for (RNode n : neighbors) {
                            neighborNames.add(n.getName());
                        }
                        
                        //Pigeon code
                        if (assemblyObj.getPigeonTextFiles().containsKey(p.getName())) {
                            pigeonCode = assemblyObj.getPigeonTextFiles().get(p.getName());
                        }
                    }
                }
            }
        }
        
        File assemblyInstructions = assemblyObj.getInstructionsFile();
        
        /*
        THESE ARE THE METHODS FOR MAKING THE RAVEN-PIGEON IMAGES
        */
//        WeyekinPoster.setDotText(RGraph.mergeWeyekinFiles(assemblyObj.getPigeonTextFiles()));
//        WeyekinPoster.postMyVision();
        
        conn.closeConnection();
        return assemblyInstructions;
    }
    
    //Convert Phoenix polynuclotides into their pairs
    public static HashMap<org.cidarlab.raven.datastructures.Part, org.cidarlab.raven.datastructures.Vector> ravenPartVectorPairs(HashSet<Polynucleotide> polyNucs, HashSet<org.cidarlab.raven.datastructures.Part> libParts, HashSet<org.cidarlab.raven.datastructures.Vector> vectorsLib) {
        
        HashMap<org.cidarlab.raven.datastructures.Part, org.cidarlab.raven.datastructures.Vector> plasmidPairs = new HashMap();
        
        //For each module, make a Raven part
        for (Polynucleotide pn : polyNucs) {
        
            //Special case for destination vector
            org.cidarlab.raven.datastructures.Vector vector;
            int level = pn.getLevel();
            if (pn.isDV()) {
                vector = phoenixVectorToRavenVector(pn.getVector(), Integer.toString(level));
                vectorsLib.add(vector);
            } else {
                org.cidarlab.raven.datastructures.Part part = phoenixPartToRavenPart(pn.getPart(), libParts);
                vector = phoenixVectorToRavenVector(pn.getVector(), Integer.toString(level));
                vectorsLib.add(vector);
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
            
//            //Determine MoClo overhangs, searching for 
//            if (partSeq.substring(0, annotationStartIndex).length() < 4 || partSeq.substring(annotationEndIndex).length() < 4) {
//                String flag = "";
//            }            
            
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
    public static org.cidarlab.raven.datastructures.Vector phoenixVectorToRavenVector(Vector pVector, String level) {
        
        //Get parameters for Raven
        String vecSeq = pVector.getSequence().getSeq();
        HashMap<String, String> moCloOHs = reverseKeysVals(PrimerDesign.getMoCloOHseqs());
        String moCloLO;
        String moCloRO;
        String resistance;
       
        //If both ends match MoClo overhangs, add them as overhangs
        moCloLO = moCloOHs.get(vecSeq.substring(vecSeq.length() - 4).toLowerCase());
        moCloRO = moCloOHs.get(vecSeq.substring(0, 4).toLowerCase());     
        
        //Get the resistance
        resistance = pVector.getResistance().getName().replaceAll(".ref", "");
         
        org.cidarlab.raven.datastructures.Vector newVector;
        if (level == null) {
            newVector = org.cidarlab.raven.datastructures.Vector.generateVector(pVector.getName().substring(0, pVector.getName().indexOf("vector")-1), pVector.getSequence().getSeq(), "", "", "vector", "", "", resistance, -1);
            newVector.setTransientStatus(false);
        } else {
            newVector = org.cidarlab.raven.datastructures.Vector.generateVector(pVector.getName().substring(0, pVector.getName().indexOf("vector")-1), pVector.getSequence().getSeq(), moCloLO, moCloRO, "destination vector", pVector.getName(), "lacZ|" + moCloLO + "|" + moCloRO + "|+", resistance, Integer.valueOf(level));
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
                if (!(pm.getModuleFeature().getSequence()==null) && pm.getPrimitiveRole() != FeatureRole.VECTOR) {
                    
                    if (pm.getPrimitiveRole() == FeatureRole.CDS_LINKER) {
                        String fName = pm.getModuleFeature().getName().replaceAll(".ref", "");
                        
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
                        
                        //for (Feature f : pm.getModuleFeatures()) {
                            String fName = pm.getModuleFeature().getName().replaceAll(".ref", "");

                            //Find Raven basic part for this composition
                            for (org.cidarlab.raven.datastructures.Part p : libParts) {
                                if (p.getName().equalsIgnoreCase(fName)) {
                                    if (p.getLeftOverhang().isEmpty() && p.getRightOverhang().isEmpty() && p.getDirections().equals(aDir)) {
                                        composition.add(p);
                                        directions.addAll(aDir);
                                    }
                                }
                            }
                        //}
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
            
            newPlasmid = org.cidarlab.raven.datastructures.Part.generateComposite(m.getName(), composition, scars, scarSeqs, linkers, directions, "", "", typeP);
            newPlasmid.setTransientStatus(false);   
            
            ravenParts.add(newPlasmid);
        }        
        return ravenParts;
    }
    
    //Convert Phoenix modules to Raven parts
    public static HashSet<org.cidarlab.raven.datastructures.Part> phoenixFeaturesToRavenParts(List<Feature> features) {
        
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
            OH = OH + "(" + linkerPart.getName() + ")";
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
