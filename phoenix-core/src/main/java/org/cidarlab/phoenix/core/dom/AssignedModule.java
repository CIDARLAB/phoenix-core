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
import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author evanappleton
 */
public class AssignedModule extends Module {

    //Name only constructor
    public AssignedModule (String name) {
        super(name);
        this.experiments = new ArrayList<>();
        this.SBMLDocument = new SBMLDocument();
        this.regulationDocument = new ArrayList<>();
    }    
    
    //Constructor for module to assignedModule
    public AssignedModule (Module m) {
//        AssignedModule aM = new AssignedModule(m.getName());
        super(m.getName());
        this.experiments = new ArrayList<>();
        this.regulationDocument = new ArrayList<>();
        this.SBMLDocument = new SBMLDocument();
        List<Feature> fList = new ArrayList<>();
        for (Feature f : m.getModuleFeatures()) {
            fList.add(f.clone());
        }
        this.setModuleFeatures(fList);
        List<PrimitiveModule> pmList = new ArrayList<>();
        for (PrimitiveModule pm : m.getSubmodules()) {
            pmList.add(pm.clone());
        }
        this.setSubmodules(pmList);
        
        this.setFunction(m.getFunction());
        this.setForward(m.isForward());
        this.setRole(m.getRole());
        this.setStage(m.getStage());
    }
    
    @Override
    public AssignedModule clone(String name) {
        AssignedModule clone = new AssignedModule(name);
        
        List<Feature> fList = new ArrayList<>();
        for (Feature f : this.getModuleFeatures()) {
            fList.add(f.clone());
        }
        clone.setModuleFeatures(fList);
        List<PrimitiveModule> pmList = new ArrayList<>();
        for (PrimitiveModule pm : this.getSubmodules()) {
            pmList.add(pm.clone());
        }
        clone.setSubmodules(pmList);

        List<Experiment> exList = new ArrayList<>();
        exList.addAll(this.experiments);
        clone.experiments = exList;

        clone.setFunction(this.getFunction());
        clone.setForward(this.isForward());
        clone.setRole(this.getRole());
        clone.setStage(this.getStage());
        
        clone.SBMLDocument = this.SBMLDocument;
        clone.regulationDocument = this.regulationDocument;
        
        return clone;
    }
    
    
    //Control Modules as a property of Assigned Modules.
    @Getter
    @Setter
    private List<Module> controlModules;
    
    //SBML Model
    @Getter
    @Setter
    private SBMLDocument SBMLDocument;
    
    @Getter
    @Setter
    private List<SBMLDocument> regulationDocument;
    
        
    //Experiment associated with this module
    @Getter
    @Setter
    private List<Experiment> experiments;
}
