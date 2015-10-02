/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.Set;
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
    public static Vector generateVector(String name, String description, NucSeq sequence, Format format, Person author, Feature origin, Feature resistance) {
        
        Vector v = (Vector) Part.generateBasic(name, description, sequence, format, author);
        v.origin = origin;
        v.resistance = resistance;        
        return v;
    }
    
    //Method for finding which origin is in an input vector sequence
    //Assumes only one origin
    public void findOriRes(NucSeq ns) {
        Set<Annotation> annotations = ns.getAnnotations();
        for (Annotation a : annotations) {
            if (a.getFeature().getRole().equals(Feature.FeatureRole.ORIGIN)) {
                this.origin = a.getFeature();
            } else if (a.getFeature().getRole().equals(Feature.FeatureRole.CDS_RESISTANCE)) {
                this.resistance = a.getFeature();
            }
        }
        
        //Re-name this vector to it's characterizing features - origin of replication and resistance marker
        if (this.origin != null && this.resistance != null) {
            this.setName(this.origin.getName() + "_" + this.resistance.getName());
        }
    }
    
    //This is the origin feature for this vector part
    @Getter
    @Setter
    private Feature origin;
    
    //This is the anitbiotic resistance feature for this vector part
    @Getter
    @Setter
    private Feature resistance;
}
