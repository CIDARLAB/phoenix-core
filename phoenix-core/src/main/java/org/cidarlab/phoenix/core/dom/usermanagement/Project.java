/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.dom.usermanagement;

import java.util.List;
import lombok.Getter;

/**
 *
 * @author prash
 */
public class Project {
    
    @Getter
    private String name;
    
    @Getter
    private List<User> members;
    
    private User creator;
    
    @Getter
    private String id;
    
}
