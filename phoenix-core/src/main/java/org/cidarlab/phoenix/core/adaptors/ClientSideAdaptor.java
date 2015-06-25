/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;

/**
 *
 * @author prash
 */
public class ClientSideAdaptor {
    public static JSONObject convertModuleToJSON(Module module){
        JSONObject json = new JSONObject();
        json.put("name", module.getName());
        json.put("clothoId", module.getClothoID());
        JSONArray children = new JSONArray();
        if((!module.getChildren().isEmpty())){
        
            
            for(Module child:module.getChildren()){
                children.add(convertModuleToJSON(child));
            }
        }
        if(module.getAssignedModules() != null){
            for(Module aModule:module.getAssignedModules()){
                JSONObject assignedModuleObj = new JSONObject();
                assignedModuleObj.put("name", aModule.getName());
                JSONArray exptChildren = new JSONArray();
                if(aModule.getExperiments()!= null){
                    for(Experiment expt:aModule.getExperiments()){
                        JSONObject exptObj = new JSONObject();
                        exptObj.put("name", expt.getExType());
                        exptChildren.add(exptObj);
                    }
                }
                if(exptChildren.size()>0){
                    assignedModuleObj.put("children", exptChildren);
                }
                children.add(assignedModuleObj);
            }
        }
        if(children.size()>0){
            json.put("children", children);
        }
        
        return json;
    }
    
    public static void createFlareFile(String filepath, JSONObject json) throws IOException{
        File file = new File(filepath);
        Writer output = new BufferedWriter(new FileWriter(file));
        output.write(json.toString());
        output.close();
    }
}
