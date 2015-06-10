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
public class Detector {
    
    @Setter
    @Getter
    private String DetectorValue;
    
    
    @Setter
    @Getter
    private double mirror;
    
    @Setter
    @Getter
    private double filterMidPoint;
    
    @Setter
    @Getter
    private double filterWidth;
    
    @Setter
    @Getter
    private int channel;
    
    @Getter
    private double lower;
    
    @Getter
    private double upper;
    
    public Detector(String _detectorValue, double _mirror, double _filterMid, double _filterWidth){
        
        this.DetectorValue = _detectorValue;
        this.mirror = _mirror;
        this.filterMidPoint = _filterMid;
        this.filterWidth = _filterWidth;
        this.lower = getLowerRange();
        this.upper = getUpperRange();
        
    }
    
    
    private double getLowerRange(){
        double filterLowerLimit = (filterMidPoint - (filterWidth/2));
        if(mirror!=-1){
            return filterLowerLimit;
        }
        else{
            if(mirror < filterLowerLimit)
                return mirror;
            else
                return filterLowerLimit;
        }
    }
    
    private double getUpperRange(){
        return (filterMidPoint + (filterWidth/2));
    }
    
}
