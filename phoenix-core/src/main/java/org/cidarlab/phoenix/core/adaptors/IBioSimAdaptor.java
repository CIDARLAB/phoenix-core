/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import com.panayotis.gnuplot.dataset.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.TimeSeriesData;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import analysis.dynamicsim.SimulatorSSADirect;
import analysis.dynamicsim.SimulatorODERK;
import java.io.IOException;

/**
 *
 * @author prash
 */
public class IBioSimAdaptor {
    
    public static List<TimeSeriesData> parseTimeSeriesDataList(String filepath){
        List<TimeSeriesData> tsdlist = new ArrayList<TimeSeriesData>();
        
        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        
        List<Double> ymin = new ArrayList<Double>();
        List<Double> ymax = new ArrayList<Double>();
        
        Map<String,List<Point>> pointSet = new HashMap<String,List<Point>>();
        
        List<String> filelines = Utilities.getFileContentAsStringList(filepath);
        
        if(filelines.get(0).startsWith("((")){
            filelines.set(0,filelines.get(0).substring(1)) ;
        }
        String lastline = filelines.get(filelines.size()-1);
        if(lastline.endsWith("))")){
            filelines.set(filelines.size()-1, (lastline.substring(0,lastline.length()-1)) + ",");
        }
        List<String> labelList = new ArrayList<String>();
        boolean firstLine= true;
        for(String line:filelines){
            line = line.trim();
            line = line.substring(0, line.length()-1);
            if(line.startsWith("(")){
                line = line.substring(1);
                line = line.trim();
            }
            if(line.endsWith(")")){
                line = line.substring(0, line.length()-1);
                line = line.trim();
            }
            
            if(!firstLine){
                String pieces[] = line.split(",");
                double timeval = Double.valueOf(pieces[0].trim());
                if(timeval > xmax){
                    xmax = timeval;
                }
                if(timeval < xmin){
                    xmin = timeval;
                }
                for(int i=1;i<pieces.length;i++){
                    double yval = Double.valueOf(pieces[i].trim());
                    if(yval > ymax.get(i-1)){
                        ymax.set(i-1,yval);
                    }
                    if(yval < ymin.get(i-1)){
                        ymin.set(i-1,yval);
                    }
                    pointSet.get(labelList.get(i)).add(new Point(timeval,yval));
                }
            }
            if(firstLine){
                String labels[] = line.split(",");
                for(String label:labels){
                    label = label.trim();
                    if(label.startsWith("\"")){
                        label = label.substring(1);
                    }
                    if(label.endsWith("\"")){
                        label = label.substring(0,label.length()-1);
                    }
                    //System.out.println(label);
                    labelList.add(label);
                }
                for(int i =1;i<labelList.size();i++){
                    pointSet.put(labelList.get(i), new ArrayList<Point>());
                    ymin.add(Double.MAX_VALUE);
                    ymax.add(Double.MIN_VALUE);
                }
                firstLine = false;
            }
            //System.out.println(line);
        }
        
        for(String label:pointSet.keySet()){
            TimeSeriesData tsd = new TimeSeriesData();
            tsd.setXlabel(labelList.get(0));
            tsd.setXmin(xmin);
            tsd.setXmax(xmax);
            Map<String,List<Point>> singleMapPoint = new HashMap();
            singleMapPoint.put(label, pointSet.get(label));
            tsd.setPointSets(singleMapPoint);
            tsd.setGraphLabel(label);
            tsd.setYlabel(label);
            double yminval = ymin.get(labelList.indexOf(label)-1);
            double ymaxval = ymax.get(labelList.indexOf(label)-1);
            if(ymaxval == yminval){
                ymaxval = ymaxval + 1;
            }
            tsd.setYmax(ymaxval);
            tsd.setYmin(yminval);
            tsdlist.add(tsd);
        }
        return tsdlist;
    }
    
    public static TimeSeriesData parseTimeSeriesData(String filepath){
        
        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        
        double ymin = Double.MAX_VALUE;
        double ymax = Double.MIN_VALUE;
        
        Map<String,List<Point>> pointSet = new HashMap<String,List<Point>>();
        
        List<String> filelines = Utilities.getFileContentAsStringList(filepath);
        
        if(filelines.get(0).startsWith("((")){
            filelines.set(0,filelines.get(0).substring(1)) ;
        }
        String lastline = filelines.get(filelines.size()-1);
        if(lastline.endsWith("))")){
            filelines.set(filelines.size()-1, (lastline.substring(0,lastline.length()-1)) + ",");
        }
        List<String> labelList = new ArrayList<String>();
        TimeSeriesData tsd = new TimeSeriesData();
        boolean firstLine= true;
        for(String line:filelines){
            line = line.trim();
            line = line.substring(0, line.length()-1);
            if(line.startsWith("(")){
                line = line.substring(1);
                line = line.trim();
            }
            if(line.endsWith(")")){
                line = line.substring(0, line.length()-1);
                line = line.trim();
            }
            
            if(!firstLine){
                String pieces[] = line.split(",");
                double timeval = Double.valueOf(pieces[0].trim());
                if(timeval > xmax){
                    xmax = timeval;
                }
                if(timeval < xmin){
                    xmin = timeval;
                }
                for(int i=1;i<pieces.length;i++){
                    double yval = Double.valueOf(pieces[i].trim());
                    if(yval > ymax){
                        ymax = yval;
                    }
                    if(yval < ymin){
                        ymin = yval;
                    }
                    pointSet.get(labelList.get(i)).add(new Point(timeval,yval));
                }
            }
            if(firstLine){
                String labels[] = line.split(",");
                for(String label:labels){
                    label = label.trim();
                    if(label.startsWith("\"")){
                        label = label.substring(1);
                    }
                    if(label.endsWith("\"")){
                        label = label.substring(0,label.length()-1);
                    }
                    //System.out.println(label);
                    labelList.add(label);
                }
                for(int i =1;i<labelList.size();i++){
                    pointSet.put(labelList.get(i), new ArrayList<Point>());
                }
                firstLine = false;
            }
            //System.out.println(line);
        }
        
        tsd.setXlabel(labelList.get(0));
        tsd.setYlabel("");
        tsd.setXmin(xmin);
        tsd.setXmax(xmax);
        tsd.setYmin(ymin);
        tsd.setYmax(ymax);
        tsd.setGraphLabel("Time Series Data");
        tsd.setPointSets(pointSet);
        return tsd;
    }
    
    /**
     * Runs an ODE simulation of the model specified in SBMLFileName.
     * 
     * @param SBMLFileName - the SBML model to be simulated
     * @param outDir - the output directory
     * @param timeLimit - the time limit of the simulation
     * @param timeStep - the time step of the simulation
     * @param printInterval - how often the simulation data should be written to the output
     * @param rndSeed - a random seed for the simulation
     * @param stoichAmpValue - stoichiometry amplification value
     * @param numSteps - number of steps to make in the simulation
     * @param relError - relative error
     * @param absError - absolute error
     * @throws IOException 
     */
    public static void simulateODE(String SBMLFileName, String outDir, double timeLimit,
            double timeStep, double printInterval, long rndSeed, double stoichAmpValue,
            int numSteps, double relError, double absError) throws IOException {
        JProgressBar progress = new JProgressBar();
        JFrame running = new JFrame();
        
        SimulatorODERK simulator = new SimulatorODERK(SBMLFileName, outDir, timeLimit,
                timeStep, rndSeed, progress, printInterval, stoichAmpValue, running,
                new String[0], numSteps, relError, absError, "amount");
//        simulator.simulate();
    }
    
    /**
     * Runs an ODE simulation of the model specified in SBMLFileName.
     * 
     * @param SBMLFileName - the SBML model to be simulated
     * @param outDir - the output directory
     * @param timeLimit - the time limit of the simulation
     * @param timeStep - the time step of the simulation
     * @param printInterval - how often the simulation data should be written to the output
     * @throws IOException 
     */
    public static void simulateODE(String SBMLFileName, String outDir, double timeLimit,
            double timeStep, double printInterval) throws IOException {
        
        simulateODE(SBMLFileName, outDir, timeLimit, timeStep, printInterval, 314159, 2.0, 50, 1e-6, 1e-9);
    }
    
    /**
     * Runs a stochastic simulation of the model specified in SBMLFileName.
     * 
     * @param SBMLFileName - the SBML model to be simulated
     * @param outDir - the output directory
     * @param timeLimit - the time limit of the simulation
     * @param timeStep - the time step of the simulation
     * @param printInterval - how often the simulation data should be written to the output
     * @param minTimeStep - the minimum time step of the simulation
     * @param rndSeed - a random seed for the simulation
     * @param stoichAmpValue - stoichiometry amplification value
     * @throws IOException 
     */
    public static void simulateStocastic(String SBMLFileName, String outDir,
            double timeLimit, double timeStep, double printInterval, double minTimeStep,
            long rndSeed, double stoichAmpValue) throws IOException {
        
        JProgressBar progress = new JProgressBar();
        JFrame running = new JFrame();
        
        SimulatorSSADirect simulator = new SimulatorSSADirect(SBMLFileName, outDir,
                timeLimit, timeStep, minTimeStep, rndSeed, progress, printInterval,
                stoichAmpValue, running, new String[0], "amount");
        simulator.simulate();
        
    }
    
    /**
     * Runs a stochastic simulation of the model specified in SBMLFileName.
     * 
     * @param SBMLFileName - the SBML model to be simulated
     * @param outDir - the output directory
     * @param timeLimit - the time limit of the simulation
     * @param timeStep - the time step of the simulation
     * @param printInterval - how often the simulation data should be written to the output
     * @throws IOException 
     */
    public static void simulateStocastic(String SBMLFileName, String outDir,
            double timeLimit, double timeStep, double printInterval) throws IOException {
        
        simulateStocastic(SBMLFileName, outDir, timeLimit, timeStep, printInterval, 0.0, 314159, 2.0);
        
    }
}
