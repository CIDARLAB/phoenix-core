/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.cidarlab.phoenix.core.grammars.structural.ForwardStructuralBaseListener;
import org.cidarlab.phoenix.core.grammars.structural.ForwardStructuralLexer;
import org.cidarlab.phoenix.core.grammars.structural.ForwardStructuralParser;

/**
 *
 * @author prash
 */
public class StructuralGrammar {
    public static ParseTree getForwardParseTree(String pigeonString){
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        ForwardStructuralLexer lexer = new ForwardStructuralLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ForwardStructuralParser parser = new ForwardStructuralParser(tokens);
        ParseTree tree = parser.root();
        
        ParseTreeWalker.DEFAULT.walk(new ForwardStructuralBaseListener(), tree);
        /*
        System.out.println("Rule Names");
        for(int i=0;i<parser.getRuleNames().length;i++){
            System.out.print(parser.getRuleNames()[i] + " ");
        }
        System.out.println("\n");
        System.out.println("Token Names");
        for(int i=0;i<parser.getTokenNames().length;i++){
            System.out.print(parser.getTokenNames()[i] + " ");
        }
        System.out.println("\n");
        */
        //System.out.println("Token Map ::" + parser.getTokenTypeMap());
        //System.out.println("Rule Map  ::" + parser.getRuleIndexMap());
        
        
        System.out.println("INPUT :: "+pigeonString);
        System.out.println("TREE :: "+ tree.toStringTree(parser));
        //System.out.println("Errors ::"+parser.getNumberOfSyntaxErrors());
        //traverseTree(tree);
        //System.out.println("\n\n");
        return tree;
        
    }
    
    public static void traverseTree(ParseTree tree){
        
        
        System.out.println("Node :: "+tree.getText());
        System.out.println("Payload ::"+tree.getPayload());
        System.out.println("Class ::"+tree.getClass().getSimpleName());
        System.out.println("Source Interval ::" + tree.getSourceInterval());
        for(int i =0;i<tree.getChildCount();i++){
            traverseTree(tree.getChild(i));
        }
    }
}
