/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author zchapasko
 */
@WebServlet(name = "ClientServlet", urlPatterns = {"/ClientServlet"})
@MultipartConfig
public class ClientServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
//        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
//        int count;
//        if(request.getSession().getAttribute("count") == null){
//            count = 0;
//        } else {
//            count = (Integer) request.getSession().getAttribute("count");
//        }
//        request.getSession().setAttribute("count", ++count);
//        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>PhoenixServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Welcome to the servlet!</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
//        }
        
        /* SERVLET METHODS BELOW */
        Collection<Part> test = request.getParts();
        
        if(test != null){
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("message","yay");      

            data = dataToSend;
            holdingData = true;
            out.write(data.toString());
        }
        else{
            JSONObject dataToSend = new JSONObject();
            dataToSend.put("message","failure!");      

            data = dataToSend;
            holdingData = true;
            out.write(data.toString());
        }
    }
    
    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        if (holdingData) {
            holdingData = false;
            //return the held data and vacate the stored data
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(data.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processGetRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processPostRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    private JSONObject data;
    private boolean holdingData = false;
}
