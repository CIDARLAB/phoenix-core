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
 * @author prash
 * @author evanappleton
 * 
 */
public class AssignedModule extends Module {

      
    //Constructor for module to assignedModule
    public AssignedModule (Module m) {
//        AssignedModule aM = new AssignedModule(m.getName());
        super(m.getName());
        this.experiments = new ArrayList<>();
        
        //this.controlModules = new ArrayList<>();
        
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
        //AssignedModule clone = new AssignedModule(name);
        AssignedModule clone = new AssignedModule(new Module(name));
        
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
        //This part needs to change. Create deep copies of Experiments and Control Modules
        //clone.setControlModules(this.controlModules);
        
        clone.setFunction(this.getFunction());
        clone.setForward(this.isForward());
        clone.setRole(this.getRole());
        clone.setStage(this.getStage());
        
        for(SBMLDocument doc:this.getSBMLDocument()){
            clone.getSBMLDocument().add(doc.clone());
        }
        //clone.setSBMLDocument(this.getSBMLDocument());
        
        
        return clone;
    }
    
    public String getFeatureString(){
        String features="";
        
        for(int i=0;i<this.getSubmodules().size()-1;i++){
            PrimitiveModule pm = this.getSubmodules().get(i);
            Feature f = pm.getModuleFeature();
            if(f.getName() == null){
                features += (pm.getPrimitiveRole() + "|");
            } else if(f.getName().equals("")){
                features += (pm.getPrimitiveRole() + "|");
            } else{
                features += (f.getName() + "|");
            }
        }
        PrimitiveModule pm = this.getSubmodules().get(this.getSubmodules().size()-1);
        Feature f = pm.getModuleFeature();
        if (f.getName() == null) {
            features += (pm.getPrimitiveRole());
        } else if (f.getName().equals("")) {
            features += (pm.getPrimitiveRole());
        } else {
            features += (f.getName());
        }
        
        return features;
    } 
    
    @Getter
    @Setter
    private String shortName;
    
    //Control Modules as a property of Assigned Modules.
    //Should be an Assigned Module
    @Getter
    @Setter
    private List<AssignedModule> controlModules;
    
    //Experiment associated with this module
    @Getter
    @Setter
    private List<Experiment> experiments;
}
