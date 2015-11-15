/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;

/**
 *
 * @author prash
 */
public class PigeonAdaptor {
    
    public static List<String> generatePigeonCode(Module module){
        List<String> pigeonLines = new ArrayList<String>();
        
        return pigeonLines;
    }
    
    public static String generatePigeonString(Module module,boolean forwardMode){
        String pigeonString = "";
        if (forwardMode) {
            for (PrimitiveModule pmodule : module.getSubmodules()) {
                pigeonString += getPigeonFeatureString(pmodule, forwardMode);
            }
        }
        else{
            for(int i=(module.getSubmodules().size()-1);i>=0;i--){
                pigeonString += getPigeonFeatureString(module.getSubmodules().get(i),forwardMode);
            }
        }
        return pigeonString;
    }
    
    public static String getPigeonFeatureString(PrimitiveModule pmodule,boolean forwardMode){
        String featureString = "";
        if(((!pmodule.isForward())&&(forwardMode)) || ((pmodule.isForward()) && (!forwardMode)))
            featureString += "<";
        switch(pmodule.getModuleFeature().getRole()){
            case PROMOTER:
            case PROMOTER_REPRESSIBLE:
            case PROMOTER_INDUCIBLE:
            case PROMOTER_CONSTITUTIVE:
                featureString += "p ";
                break;
            case RBS:
                featureString += "r ";
                break;
            case CDS:
            case CDS_REPRESSOR:
            case CDS_ACTIVATOR:
            case CDS_REPRESSIBLE_REPRESSOR:
            case CDS_ACTIVATIBLE_ACTIVATOR:
            case CDS_LINKER:
            case CDS_TAG:
            case CDS_RESISTANCE:
            case CDS_FLUORESCENT:
                featureString += "c ";
                break;
            case CDS_FLUORESCENT_FUSION:
                featureString += "f ";
                break;
            case TERMINATOR:
                featureString += "t ";
                break;
        }
        return featureString;
    }
    
    
}
