/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

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
import org.cidarlab.phoenix.core.adaptors.SBMLAdaptor;
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
        String sbml = Utilities.getResourcesFilepath() + "iBioSimTest/degrade.xml";
	List<String> params = new ArrayList<String>();
	params.add("y");
	params.add("K_d");
	List<String> experimentFiles = new ArrayList<String>();
	experimentFiles.add(Utilities.getResourcesFilepath() + "iBioSimTest/degrade.csv");
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
        Map<String, Double> results = IBioSimAdaptor.estimateExpressorParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/degradationTimeSeriesPlotPoints.csv",
                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressorSteadyState.csv", "\"MEAN_FITC.A\"");
        double y = results.get("y");
        double K_d = results.get("K_d");
        double k_EXE = results.get("k_EXE");
        SBMLDocument doc = SBMLReader.read(new File(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expression.xml"));
        Model model = doc.getModel();
        Reaction react = model.getReaction("exp_degradation");
        react.getKineticLaw().getLocalParameter("K_d").setValue(K_d);
        react.getKineticLaw().getLocalParameter("y").setValue(y);
        react = model.getReaction("exp_expression");
        react.getKineticLaw().getLocalParameter("k_EXE").setValue(k_EXE);
        SBMLWriter writer = new SBMLWriter();
        writer.write(doc, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressor_expression_with_params.xml");
        IBioSimAdaptor.simulateODE(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/expressor_expression_with_params.xml", Utilities.getFilepath() + "/src/main/resources/iBioSimTest/", 100, 1, 1);
    }
    
    
    @Test
    public void testInverterEstimationAndSimulation() throws IOException, XMLStreamException {
        List<Double> smallMoleculesValues = new ArrayList<Double>();
        smallMoleculesValues.add(100.0);
        smallMoleculesValues.add(1000.0);
        smallMoleculesValues.add(10000.0);
        List<String> smallMoleculeTimeSeriesData = new ArrayList<String>();
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/nM_ara/100/timeSeriesPlotPoints.csv");
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/nM_ara/1000/timeSeriesPlotPoints.csv");
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/nM_ara/10000/timeSeriesPlotPoints.csv");
        Map<String, Double> araCExeParams = IBioSimAdaptor.estimateExpresseeParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/bfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
                smallMoleculesValues, smallMoleculeTimeSeriesData, "\"MEAN_FITC.A\"", "\"MEAN_Pacific_Blue.A\"", false);
                
        smallMoleculesValues = new ArrayList<Double>();
        smallMoleculesValues.add(1.0);
        smallMoleculesValues.add(10.0);
        smallMoleculesValues.add(100.0);
        smallMoleculeTimeSeriesData = new ArrayList<String>();
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/tetR_bfp_AAV_EXPRESSEE_pTet_G_araC_REG/M9_glucose_ara/ng_mL_aTc/1/timeSeriesPlotPoints.csv");
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/tetR_bfp_AAV_EXPRESSEE_pTet_G_araC_REG/M9_glucose_ara/ng_mL_aTc/10/timeSeriesPlotPoints.csv");
        smallMoleculeTimeSeriesData.add(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/tetR_bfp_AAV_EXPRESSEE_pTet_G_araC_REG/M9_glucose_ara/ng_mL_aTc/100/timeSeriesPlotPoints.csv");
        Map<String, Double> tetRExeParams = IBioSimAdaptor.estimateExpresseeParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/gfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
                smallMoleculesValues, smallMoleculeTimeSeriesData, "\"MEAN_Pacific_Blue.A\"", "\"MEAN_FITC.A\"", true);
        
        Map<String, Double> pPhlFExpParams = IBioSimAdaptor.estimateExpressorParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/gfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/pPhlF_EXPRESSOR/one_media_MEFL/oneMediaPlotPoints.csv", "\"MEAN_FITC.A\"");
        
        
//        Map<String, Double> pTetExpParams = IBioSimAdaptor.estimateExpressorParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/gfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
//                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/pTet_EXPRESSOR/one_media_MEFL/oneMediaPlotPoints.csv", "\"MEAN_FITC.A\"");
//        Map<String, Double> araCGFPExeParams = IBioSimAdaptor.estimateExpresseeParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/timeSeriesPlotPoints.csv",
//                pTetExpParams, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose/ng_mL_aTc/regulation/regulationPlotPoints.csv",
//                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose/ng_mL_aTc/mediaTitrationPlotPoints.csv", "\"MEAN_FITC.A\"", "\"MEAN_Pacific_Blue.A\"", true, true);
//        Map<String, Double> pLacpBADExpParams = IBioSimAdaptor.estimateExpressorParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/bfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
//                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/pLac_pBAD_EXPRESSOR/one_media_MEFL/oneMediaPlotPoints.csv", "\"MEAN_Pacific_Blue.A\"");
//        Map<String, Double> bfpExeParams = IBioSimAdaptor.estimateExpresseeParameters(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/bfp_EXP/M9_glucose_ara_CAM/timeSeriesPlotPoints.csv",
//                pLacpBADExpParams, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/nM_ara/regulation/regulationPlotPoints.csv",
//                Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter/araC_gfp_EXPRESSEE_pBAD_B_tetR_REG/M9_glucose_ara/nM_ara/mediaTitrationPlotPoints.csv", "\"MEAN_Pacific_Blue.A\"", "\"MEAN_FITC.A\"", false, false);
        
        
        SBMLDocument araCDoc = SBMLAdaptor.createInductionRepressionModel("aTc", "TetR", "AraC");
        double K_d = tetRExeParams.get("K_d");
        double y = tetRExeParams.get("y");
        double k_EXE = tetRExeParams.get("k_EXR");
        double K_r = tetRExeParams.get("K_r");
        double K_i = tetRExeParams.get("K_i");
        Model model = araCDoc.getModel();
        model.getSpecies("aTc").setBoundaryCondition(true);
        model.getSpecies("aTc").setValue(100);
        Reaction react = model.getReaction("AraC_degradation");
        react.getKineticLaw().getLocalParameter("K_d").setValue(K_d);
        react.getKineticLaw().getLocalParameter("y").setValue(y);
        react = model.getReaction("AraC_expression");
        react.getKineticLaw().getLocalParameter("k_EXR").setValue(k_EXE);
        react.getKineticLaw().getLocalParameter("K_r").setValue(K_r);
        react.getKineticLaw().getLocalParameter("K_i").setValue(K_i);
        SBMLDocument bfpDoc = SBMLAdaptor.createInductionActivationModel("ara", "AraC", "BFP");
        K_d = araCExeParams.get("K_d");
        y = araCExeParams.get("y");
        k_EXE = araCExeParams.get("k_EXA");
        K_r = araCExeParams.get("K_a");
        K_i = araCExeParams.get("K_i");
        model = bfpDoc.getModel();
        model.getSpecies("ara").setBoundaryCondition(true);
        model.getSpecies("ara").setValue(100);
        react = model.getReaction("BFP_degradation");
        react.getKineticLaw().getLocalParameter("K_d").setValue(K_d);
        react.getKineticLaw().getLocalParameter("y").setValue(y);
        react = model.getReaction("BFP_expression");
        react.getKineticLaw().getLocalParameter("k_EXA").setValue(k_EXE);
        react.getKineticLaw().getLocalParameter("K_a").setValue(K_r);
        react.getKineticLaw().getLocalParameter("K_i").setValue(K_i);
        SBMLDocument tetRDoc = SBMLAdaptor.createExpressionModel("TetR");
        K_d = pPhlFExpParams.get("K_d");
        y = pPhlFExpParams.get("y");
        k_EXE = pPhlFExpParams.get("k_EXE");
        model = tetRDoc.getModel();
        react = model.getReaction("TetR_degradation");
        react.getKineticLaw().getLocalParameter("K_d").setValue(K_d);
        react.getKineticLaw().getLocalParameter("y").setValue(y);
        react = model.getReaction("TetR_expression");
        react.getKineticLaw().getLocalParameter("k_EXE").setValue(k_EXE);
        List<Model> mods = new ArrayList<Model>();
        mods.add(araCDoc.getModel());
        mods.add(bfpDoc.getModel());
        mods.add(tetRDoc.getModel());
        SBMLDocument composedDoc = SBMLAdaptor.composeExpressionModels(mods);
        SBMLWriter writer = new SBMLWriter();
        writer.write(composedDoc, Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter.xml");
        IBioSimAdaptor.simulateODE(Utilities.getFilepath() + "/src/main/resources/iBioSimTest/inverter.xml", Utilities.getFilepath() + "/src/main/resources/iBioSimTest/", 1000.0, 0.5, 1.0, 314159, 2.0, 1000, 1e-6, 1e-9);
    }
}

