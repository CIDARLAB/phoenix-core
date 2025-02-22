/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.cidarlab.phoenix.core.adaptors.D3Adaptor;
import static org.cidarlab.phoenix.core.controller.PhoenixController.initializeDesign;
import static org.cidarlab.phoenix.core.controller.PhoenixController.preliminaryDataUpload;
import org.cidarlab.phoenix.core.controller.Utilities;
import org.cidarlab.phoenix.core.dom.Module;
import static org.cidarlab.phoenix.core.servlets.ServletIO.writeUpdatedJSON;
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
    private static String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), "UTF-8"));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            value.append(buffer, 0, length);
        }
        return value.toString();
    }
    
    public static String getFilepath()
    {
        String filepath = ClientServlet.class.getClassLoader().getResource(".").getPath();
        System.out.println("File path :: " + filepath);
        if(filepath.contains("WEB-INF/classes/")){
            filepath = filepath.substring(0,filepath.indexOf("WEB-INF/classes/"));
            filepath += "upload/";
        }
        else if(filepath.contains("/target/classes/")){
            filepath = filepath.substring(0,filepath.indexOf("target/classes/"));
            filepath += "src/main/resources/upload/";
        }
        return filepath;
    }
    
    public File partConverter(Part part, String fileName) throws IOException {
        String pathAndName = getFilepath() + fileName;
        
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
        // Disable the cache once and for all
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache");                                   // HTTP 1.0.
        response.setDateHeader("Expires", 0);                                       // Proxies.
        // Figure out what mode to use
        String mode = getValue(request.getPart("mode"));
        // Request was sent from upload.html
        if(mode.equals("upload")){
            // Get files in
            Part plasmidPart = request.getPart("plasmidLib");
            Part featurePart = request.getPart("featureLib");
            Part cytometerPart = request.getPart("fcConfig");
            Part fluorophoreSpectraPart = request.getPart("fSpectra");
            // Get filenames
            String plasmidPartName = getValue(request.getPart("plasmidLibName"));
            String featurePartName = getValue(request.getPart("featureLibName"));
            String cytometerPartName = getValue(request.getPart("fcConfigName"));
            String fluorophoreSpectraPartName = getValue(request.getPart("fSpectraName"));
            // If files are valid, proceed
            if(plasmidPart != null && featurePart != null && cytometerPart != null && fluorophoreSpectraPart != null){
                // Convert servlet.Part objects to java.io.File objects
                File plasmidLib = partConverter(plasmidPart, plasmidPartName);
                File featureLib = partConverter(featurePart, featurePartName);
                File cytometer = partConverter(cytometerPart, cytometerPartName);
                File fluorophoreSpectra = partConverter(fluorophoreSpectraPart, fluorophoreSpectraPartName);
                // Pass files to correct method
                preliminaryDataUpload (featureLib, plasmidLib, fluorophoreSpectra, cytometer);
                // If we made it here then everything was successful
                System.out.println("\n\nINFO: SUCCESS\n\n" + getFilepath());
                holdingData = true;
                PrintWriter out = response.getWriter();
                out.write("Done!");
            } else {
                // If here there was an error with the file upload
                System.out.println("\n\nINFO: FAILURE\n\n");
                holdingData = true;
                PrintWriter out = response.getWriter();
                out.write("Error");
            }
            // Request was sent from specification.html
        } else if(mode.equals("specification")){
            // Get files in
            Part structuralPart = request.getPart("structuralSpec");
            Part functionalPart = request.getPart("functionalSpec");
            // Get filenames
            String structuralPartName = getValue(request.getPart("structuralSpecName"));
            String functionalPartName = getValue(request.getPart("functionalSpecName"));
            // If files are valid, proceed
            if(structuralPart != null && functionalPart != null){
                // Convert servlet.Part objects to java.io.File objects
                File structuralSpec = partConverter(structuralPart, structuralPartName);
                File functionalSpec = partConverter(functionalPart, functionalPartName);
                // Pass files to correct method
                Module bestModule = initializeDesign(structuralSpec, functionalSpec); //Get the best Module
                String flarefilepath = Utilities.getFilepath() + Utilities.getSeparater() + "src" + Utilities.getSeparater() + "main" + Utilities.getSeparater() + "webapp" + Utilities.getSeparater() + "flare.json";
                String couplingfilepath = Utilities.getFilepath() + Utilities.getSeparater() + "src" + Utilities.getSeparater() + "main" + Utilities.getSeparater() + "webapp" + Utilities.getSeparater() + "couplings.json";
                net.sf.json.JSONObject flarejsonObjects = new net.sf.json.JSONObject();
                flarejsonObjects = D3Adaptor.convertModuleToJSON(bestModule);
                D3Adaptor.createJSONFile(flarefilepath, flarejsonObjects.getJSONObject("flare"));
                D3Adaptor.createJSONFile(couplingfilepath, flarejsonObjects.getJSONObject("coupling"));
                
                // If we made it here then everything was successful
                System.out.println("\n\nINFO: SUCCESS\n\n");
                holdingData = true;
                PrintWriter out = response.getWriter();
                out.write("Done!");
            } else {
                // If here there was an error with the file upload
                System.out.println("\n\nINFO: FAILURE\n\n");
                holdingData = true;
                PrintWriter out = response.getWriter();
                out.write("Error");
            }
        } else if(mode.equals("update")){
            String updatedJSON = getValue(request.getPart("treeUpdate"));
            writeUpdatedJSON(updatedJSON);
            System.out.println("\n\nINFO: SUCCESS\n\n");
            holdingData = true;
            PrintWriter out = response.getWriter();
            out.write("Done!");
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
