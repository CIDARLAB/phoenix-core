/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom.usermanagement;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class User{
    
    @Getter
    @Setter
    private String firstName;
    
    @Getter
    @Setter
    private String lastName;
    
    @Getter
    @Setter
    private String id;
    
    @Getter
    @Setter
    private String email;
    
    private byte[] salt;
    
       
    
}
