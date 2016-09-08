/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import javax.servlet.MultipartConfigElement;
import org.cidarlab.phoenix.core.servlets.ClientServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 * @author prash
 */
public class ClientSideAdaptor {
    
    public static void main(String[] args){
        Server server = new Server(9090);
        //ServerConnector connector = new ServerConnector(server);
        //connector.setPort(9090);
        //server.addConnector(connector);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        //server.setHandler(context);
        
        //ServletHolder holderEvents = new ServletHolder("ws-events", PhagebookServlet.class);
        
        ServletHolder fileUploadServletHolder = new ServletHolder(new ClientServlet());
        fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
        context.addServlet(fileUploadServletHolder, "/ClientServlet");

        
        WebAppContext contextWeb = new WebAppContext();
        contextWeb.setDescriptor(context + "/WEB-INF/web.xml");
        contextWeb.setResourceBase("../phoenix-core/src/main/webapp");
        contextWeb.setContextPath("/");
        contextWeb.setParentLoaderPriority(true);
        //server.setHandler(contextWeb);
       
        HandlerList handlers = new HandlerList();
        handlers.addHandler(context);
        handlers.addHandler(contextWeb);
        server.setHandler(handlers);
        
        try
        {
            server.start();
            server.join();
        }
        catch(Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
    
}
