package org.cidarlab.phoenix.core.grammar;

import java.util.*;

/**
 * A class representing a production rule in a CFG.  
 * 
 * The production expands out a nonterminal symbol to some 
 * (potentially empty) sequence of terminals and nonterminals.
 * 
 * @author Ernst Oberortner
 */
public final class ProductionRule 
		implements Iterable<Symbol> {
	
    /* 
     * Internally, a production is a pair of a nonterminal symbol and the
     * sequence of symbols it can expand out to.
     */
    private final Nonterminal nt;
    private final List<Symbol> production;

    /**
     * Constructs a production that may expand the given nonterminal symbol
     * into a string of symbols.
     *
     * @param nt The nonterminal symbol to expand
     * @param production The series of terminals and nonterminals it may be
     *        expanded to form.
     */
    public ProductionRule(Nonterminal nt, List<Symbol> production) {
        /* Sanity-check the inputs */
        if (nt == null || production == null || production.isEmpty())
            throw new IllegalArgumentException("Arguments to Production must be non-null and non-empty.");

        this.nt = nt;
        this.production = production;
    }

    /**
     * Returns the nonterminal associated with this production.
     *
     * @return The nonterminal associated with this production.
     */
    public Nonterminal getNonterminal() {
        return nt;
    }

    /**
     * Returns the list of symbols produced by this production.  This list is
     * immutable.
     *
     * @return The list of symbols produced by this production.
     */
    public List<Symbol> getProduction() {
        return Collections.unmodifiableList(production);
    }

    /**
     * Returns an immutable iterator that traverses the symbols produced by
     * this production.
     *
     * @return An iterator over the symbols in this production.
     */
    public Iterator<Symbol> iterator() {
        return getProduction().iterator();
    }

    /**
     * Returns a human-readable representation of this production.
     *
     * @return A human-readable representation of this production.
     */
    @Override 
    public String toString() {
        return nt + " -> " + production;
    }
} 