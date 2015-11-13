/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author prash
 */
public class RAdaptor {
    public static void getAllData(String directoryPath) throws IOException{
        File node = new File(directoryPath);
        System.out.println("Folder Structure :: "+node.getAbsolutePath());
        if(node.isDirectory()){
            String[] subfolders = node.list();
            System.out.println("Canonical File path");
            File[] fileList = node.listFiles();
            for(int i=0;i<fileList.length;i++){
                if(fileList[i].isDirectory())
                    if(!findTimeSeriesPlotPointsCSV(fileList[i].getCanonicalPath()).equals(""))
                        System.out.println("File :: "+findTimeSeriesPlotPointsCSV(fileList[i].getCanonicalPath()));
            }
        }
    }
    public static String findTimeSeriesPlotPointsCSV(String directoryPath) throws IOException{
        File node = new File(directoryPath);
        String filePath = "";
        File[] fileList = node.listFiles();
        
        for(int i=0;i<fileList.length;i++){
            if(fileList[i].getName().equals("timeSeriesPlotPoints.csv")){
                //System.out.println("Found it :: " + fileList[i].getCanonicalPath());
                return fileList[i].getCanonicalPath();
            }
            else{
                if(fileList[i].isDirectory()){
                    return findTimeSeriesPlotPointsCSV(fileList[i].getCanonicalPath());
                }
            }           
        }
        return "";
        
    }
}
