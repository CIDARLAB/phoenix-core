/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import java.io.File;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author evanappleton
 */
public class Sample {
   
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
    
     //If this file is a special type of control
    @Setter
    @Getter
    private boolean isControl;
    
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
    private Date time;
    
    //Control type
    @Setter
    @Getter
    private ControlType controlType;
    
    //fcs files
    @Setter
    @Getter
    private File fcsFile;
    
    //Experiment types
    public enum ControlType {
        BEADS,
        NEGATIVE,
        FLUORESCENT;
    }
}
