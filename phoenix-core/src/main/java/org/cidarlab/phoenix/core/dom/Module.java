/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.clothocad.model.Feature;

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
        this.children = new ArrayList<Module>();
        this.parents = new ArrayList<Module>();
        
    }
    
    //Module constructor for root and stage
    public Module(boolean _isRoot,int _stage) {
        this.stage = _stage;
        this.isRoot = _isRoot;
        this.children = new ArrayList<Module>();
        this.parents = new ArrayList<Module>();  
    }
    
    //Get all neighbors i.e. parents and children
    public List<Module> getAllNeighbors() {
        List<Module> neigh = new ArrayList<Module>();
        neigh.addAll(this.parents);
        neigh.addAll(this.children);
        return neigh;
    }
    
    //Module features
    @Getter
    @Setter
    private List<Feature> moduleFeature;
    
    //LTL function associated with this module
    @Getter
    @Setter
    private LTLFunction function;
    
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
    private int stage;
    
    @Getter
    @Setter
    private boolean isRoot;
    
    @Getter
    @Setter
    private Experiment experiment;
    
    @Getter
    @Setter
    private Interaction interaction;
}
