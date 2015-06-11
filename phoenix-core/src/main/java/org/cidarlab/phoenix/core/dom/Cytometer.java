/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Cytometer {
    
    //New cytometer constructor
    public Cytometer() {
        this._lasers = new HashSet<>();
        this.filters = new HashSet<>();
        this.configuration = new HashMap<>();
        this.name = "";
    }
    
    //Parameterized cytometer constructor
    public Cytometer(String name, HashSet<String> lasers, HashSet<String> filters, HashMap<String, ArrayList<String[]>> configuration) {
        this.name = name;
        this._lasers = lasers;
        this.filters = filters;
        this.configuration = configuration;        
    }
    
    @Getter
    @Setter
    private List<Laser> lasers; 
    
    //Lasers -> Key: Wavelength, Value: Power
    @Getter
    @Setter
    private HashSet<String> _lasers; //Remove this eventually.
    
    //Filter -> Key: Midpoint wavelength, Value: Width wavelength
    @Getter
    @Setter
    private HashSet<String> filters;
    
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
}
