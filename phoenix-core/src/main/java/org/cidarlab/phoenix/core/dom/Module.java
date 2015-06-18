/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
        this.assignedModules = new HashSet<>();
        this.submodules = new ArrayList<>();
        this.moduleFeatures = new ArrayList<>();
        this.isForward = true;
        this.name = name;
        this.clothoID = name;
        this.color = Color.white;
    }
    
    //Module constructor for root and stage
//    public Module(boolean _isRoot,int _stage) {
//        this.stage = _stage;
//        this.isRoot = _isRoot;
//        this.children = new ArrayList<>();
//        this.parents = new ArrayList<>();  
//        this.submodules = new ArrayList<>();
//        this.moduleFeatures = new ArrayList<>();
//        this.isForward = true;
//    }
    
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
    @Getter
    @Setter
    private List<Arc> arcs;
    
    //Module features
    @Getter
    @Setter
    private List<Feature> moduleFeatures;
    
    //LTL function associated with this module
    @Getter
    @Setter
    private LTLFunction function;
    
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
    
    //Child module(s)
    @Getter
    @Setter
    private HashSet<Module> controlModules;
    
    //Assigned module(s)
    @Getter
    @Setter
    private HashSet<Module> assignedModules;
    
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
    
    //Experiment associated with this module
    @Getter
    @Setter
    private List<Experiment> experiments;

    //Graph Traversal
    public enum Color{
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
        TESTING_CONTROL,
        HIGHER_FUNCTION;
    }
    
    //Update moduleFeatures based on submodule features
    public void updateModuleFeatures() {
        List<Feature> updatedFeatures = new ArrayList<>();
        for (PrimitiveModule pm : this.submodules) {
            updatedFeatures.addAll(pm.getModuleFeatures());
        }
        this.setModuleFeatures(updatedFeatures);
    }
    
    //Make sure that every node's color is white. Initialize the entire tree. Work on this. 
    public void initializeColor(){
    
    }
}
