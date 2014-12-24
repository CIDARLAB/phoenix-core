package org.cidarlab.phoenix.core.formalgrammar;

/**
 * In terms of Biology, a primitive represents a genetic part.
 * 
 * From a Language perspective, a primitive represents a terminal symbol 
 * in a grammar.
 * 
 * @author Ernst Oberortner
 */
public final class Terminal 
	extends Symbol {
	
    /**
     * Creates a new terminal symbol with the given name, which must not be
     * null or the empty string.
     *
     * @param name The name of the terminal.
     */
    public Terminal(String name) {
        super(name, true);
    }
}