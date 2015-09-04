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
import org.cidarlab.phoenix.core.dom.NucSeq;
import org.cidarlab.phoenix.core.dom.Primitive;
import org.cidarlab.phoenix.core.dom.PrimitiveModule;
import org.cidarlab.phoenix.core.dom.Sequence;
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
        
        initializeCandidateFeatures();
        initializeTestFeatures();
        
        //Initialize Root Module
        Module root = new Module("notGate");
        root.setRoot(true);
        
        //Create Features for Root Module. (like how it would read from Eugene)
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
        
        //Create Primitive Modules for the root module
        PrimitiveModule pmp1 = new PrimitiveModule(FeatureRole.PROMOTER_INDUCIBLE,new Primitive(new ComponentType("ip"),"p1"),p1);
        PrimitiveModule pmr1 = new PrimitiveModule(FeatureRole.RBS,new Primitive(new ComponentType("r"),"r1"),r1);
        PrimitiveModule pmc1 = new PrimitiveModule(FeatureRole.CDS_REPRESSOR,new Primitive(new ComponentType("g"),"c1"),c1);
        PrimitiveModule pmt1 = new PrimitiveModule(FeatureRole.TERMINATOR,new Primitive(new ComponentType("t"),"t1"),t1);
        
        PrimitiveModule pmp2 = new PrimitiveModule(FeatureRole.PROMOTER_REPRESSIBLE,new Primitive(new ComponentType("rp"),"p2"),p2);
        PrimitiveModule pmr2 = new PrimitiveModule(FeatureRole.RBS,new Primitive(new ComponentType("r"),"r2"),r2);
        PrimitiveModule pmc2 = new PrimitiveModule(FeatureRole.CDS,new Primitive(new ComponentType("unk"),"c2"),c2);
        PrimitiveModule pmt2 = new PrimitiveModule(FeatureRole.TERMINATOR,new Primitive(new ComponentType("t"),"t2"),t2);
        
        //Set primitive modules
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
        
        //Create Transcriptional Unit 1 & 2 (children of the root module)
        Module tu1 = new Module("tu1");
        Module tu2 = new Module("tu2");
        
        //Set properties for TU1
        tu1.getModuleFeatures().add(p1.clone());
        tu1.getModuleFeatures().add(r1.clone());
        tu1.getModuleFeatures().add(c1.clone());
        tu1.getModuleFeatures().add(t1.clone());
        
        tu1.getSubmodules().add(pmp1.clone());
        tu1.getSubmodules().add(pmr1.clone());
        tu1.getSubmodules().add(pmc1.clone());
        tu1.getSubmodules().add(pmt1.clone());
        
        tu1.setRole(ModuleRole.TRANSCRIPTIONAL_UNIT);
        
        //Set properties for TU2
        tu2.getModuleFeatures().add(p2.clone());
        tu2.getModuleFeatures().add(r2.clone());
        tu2.getModuleFeatures().add(c2.clone());
        tu2.getModuleFeatures().add(t2.clone());
        
        tu2.getSubmodules().add(pmp2.clone());
        tu2.getSubmodules().add(pmr2.clone());
        tu2.getSubmodules().add(pmc2.clone());
        tu2.getSubmodules().add(pmt2.clone());
        
        tu2.setRole(ModuleRole.TRANSCRIPTIONAL_UNIT);
        
        tu1.getParents().add(root);
        tu2.getParents().add(root);
        
        root.getChildren().add(tu1);
        root.getChildren().add(tu2);
        
        
        //Create Expressors for TU1 and TU2
        Module exp1 = new Module("exp1");
        Module exp2 = new Module("exp2");
        
        exp1.getModuleFeatures().add(p1.clone());
        exp1.getModuleFeatures().add(r1.clone());
        exp1.getModuleFeatures().add(t1.clone());
        
        exp1.getSubmodules().add(pmp1.clone());
        exp1.getSubmodules().add(pmr1.clone());
        exp1.getSubmodules().add(pmt1.clone());
        
        
        exp2.getModuleFeatures().add(p2.clone());
        exp2.getModuleFeatures().add(r2.clone());
        exp2.getModuleFeatures().add(t2.clone());
        
        exp2.getSubmodules().add(pmp2.clone());
        exp2.getSubmodules().add(pmr2.clone());
        exp2.getSubmodules().add(pmt2.clone());
        
        exp1.setRole(ModuleRole.EXPRESSOR);
        exp2.setRole(ModuleRole.EXPRESSOR);
        
        Module exe1 = new Module("exe1");
        Module exe2 = new Module("exe2");
        
        exe1.getModuleFeatures().add(c1.clone());
        exe1.getSubmodules().add(pmc1.clone());
        
        exe2.getModuleFeatures().add(c2.clone());
        exe1.getSubmodules().add(pmc2.clone());
        
        exe1.setRole(ModuleRole.EXPRESSEE);
        exe2.setRole(ModuleRole.EXPRESSEE);
       
        tu1.getChildren().add(exp1);
        tu1.getChildren().add(exe1);
        
        tu2.getChildren().add(exp2);
        tu2.getChildren().add(exe2);
        
        exp1.getParents().add(tu1);
        exe1.getParents().add(tu1);
        
        exp2.getParents().add(tu2);
        exe1.getParents().add(tu2);
        
        return root;
        
    }
    
    public static void initializeTestFeatures(){
        
        Feature tp = new Feature();
        Feature tr = new Feature();
        Feature tl = new Feature();
        Feature tc1 = new Feature();
        Feature tc2 = new Feature();
        Feature tt = new Feature();
        
        tp.setName("tp");
        tr.setName("tr");
        tl.setName("tl");
        tc1.setName("tc1");
        tc2.setName("tc2");
        tt.setName("tt");
        
        tp.setRole(FeatureRole.PROMOTER_CONSTITUTIVE);
        tr.setRole(FeatureRole.RBS);
        tl.setRole(FeatureRole.CDS_LINKER);
        tc1.setRole(FeatureRole.CDS_FLUORESCENT);
        tc2.setRole(FeatureRole.CDS_FLUORESCENT);
        tt.setRole(FeatureRole.TERMINATOR);
        
        testPromoter = new PrimitiveModule(FeatureRole.PROMOTER_CONSTITUTIVE,new Primitive(new ComponentType("cp"),"tp"),tp);
        testRBS = new PrimitiveModule(FeatureRole.RBS,new Primitive(new ComponentType("r"),"tr"),tr);
        testLinker = new PrimitiveModule(FeatureRole.CDS_LINKER,new Primitive(new ComponentType("c"),"tl"),tl);
        testCDS1 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT,new Primitive(new ComponentType("c"),"tc1"),tc1);
        testCDS2 = new PrimitiveModule(FeatureRole.CDS_FLUORESCENT,new Primitive(new ComponentType("c"),"tc2"),tc2);
        testTerminator = new PrimitiveModule(FeatureRole.TERMINATOR,new Primitive(new ComponentType("t"),"tt"),tt);
        
    }
    
    public static void initializeCandidateFeatures(){
        cp1 = new Feature();
        cp2 = new Feature();
        cc1 = new Feature();
        cc2 = new Feature();
        
        cp1.setRole(FeatureRole.PROMOTER_INDUCIBLE);
        cp2.setRole(FeatureRole.PROMOTER_REPRESSIBLE);
        cc1.setRole(FeatureRole.CDS_REPRESSOR);
        cc2.setRole(FeatureRole.CDS);
        
        cp1.setName("cp1");
        cc1.setName("cc1");
        cp2.setName("cp2");
        cc2.setName("cc2");
        
        cp1.setSequence(new NucSeq("ATGC"));
        cc1.setSequence(new NucSeq("ATCC"));
        cp2.setSequence(new NucSeq("TTGC"));
        cc2.setSequence(new NucSeq("AAGC"));
        
        Arc a = new Arc();
        a.setRole(ArcRole.REPRESSION);
        a.setRegulatee(cp2);
        a.setRegulator(cc1);
        
        cp2.getArcs().add(a);
        cc1.getArcs().add(a);
    }
    
    private static Feature cp1;
    private static Feature cp2;
    private static Feature cc1;
    private static Feature cc2;
    
    
    private static PrimitiveModule testPromoter;
    private static PrimitiveModule testRBS;
    private static PrimitiveModule testLinker;
    private static PrimitiveModule testCDS1;
    private static PrimitiveModule testCDS2;
    private static PrimitiveModule testTerminator;
    
    
}
