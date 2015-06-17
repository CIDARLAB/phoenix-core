/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Arc {
    
    public Arc(){
        
    }
    
    public Arc(Feature _regulator, Feature _regulatee, ArcRole _role){
        regulator = _regulator;
        regulatee = _regulatee;
        role = _role;
    }
    
    //Regulator
    @Getter
    @Setter
    private Feature regulator;
    
    //Regulatee
    @Getter
    @Setter
    private Feature regulatee;
    
    //Arc role
    @Getter
    @Setter
    private ArcRole role;
    
    //Small molecules
    @Getter
    @Setter
    private HashSet<SmallMolecule> molecules;
    
    //Might have more Roles. Something to look into.
    public enum ArcRole{
        REPRESSION,
        ACTIVATION;
    }
    
    
    
}
