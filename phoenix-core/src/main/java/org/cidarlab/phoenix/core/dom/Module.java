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
    //Need to formally define these methods and constructors. But this is the basic essence of this Class. 
    
    @Getter
    @Setter
    private Feature moduleFeature;
    
    @Getter
    @Setter
    private LTLFunction function;
    
    @Getter
    @Setter
    private List<Module> parents;
    
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
    
    
    
    public Module()
    {
        this.stage = -1;
        this.isRoot = false;
        this.children = new ArrayList<Module>();
        this.parents = new ArrayList<Module>();
        
    }
    
    public Module(boolean _isRoot,int _stage)
    {
        this.stage = _stage;
        this.isRoot = _isRoot;
        this.children = new ArrayList<Module>();
        this.parents = new ArrayList<Module>();
        
    }
    
    public List<Module> getChildren()
    {
        return this.children;
    }
    public List<Module> getParents()
    {
        return this.parents;
    }
    public List<Module> getAllNeighbors()
    {
        List<Module> neigh = new ArrayList<Module>();
        neigh.addAll(this.parents);
        neigh.addAll(this.children);
        return neigh;
    }
    
}
