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
        this.submodules = new ArrayList<>();
    }
    
    //Module constructor for root and stage
    public Module(boolean _isRoot,int _stage) {
        this.stage = _stage;
        this.isRoot = _isRoot;
        this.children = new ArrayList<Module>();
        this.parents = new ArrayList<Module>();  
        this.submodules = new ArrayList<>();
    }
    
    //Get all neighbors i.e. parents and children
    public List<Module> getAllNeighbors() {
        List<Module> neigh = new ArrayList<Module>();
        neigh.addAll(this.parents);
        neigh.addAll(this.children);
        return neigh;
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
    @Getter
    @Setter
    private Interaction interaction;
    
    //Module roles
    public enum ModuleRole {
    	EXPRESSOR,
        EXPRESSEE,
        TRANSCRIPTIONAL_UNIT,
        HIGHER_FUNCTION;
    }
}
