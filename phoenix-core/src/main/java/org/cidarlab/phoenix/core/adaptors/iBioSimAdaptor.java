/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author evanappleton
 */
public class iBioSimAdaptor {
    
    //Run simulation on modules with k-values to predict best matches in next unassigned stage
    public static List<Module> runSimulations (List<Module> modules) {
        return null;
    }
    
    /*
    * METHODS FOR SBML MODEL CREATION
    */    
    
    public static SBMLDocument makeSBMLDocument(Module m) {
     
        SBMLDocument sbmlDoc = new SBMLDocument();
        Model model = new Model();
        if (m.getRole().equals(ModuleRole.EXPRESSEE) || m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR)) {
            model = makeEXPRESSEEModel();
        } else if (m.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL)) {
            model = makeEXPDEGModel();
        } else if (m.getRole().equals(ModuleRole.REGULATION_CONTROL)) {
            model = makeRegulationModel();
        } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            model = makeTUModel(new ArrayList(), new ArrayList());
        } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
            model = makeSpecificationModel(new ArrayList());
        }
        sbmlDoc.setModel(model);
        
        return sbmlDoc;
    }
    
    //Makes SBML Model for EXPRESSEE
    private static Model makeEXPRESSEEModel () {
        return null;
    }
    
    //Makes SBML Model for EXPRESSEE
    private static Model makeEXPDEGModel () {
        return null;
    }
    
    //Makes SBML Model for EXPRESSEE
    private static Model makeRegulationModel () {
        return null;
    }
    
    //Makes SBML Model for EXPRESSOR
    private static Model makeEXPRESSORModel () {
        return null;
    }
    
    private static Model makeTUModel (ArrayList<Module> expressors, ArrayList<Module> expressees) {
        return null;
    }
    
    private static Model makeSpecificationModel (ArrayList<Module> TUs) {
        return null;
    }
}
