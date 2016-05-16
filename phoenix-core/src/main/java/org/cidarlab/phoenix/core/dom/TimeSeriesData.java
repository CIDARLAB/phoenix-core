/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import com.panayotis.gnuplot.dataset.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class TimeSeriesData {
    
    @Getter @Setter
    private double xmin;
    @Getter @Setter
    private double xmax;
    
    @Getter @Setter
    private double ymin;
    @Getter @Setter
    private double ymax;
    
    @Getter @Setter
    private String xlabel;
    
    @Getter @Setter
    private String ylabel;
    
    @Getter @Setter
    private String graphLabel;
    
    @Getter @Setter
    private Map<String,List<Point>> pointSets;
    
    public TimeSeriesData(){
        xmin = Double.MAX_VALUE;
        xmax = Double.MIN_VALUE;
        ymin = Double.MAX_VALUE;
        ymax = Double.MIN_VALUE;
        
        xlabel = "";
        ylabel = "";
        graphLabel = "";
        
        pointSets = new HashMap<String,List<Point>>();
        
    }
    
}
