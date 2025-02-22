/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Cytometer {
    
    //New cytometer constructor
    public Cytometer() {
        this.lasers = new HashSet<>();
        this.filters = new HashSet<>();
        this.configuration = new HashMap<>();
        this.name = "";
    }
    
    //Parameterized cytometer constructor
    public Cytometer(String name, HashSet<String> lasers, HashSet<String> filters, HashMap<String, ArrayList<String[]>> configuration) {
        this.name = name;
        this.lasers = lasers;
        this.filters = filters;
        this.configuration = configuration;        
    }
    
    //Lasers -> Key: Wavelength, Value: Power
    @Getter
    @Setter
    private HashSet<String> lasers;
    
    //Filter -> Key: Midpoint wavelength, Value: Width wavelength
    @Getter
    @Setter
    private HashSet<String> filters;
    
    @Getter
    @Setter
    private Map<String,String> channelMap;
    
    //Setting -> Key: Midpoint wavelength, Value: Width wavelength
    @Getter
    @Setter
    private HashMap<String, ArrayList<String[]>> configuration;
    
    //Name
    @Getter
    @Setter
    private String name;
    
    //Clotho ID
    @Setter
    @Getter
    private String clothoID;
    
    public static String getLaserFilter(String laser,String filter){
        return (laser + ","+filter);
    }
}
