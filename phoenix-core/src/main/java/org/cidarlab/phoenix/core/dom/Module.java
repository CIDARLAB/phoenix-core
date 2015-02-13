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
    public Module() {
        this.stage = -1;
        this.isRoot = false;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.assignedModules = new HashSet<>();
        this.submodules = new ArrayList<>();
        this.moduleFeatures = new ArrayList<>();
        this.isForward = true;
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
    @Override
    public Module clone() {
        Module clone = new Module();
        
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
    
    //Module roles
    @Getter
    @Setter
    private ModuleRole role;    

    
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
    private HashSet<Module> assignedModules;
    
    // Sub Module (s)
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
    
    //Experiment associated with this module
    @Getter
    @Setter
    private Experiment experiment;
    
    //Interaction associated with this module
//    @Getter
//    @Setter
//    private Interaction interaction;
    
    //Module roles
    public enum ModuleRole {
    	EXPRESSOR,
        EXPRESSEE,
        TRANSCRIPTIONAL_UNIT,
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
}
