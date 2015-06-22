package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

public class Strain {

    public Strain (String _name) {
        name = _name;
    }
    
    //Module measured
    @Setter
    @Getter
    private String name;
}
