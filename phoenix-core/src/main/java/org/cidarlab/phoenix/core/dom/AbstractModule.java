/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public abstract class AbstractModule {
    //Module name
    @Getter
    @Setter
    protected String name;

    //Module clothoID
    @Getter
    @Setter
    protected String clothoID;
    
     //Directionality
    @Getter
    @Setter
    protected boolean isForward;
}
