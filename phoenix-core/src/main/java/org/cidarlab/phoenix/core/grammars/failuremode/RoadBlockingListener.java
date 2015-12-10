// Generated from RoadBlocking.g4 by ANTLR 4.5.1

    package org.cidarlab.phoenix.core.grammars.failuremode;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RoadBlockingParser}.
 */
public interface RoadBlockingListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RoadBlockingParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(RoadBlockingParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoadBlockingParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(RoadBlockingParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoadBlockingParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(RoadBlockingParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoadBlockingParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(RoadBlockingParser.ModuleContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoadBlockingParser#road_blocking}.
	 * @param ctx the parse tree
	 */
	void enterRoad_blocking(RoadBlockingParser.Road_blockingContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoadBlockingParser#road_blocking}.
	 * @param ctx the parse tree
	 */
	void exitRoad_blocking(RoadBlockingParser.Road_blockingContext ctx);
	/**
	 * Enter a parse tree produced by {@link RoadBlockingParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void enterWildcard(RoadBlockingParser.WildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link RoadBlockingParser#wildcard}.
	 * @param ctx the parse tree
	 */
	void exitWildcard(RoadBlockingParser.WildcardContext ctx);
}