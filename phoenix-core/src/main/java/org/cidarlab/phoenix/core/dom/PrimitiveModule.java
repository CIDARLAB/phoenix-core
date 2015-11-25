/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;

/**
 *
 * @author prash
 */
@Slf4j
public class PrimitiveModule extends AbstractModule {
    
    @Getter
    @Setter
    private Feature moduleFeature;
    
    //Set primitive
    @Getter
    @Setter        
    private Primitive primitive;
    
    //Module role
    @Getter
    @Setter
    private FeatureRole primitiveRole;
    
    public PrimitiveModule(FeatureRole role, Primitive primitive, Feature moduleFeature) {
        this.primitiveRole = role;
        this.primitive = primitive;
        this.moduleFeature = moduleFeature.clone();
        //this.getModuleFeatures().add(moduleFeature);
    }
    
    @Override
    public PrimitiveModule clone() {
        
        PrimitiveModule clone = new PrimitiveModule(this.primitiveRole,this.primitive,this.moduleFeature);
        //List<Feature> mFeatures = new ArrayList<>();
        //mFeatures.addAll(this.getModuleFeatures());
        //clone.setModuleFeatures(mFeatures);
        
        return clone;
    }
}
