package org.cidarlab.phoenix.core.formalgrammar;

import java.util.*;

/**
 * In Biology, a grammar is a rule-based specification of 
 * the hierarchical composition of a biological system 
 * under design.
 * 
 * In the Phoenix project, we utilize context-free grammars.
 * 
 * @author Ernst Oberortner
 */
public final class Grammar 
		implements Iterable<ProductionRule> {
	
    /** A collection of the productions in the grammar. */
    private final Collection<ProductionRule> productions;

    /** A collection of the nonterminals in the grammar. */
    private final Set<Nonterminal> nonterminals = new HashSet<Nonterminal>();

    /** A collection of all the terminals in the grammar. */
    private final Set<Terminal> terminals = new HashSet<Terminal>();

    /** The start symbol. */
    private final Nonterminal start;

    /**
     * Constructs a new grammar from the given productions and start symbol.
     *
     * @param productions The productions in the grammar.
     * @param start The start symbol of the grammar.
     */
    public Grammar(Collection<ProductionRule> productions, Nonterminal start) {
        if (productions == null || productions.isEmpty()) {
            throw new IllegalArgumentException("Productions cannot be null nor empty.");
        }
        if(start == null) {
        	throw new IllegalArgumentException("Invalid start symbol!");
        }
        
        this.productions = productions;
        this.start = start;

        /* Fill in the list of terminals and nonterminals. */
        for (ProductionRule p: productions) {
            nonterminals.add(p.getNonterminal());
            for (Symbol s: p.getProduction()) {
                if (s instanceof Nonterminal)
                    nonterminals.add((Nonterminal) s);
                else // s instanceof Terminal
                    terminals.add((Terminal) s);
            }
        }

        nonterminals.add(start);
    }

    /**
     * Returns the start symbol of the grammar.
     *
     * @return The start symbol of the grammar.
     */
    public Nonterminal getStartSymbol() {
        return start;
    }

    /**
     * Returns an immutable view of the productions in the grammar.
     *
     * @return An immutable view of the productions in the grammar.
     */
    public Collection<ProductionRule> getProductions() {
        return Collections.unmodifiableCollection(productions);
    }
    
    /**
     * Returns the production rule for a given non-terminal.
     * 
     * @param t ... the production rule
     */
    public ProductionRule getProductions(Nonterminal t) {
    	for(ProductionRule pr : this.getProductions()) {
    		if(pr.getNonterminal().getName().equals(t.getName())) {
    			return pr;
    		}
    	}
    	return null;
    }
    
    /**
     * The isEmpty method checks if the grammar has an empty 
     * set of production rules.
     * 
     * @return   true ... no productions
     *          false ... otherwise
     */
    public boolean isEmpty() {
    	return this.productions.isEmpty();
    }

    
    
    /**
     * Returns an immutable iterator over the productions in the grammar.
     *
     * @return An immutable iterator over the productions in the grammar.
     */
    public Iterator<ProductionRule> iterator() {
        return getProductions().iterator();
    }

    /**
     * Returns an immutable view of the terminals in the grammar.
     *
     * @return An immutable view of the terminals in the grammar.
     */
    public Collection<Terminal> getTerminals() {
        return Collections.unmodifiableCollection(terminals);
    }

    /**
     * Returns an immutable view of the nonterminals in the grammar.
     *
     * @return An immutable view of the nonterminals in the grammar.
     */
    public Collection<Nonterminal> getNonterminals() {
        return Collections.unmodifiableCollection(nonterminals);
    }

    /**
     * Returns a human-readable description of the grammar.
     *
     * @return A human-readable description of the grammar.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ProductionRule p: this) {
            builder.append(p.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
} 