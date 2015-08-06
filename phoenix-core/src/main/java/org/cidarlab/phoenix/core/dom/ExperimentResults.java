/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.phoenix.core.dom.Experiment.ExperimentType;

/**
 *
 * @author evanappleton
 */
public class ExperimentResults {
    
    //No args constructor
    public ExperimentResults (ExperimentType eType) {
        if (eType.equals(ExperimentType.DEGRADATION)) {
            timeSeries = new HashMap();
        } else if (eType.equals(ExperimentType.EXPRESSION)) {
            steadyState = new ArrayList();
        } else if (eType.equals(ExperimentType.REGULATION)) {
            regulation = new HashMap();
        } else if (eType.equals(ExperimentType.SMALL_MOLECULE)) {
            induction = new HashMap();
        }
    }
    
    //Time series metadata
    @Setter
    @Getter
    private HashMap<Double, Double> timeSeries;
    
    //Media induction metadata
    @Setter
    @Getter
    private HashMap<Medium, Double> induction;
    
    //Media induction metadata
    @Setter
    @Getter
    private HashMap<Double, Double> regulation;
    
    //Media induction metadata
    @Setter
    @Getter
    private List<Double> steadyState;
}
