/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.adaptors.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.stream.XMLStreamException;
import learn.genenet.Experiments;
import learn.genenet.SpeciesCollection;
import org.cidarlab.phoenix.core.adaptors.IBioSimAdaptor;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

/**
 *
 * @author prash
 */
public class IBioSimAdaptorTest {
    
    public IBioSimAdaptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of importSBML method, of class CopasiAdaptor.
     */
    
    
    @Test
    public void testDegradationParameterEstimation() throws IOException, XMLStreamException {
        System.out.println("parameterEstimation");
        String sbml = Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degrade.xml";
	List<String> params = new ArrayList<String>();
	params.add("y");
	params.add("K_d");
	List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degrade.csv");
	Map<String, Double> results = IBioSimAdaptor.estimateParameters(sbml, params, experimentFiles);
        for(String param : results.keySet()) {
            System.out.println(param + " = " + results.get(param));
        }
    }
    
    @Test
    public void testRegulationParameterEstimation() throws IOException, XMLStreamException {
        System.out.println("parameterEstimation");
        String sbml = Utilities.getFilepath() + "/src/main/resources/iBioSimTest/regulate.xml";
	List<String> params = new ArrayList<String>();
	params.add("n");
	params.add("K_r");
	List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/regulate.csv");
	Map<String, Double> results = IBioSimAdaptor.estimateParameters(sbml, params, experimentFiles);
        for(String param : results.keySet()) {
            System.out.println(param + " = " + results.get(param));
        }
    }
    
    @Test
    public void testExpressorEstimationAndSimulation() throws IOException, XMLStreamException {
        System.out.println("parameterEstimation");
        String sbml = Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degrade.xml";
	List<String> params = new ArrayList<String>();
	params.add("y");
	params.add("K_d");
        List<List<String>> data = parseCSV(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degradationTimeSeriesPlotPoints.csv");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals("\"MEAN_FITC.A\"")) {
                data.get(i).set(0, "\"deg\"");
            }
        }
        writeCSV(data, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/data.csv");
	List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/data.csv");
	Map<String, Double> results = IBioSimAdaptor.estimateParameters(sbml, params, experimentFiles);
        for(String param : results.keySet()) {
            System.out.println(param + " = " + results.get(param));
        }
        data = parseCSV(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressorSteadyState.csv");
        double Fl_t = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).equals("\"MEAN_FITC.A\"")) {
                Fl_t = Double.parseDouble(data.get(i).get(1));
            }
        }
        double y = results.get("y");
        double K_d = results.get("K_d");
        double kp = (y * (Fl_t/K_d))/(1+(Fl_t/K_d));
        SBMLDocument doc = SBMLReader.read(new File(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expression.xml"));
        Model model = doc.getModel();
        Reaction react = model.getReaction("exp_degradation");
        react.getKineticLaw().getLocalParameter("K_d").setValue(K_d);
        react.getKineticLaw().getLocalParameter("y").setValue(y);
        react = model.getReaction("exp_expression");
        react.getKineticLaw().getLocalParameter("k_EXE").setValue(kp);
        SBMLWriter writer = new SBMLWriter();
        writer.write(doc, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressor_expression_with_params.xml");
        IBioSimAdaptor.simulateODE(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressor_expression_with_params.xml", Utilities.getFilepath() + "/src/main/resources/iBioSimTest/", 100, 1, 1);
    }
    
    
    private static List<List<String>> parseCSV(String filename) {
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
    
    private static void writeCSV(List<List<String>> data, String filename) {
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
    
}
