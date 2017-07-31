/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.adaptors;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.sbolstandard.core2.SBOLDocument;
import org.synbiohub.frontend.IdentifiedMetadata;

/**
 *
 * @author ckmadsen
 */
public class SynBioHubAdaptorTest {
    
    private SynBioHubAdaptor instance;
    
    public SynBioHubAdaptorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SynBioHubAdaptor("https://synbiohub.programmingbiology.org/");
//        instance = new SynBioHubAdaptor("https://synbiohub.cidarlab.org/", "http://synbiohub.cidarlab.org/");
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of getSBOL method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetSBOL() throws Exception {
        System.out.println("getSBOL");
        List<String> result = instance.getPromoterURIsFromCollection("https://synbiohub.programmingbiology.org/public/Cello_Parts/Cello_Parts_collection/1");
        for (String promoter : result) {
            SBOLDocument sbol = instance.getSBOL(promoter);
            System.out.println(sbol);
        }
        System.out.println(result);
    }

    /**
     * Test of getCollections method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetCollections() throws Exception {
        System.out.println("getCollections");
        List<IdentifiedMetadata> result = instance.getCollectionMetadata();
        System.out.println(result);
    }

    /**
     * Test of getPromoters method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetPromoters() throws Exception {
        System.out.println("getPromoters");
        List<String> result = instance.getPromoterURIs();
        System.out.println(result);
    }
    
    /**
     * Test of getPromotersFromCollection method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetPromotersFromCollection() throws Exception {
        System.out.println("getPromotersFromCollection");
        List<IdentifiedMetadata> result = instance.getPromotersMetadataFromCollection("https://synbiohub.programmingbiology.org/public/Cello_Parts/Cello_Parts_collection/1");
        System.out.println(result);
    }

    /**
     * Test of getAllComponentDefinitions method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetAllComponentDefinitions() throws Exception {
        System.out.println("getAllComponentDefinitions");
        List<IdentifiedMetadata> result = instance.getAllComponentDefinitionMetadata();
        System.out.println(result);
    }

    /**
     * Test of getRBSs method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetRBSs() throws Exception {
        System.out.println("getRBSs");
        List<String> result = instance.getRBSURIs();
        System.out.println(result);
    }

    /**
     * Test of getCDSs method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetCDSs() throws Exception {
        System.out.println("getCDSs");
        List<String> result = instance.getCDSURIs();
        System.out.println(result);
    }

    /**
     * Test of getTerminators method, of class SynBioHubAdaptor.
     */
    @Test
    public void testGetTerminators() throws Exception {
        System.out.println("getTerminators");
        List<String> result = instance.getTerminatorURIs();
        System.out.println(result);
    }
    
}
