/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;


/**
 * This class has all methods for creating JSON objects for OWL.
 * 
 * @author prash
 */
public class OwlAdaptor {
    
    //Make datasheets for all the experiment parts from a set of experiments
    public static JSONObject createOwlJSON (AssignedModule aModule) {
        JSONObject owlJSON = new JSONObject();
        owlJSON.put("title1", aModule.getName());
        //Fucntion to get sequence?
        owlJSON.put("title2", "Module Information");
        owlJSON.put("Module Role", aModule.getRole());
        owlJSON.put("title3", "Assembly Information");
        
        owlJSON.put("title4", "Testing Data");
        
        int k=0;
        for(Experiment experiment:aModule.getExperiments()){
            
            String title = "title"+(k+5);
            owlJSON.put(title, ("Experiment"+(k+1)));
            owlJSON.put("Type", experiment.getExType());
            
            k++;
        }
        
        return owlJSON;
    }
    
    
    
}
