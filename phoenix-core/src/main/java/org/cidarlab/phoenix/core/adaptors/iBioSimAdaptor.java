/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.text.parser.FormulaParserLL3;
import org.sbml.jsbml.text.parser.IFormulaParser;
import org.sbml.jsbml.text.parser.ParseException;

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
    
    public static void makeSBMLDocument(Module m) {
     
        SBMLDocument sbmlDoc = new SBMLDocument();
        Model model = new Model();
        if (m.getRole().equals(ModuleRole.EXPRESSEE) || m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATIBLE_ACTIVATOR) || m.getRole().equals(ModuleRole.EXPRESSEE_ACTIVATOR) || m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSIBLE_REPRESSOR) || m.getRole().equals(ModuleRole.EXPRESSEE_REPRESSOR) || m.getRole().equals(ModuleRole.EXPRESSION_DEGRATATION_CONTROL) || m.getRole().equals(ModuleRole.EXPRESSOR)) {
            model = makeEXPDEGModel(m);
        } else if (m.getRole().equals(ModuleRole.REGULATION_CONTROL)) {
            model = makeRegulationModel(m);
        } else if (m.getRole().equals(ModuleRole.TRANSCRIPTIONAL_UNIT)) {
            model = makeTUModel(m);
        } else if (m.getRole().equals(ModuleRole.HIGHER_FUNCTION)) {
            model = makeSpecificationModel(m);
        }
        sbmlDoc.setModel(model);
    }    
    
    //Makes SBML Model for single expression cassettes
    private static Model makeEXPDEGModel (Module m) {
        
        //Search the module to find the name of the species being expressed i.e. REGULATORS or FPs
        
        
        //Create new model reactions for protein expression and degradation        
        Species product = new Species();
        
        Reaction proteinExpression = new Reaction();                                                      
        proteinExpression.createProduct(product);        
        
        KineticLaw expressionKL = new KineticLaw(proteinExpression);
        expressionKL.setMath(parseFormula("k0"));
        
        Reaction proteinDegradation = new Reaction();          
        
        Model model = new Model();
        model.addReaction(proteinExpression);
        model.addReaction(proteinDegradation);
        return model;
    }
    
    //Makes SBML Model for regulation controls
    private static Model makeRegulationModel (Module regControl) {
        
        //Search primitive modules for promoter and regulator arcs?
        
        //Create expression and degradation reactions for both the transcriptional unit expressing regulator and being regulated
        //Search arcs to create links?
        
        Model model = new Model();
        return model;
    }
    
    //Makes SBML Model for Transcriptional Units
    private static Model makeTUModel (Module TU) {
        
        //Find the child expressor and expressee modules
        
        //Grab the expression reaction from the expressor and degradation reactions from the expressees
        
        Model model = new Model();
        return model;
    }
    
    
    private static Model makeSpecificationModel (Module specified) {
        
        //Find child TUs
        
        //Search primitive modules for promoter and regulator arcs?
        
        //Use expression and degradation reactions from TUs, link together and add regulation modification to to reactions based upon arcs
        
        Model model = new Model();
        return model;
    }

    //Parse a string fomula to make an ASTNode for a KineticLaw object
    //String syntax at: http://sbml.org/Special/Software/JSBML/latest-stable/build/apidocs/org/sbml/jsbml/text/parser/FormulaParserLL3.html
    public static ASTNode parseFormula(String formula) {
        ASTNode mathFormula = null;
        try {
            IFormulaParser parser = new FormulaParserLL3(new StringReader(""));
            mathFormula = ASTNode.parseFormula(formula, parser);
        } catch (ParseException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return mathFormula;
    }
}
