/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.phoenix.core.grammars;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.cidarlab.phoenix.core.grammars.structural.StructuralLexer;
import org.cidarlab.phoenix.core.grammars.structural.StructuralParser;

/**
 *
 * @author prash
 */
public class StructuralGrammar {
    public static ParseTree getAST(String pigeonString){
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        StructuralLexer lexer = new StructuralLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StructuralParser parser = new StructuralParser(tokens);
        ParseTree tree = parser.root();
        System.out.println("INPUT :: "+pigeonString);
        System.out.println("TREE :: "+ tree.toStringTree(parser));
        
        return tree;
        
    }
    
    public static void traverseTree(ParseTree tree){
        
    }
}
