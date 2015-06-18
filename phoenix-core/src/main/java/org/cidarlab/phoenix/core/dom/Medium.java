package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

public class Medium {

    //Constructor
    public Medium() {
        
    }
    
    //Module measured
    @Setter
    @Getter
    private String name;
    
    //Media type
    @Setter
    @Getter
    private MediaType type;
    
    //Small molecule in solution measured
    @Setter
    @Getter
    private SmallMolecule smallmolecule;
    
    //Concentration of small molecule in solution
    //This should only have a value if a small moecule is specified
    @Setter
    @Getter
    private Double concentration;
    
    //Media type - only limited types of media that can support cell cultures
    public static enum MediaType {
        RICH,
        MINIMAL;
    }   
}
