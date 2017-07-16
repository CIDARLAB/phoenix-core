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
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
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
        
        clone.setSBMLDocument(this.getSBMLDocument().clone());
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
    
    public String getRoleFeature(){
        if(this.getRole().equals(ModuleRole.EXPRESSOR)){
            //Return Promoter
            for(Feature f:this.getAllModuleFeatures()){
                if(f.getRole().equals(FeatureRole.PROMOTER) || f.getRole().equals(FeatureRole.PROMOTER_CONSTITUTIVE) || f.getRole().equals(FeatureRole.PROMOTER_INDUCIBLE) || f.getRole().equals(FeatureRole.PROMOTER_REPRESSIBLE)){
                    return f.getName();
                }
            }
        } else if(this.getRole().equals(ModuleRole.EXPRESSEE)  ||  this.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) ||  this.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) ||  this.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) ||  this.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            //Return CDS
            for(Feature f:this.getAllModuleFeatures()){
                if(f.getRole().equals(FeatureRole.CDS) || f.getRole().equals(FeatureRole.CDS_ACTIVATIBLE_ACTIVATOR) || f.getRole().equals(FeatureRole.CDS_ACTIVATOR) || f.getRole().equals(FeatureRole.CDS_REPRESSIBLE_REPRESSOR) || f.getRole().equals(FeatureRole.CDS_REPRESSOR) || f.getRole().equals(FeatureRole.CDS_FLUORESCENT) || f.getRole().equals(FeatureRole.CDS_FLUORESCENT_FUSION) || f.getRole().equals(FeatureRole.CDS_LINKER) || f.getRole().equals(FeatureRole.CDS_RESISTANCE) || f.getRole().equals(FeatureRole.CDS_TAG)){
                    return f.getName();
                }
            }
        } else {
                return this.getRole().toString();
        }
        return null;
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
