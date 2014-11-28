package org.cidarlab.phoenix.core.grammar;

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
    private final Set<Type> nonterminals = new HashSet<Type>();

    /** A collection of all the terminals in the grammar. */
    private final Set<Primitive> terminals = new HashSet<Primitive>();

    /** The start symbol. */
    private final Type start;

    /**
     * Constructs a new grammar from the given productions and start symbol.
     *
     * @param productions The productions in the grammar.
     * @param start The start symbol of the grammar.
     */
    public Grammar(Collection<ProductionRule> productions, Type start) {
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
                if (s instanceof Type)
                    nonterminals.add((Type) s);
                else // s instanceof Terminal
                    terminals.add((Primitive) s);
            }
        }

        nonterminals.add(start);
    }

    /**
     * Returns the start symbol of the grammar.
     *
     * @return The start symbol of the grammar.
     */
    public Type getStartSymbol() {
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
    public Collection<Primitive> getTerminals() {
        return Collections.unmodifiableCollection(terminals);
    }

    /**
     * Returns an immutable view of the nonterminals in the grammar.
     *
     * @return An immutable view of the nonterminals in the grammar.
     */
    public Collection<Type> getNonterminals() {
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