/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dataprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.cidarlab.phoenix.core.adaptors.IBioSimAdaptor;
import org.cidarlab.phoenix.core.adaptors.SBMLAdaptor;
import org.cidarlab.phoenix.core.controller.PhoenixController;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;

/**
 *
 * @author prash
 */
public class ParameterEstimation {
    
    public static void estimateDegradationParameters(AssignedModule amodule, String sbmldirectory, List<String> dataLines){
        String fname = PhoenixController.getExpresseeFeature(amodule).getName();
        SBMLDocument sbmldoc = getSBMLDocument(amodule,fname);
        
        String speciesId = getDegradationSpeciesId(sbmldoc);
        List<String> csvFileLines = new ArrayList<String>();
        csvFileLines.add("Time,"+speciesId);
        csvFileLines.addAll(dataLines);
        sbmldoc = SBMLAdaptor.convertParamsLocalToGlobal(sbmldoc);
        String sbmldocFilepath = sbmldirectory + "/" + amodule.getShortName() + "_deg.xml";
        createSBMLFile(sbmldoc,sbmldocFilepath);
        List<List<String>> allExpLines = new ArrayList();
        allExpLines.add(csvFileLines);
        try {
            Map<String,Double> params = IBioSimAdaptor.estimateParametersWithFileLines(sbmldocFilepath, getGlobalParams(sbmldoc), allExpLines);
            writeToNewSBMLDoc(sbmldoc,sbmldocFilepath,params);
        } catch (IOException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void writeToNewSBMLDoc(SBMLDocument doc, String filepath, Map<String,Double> params){
        Model model = doc.getModel();
        ListOf<Parameter> paramsList = model.getListOfParameters();
        for(Parameter param:paramsList){
            if(params.containsKey(param.getId())){
                param.setValue(params.get(param.getId()));
            }
        }
        SBMLWriter writer = new SBMLWriter();
        try {
            writer.writeSBMLToFile(doc, filepath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SBMLException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static List<String> getGlobalParams(SBMLDocument doc){
        List<String> params = new ArrayList<String>();
        Model model = doc.getModel();
        ListOf<Parameter> paramslist = model.getListOfParameters();
        for(Parameter param:paramslist){
            params.add(param.getId());
        }
        return params;
    }
    
    private static void createSBMLFile(SBMLDocument doc, String filepath){
        SBMLWriter writer = new SBMLWriter();
        try {
            writer.writeSBMLToFile(doc, filepath);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SBMLException ex) {
            Logger.getLogger(ParameterEstimation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static String getDegradationSpeciesId(SBMLDocument doc){
        Model model = doc.getModel();
        ListOf<Species> species = model.getListOfSpecies();
        for(Species sp:species){
            return sp.getId();
        }
        return "";
    } 
    
    private static SBMLDocument getSBMLDocument(AssignedModule amodule, String fname){
        for(SBMLDocument sbmldoc: amodule.getSBMLDocument()){
            Model model = sbmldoc.getModel();
            ListOf<Species> species = model.getListOfSpecies();
            for(Species sp:species){
                if(sp.getName().equals(fname)){
                    return sbmldoc;
                }
            }
        }
        return null;
    }
}
