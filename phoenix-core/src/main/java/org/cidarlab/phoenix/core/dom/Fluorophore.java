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
public class Fluorophore extends Feature {
    
    
    //Default fluorophore constructor
    public Fluorophore(String name, Integer oligo, Double brightness, Double ex_max, Double em_max, HashMap<Double, Double> em_spectrum, HashMap<Double,Double> ex_spectrum) {
        this.oligomerization = oligo;
        this.brightness = brightness;
        this.excitation_max = ex_max;
        this.emission_max = em_max;
        this.name = name;
        this.em_spectrum = em_spectrum;
        this.ex_spectrum = ex_spectrum;
    }
    
    //Default fluorophore constructor
    public Fluorophore() {
        this.oligomerization = 0;
        this.brightness = 0.0;
        this.excitation_max = 0.0;
        this.emission_max = 0.0;
        this.name = "";
        this.em_spectrum = new HashMap<>();
        this.ex_spectrum = new HashMap<>();
    }
    
    //Oligomerization of fluorophore
    @Setter
    @Getter
    private Integer oligomerization;
    
    //Excitation maximum
    @Setter
    @Getter
    private Double excitation_max;
    
    //Excitation maximum
    @Setter
    @Getter
    private Double emission_max;
    
    //Brightness relative to wtGFP
    @Setter
    @Getter
    private Double brightness;
    
    //Name of fluorophore
    @Setter
    @Getter
    private String name;
    
    //Fluorophore emission/absorption spectrum
    @Setter
    @Getter
    private HashMap<Double, Double> em_spectrum;
    
    //Fluorophore excitation spectrum
    @Setter
    @Getter
    private HashMap<Double, Double> ex_spectrum;
    
    //Clotho ID
    @Setter
    @Getter
    private String clothoID;
}
