/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Fluorophore;
import org.clothocad.model.Feature;

/**
 * This class contains the methods for determining where to place fluorescent proteins in a phoenix
 * It inputs a module graph, a cytometer with settings, and fluorophore set and returns a modified modified graph
 * 
 * @author evanappleton
 */
public class FluorescentProteinSelector {
    
    //Blank constructor
    public FluorescentProteinSelector() {
        this.cytometerConfiguration = new HashMap<>();
        this.selectedFluorophores = new HashSet<>();
    }
    
    //Blank constructor
    public FluorescentProteinSelector(HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>> config, HashSet<Feature> fluorophores) {
        this.cytometerConfiguration = config;
        this.selectedFluorophores = fluorophores;
    }
    
    //
    
    //Main solver method
    public void solve(HashSet<Fluorophore> FPs, Cytometer cytometer){
        
    }
    
    //
    @Getter
    @Setter
    private HashSet<Feature> selectedFluorophores;
    
    //LTL function associated with this module
    @Getter
    @Setter
    private HashMap<HashMap<Integer, Integer>, HashMap<Integer, Integer>> cytometerConfiguration;
}
