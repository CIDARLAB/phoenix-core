package org.cidarlab.phoenix.core.grammar;

/**
 * The Enumerator class provides methods to enumerate 
 * all instances of a context-free grammar.
 * 
 * The algorithm is inspired by:
 * http://lukepalmer.wordpress.com/2008/05/02/enumerating-a-context-free-language/
 * 
 *  enumerate (Terminal a) = return [a]
 *  enumerate (Nonterminal alts) = do
 *      alt <- each alts          -- for each alternative
 *                                -- (each is the Omega constructor :: [a] -> Omega a)
 *      rep <- mapM enumerate alt -- enumerate each symbol in the sequence
 *      return $ concat rep       -- and concatenate the results
 *      
 * @author Ernst Oberortner
 */
public class Enumerator {
	
	private Grammar g;
	
	public Enumerator(Grammar g) 
			throws IllegalArgumentException {
		if(null == g || g.isEmpty()) {
			throw new IllegalArgumentException("Invalid grammar!");
		}
		
		this.g = g;
	}
	
	/**
	 * 
	 * 
	 */
	public void enumerate() {
		/*
		 * first, we get the start symbol
		 */
		Nonterminal t = this.g.getStartSymbol();
		if(null != t) {
			this.enumerate(t);
		}
	}
	
	// NON-TERMINAL
	private void enumerate(Nonterminal t) {
		/*
		 * get the productions of the non-terminal t
		 */
		ProductionRule pr = this.g.getProductions(t);
		if(null != pr) {
			for(Symbol s : pr.getProduction()) {
				if(s instanceof Nonterminal) {  // NON-TERMINAL
					enumerate((Nonterminal)s);
					System.out.println();
				} else {                 // TERMINAL
					enumerate((Terminal)s);
				}
			}
		}
	}
	
	// TERMINAL
	private void enumerate(Terminal p) {
		System.out.print(p);
	}
	
	

}
