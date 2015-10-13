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
import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingBaseListener;
import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingLexer;
import org.cidarlab.phoenix.core.grammars.failuremode.RoadBlockingParser;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingBaseListener;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingLexer;
import org.cidarlab.phoenix.core.grammars.failuremode.SuperCoilingParser;


/**
 *
 * @author prash
 */
public class FailureModeGrammar {
    
    public static ParseTree getRoadBlockingTree(String pigeonString) {
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        RoadBlockingLexer lexer = new RoadBlockingLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        RoadBlockingParser parser = new RoadBlockingParser(tokens);
        ParseTree tree = parser.root();

        ParseTreeWalker.DEFAULT.walk(new RoadBlockingBaseListener(), tree);

        System.out.println("INPUT :: " + pigeonString);
        System.out.println("TREE :: " + tree.toStringTree(parser));

        return tree;
    }
    
    
    public static ParseTree getSuperCoilingTree(String pigeonString) {
        ANTLRInputStream input = new ANTLRInputStream(pigeonString);
        SuperCoilingLexer lexer = new SuperCoilingLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SuperCoilingParser parser = new SuperCoilingParser(tokens);
        ParseTree tree = parser.root();

        ParseTreeWalker.DEFAULT.walk(new SuperCoilingBaseListener(), tree);

        System.out.println("INPUT :: " + pigeonString);
        System.out.println("TREE :: " + tree.toStringTree(parser));

        return tree;
    }
    
}
