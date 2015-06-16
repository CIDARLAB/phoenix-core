package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

public class Medium {

    //Module measured
    @Setter
    @Getter
    public String name;
    
    //Media type
    @Setter
    @Getter
    public MediaType type;
    
    //Small molecule in solution measured
    @Setter
    @Getter
    public String smallmolecule = null;
    
    //Concentration of small molecule in solution
    //This should only have a value if a small moecule is specified
    @Setter
    @Getter
    public Double concentration = null;
    
    //Media type - only limited types of media that can support cell cultures
    public static enum MediaType {
        RICH,
        MINIMAL;
    }   
}
