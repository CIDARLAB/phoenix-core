/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Arc {
    
    @Getter
    @Setter
    private PrimitiveModule from;
    
    @Getter
    @Setter
    private PrimitiveModule to;
    
    @Getter
    @Setter
    private ArcRole role;
    
    public Arc(){
        
    }
    
    //Might have more Roles. Something to look into.
    public enum ArcRole{
        REPRESSION,
        ACTIVATION
    }
    
    
    
}
