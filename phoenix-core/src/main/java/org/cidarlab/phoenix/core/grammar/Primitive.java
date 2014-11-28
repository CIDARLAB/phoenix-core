package org.cidarlab.phoenix.core.grammar;

/**
 * In terms of Biology, a primitive represents a genetic part.
 * 
 * From a Language perspective, a primitive represents a terminal symbol 
 * in a grammar.
 * 
 * @author Ernst Oberortner
 */
public final class Primitive 
	extends Symbol {
	
    /**
     * Creates a new terminal symbol with the given name, which must not be
     * null or the empty string.
     *
     * @param name The name of the terminal.
     */
    public Primitive(String name) {
        super(name, true);
    }
}