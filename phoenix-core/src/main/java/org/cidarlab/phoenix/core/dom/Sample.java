/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.io.File;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Sample {
   
    //Constructor
    public Sample(String _name, SampleType _type) {
        name = _name;
        clothoID = _name;
        type = _type;
    }
    
    //Module measured
    @Setter
    @Getter
    public String name;
    
    //Module measured
    @Setter
    @Getter
    public String clothoID;
    
    //Module measured
    @Setter
    @Getter
    public Medium media;
    
    //Polynucleotide measured
    @Setter
    @Getter
    private Polynucleotide polynucleotide;
    
    //Test strain
    @Setter
    @Getter
    private Strain strain;
    
    //Time of experiment (for dynamic measurements)
    @Setter
    @Getter
    private Integer time;
    
    //Control type
    @Setter
    @Getter
    private SampleType type;
    
    //fcs files
    @Setter
    @Getter
    private File fcsFile;
    
    //Experiment types
    public enum SampleType {
        EXPERIMENT,
        BEADS,
        NEGATIVE,
        FLUORESCENT,
        EXPRESSION_DEGRATATION,
        REGULATION;
    }
}
