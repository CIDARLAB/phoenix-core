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
 * @author evanappleton
 */
public class Vector extends Part {
    
    /*
    * Method for creating a vector - vectors are just a part with origin of replication and resistance features noted
    */
    public static Vector generateVector (String name, String description, NucSeq sequence, Format format, Person author, Feature origin, Feature resistance) {
        
        Vector v = (Vector) Part.generateBasic(name, description, sequence, format, author);
        v.origin = origin;
        v.resistance = resistance;        
        return v;
    }
    
    @Getter
    @Setter
    private Feature origin;
    
    @Getter
    @Setter
    private Feature resistance;
}
