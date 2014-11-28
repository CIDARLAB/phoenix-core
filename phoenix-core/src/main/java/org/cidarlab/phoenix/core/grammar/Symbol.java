package org.cidarlab.phoenix.core.grammar;

/**
 * A base class representing a symbol in a CFG, which can be either a terminal
 * or nonterminal symbol.
 * <p>
 * This class hierarchy contains only two classes - Terminal and Nonterminal.
 * This allows us to manipulate strings of symbols in a typesafe fashion
 * without ever allowing for confusion between terminal and nonterminal
 * symbols.
 * 
 * @author Ernst Oberortner
 */
public abstract class Symbol {
	
    /** The name of the symbol being represented. */
    private final String name;

    /** Whether this is a terminal symbol. */
    private final boolean terminal;

    /**
     * Constructs a symbol with the given name and terminal status.
     * <p>
     * Note that this class is package-private so that the only two legal
     * subclasses are Terminal and Nonterminal.
     *
     * @param name The name for the symbol.
     * @param isTerminal Whether or not the symbol is a terminal symbol;
     */
    Symbol(String name, boolean isTerminal) {
        if (name == null || name.isEmpty())
            throw new RuntimeException("Invalid name!");

        this.name = name;
        this.terminal = isTerminal;
    }

    /**
     * Returns whether the symbol is a terminal.
     *
     * @return Whether the symbol is a terminal.
     */
    public final boolean isTerminal() {
        return terminal;
    }

    /**
     * Returns the name of the symbol.
     *
     * @return The name of the terminal.
     */
    public final String getName() {
        return name;
    }

    /**
     * Computes a hash code for a Symbol, which is designed to be as random as
     * possible.
     *
     * @return A hash code for this symbol.
     */
    @Override
    public int hashCode() {
        return 31 * name.hashCode() + (terminal? 1 : 0);
    }

    /**
     * Returns whether this Symbol is equal to some other object.
     *
     * @param o The object to compare this Symbol to.
     * @return Whether this Symbol is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        /* If this object isn't a symbol, we can't be equal to it. */
        if (!(o instanceof Symbol)) return false;

        /* Downcast the object to the symbol it is. */
        Symbol other = (Symbol) o;

        /* Confirm whether we have the same name/terminal status. */
        return terminal == other.terminal && name.equals(other.name);
    }

    /**
     * Provides a human-readable representation of the symbol.
     *
     * @return A human-readable representation of the symbol.
     */
    @Override
    public String toString() {
        /* Terminal symbols expand out to themselves. */
        if (terminal) return name;

        /* Nonterminals expand out to themselves with a marker indicating that
         * they are nonterminal.
         */
        return "<" + name + ">";
    }
}