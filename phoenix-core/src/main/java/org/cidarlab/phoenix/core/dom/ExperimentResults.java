/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.phoenix.core.dom.Experiment.ExperimentType;

/**
 *
 * @author prash    
 * @author evanappleton
 */
public class ExperimentResults {
    
    //No args constructor
    public ExperimentResults (ExperimentType eType) {
        if (eType.equals(ExperimentType.DEGRADATION)) {
            meanTimeSeries = new HashMap();
            stdTimeSeries = new HashMap();
        } else if (eType.equals(ExperimentType.EXPRESSION)) {
            steadyState = new ArrayList();
        } else if (eType.equals(ExperimentType.REGULATION)) {
            regulation = new HashMap();
        } else if (eType.equals(ExperimentType.SMALL_MOLECULE)) {
            meanInduction = new HashMap();
            stdInduction = new HashMap();
        } else {
            meanTimeSeries = new HashMap();
            meanInduction = new HashMap();
            stdTimeSeries = new HashMap();
            stdInduction = new HashMap();
        }
    }
    
    //Time series metadata
    @Setter
    @Getter
    private Map<Double, Double> meanTimeSeries;
    
    //Time series metadata
    @Setter
    @Getter
    private Map<Double, Double> stdTimeSeries;
    
    
    //Media induction metadata
    @Setter
    @Getter
    private Map<Double, Double> meanInduction;
    
    //Media induction metadata
    @Setter
    @Getter
    private Map<Double, Double> stdInduction;
    
    //Media induction metadata
    @Setter
    @Getter
    private Map<Double, Double> regulation;
    
    //Media induction metadata
    @Setter
    @Getter
    private List<Double> steadyState;
}
