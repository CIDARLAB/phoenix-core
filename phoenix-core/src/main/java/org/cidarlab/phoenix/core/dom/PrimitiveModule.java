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
    
    @Getter
    @Setter        
    private Primitive primitive;
    
    public PrimitiveModule()
    {
    }
    
    public PrimitiveModule(Module node)
    {
    }
    
}
