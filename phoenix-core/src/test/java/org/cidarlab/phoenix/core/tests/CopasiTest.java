/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.tests;

import org.cidarlab.phoenix.core.dom.Arc;
import org.cidarlab.phoenix.core.dom.Arc.ArcRole;
import org.cidarlab.phoenix.core.dom.AssignedModule;
import org.cidarlab.phoenix.core.dom.ComponentType;
import org.cidarlab.phoenix.core.dom.Feature;
import org.cidarlab.phoenix.core.dom.Feature.FeatureRole;
import org.cidarlab.phoenix.core.dom.Module;
import org.cidarlab.phoenix.core.dom.Module.ModuleRole;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.grammars.PhoenixGrammar;
import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author prash
 */
public class CopasiTest {
    public static void createSBMLFile(){
        SBMLDocument doc = new SBMLDocument();
        
    }
    
    public static Module createTestModule(){
        Module root = new Module("notGate");
        root.setRoot(true);
        
        Feature p1 = new Feature();
        Feature r1 = new Feature();
        Feature c1 = new Feature();
        Feature t1 = new Feature();
        
        Feature p2 = new Feature();
        Feature r2 = new Feature();
        Feature c2 = new Feature();
        Feature t2 = new Feature();
        
        p1.setRole(FeatureRole.PROMOTER_INDUCIBLE);
        r1.setRole(FeatureRole.RBS);
        c1.setRole(FeatureRole.CDS_REPRESSOR);
        t1.setRole(FeatureRole.TERMINATOR);
        
        p2.setRole(FeatureRole.PROMOTER_REPRESSIBLE);
        r2.setRole(FeatureRole.RBS);
        c2.setRole(FeatureRole.CDS);
        t2.setRole(FeatureRole.TERMINATOR);
        
        p1.setName("p1");
        r1.setName("r1");
        c1.setName("c1");
        t1.setName("t1");
        
        p2.setName("p2");
        r2.setName("r2");
        c2.setName("c2");
        t2.setName("t2");
        
        Arc a = new Arc();
        a.setRegulator(c1);
        a.setRegulatee(p2);
        a.setRole(ArcRole.REPRESSION);
        
        c1.getArcs().add(a);
        p2.getArcs().add(a);
        
        PrimitiveModule pmp1 = new PrimitiveModule(FeatureRole.PROMOTER_INDUCIBLE,new Primitive(new ComponentType("ip"),"p1"),p1);
        PrimitiveModule pmr1 = new PrimitiveModule(FeatureRole.RBS,new Primitive(new ComponentType("r"),"r1"),r1);
        PrimitiveModule pmc1 = new PrimitiveModule(FeatureRole.CDS_REPRESSOR,new Primitive(new ComponentType("g"),"c1"),c1);
        PrimitiveModule pmt1 = new PrimitiveModule(FeatureRole.TERMINATOR,new Primitive(new ComponentType("t"),"t1"),t1);
        
        PrimitiveModule pmp2 = new PrimitiveModule(FeatureRole.PROMOTER_REPRESSIBLE,new Primitive(new ComponentType("rp"),"p2"),p2);
        PrimitiveModule pmr2 = new PrimitiveModule(FeatureRole.RBS,new Primitive(new ComponentType("r"),"r2"),r2);
        PrimitiveModule pmc2 = new PrimitiveModule(FeatureRole.CDS,new Primitive(new ComponentType("unk"),"c2"),c2);
        PrimitiveModule pmt2 = new PrimitiveModule(FeatureRole.TERMINATOR,new Primitive(new ComponentType("t"),"t2"),t2);
        
        root.getSubmodules().add(pmp1);
        root.getSubmodules().add(pmr1);
        root.getSubmodules().add(pmc1);
        root.getSubmodules().add(pmt1);
        
        root.getSubmodules().add(pmp2);
        root.getSubmodules().add(pmr2);
        root.getSubmodules().add(pmc2);
        root.getSubmodules().add(pmt2);
        
        root.updateModuleFeatures();
        
        root.setRole(ModuleRole.HIGHER_FUNCTION);
        
        //AssignedModule am = new AssignedModule();
        
        //PhoenixGrammar.decompose(root);
        return root;
        
    }
    
}
