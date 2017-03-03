/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import com.panayotis.gnuplot.dataset.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.TimeSeriesData;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import analysis.dynamicsim.flattened.SimulatorSSADirect;
import analysis.dynamicsim.flattened.SimulatorODERK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import learn.genenet.Experiments;
import learn.genenet.SpeciesCollection;
import learn.parameterestimator.ParameterEstimator;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;

/**
 *
 * @author prash
 */
public class IBioSimAdaptor {

    public static Map<String, Double> estimateParameters(String sbmlFile, List<String> parameters, List<String> experimentFiles) throws IOException, XMLStreamException {
        String[] split = sbmlFile.split(File.separator);
        String root = sbmlFile.substring(0, sbmlFile.length() - split[split.length - 1].length());
        Experiments experiments = new Experiments();
        SpeciesCollection speciesCollection = new SpeciesCollection();
        for (String experiment : experimentFiles) {
            parseCSV(experiment, speciesCollection, experiments);
        }
        return ParameterEstimator.estimate(sbmlFile, root, parameters, experiments, speciesCollection);
    }
    
    public static Map<String, Double> estimateExpressorParameters(String degTimeSeriesData, String expSteadyStateData, String channel) throws XMLStreamException, FileNotFoundException, IOException {
        SBMLDocument degradationDoc = SBMLAdaptor.createDegradationModel("exp");
        Map<String, Double> results = estimateDegradationParams(degTimeSeriesData, channel);
        List<List<String>> data = parseCSV(expSteadyStateData);
        double y = results.get("y");
        double K_d = results.get("K_d");
        double k_EXE = 0;
        int j = 1;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(channel)) {
                for (j = 1; j < data.get(i).size(); j++) {
                    double steady = Double.parseDouble(data.get(i).get(j));
                    k_EXE += (y * (steady/K_d))/(1+(steady/K_d));
                }
            }
        }
        k_EXE /= j;
        if (k_EXE == 0) {
            k_EXE = 1;
        }
        results.put("k_EXE", k_EXE);
        return results;
    }
    
    public static Map<String, Double> estimateExpresseeParameters(String degTimeSeriesData, List<Double> smallMoleculesValues, List<String> smallMoleculeTimeSeriesData,
            String expresseeChannel, String regulatedChannel, boolean repression) throws XMLStreamException, IOException {
        Map<String, Double> results = estimateDegradationParams(degTimeSeriesData, regulatedChannel);
        boolean includeRegulation = true;
        if (smallMoleculesValues.contains(0.0)) {
            int index = smallMoleculesValues.indexOf(0.0);
            Map<String, Double> regulationResults = estimateRegulationParams(smallMoleculeTimeSeriesData.get(index), results, expresseeChannel, regulatedChannel, repression);
            if (repression) {
                results.put("k_EXR", regulationResults.get("k_EXR"));
                results.put("K_r", regulationResults.get("K_r"));
            }
            else {
                results.put("k_EXA", regulationResults.get("k_EXA"));
                results.put("K_a", regulationResults.get("K_a"));
            }
            smallMoleculesValues.remove(index);
            smallMoleculeTimeSeriesData.remove(index);
            includeRegulation = false;
        }
        double k_EXE = 0.0;
        double K_r = 0.0;
        double K_i = 0.0;
        for (int i = 0; i < smallMoleculesValues.size(); i ++) {
            Map<String, Double> smallMoleculeResults = estimateSmallMoleculeRegulationParams(smallMoleculesValues.get(i), smallMoleculeTimeSeriesData.get(i),
                    results, expresseeChannel, regulatedChannel, repression);
            K_i += smallMoleculeResults.get("K_i");
            if (includeRegulation) {
                if (repression) {
                    k_EXE += smallMoleculeResults.get("k_EXR");
                    K_r += smallMoleculeResults.get("K_r");
                }
                else {
                    k_EXE += smallMoleculeResults.get("k_EXA");
                    K_r += smallMoleculeResults.get("K_a");
                }
            }
        }
        results.put("K_i", K_i/smallMoleculesValues.size());
        if (includeRegulation) {
            if (repression) {
                results.put("k_EXR", k_EXE/smallMoleculesValues.size());
                results.put("K_r", K_r/smallMoleculesValues.size());
            }
            else {
                results.put("k_EXA", k_EXE/smallMoleculesValues.size());
                results.put("K_a", K_r/smallMoleculesValues.size());
            }
        }
        return results;        
    }
    
    public static Map<String, Double> estimateExpresseeParameters(String degTimeSeriesData, Map<String, Double> expParams,
            String regulationSteadyStateData, String smallMoleculeSteadyStateData, String expresseeChannel, String regulatorChannel,
            boolean repression, boolean regFromSmallMoleculeData) throws XMLStreamException, IOException {
        Map<String, Double> results = estimateDegradationParams(degTimeSeriesData, expresseeChannel);
        results.put("k_EXE", expParams.get("k_EXE"));
        double y = results.get("y");
        double K_d = results.get("K_d");
        double k_EXE = results.get("k_EXE");
        int j = 1;
        int expresseeIndex = -1;
        int regulatorIndex = -1;
        List<List<String>> data;
        if (!regFromSmallMoleculeData) {
            data = parseCSV(regulationSteadyStateData);
            for (int i = 0; i < data.size(); i++) {
                if (expresseeChannel.contains(data.get(i).get(0).replace("\"", ""))) {
                    expresseeIndex = i;
                } else if (regulatorChannel.contains(data.get(i).get(0).replace("\"", ""))) {
                    regulatorIndex = i;
                }
            }
            double K_r = 0;
            for (j = 1; j < data.get(0).size(); j++) {
                double expresseeSteady = Double.parseDouble(data.get(expresseeIndex).get(j));
                double regulatorSteady = Double.parseDouble(data.get(regulatorIndex).get(j));
                if (repression) {
                    K_r += (((k_EXE * (1 + (expresseeSteady / K_d))) / (y * (expresseeSteady / K_d))) - 1) / regulatorSteady;
                }
                else {
                    K_r += ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))/(regulatorSteady * (k_EXE - ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))));
                }
            }
            K_r /= j;
            if (K_r == 0) {
                K_r = 1;
            }
            if (repression) {
                results.put("K_r", K_r);
            }
            else {
                results.put("K_a", K_r);
            }
        }
        else {
            data = parseCSV(smallMoleculeSteadyStateData);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).equals(expresseeChannel)) {
                    expresseeIndex = i;
                } else if (data.get(i).get(0).equals(regulatorChannel)) {
                    regulatorIndex = i;
                }
            }
            double K_r = 0;
            for (j = 1; j < data.get(0).size(); j++) {
                double smallMoleculeCount = Double.parseDouble(data.get(0).get(j));
                double expresseeSteady = Double.parseDouble(data.get(expresseeIndex).get(j));
                double regulatorSteady = Double.parseDouble(data.get(regulatorIndex).get(j));
                if (smallMoleculeCount == 0) {
                    if (repression) {
                        K_r = (((k_EXE * (1 + (expresseeSteady / K_d))) / (y * (expresseeSteady / K_d))) - 1) / regulatorSteady;
                    }
                    else {
                        K_r = ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))/(regulatorSteady * (k_EXE - ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))));
                    }
                }
            }
            if (repression) {
                results.put("K_r", K_r);
            }
            else {
                results.put("K_a", K_r);
            }
        }
        double K_r;
        if (repression) {
            K_r = results.get("K_r");
        }
        else {
            K_r = results.get("K_a");
        }
        data = parseCSV(smallMoleculeSteadyStateData);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals(expresseeChannel)) {
                expresseeIndex = i;
            }
            else if (data.get(i).get(0).equals(regulatorChannel)) {
                regulatorIndex = i;
            }
        }
        double K_i = 0;
        int count = 0;
        for (j = 1; j < data.get(0).size(); j++) {
            double smallMoleculeCount = Double.parseDouble(data.get(0).get(j));
            double expresseeSteady = Double.parseDouble(data.get(expresseeIndex).get(j));
            double regulatorSteady = Double.parseDouble(data.get(regulatorIndex).get(j));
            if (smallMoleculeCount != 0) {
                if (repression) {
                    K_i += (((K_r*regulatorSteady)/(((k_EXE*(1+(expresseeSteady/K_d)))/(y*(expresseeSteady/K_d)))-1))-1)/smallMoleculeCount;
                }
                else {
                    K_i += ((((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))/(k_EXE - ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))))/((K_r*regulatorSteady)-(((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))/(k_EXE - ((y * (expresseeSteady / K_d))/(1 + (expresseeSteady / K_d)))))))/smallMoleculeCount;
                }
                count ++;
            }
        }
        if (K_i == 0 || count == 0) {
            K_i = 1;
        }
        else {
            K_i /= count;
        }
        results.put("K_i", K_i);
        return results;
    }
    
    private static Map<String, Double> estimateDegradationParams(String degTimeSeriesData, String channel) throws XMLStreamException, FileNotFoundException, IOException {
        SBMLDocument degradationDoc = SBMLAdaptor.createDegradationModel("exp");
        degradationDoc.getModel().getReaction("exp_degradation").getKineticLaw().removeLocalParameter("y");
        degradationDoc.getModel().getReaction("exp_degradation").getKineticLaw().removeLocalParameter("K_d");
        degradationDoc.getModel().addParameter(SBMLAdaptor.createParameter("y", degradationDoc.getModel()));
        degradationDoc.getModel().addParameter(SBMLAdaptor.createParameter("K_d", degradationDoc.getModel()));
        List<String> estimateParams = new ArrayList<String>();
	estimateParams.add("y");
	estimateParams.add("K_d");
        Map<String, String> channelMapping = new HashMap<String, String>();
        channelMapping.put(channel, "\"exp\"");
        return estimateParams(degradationDoc, estimateParams, degTimeSeriesData, channelMapping);
    }
    
    private static Map<String, Double> estimateRegulationParams(String regulationTimeSeriesData, Map<String, Double> params,
            String expresseeChannel, String regulatedChannel, boolean repression) throws XMLStreamException, FileNotFoundException, IOException {
        SBMLDocument doc;
        List<String> estimateParams = new ArrayList<String>();
        if (repression) {
            doc = SBMLAdaptor.createRepressionModel("expressee", "regulated");
            doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("k_EXR");
            doc.getModel().addParameter(SBMLAdaptor.createParameter("k_EXR", doc.getModel()));
            estimateParams.add("k_EXR");
            doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("K_r");
            doc.getModel().addParameter(SBMLAdaptor.createParameter("K_r", doc.getModel()));
            estimateParams.add("K_r");
        }
        else {
            doc = SBMLAdaptor.createActivationModel("expressee", "regulated");
            doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("k_EXA");
            doc.getModel().addParameter(SBMLAdaptor.createParameter("k_EXA", doc.getModel()));
            estimateParams.add("k_EXA");
            doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("K_a");
            doc.getModel().addParameter(SBMLAdaptor.createParameter("K_a", doc.getModel()));
            estimateParams.add("K_a");
        }
        doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("y").setValue(params.get("y"));
        doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("K_d").setValue(params.get("K_d"));
        Map<String, String> channelMapping = new HashMap<String, String>();
        channelMapping.put(expresseeChannel, "\"expressee\"");
        channelMapping.put(regulatedChannel, "\"regulated\"");
        return estimateParams(doc, estimateParams, regulationTimeSeriesData, channelMapping);
    }
    
    private static Map<String, Double> estimateSmallMoleculeRegulationParams(Double smallMoleculesValue, String smallMoleculeTimeSeriesData, Map<String, Double> params,
            String expresseeChannel, String regulatedChannel, boolean repression) throws XMLStreamException, FileNotFoundException, IOException {
        if (smallMoleculesValue == 0.0) {
            return estimateRegulationParams(smallMoleculeTimeSeriesData, params, expresseeChannel, regulatedChannel, repression);
        }
        SBMLDocument doc;
        List<String> estimateParams = new ArrayList<String>();
        if (repression) {
            doc = SBMLAdaptor.createInductionRepressionModel("inducer", "expressee", "regulated");
            if (params.containsKey("k_EXR")) {
                doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("k_EXR").setValue(params.get("k_EXR"));
            }
            else {
                doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("k_EXR");
                doc.getModel().addParameter(SBMLAdaptor.createParameter("k_EXR", doc.getModel()));
                estimateParams.add("k_EXR");
            }
            if (params.containsKey("K_r")) {
                doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("K_r").setValue(params.get("K_r"));
            }
            else {
                doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("K_r");
                doc.getModel().addParameter(SBMLAdaptor.createParameter("K_r", doc.getModel()));
                estimateParams.add("K_r");
            }
        }
        else {
            doc = SBMLAdaptor.createInductionActivationModel("inducer", "expressee", "regulated");
            if (params.containsKey("k_EXA")) {
                doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("k_EXA").setValue(params.get("k_EXA"));
            }
            else {
                doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("k_EXA");
                doc.getModel().addParameter(SBMLAdaptor.createParameter("k_EXA", doc.getModel()));
                estimateParams.add("k_EXA");
            }
            if (params.containsKey("K_a")) {
                doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("K_a").setValue(params.get("K_a"));
            }
            else {    
                doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("K_a");
                doc.getModel().addParameter(SBMLAdaptor.createParameter("K_a", doc.getModel()));
                estimateParams.add("K_a");
            }
        }
        doc.getModel().getReaction("regulated_expression").getKineticLaw().removeLocalParameter("K_i");
        doc.getModel().addParameter(SBMLAdaptor.createParameter("K_i", doc.getModel()));
        estimateParams.add("K_i");
        doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("y").setValue(params.get("y"));
        doc.getModel().getReaction("regulated_degradation").getKineticLaw().getLocalParameter("K_d").setValue(params.get("K_d"));
        doc.getModel().getSpecies("inducer").setBoundaryCondition(true);
        doc.getModel().getSpecies("inducer").setValue(smallMoleculesValue);
        Map<String, String> channelMapping = new HashMap<String, String>();
        channelMapping.put(expresseeChannel, "\"expressee\"");
        channelMapping.put(regulatedChannel, "\"regulated\"");
        return estimateParams(doc, estimateParams, smallMoleculeTimeSeriesData, channelMapping);
    }
    
    private static Map<String, Double> estimateParams(SBMLDocument doc, List<String> params, String timeSeriesDataFile,
            Map<String, String> channelMapping) throws XMLStreamException, FileNotFoundException, IOException {
        SBMLWriter writer = new SBMLWriter();
        writer.write(doc, "sbml.xml");
        List<List<String>> data = parseCSV(timeSeriesDataFile);
        for (int i = 0; i < data.size(); i++) {
            for (String channel : channelMapping.keySet()) {
                if (data.get(i).get(0).equals(channel)) {
                    data.get(i).set(0, channelMapping.get(channel));
                }
            }
        }
        writeCSV(data, "data.csv");
        List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(new File("data.csv").getAbsolutePath());
        Map<String, Double> results = estimateParameters(new File("sbml.xml").getAbsolutePath(), params, experimentFiles);
        new File("sbml.xml").delete();
        new File("data.csv").delete();
        return results;
    }

    private static void parseCSV(String filename, SpeciesCollection speciesCollection, Experiments experiments) {
        int experiment = experiments.getNumOfExperiments();
        Scanner scan = null;
        boolean isFirst = true;
        try {
            scan = new Scanner(new File(filename));
            int row = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                String[] values = line.split(",");

                if (isFirst) {
                    for (int i = 0; i < values.length; i++) {
                        speciesCollection.addSpecies(values[i], i);
                    }
                    isFirst = false;
                } else {
                    for (int i = 0; i < values.length; i++) {
                        experiments.addExperiment(experiment, row, i, Double.parseDouble(values[i]));
                    }
                    row++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file!");
        } finally {
            if (scan != null) {
                scan.close();
            }

        }
    }
    
    public static List<List<String>> parseCSV(String filename) {
        List<List<String>> data = new ArrayList<List<String>>();
        Scanner scan = null;
        boolean isFirst = true;
        try {
            scan = new Scanner(new File(filename));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                String[] values = line.split(",");

                if (isFirst) {
                    for (int i = 0; i < values.length; i++) {
                        List<String> dataLine = new ArrayList<String>();
                        dataLine.add(values[i]);
                        data.add(dataLine);
                    }
                    isFirst = false;
                } else {
                    for (int i = 0; i < values.length; i++) {
                        data.get(i).add(values[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file!");
        } finally {
            if (scan != null) {
                scan.close();
            }

        }
        return data;
    }
    
    public static void writeCSV(List<List<String>> data, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            for (int j = 0; j < data.get(0).size(); j ++) {
                String line = data.get(0).get(j);
                for (int i = 1; i < data.size(); i ++) {
                    line += "," + data.get(i).get(j);
                }
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write to file!");
        }
    }

    public static List<TimeSeriesData> parseTimeSeriesDataList(String filepath) {
        List<TimeSeriesData> tsdlist = new ArrayList<TimeSeriesData>();

        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;

        List<Double> ymin = new ArrayList<Double>();
        List<Double> ymax = new ArrayList<Double>();

        Map<String, List<Point>> pointSet = new HashMap<String, List<Point>>();

        List<String> filelines = Utilities.getFileContentAsStringList(filepath);

        if (filelines.get(0).startsWith("((")) {
            filelines.set(0, filelines.get(0).substring(1));
        }
        String lastline = filelines.get(filelines.size() - 1);
        if (lastline.endsWith("))")) {
            filelines.set(filelines.size() - 1, (lastline.substring(0, lastline.length() - 1)) + ",");
        }
        List<String> labelList = new ArrayList<String>();
        boolean firstLine = true;
        for (String line : filelines) {
            line = line.trim();
            line = line.substring(0, line.length() - 1);
            if (line.startsWith("(")) {
                line = line.substring(1);
                line = line.trim();
            }
            if (line.endsWith(")")) {
                line = line.substring(0, line.length() - 1);
                line = line.trim();
            }

            if (!firstLine) {
                String pieces[] = line.split(",");
                double timeval = Double.valueOf(pieces[0].trim());
                if (timeval > xmax) {
                    xmax = timeval;
                }
                if (timeval < xmin) {
                    xmin = timeval;
                }
                for (int i = 1; i < pieces.length; i++) {
                    double yval = Double.valueOf(pieces[i].trim());
                    if (yval > ymax.get(i - 1)) {
                        ymax.set(i - 1, yval);
                    }
                    if (yval < ymin.get(i - 1)) {
                        ymin.set(i - 1, yval);
                    }
                    pointSet.get(labelList.get(i)).add(new Point(timeval, yval));
                }
            }
            if (firstLine) {
                String labels[] = line.split(",");
                for (String label : labels) {
                    label = label.trim();
                    if (label.startsWith("\"")) {
                        label = label.substring(1);
                    }
                    if (label.endsWith("\"")) {
                        label = label.substring(0, label.length() - 1);
                    }
                    //System.out.println(label);
                    labelList.add(label);
                }
                for (int i = 1; i < labelList.size(); i++) {
                    pointSet.put(labelList.get(i), new ArrayList<Point>());
                    ymin.add(Double.MAX_VALUE);
                    ymax.add(Double.MIN_VALUE);
                }
                firstLine = false;
            }
            //System.out.println(line);
        }

        for (String label : pointSet.keySet()) {
            TimeSeriesData tsd = new TimeSeriesData();
            tsd.setXlabel(labelList.get(0));
            tsd.setXmin(xmin);
            tsd.setXmax(xmax);
            Map<String, List<Point>> singleMapPoint = new HashMap();
            singleMapPoint.put(label, pointSet.get(label));
            tsd.setPointSets(singleMapPoint);
            tsd.setGraphLabel(label);
            tsd.setYlabel(label);
            double yminval = ymin.get(labelList.indexOf(label) - 1);
            double ymaxval = ymax.get(labelList.indexOf(label) - 1);
            if (ymaxval == yminval) {
                ymaxval = ymaxval + 1;
            }
            tsd.setYmax(ymaxval);
            tsd.setYmin(yminval);
            tsdlist.add(tsd);
        }
        return tsdlist;
    }

    public static TimeSeriesData parseTimeSeriesData(String filepath) {

        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;

        double ymin = Double.MAX_VALUE;
        double ymax = Double.MIN_VALUE;

        Map<String, List<Point>> pointSet = new HashMap<String, List<Point>>();

        List<String> filelines = Utilities.getFileContentAsStringList(filepath);

        if (filelines.get(0).startsWith("((")) {
            filelines.set(0, filelines.get(0).substring(1));
        }
        String lastline = filelines.get(filelines.size() - 1);
        if (lastline.endsWith("))")) {
            filelines.set(filelines.size() - 1, (lastline.substring(0, lastline.length() - 1)) + ",");
        }
        List<String> labelList = new ArrayList<String>();
        TimeSeriesData tsd = new TimeSeriesData();
        boolean firstLine = true;
        for (String line : filelines) {
            line = line.trim();
            line = line.substring(0, line.length() - 1);
            if (line.startsWith("(")) {
                line = line.substring(1);
                line = line.trim();
            }
            if (line.endsWith(")")) {
                line = line.substring(0, line.length() - 1);
                line = line.trim();
            }

            if (!firstLine) {
                String pieces[] = line.split(",");
                double timeval = Double.valueOf(pieces[0].trim());
                if (timeval > xmax) {
                    xmax = timeval;
                }
                if (timeval < xmin) {
                    xmin = timeval;
                }
                for (int i = 1; i < pieces.length; i++) {
                    double yval = Double.valueOf(pieces[i].trim());
                    if (yval > ymax) {
                        ymax = yval;
                    }
                    if (yval < ymin) {
                        ymin = yval;
                    }
                    pointSet.get(labelList.get(i)).add(new Point(timeval, yval));
                }
            }
            if (firstLine) {
                String labels[] = line.split(",");
                for (String label : labels) {
                    label = label.trim();
                    if (label.startsWith("\"")) {
                        label = label.substring(1);
                    }
                    if (label.endsWith("\"")) {
                        label = label.substring(0, label.length() - 1);
                    }
                    //System.out.println(label);
                    labelList.add(label);
                }
                for (int i = 1; i < labelList.size(); i++) {
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
     * @param printInterval - how often the simulation data should be written to
     * the output
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
        simulator.simulate();
    }

    /**
     * Runs an ODE simulation of the model specified in SBMLFileName.
     *
     * @param SBMLFileName - the SBML model to be simulated
     * @param outDir - the output directory
     * @param timeLimit - the time limit of the simulation
     * @param timeStep - the time step of the simulation
     * @param printInterval - how often the simulation data should be written to
     * the output
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
     * @param printInterval - how often the simulation data should be written to
     * the output
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
     * @param printInterval - how often the simulation data should be written to
     * the output
     * @throws IOException
     */
    public static void simulateStocastic(String SBMLFileName, String outDir,
            double timeLimit, double timeStep, double printInterval) throws IOException {

        simulateStocastic(SBMLFileName, outDir, timeLimit, timeStep, printInterval, 0.0, 314159, 2.0);

    }
}