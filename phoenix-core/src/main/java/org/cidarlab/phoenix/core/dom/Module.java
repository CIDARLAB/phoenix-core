/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author prash
 */
public class Module {

    /*
     * Need to formally define these methods and constructors. But this is the basic essence of this Class. 
     */
    //Default module contructor
    public Module(String name) {
        this.stage = -1;
        this.isRoot = false;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.assignedModules = new ArrayList<>();
        this.submodules = new ArrayList<>();
        this.moduleFeatures = new ArrayList<>();
        this.isForward = true;
        this.name = name;
        this.clothoID = name;
        this.color = Color.white;
//        this.experiments = new ArrayList<>();
    }

    public Module() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //Get all neighbors i.e. parents and children
    public List<Module> getAllNeighbors() {
        List<Module> neigh = new ArrayList<>();
        neigh.addAll(this.parents);
        neigh.addAll(this.children);
        return neigh;
    }

    //This cloning method ignores neighbors, clones everything else
//    @Override
    public Module clone(String name) {
        Module clone = new Module(name);

        List<Feature> fList = new ArrayList<>();
        for (Feature f : this.moduleFeatures) {
            fList.add(f.clone());
        }
        clone.moduleFeatures = fList;
        List<PrimitiveModule> pmList = new ArrayList<>();
        for (PrimitiveModule pm : this.submodules) {
            pmList.add(pm.clone());
        }
        clone.submodules = pmList;

//        List<Experiment> exList = new ArrayList<>();
//        exList.addAll(this.experiments);
//        clone.experiments = exList;
        clone.SBMLDocument = this.SBMLDocument;
        clone.function = this.function;
        clone.isForward = this.isForward;
        clone.role = this.role;
        clone.stage = this.stage;

        return clone;
    }

    //Module name
    @Getter
    @Setter
    private String name;

    //Module clothoID
    @Getter
    @Setter
    private String clothoID;

    //Module roles
    @Getter
    @Setter
    private ModuleRole role;

    
    //Repression or Activation Arcs. This will be used to indentify structures that can realize an Inverter, Oscillator or Switches
    //Arcs created by this module
    /*
    @Getter
    @Setter
    private List<Arc> arcs;
    */
    
    
    //Module features
    @Getter
    @Setter
    private List<Feature> moduleFeatures;

    //STL function associated with this module
    @Getter
    @Setter
    private STLFunction function;

    //Directionality
    @Getter
    @Setter
    private boolean isForward;

    //Parent module(s)
    @Getter
    @Setter
    private List<Module> parents;

    //Child module(s)
    @Getter
    @Setter
    private List<Module> children;

   
    @Getter
    @Setter
    private String index;

    //Assigned module(s)
    @Getter
    @Setter
    private List<AssignedModule> assignedModules;
    
    //SBML Model
    @Getter
    @Setter
    private SBMLDocument SBMLDocument;

    // Sub-Module(s)
    @Getter
    @Setter
    private List<PrimitiveModule> submodules;

    //Module stage
    @Getter
    @Setter
    private int stage;

    //Inidication of root
    @Getter
    @Setter
    private boolean isRoot;

    @Getter
    @Setter
    private Color color;

    //Graph Traversal
    public enum Color {

        white,
        gray,
        black
    }

    //Module roles
    public enum ModuleRole {

        EXPRESSOR,
        EXPRESSEE,
        EXPRESSEE_REPRESSOR,
        EXPRESSEE_REPRESSIBLE_REPRESSOR,
        EXPRESSEE_ACTIVATOR,
        EXPRESSEE_ACTIVATIBLE_ACTIVATOR,
        TRANSCRIPTIONAL_UNIT,
        EXPRESSION_DEGRATATION_CONTROL,
        REGULATION_CONTROL,
        COLOR_CONTROL,
        HIGHER_FUNCTION;
    }

    
    public static String getShortModuleRole(ModuleRole role) {
        switch (role) {
            case EXPRESSOR:
                return "exp";
            case EXPRESSEE:
                return "exe";
            case EXPRESSEE_REPRESSOR:
                return "exr";
            case EXPRESSEE_REPRESSIBLE_REPRESSOR:
                return "err";
            case EXPRESSEE_ACTIVATOR:
                return "exa";
            case EXPRESSEE_ACTIVATIBLE_ACTIVATOR:
                return "eaa";
            case TRANSCRIPTIONAL_UNIT:
                return "tu";
            case EXPRESSION_DEGRATATION_CONTROL:
                return "edc";
            case REGULATION_CONTROL:
                return "rc";
            case COLOR_CONTROL:
                return "cc";
            case HIGHER_FUNCTION:
                return "hf";
            default:
                return "";
        }
    }
    
    
    //Update moduleFeatures based on submodule features
    public void updateModuleFeatures() {
        List<Feature> updatedFeatures = new ArrayList<>();
        for (PrimitiveModule pm : this.submodules) {
            updatedFeatures.add(pm.getModuleFeature());
            //updatedFeatures.addAll(pm.getModuleFeatures());
        }
        this.setModuleFeatures(updatedFeatures);
    }
    
    public String getFeatureShortString(){
        String featureString = "";
        for(Feature feature:this.moduleFeatures){
            featureString += Feature.getShortFeatureRole(feature.getRole()) + " ";
        }
        featureString = featureString.trim();
        return featureString;
    }
    
    //Make sure that every node's color is white. Initialize the entire tree. Work on this. 
    public void initializeColor() {

    }
}
