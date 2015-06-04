/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.clothoapi.clotho3javaapi;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jetty.websocket.WebSocket;
import org.json.simple.JSONValue;

/**
 *
 * @author prashantvaidyanathan
 */

@Slf4j
public class Clotho implements MessageListener
{
    
    public static Object receivedObject;
    private static boolean received;
    private static boolean successfulResult;
    
    public static String requestId;
    public static Channel channel;
    
    
    private static WebSocket.Connection connection;
  
    public Clotho(ClothoConnection clothoConn)
    {
        System.out.println("New Connection established");
        connection = clothoConn.getServerConnection();
        connection.setMaxTextMessageSize(999999999);
        clothoConn.clothoSocket.addMessageListener(this);
    }
    
    private static String getRequestId()
    {
        String reqId = "";
        reqId += System.currentTimeMillis();
        //System.out.println("Generated Req ID :" + reqId);
        return reqId;
    }
    
    
    
    
    
    public static Object queryOne(Map map) 
    {
        JSONObject resultObject = null;
        getRequestId();
        channel = Channel.queryOne;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = JSONObject.fromObject(receivedObject);
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static Object query(Map map) 
    {
        JSONArray resultObject = null;
        getRequestId();
        channel = Channel.query;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60)) 
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = JSONArray.fromObject(receivedObject);
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public static Object submit(String getString) 
    {
        JSONObject resultObject = null;
        getRequestId();
        channel = Channel.submit;
        received = false;
        successfulResult = false;
        try {
            /*StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();*/
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", getString);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = JSONObject.fromObject(receivedObject);
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public static Object get(String getString) 
    {
        JSONObject resultObject = null;
        getRequestId();
        channel = Channel.get;
        received = false;
        successfulResult = false;
        try {
            /*StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();*/
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", getString);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = JSONObject.fromObject(receivedObject);
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public static Object getAll(List<String> getStringList) 
    {
        JSONArray resultObject = null;
        getRequestId();
        channel = Channel.getAll;
        received = false;
        successfulResult = false;
        try {
            /*StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();*/
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", getStringList);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = JSONArray.fromObject(receivedObject);
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public static Object create(Map map) 
    {
        Object resultObject = null;
        getRequestId();
        channel = Channel.create;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = receivedObject;
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static Object set(Map map) 
    {
        Object resultObject = null;
        getRequestId();
        channel = Channel.set;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);
            
            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            //if(map.get("name").equals("mOrange.ref"))
            //    System.out.println(queryString);
            //try{
                connection.sendMessage(queryString);
            //}
            /*catch(Exception e)
            {
                System.out.println("ERROR Message: "+e.getMessage());
                System.out.println("ERROR Localized message"+e.getLocalizedMessage());
                System.out.println("ERROR toString()" + e.toString());
            
            }*/
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = receivedObject;
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            System.out.println("ERROR Message: "+ex.getMessage());
            System.out.println("ERROR Localized message"+ex.getLocalizedMessage());
            System.out.println("ERROR toString()" + ex.toString());
            
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object createAll(List<Map> map) 
    {
        Object resultObject = null;
        getRequestId();
        channel = Channel.createAll;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime <60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = receivedObject;
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    public static Object setAll(List<Map> map) 
    {
        Object resultObject = null;
        getRequestId();
        channel = Channel.setAll;
        received = false;
        successfulResult = false;
        try {
            StringWriter mapStringWriter = new StringWriter();
            JSONValue.writeJSONString(map, mapStringWriter);
            String mapText = mapStringWriter.toString();
            //System.out.println(jsonText);
            requestId = getRequestId();
            Map queryMap = new HashMap();
            queryMap.put("channel", channel.toString());
            queryMap.put("data", map);
            queryMap.put("requestId", requestId);

            StringWriter queryStringWriter = new StringWriter();
            JSONValue.writeJSONString(queryMap, queryStringWriter);
            String queryString = queryStringWriter.toString();
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            connection.sendMessage(queryString);
            while((!received) && (elapsedTime < 60))
            {
                System.out.print("");
                elapsedTime = (System.currentTimeMillis() - startTime)/1000;
            }
            if(elapsedTime >= 60)
            {
                System.out.println("System time out. Please check your Clotho Connection");
            }
            received = false;
            
            if(successfulResult)
            {
                resultObject = receivedObject;
            }
            return resultObject;
            
            
        } catch (IOException ex) {
            Logger.getLogger(Clotho.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    @Override
    public void messageRecieved(OnMessageEvent event) 
    {
        String message = event.getMessage();
        String nullString = null;
        //System.out.println("This is a message :" + message);
        JSONObject messageObject = JSONObject.fromObject(message);
        try
        {
            if (this.requestId.equals(messageObject.get("requestId").toString())) 
            {
                if (messageObject.get("channel").equals("say")) 
                {

                    JSONObject dataObject = JSONObject.fromObject(messageObject.get("data"));
                    if (dataObject.get("text").equals(nullString)) 
                    {
                        System.out.println("No results found!");
                        received = true;
                        receivedObject = messageObject.get("data");

                    } 
                    else 
                    {
                        System.out.println(dataObject.get("text"));
                    }
                }
                if (messageObject.get("channel").equals(this.channel.toString())) 
                {
                    successfulResult = true;
                    receivedObject = messageObject.get("data");
                    received = true;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error accessing one of the object values");
        }
    }
    
}
