/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothoapi.clotho3javaapi;

import java.util.EventObject;

/**
 *
 * @author prashantvaidyanathan
 */
public class OnMessageEvent extends EventObject {
    
    
    private String _serverMessage;
    
    public OnMessageEvent(Object source, String serverMessage) {
        super(source);
        _serverMessage = serverMessage;
    }
    
    
    public String getMessage(){
        return _serverMessage;
    }
    
}
