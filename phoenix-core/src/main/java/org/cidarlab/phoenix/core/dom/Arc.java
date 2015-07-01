/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Arc {
    
    public Arc(){
        molecules = new ArrayList<>();
        
    }
    
    public Arc(Feature _regulator, Feature _regulatee, ArcRole _role){
        regulator = _regulator;
        regulatee = _regulatee;
        role = _role;
        molecules = new ArrayList<>();
        
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
    private List<SmallMolecule> molecules;
    
    //Might have more Roles. Something to look into.
    public enum ArcRole{
        REPRESSION,
        ACTIVATION;
    }
    
    
    
}
