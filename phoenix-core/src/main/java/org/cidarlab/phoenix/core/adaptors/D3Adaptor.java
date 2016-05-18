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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.Experiment;
import org.cidarlab.phoenix.core.dom.Module;

/**
 *
 * @author prash
 */
public class D3Adaptor {
    
    
    /*private static void getControlJSON(Module module, Map<String,JSONObject> map){
        if(map == null){
            map = new HashMap();
        }
        for(AssignedModule amodule:module.getAssignedModules()){
            for(AssignedModule control:amodule.getControlModules()){
                if(!map.containsKey(control.getName())){
                    JSONObject ctrl = new JSONObject();
                    ctrl.put("name", control.getName());
                    if(control.getClothoID() == null){
                        ctrl.put("clothoId", "Not in Clotho Yet");
                    }else{
                       ctrl.put("clothoId", control.getClothoID()); 
                    }
                       
                    switch(control.getRole()){
                        case EXPRESSION_DEGRATATION_CONTROL:
                            ctrl.put("hex", "0000FF");
                            break;
                        case REGULATION_CONTROL:
                            ctrl.put("hex", "0001FF");
                            break;
                        case COLOR_CONTROL:
                            ctrl.put("hex", "0002FF");
                            break;
                        default: 
                            ctrl.put("hex", "#000000");
                    }
                    map.put(control.getName(), ctrl);
                }
            }
        }
        for(Module child:module.getChildren()){
            getControlJSON(child,map);
        }
    }
    */
    
    private static JSONObject convertModuleToJSON(Module module, Set<String> controls, Map<String,List<String>> parentChildNodes){
        JSONObject json = new JSONObject();
        //json.put("name", module.getName());
        //json.put("clothoId", module.getClothoID());
        /*System.out.println("Module Name :: " + module.getName());
        for(AssignedModule amodule:module.getAssignedModules()){
            System.out.println("Assgined Module Name :: " + amodule.getName());
            for(AssignedModule control:amodule.getControlModules()){
                System.out.println("Control Module Name :: " + control.getName());
            }
            for (Experiment exp : amodule.getExperiments()) {
                System.out.println("Experiment Name :: " + exp.getName());
            }
        }*/
        
        json.put("name", module.getName());
        if(module.getClothoID()!= null){
            json.put("clothoId", module.getClothoID());
        }
        else{
            json.put("clothoId", "Not in Clotho Yet");
        }
        json.put("hex", "#FF0000");
        
        JSONArray children = new JSONArray();
        for(Module child:module.getChildren()){
            children.add(convertModuleToJSON(child,controls,parentChildNodes));
        }
        for(AssignedModule amodule:module.getAssignedModules()){
            JSONObject ajson = new JSONObject();
            ajson.put("name", amodule.getName());
            if (amodule.getClothoID() != null) {
                ajson.put("clothoId", amodule.getClothoID());
            } else {
                ajson.put("clothoId", "Not in Clotho Yet");
            }
            ajson.put("hex", "#00FF00");
            JSONArray achildren = new JSONArray();
            
            for(AssignedModule control:amodule.getControlModules()){
                if(controls.contains(control.getName())){
                    if(!parentChildNodes.containsKey(amodule.getName())){
                        parentChildNodes.put(amodule.getName(), new ArrayList<String>());
                    }
                    parentChildNodes.get(amodule.getName()).add(control.getName());
                }
                else {
                    JSONObject cjson = new JSONObject();
                    cjson.put("name", control.getName());
                    if (control.getClothoID() != null) {
                        cjson.put("clothoId", control.getClothoID());
                    } else {
                        cjson.put("clothoId", "Not in Clotho Yet");
                    }
                    switch (control.getRole()) {
                        case EXPRESSION_DEGRATATION_CONTROL:
                            cjson.put("hex", "0001FF");
                            break;
                        case REGULATION_CONTROL:
                            cjson.put("hex", "0002FF");
                            break;
                        case COLOR_CONTROL:
                            cjson.put("hex", "0003FF");
                            break;
                        default:
                            cjson.put("hex", "#FFFFFF");
                    }
                    achildren.add(cjson);
                    controls.add(control.getName());
                }
                
            }
            for(Experiment exp:amodule.getExperiments()){
                JSONObject ejson = new JSONObject();
                ejson.put("name", exp.getName());
                if (exp.getClothoID() != null) {
                    ejson.put("clothoId", exp.getClothoID());
                } else {
                    ejson.put("clothoId", "Not in Clotho Yet");
                }
                switch(exp.getExType()){
                    case EXPRESSION:
                        ejson.put("hex", "#0010FF");
                    case DEGRADATION:
                        ejson.put("hex", "#0020FF");
                    case REGULATION:
                        ejson.put("hex", "#0030FF");
                    case SMALL_MOLECULE:
                        ejson.put("hex", "#0040FF");
                    case SIGNAL_MISMATCH:
                        ejson.put("hex", "#0050FF");
                    case PROMOTER_CONTEXT:
                        ejson.put("hex", "#0060FF");
                    case RBS_CONTEXT:
                        ejson.put("hex", "#0070FF");
                    case READ_THROUGH:
                        ejson.put("hex", "#0080FF");
                    case ORTHOGONALITY:
                        ejson.put("hex", "#0090FF");
                    case RECOMBINATION:
                        ejson.put("hex", "#00A0FF");
                    case TRANSCRIPTIONAL_INTERFERENCE:
                        ejson.put("hex", "#00B0FF");
                    case SPECIFICATION:
                        ejson.put("hex", "#00C0FF");
                    default:
                        ejson.put("hex", "#FFFFFF");
                        break;
                }
                achildren.add(ejson);
            }
            
            if(!achildren.isEmpty()){
                ajson.put("children", achildren);
            }
            
            children.add(ajson);
        }
        if(!children.isEmpty()){
            json.put("children", children);
        }
        
        
        
        return json;
    }
    
    public static JSONObject convertModuleToJSON(Module module){
        JSONObject jsons = new JSONObject();
        Map<String,List<String>> parentChildNodes = new HashMap();
        
        jsons.put("flare", convertModuleToJSON(module,new HashSet(),parentChildNodes));
        JSONArray parentChildList = new JSONArray();
        for(String parent:parentChildNodes.keySet()){
            JSONObject parentChildNode = new JSONObject();
            parentChildNode.put("parent", parent);
            JSONArray children = new JSONArray();
            for(String child:parentChildNodes.get(parent)){
                children.add(child);
            }
            parentChildNode.put("children", children);
            parentChildList.add(parentChildNode);
        }
        JSONObject coupling = new JSONObject();
        coupling.put("coupling", parentChildList);
        jsons.put("coupling", coupling);
        return jsons;
        
    }
    
    public static void createJSONFile(String filepath, JSONObject json) throws IOException{
        File file = new File(filepath);
        Writer output = new BufferedWriter(new FileWriter(file));
        output.write(json.toString());
        output.close();
    }
}
