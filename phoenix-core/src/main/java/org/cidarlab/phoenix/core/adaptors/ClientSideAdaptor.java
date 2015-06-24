/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
        if(!module.getChildren().isEmpty()){
        
            JSONArray children = new JSONArray();
            for(Module child:module.getChildren()){
                children.add(convertModuleToJSON(child));
            }
            json.put("children", children);
        }
        return json;
    }
    
    public static void createFlareFile(String filepath, JSONObject json) throws IOException{
        FileWriter file = new FileWriter(filepath);
        try{
            file.write(json.toString());
        }catch(Exception e){
            System.out.println("Error ::" + e.getMessage());
        }
        
    }
}
