/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class SmallMolecule {
    
    //No-args constructor
    public SmallMolecule() {        
    }
    
    //Constructor
    //No-args constructor
    public SmallMolecule(String _name, SmallMoleculeRole _role) {
        role = _role;
        name = _name;
    }    
    
    //Small molecule role
    @Getter
    @Setter
    private SmallMoleculeRole role;    
    
    //Name
    @Getter
    @Setter
    private String name;    
    
   
    //Might have more Roles. Something to look into.
    public enum SmallMoleculeRole{
        REPRESSION,
        ACTIVATION;
    }
    
}
