package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

public class Medium {

    //Constructor
    public Medium(String _name, MediaType _type) {
        this.name = _name;
        this.type = _type;
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
    
    //Media type - only limited types of media that can support cell cultures
    public static enum MediaType {
        RICH,
        MINIMAL;
    }   
}
