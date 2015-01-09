/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

import java.util.HashSet;
import org.cidarlab.phoenix.core.dom.Cytometer;
import org.cidarlab.phoenix.core.dom.Fluorophore;

/**
 * This class contains the methods for determining where to place fluorescent proteins in a phoenix
 * It inputs a module graph, a cytometer with settings, and fluorophore set and returns a modified modified graph
 * 
 * @author evanappleton
 */
public class FluorescentProteinSelector {
    
    //Choose n FPs given a machine with lasers and filters, but no configuration
    public static HashSet<Fluorophore> solve(HashSet<Fluorophore> FPs, Cytometer cytometer, Integer n){
        return null;
    }
    
    //From FP set, determine optimized configuration of lasers and filters for a machine
    public static Cytometer getOptimizedCytomterConfiguration (HashSet<Fluorophore> FPs, Cytometer cytometer) {
        return null;
    }    
}
