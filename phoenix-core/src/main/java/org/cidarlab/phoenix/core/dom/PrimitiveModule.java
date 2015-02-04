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
@Slf4j
public class PrimitiveModule extends Module {
    
    //Set primitive
    @Getter
    @Setter        
    private Primitive primitive;
    
    //Module role
    @Getter
    @Setter
    private PrimitiveModuleRole primitiveRole;
    
    //Blank constructor
    public PrimitiveModule() {
    }
    
    public PrimitiveModule(PrimitiveModuleRole role, Primitive primitive) {
        this.primitiveRole = role;
        this.primitive = primitive;
    }
    
    @Override
    public PrimitiveModule clone() {
        
        PrimitiveModule clone = new PrimitiveModule();
        clone.primitive = this.primitive;
        clone.primitiveRole = this.primitiveRole;
        List<Feature> mFeatures = new ArrayList<>();
        mFeatures.addAll(this.getModuleFeatures());
        clone.setModuleFeatures(mFeatures);
        
        return clone;
    }
    
     public enum PrimitiveModuleRole {
       PROMOTER,
       PROMOTER_REPRESSIBLE,
       PROMOTER_INDUCIBLE,
       PROMOTER_CONSTITUTIVE,
       RBS,
       CDS,
       CDS_REPRESSOR,
       CDS_ACTIVATOR,
       CDS_LINKER,
       CDS_TAG,
       CDS_FLUORESCENT,
       CDS_FLUORESCENT_FUSION,
       TERMINATOR,
       VECTOR,
       TESTING,
       VECTOR,
       WILDCARD
   }
    
}
