/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Cytometer {
    
    //New cytometer constructor
    public Cytometer() {
        this.lasers = new HashMap<>();
        this.filters = new HashMap<>();
        this.configuration = new HashMap<>();
        this.name = "";
    }
    
    //Parameterized cytometer constructor
    public Cytometer(String name, HashMap<Integer, Integer> lasers, HashMap<Integer, Integer> filters, HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>> configuration) {
        this.name = name;
        this.lasers = lasers;
        this.filters = filters;
        this.configuration = configuration;        
    }
    
    //Lasers -> Key: Wavelength, Value: Power
    @Getter
    @Setter
    private HashMap<Integer, Integer> lasers;
    
    //Filter -> Key: Midpoint wavelength, Value: Width wavelength
    @Getter
    @Setter
    private HashMap<Integer, Integer> filters;
    
    //Setting -> Key: Midpoint wavelength, Value: Width wavelength
    @Getter
    @Setter
    private HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>> configuration;
    
    //Name
    @Getter
    @Setter
    private String name;
}
