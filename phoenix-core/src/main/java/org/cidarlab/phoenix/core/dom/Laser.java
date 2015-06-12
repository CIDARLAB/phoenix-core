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

/**
 *
 * @author prash
 */
public class Laser {
    
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private double power;
    
    @Getter
    @Setter
    private double wavelength;
     
    
    @Getter
    @Setter
    private LaserType type;
    
    @Getter
    @Setter
    private DetectorArray array;
    
    @Getter
    @Setter
    private List<Detector> detectors;
    
    public static enum LaserType{
        CUSTOM
    }
    
    public static enum DetectorArray{
        OCTAGON,
        TRIGON
    }
    
    public Laser(){
        detectors = new ArrayList<Detector>();
    }
    
}
