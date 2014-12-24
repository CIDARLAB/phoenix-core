/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothoapi.clotho3javaapi;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jetty.websocket.WebSocket;
//import lombok.extern.slf4j.Slf4j;
/**
 *
 * @author prashantvaidyanathan
 */

//@Slf4j
public class ClothoWebSocket  implements WebSocket.OnTextMessage
{
    
    private String _messageData;
    private List _listeners = new ArrayList();

    
    @Override
    public void onMessage(String message) {
        _messageData = message;
        fireMessageEvent();
    }
    
    public synchronized void addMessageListener(MessageListener l)
    {
        _listeners.add(l);
    }
    
    public synchronized void removeMessageListener(MessageListener l)
    {
        _listeners.remove(l);
    }
    
    
    private synchronized void fireMessageEvent()
    {
        
        OnMessageEvent event = new OnMessageEvent(this,_messageData);
        //addMessageListener(new Message)
        Iterator listeners = _listeners.iterator();
        //messageRecieved(event);
        //System.out.println(_listeners.size());
        while(listeners.hasNext()){
            //System.out.println("Reached here!!");
            ((MessageListener) listeners.next()).messageRecieved(event);
        }
    }
    
    
    
    @Override
    public void onOpen(Connection connection) {
        System.out.println("Connection has been established!\nConnection id : "+connection);
        /*try {
            String helloString = "{\"channel\":\"say\",\"data\":\"Establish Connection!\",\"requestId\":1}";
            connection.sendMessage(helloString);
        
        } catch (IOException ex) {
            Logger.getLogger(ClothoWebSocket.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    @Override
    public void onClose(int i, String string) {
        System.out.println("Connection closed.\nConnection Id : "+i+"\nClosing Message : "+string);
    }
    
}
