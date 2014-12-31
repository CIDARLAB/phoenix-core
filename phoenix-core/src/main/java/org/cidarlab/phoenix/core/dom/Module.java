/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.List;

/**
 *
 * @author prash
 */
public class Module {
    //Need to formally define these methods and constructors. But this is the basic essence of this Class. 
    LTLFunction function;
    List<Module> parents;
    List<Module> children;
    int stage;
    Experiment experiment;
    Interaction interaction;
}
