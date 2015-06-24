/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import static org.cidarlab.phoenix.core.controller.PhoenixController.preliminaryDataUpload;
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
    public String getFilepath() {
        //String filepath = "/home/prash/cidar/phoenix-core/phoenix-core/src/main";
        String filepath = "/C:/Users/zchap_000/Documents/BU_Spring_2015/phoenix-core/phoenix-core/src/main";
        return filepath;
    }
    
    public File partConverter(Part part, String fileName) throws IOException {
        String pathAndName = getFilepath() + "/webapp/tmp/" + fileName;
        
        OutputStream out = null;
        InputStream filecontent = null;
        
        try {
            out = new FileOutputStream(new File(pathAndName));
            filecontent = part.getInputStream();

            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (FileNotFoundException fne) {
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, fne);
        }
        
        return new File(pathAndName);
    }
    
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException, Exception {
        Part plasmidPart = request.getPart("plasmidLib");
        Part featurePart = request.getPart("featureLib");
        Part cytometerPart = request.getPart("fcConfig");
        Part fluorophoreSpectraPart = request.getPart("fSpectra");

        if(plasmidPart != null && featurePart != null && cytometerPart != null && fluorophoreSpectraPart != null){
            File plasmidLib = partConverter(plasmidPart, "plasmid_lib.gb");
            File featureLib = partConverter(featurePart, "feature_lib.gb");
            File cytometer = partConverter(cytometerPart, "cytometer_config.csv");
            File fluorophoreSpectra = partConverter(fluorophoreSpectraPart, "fp_spectra.csv");
            
            preliminaryDataUpload (featureLib, plasmidLib, fluorophoreSpectra, cytometer);
            
            System.out.println("\n\nSUCCESS\n\n");
            holdingData = true;
            PrintWriter out = response.getWriter();
            out.write("Done!");
        } else {
            System.out.println("\n\nFAILURE\n\n");
            holdingData = true;
            PrintWriter out = response.getWriter();
            out.write("Error");
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
        try {
            processPostRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ClientServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
