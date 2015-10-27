/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

/**
 *
 * @author evanappleton
 */
public class AssemblyParameters {
 
    //Recommended compositions
    @Setter
    @Getter
    private HashSet<String> recommended;
    
    //Discouraged compositions
    @Setter
    @Getter
    private HashSet<String> discouraged;
    
    //Requried compositions
    @Setter
    @Getter
    private HashSet<String> required;
    
    //Forbidden compositions
    @Setter
    @Getter
    private HashSet<String> forbidden;
    
    //Oligo name root
    @Setter
    @Getter
    private String oligoNameRoot;
    
    //Oligo melting temp
    @Setter
    @Getter
    private String meltingTemperature;
    
    //Oligo min PCR length
    @Setter
    @Getter
    private String minPCRLength;
    
    //Oligo target homology length
    @Setter
    @Getter
    private String targetHomologyLength;
    
    //Oligo minimum cloning length
    @Setter
    @Getter
    private String minCloneLength;
    
    //Oligo maximum primer length
    @Setter
    @Getter
    private String maxPrimerLength;
    
    //Assembly efficiencies
    @Setter
    @Getter
    private List<String> efficiency;
    
    //Assembly method
    @Setter
    @Getter
    private String method;
    
    //Assembly method
    @Setter
    @Getter
    private String clothoID;
    
    //Assembly method
    @Setter
    @Getter
    private String name;
    
    
    //No parameters constructor
    public AssemblyParameters() {
        efficiency = new ArrayList<>();
        forbidden = new HashSet<>();
        required = new HashSet<>();
        discouraged = new HashSet<>();
        recommended = new HashSet<>();
    }
    
    //JSON contructor
    public AssemblyParameters(JSONObject json) {
        
        //Regular strings
        if (json.has("method")) {
            method = json.get("method").toString();
        }
        if (json.has("name")) {
            name = json.get("name").toString();
        }
        if (json.has("clothoID")) {
            clothoID = json.get("clothoID").toString();
        }
        if (json.has("id")) {
            clothoID = json.get("id").toString();
        }
        if (json.has("oligoNameRoot")) {
            oligoNameRoot = json.get("oligoNameRoot").toString();
        }
        if (json.has("meltingTemperature")) {
            meltingTemperature = json.get("meltingTemperature").toString();
        }
        if (json.has("minPCRLength")) {
            minPCRLength = json.get("minPCRLength").toString();
        }
        if (json.has("targetHomologyLength")) {
            targetHomologyLength = json.get("targetHomologyLength").toString();
        }
        if (json.has("minCloneLength")) {
            minCloneLength = json.get("minCloneLength").toString();
        }
        if (json.has("maxPrimerLength")) {
            maxPrimerLength = json.get("maxPrimerLength").toString();
        }
        
        //Lists
        if (json.has("recommended")) {
            HashSet<String> _recommended = new HashSet();
            String rStr = json.get("recommended").toString();
            String[] tokens = rStr.split(";");
            _recommended.addAll(Arrays.asList(tokens));
            recommended = _recommended;
        }
        if (json.has("required")) {
            HashSet<String> _required = new HashSet();
            String rStr = json.get("required").toString();
            String[] tokens = rStr.split(";");
            _required.addAll(Arrays.asList(tokens));
            recommended = _required;
        }
        if (json.has("discouraged")) {
            HashSet<String> _discouraged = new HashSet();
            String rStr = json.get("discouraged").toString();
            String[] tokens = rStr.split(";");
            _discouraged.addAll(Arrays.asList(tokens));
            recommended = _discouraged;
        }
        if (json.has("forbidden")) {
            HashSet<String> _forbidden = new HashSet();
            String rStr = json.get("forbidden").toString();
            String[] tokens = rStr.split(";");
            _forbidden.addAll(Arrays.asList(tokens));
            recommended = _forbidden;
        }
        if (json.has("efficiency")) {
            List<String> _efficiency = new ArrayList();
            String rStr = json.get("efficiency").toString();
            rStr = rStr.substring(1, rStr.length()-1);
            String[] tokens = rStr.split(",");
            _efficiency.addAll(Arrays.asList(tokens));
            efficiency = _efficiency;
        }
    }
    
    //Parameterized constructor
    public AssemblyParameters(HashSet<String> _recommended, HashSet<String> _discouraged, HashSet<String> _required, HashSet<String> _forbidden, String _oligoNameRoot,String _meltingTemperature, String _minPCRLength, String _targetHomologyLength, String _minCloneLength, String _maxPrimerLength, List<String> _efficiency, String _method, String _name, String _clothoID) {
        recommended = _recommended;
        discouraged = _discouraged;
        required = _required;
        forbidden = _forbidden;
        oligoNameRoot = _oligoNameRoot;
        meltingTemperature = _meltingTemperature;
        minPCRLength = _minPCRLength;
        targetHomologyLength = _targetHomologyLength;
        minCloneLength = _minCloneLength;
        maxPrimerLength = _maxPrimerLength;
        efficiency = _efficiency;
        method = _method;
        name = _name;
        clothoID = _clothoID;
    }
    
    //Convert to JSON
    public JSONObject toJSON() {
        
        JSONObject json = new JSONObject();
        
        //String fields
        if (method != null) {
            json.put("method", method);
        }
        if (name != null) {
            json.put("name", name);
        }
        if (clothoID != null) {
            json.put("clothoID", clothoID);
        }
        if (oligoNameRoot != null) {
            json.put("oligoNameRoot", oligoNameRoot);
        }
        if (meltingTemperature != null) {
            json.put("meltingTemperature", meltingTemperature);
        }
        if (minPCRLength != null) {
            json.put("minPCRLength", minPCRLength);
        }
        if (targetHomologyLength != null) {
            json.put("targetHomologyLength", targetHomologyLength);
        }
        if (minCloneLength != null) {
            json.put("minCloneLength", minCloneLength);
        }
        if (maxPrimerLength != null) {
            json.put("maxPrimerLength", maxPrimerLength);
        }
        
        //Lists
        if (recommended != null) {
            String rStr = "";
            for (String r : recommended) {
                rStr = rStr + r + ";";
            }
            rStr = rStr.substring(0, rStr.length()-1);
            json.put("recommended", rStr);
        }
        if (required != null) {
            String rStr = "";
            for (String r : required) {
                rStr = rStr + r + ";";
            }
            rStr = rStr.substring(0, rStr.length()-1);
            json.put("required", rStr);
        }
        if (discouraged != null) {
            String rStr = "";
            for (String r : discouraged) {
                rStr = rStr + r + ";";
            }
            rStr = rStr.substring(0, rStr.length()-1);
            json.put("discouraged", rStr);
        }
        if (forbidden != null) {
            String rStr = "";
            for (String r : forbidden) {
                rStr = rStr + r + ";";
            }
            rStr = rStr.substring(0, rStr.length()-1);
            json.put("forbidden", rStr);
        }
        if (efficiency != null) {
            String rStr = "";
            for (String r : efficiency) {
                rStr = rStr + r + ",";
            }
            rStr = rStr.substring(0, rStr.length()-1);
            json.put("efficiency", rStr);
        }        
        
        return json;
    }    
}
