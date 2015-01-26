/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothoapi.clotho3javaapi;

import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

/**
 *
 * @author prashantvaidyanathan
 */
@Slf4j
public class ClothoConnection {
    private static WebSocketClientFactory factory;
    public static ClothoWebSocket clothoSocket;
    private Future fut;
    
    
    @Getter
    private WebSocket.Connection serverConnection; 
    
    public ClothoConnection(String clothoURI)
    {
        factory = new WebSocketClientFactory();
        clothoSocket = new ClothoWebSocket();
        try {
            URI uri = new URI(clothoURI);
            factory.start();
            WebSocketClient wsClient = factory.newWebSocketClient();
            wsClient.setMaxTextMessageSize(99999999);
            fut = wsClient.open(uri, clothoSocket);
            
            serverConnection = (WebSocket.Connection) fut.get();
                    //.get(10, TimeUnit.SECONDS);
            
        } catch (Exception ex) {
            Logger.getLogger(ClothoConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void closeConnection()
    {
        System.out.println("Closing Connection");
        try {
            factory.stop();
        } catch (Exception ex) {
            Logger.getLogger(ClothoConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
