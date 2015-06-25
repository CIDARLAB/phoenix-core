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
        if((!module.getChildren().isEmpty()) || (!module.getExperiments().isEmpty())){
        
            JSONArray children = new JSONArray();
            for(Module child:module.getChildren()){
                children.add(convertModuleToJSON(child));
            }
            for(Experiment expt:module.getExperiments()){
                JSONObject exptObj = new JSONObject();
                exptObj.put("name", expt.getName());
                children.add(exptObj);
            }
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
