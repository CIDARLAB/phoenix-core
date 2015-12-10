/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.controller;

/**
 *
 * @author prash
 */
public class Args {
    
    
    public static final long maxTimeOut = 120;
    public static final String clothoLocation = "wss://localhost:8443/websocket";
    //public static final String flareJSONfilepath = "/C:/Users/zchap_000/Documents/BU_Spring_2015/phoenix-core/phoenix-core/src/main/webapp/flare.json";
    public static final String flareJSONfilepath = Utilities.getFilepath() + "/src/main/webapp/flare.json";
}
