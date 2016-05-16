/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.cidarlab.phoenix.core.dom.TimeSeriesData;

/**
 *
 * @author prash
 */
public class JavaPlotAdaptor {
    
    public static JavaPlot createPlot(TimeSeriesData tsd){
        
        JavaPlot plot = new JavaPlot();
        Set<NamedPlotColor> chosenColors = new HashSet<NamedPlotColor>();
        Iterator it = tsd.getPointSets().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            List<Point> value = (List<Point>)entry.getValue();
            PlotStyle ps = new PlotStyle();
            ps.setStyle(Style.LINES);
            
            boolean colorFound = false;
            do{
                int randomNumber = ThreadLocalRandom.current().nextInt(0, NamedPlotColor.values().length);
                if(!chosenColors.contains(NamedPlotColor.values()[randomNumber])){
                    colorFound = true;
                    ps.setLineType(NamedPlotColor.values()[randomNumber]);
                    chosenColors.add(NamedPlotColor.values()[randomNumber]);
                } 
                
            }while(!colorFound);
            
            PointDataSet pds = new PointDataSet(value);
            DataSetPlot dsp = new DataSetPlot(pds);
            dsp.setTitle(key);
            
            plot.addPlot(dsp);
        }
        
        plot.getAxis("x").setLabel(tsd.getXlabel());
        plot.getAxis("y").setLabel(tsd.getYlabel());
        
        plot.getAxis("x").setBoundaries(tsd.getXmin(), tsd.getXmax());
        plot.getAxis("y").setBoundaries(tsd.getYmin(), tsd.getYmax());
        
        plot.setTitle(tsd.getGraphLabel());
        return plot;
    }
    
    
    public static void plotToFile(JavaPlot plot, String filepath){
        
        ImageTerminal png = new ImageTerminal();
        File file = new File(filepath);
        
        try {
            file.createNewFile();
            png.processOutput(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.print("File " + filepath + " not found.\n");
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }
        
        plot.setPersist(false);
        plot.setTerminal(png);
        plot.plot();
        
        try {
            ImageIO.write(png.getImage(), "png", file);
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
    
}
