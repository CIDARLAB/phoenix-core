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
    * Modified this to be a Constructor instead of a create method
    */
    public Vector (String name, String description, NucSeq sequence, Format format, Person author, Feature origin, Feature resistance) {
        super(name, description, sequence, format, author);
        this.origin = origin;
        this.resistance = resistance;        
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
            this.setName(this.origin.getName().replaceAll(".ref", "") + "_" + this.resistance.getName().replaceAll(".ref", "") + "_vector_" + this.getName().substring(this.getName().indexOf("vector")+7));
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
