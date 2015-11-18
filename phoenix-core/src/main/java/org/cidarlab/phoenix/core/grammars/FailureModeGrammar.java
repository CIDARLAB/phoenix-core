/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cidarlab.phoenix.core.adaptors.PigeonAdaptor;
import org.cidarlab.phoenix.core.dom.FailureMode;
import org.cidarlab.phoenix.core.dom.Module;

import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingBaseListener;
import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingLexer;
import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingParser;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingBaseListener;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingLexer;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingParser;
import org.cidarlab.phoenix.core.grammars.failuremode.TranscriptionalInterferenceBaseListener;
import org.cidarlab.phoenix.core.grammars.failuremode.TranscriptionalInterferenceLexer;
import org.cidarlab.phoenix.core.grammars.failuremode.TranscriptionalInterferenceParser;


/**
 *
 * @author prash
 */
public class FailureModeGrammar {
    
    public static int getRoadBlockingCount(String pigeonString) {
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        RoadBlockingLexer lexer = new RoadBlockingLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RoadBlockingParser parser = new RoadBlockingParser(tokens);
        ParseTree tree = parser.root();
        
        RoadBlockingBaseListener rbListener = new RoadBlockingBaseListener();
        ParseTreeWalker.DEFAULT.walk(rbListener, tree);
        
        System.out.println("Number of RB Failure Modes :: " + rbListener.getRoadBlockCount());
        System.out.println("INPUT :: " + pigeonString);
        System.out.println("TREE :: " + tree.toStringTree(parser));

        return rbListener.getRoadBlockCount();
    }
    
    public static int getSuperCoilingCount(String pigeonString) {
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        SuperCoilingLexer lexer = new SuperCoilingLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SuperCoilingParser parser = new SuperCoilingParser(tokens);
        ParseTree tree = parser.root();
        SuperCoilingBaseListener scListener = new SuperCoilingBaseListener();
        ParseTreeWalker.DEFAULT.walk(scListener, tree);

        System.out.println("INPUT :: " + pigeonString);
        System.out.println("TREE :: " + tree.toStringTree(parser));

        return scListener.getSuperCoilingCount();
    }
    
    public static int getTranscriptionalInterferenceCount(String pigeonString) {
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        TranscriptionalInterferenceLexer lexer = new TranscriptionalInterferenceLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TranscriptionalInterferenceParser parser = new TranscriptionalInterferenceParser(tokens);
        ParseTree tree = parser.root();
        TranscriptionalInterferenceBaseListener tiListener = new TranscriptionalInterferenceBaseListener();
        ParseTreeWalker.DEFAULT.walk(tiListener, tree);

        System.out.println("INPUT :: " + pigeonString);
        System.out.println("TREE :: " + tree.toStringTree(parser));

        return tiListener.getTranscriptionalInterferenceCount();
    }
    
    
    
    public static void assignFailureModes(Module module){
        String featureString = PigeonAdaptor.generatePigeonString(module, true);
        if(getRoadBlockingCount(featureString)>0){
            module.getFailureModes().add(FailureMode.ROAD_BLOCKING);
        }
        if(getSuperCoilingCount(featureString)>0){
            module.getFailureModes().add(FailureMode.SUPER_COILING);
        }
        if(getTranscriptionalInterferenceCount(featureString)>0){
            module.getFailureModes().add(FailureMode.TRANSCRIPTIONAL_INTERFERENCE);
        }
    }
    
    
    public static List<Module> sortByFailureModes(List<Module> rootModules){
        List<Module> sortedModules = new ArrayList<Module>();
        Map<Integer,List<Module>> bins = new HashMap<Integer,List<Module>>();
        for(int i=0;i<=FailureMode.values().length;i++){
            bins.put(i,new ArrayList<Module>());
        }
        for(Module module:rootModules){
            bins.get(module.getFailureModes().size()).add(module);
        }
        for(int i=0;i<=FailureMode.values().length;i++){
            for(Module module:bins.get(i)){
                sortedModules.add(module);
            }
        }
        return sortedModules;
    }
    
    
}
