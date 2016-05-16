/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import com.panayotis.gnuplot.GNUPlotParameters;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import org.cidarlab.phoenix.core.adaptors.JavaPlotAdaptor;

/**
 *
 * @author prash
 */
public class JavaPlotTest {

    public static void main(String[] args) {

        /*double[][] data = new double[3][3];
         data[0][0] = 1.0;
         data[0][1] = 2.2;
         data[0][2] = 4.2;
         data[1][0] = 5.6;
         data[1][1] = 7.3;
         data[1][2] = 3.0;
         data[2][0] = 4.0;
         data[2][1] = 5.8;
         data[2][2] = 6.9;
         JavaPlot plot = JavaPlotAdaptor.createPlot(data);
         plot.plot();*/
        double[][] values = new double[3][2];
        values[0][0] = 0.1;
        values[0][1] = 0.3;
        values[1][0] = 0.4;
        values[1][1] = 0.3;
        values[2][0] = 0.5;
        values[2][1] = 0.5;

        double[][] values2 = new double[3][2];
        values2[0][0] = 0.2;
        values2[0][1] = 0.0;
        values2[1][0] = 0.7;
        values2[1][1] = 0.1;
        values2[2][0] = 0.6;
        values2[2][1] = 0.5;

        PlotStyle styleDeleted = new PlotStyle();
        styleDeleted.setStyle(Style.LINES);
        styleDeleted.setLineType(NamedPlotColor.BLUE);

        PlotStyle styleExist = new PlotStyle();
        styleExist.setStyle(Style.LINESPOINTS);
        styleExist.setLineType(NamedPlotColor.GREEN);

        DataSetPlot setDeleted = new DataSetPlot(values);
        setDeleted.setPlotStyle(styleDeleted);
        setDeleted.setTitle("deleted EMs");

        DataSetPlot setExist = new DataSetPlot(values2);
        setExist.setPlotStyle(styleExist);
        setExist.setTitle("remaining EMs");

        ImageTerminal png = new ImageTerminal();
        File file = new File("/home/prash/anotherplot.png");
        try {
            file.createNewFile();
            png.processOutput(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }

        JavaPlot p = new JavaPlot();
        p.setTerminal(png);

        p.getAxis("x").setLabel("yield");
        p.getAxis("y").setLabel("biomass");
        p.getAxis("x").setBoundaries(0.0, 1.0);
        p.getAxis("y").setBoundaries(0.0, 1.0);
        p.addPlot(setDeleted);
        p.addPlot(setExist);
        p.setTitle("remaining EMs");
        p.setPersist(false);
        p.plot();

        try {
            ImageIO.write(png.getImage(), "png", file);
        } catch (IOException ex) {
            System.err.print(ex);
        }

    }

}
