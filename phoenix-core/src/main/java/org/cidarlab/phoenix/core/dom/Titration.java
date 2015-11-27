/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Titration {
    
    
    @Getter
    @Setter
    private List<Double> titrationValues;
    
    @Getter
    @Setter
    private String units;
    
    @Getter
    @Setter
    private String smallMolecule;
    
    
    public Titration(){
        titrationValues = new ArrayList<>();
    }
}
