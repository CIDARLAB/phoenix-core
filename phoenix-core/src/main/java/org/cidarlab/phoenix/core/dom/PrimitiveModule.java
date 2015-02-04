/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
       TERMINATOR,
       TESTING,
       WILDCARD
   }
    
}
