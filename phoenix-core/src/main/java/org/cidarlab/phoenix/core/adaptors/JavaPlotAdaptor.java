/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;

/**
 *
 * @author prash
 */
public class JavaPlotAdaptor {
    
    public static JavaPlot createPlot(double[][] data){
        JavaPlot plot = new JavaPlot();
        DataSetPlot dsp = new DataSetPlot(data);
        plot.addPlot(dsp);
        plot.setTitle("Random Graph");
        return plot;
    }
    
}
