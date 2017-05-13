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
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;

/**
 *
 * @author prash
 */
public class ExperimentProcessedData {
    
    @Getter
    @Setter
    private Map<Double, List<String>> regulationData;
    
    @Getter
    @Setter
    String degradationFilepath;
    
    @Getter
    @Setter
    ModuleRole role;
    
    @Getter
    @Setter
    private String expresseChannel;
    
    @Getter
    @Setter
    private String regulatedChannel;
    
    @Getter
    @Setter
    private String expressee;
    
    @Getter
    @Setter
    private String regulated;
    
    @Getter
    @Setter
    private String inducer;
    
    @Getter
    @Setter
    private String expExpressor;
    
    @Getter
    @Setter
    private boolean isFP;
    
    public ExperimentProcessedData(){
        regulationData = new HashMap<Double,List<String>>();
       
    }
    
    public void addRegulationFile(Double regulationVal, List<String> filecontents){
        regulationData.put(regulationVal, filecontents);
    }
    
}
