/*
Copyright (c) 2012 Boston University.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL BOSTON UNIVERSITY BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
BOSTON UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

BOSTON UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND BOSTON UNIVERSITY HAS
NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.
 */

package org.cidarlab.phoenix.core.dom;

/**
 * In Phoenix, Components are a super-type of genetic elements (such as parts).
 * A Component has three predefined characteristics: - a sequence (represented
 * as String), - an orientation (represented as an enum of pre-defined
 * orientations), and - a type (represented as instance of ComponentType)
 *
 * Besides, components can have user-defined characteristics, which are in
 * Phoenix terminology, Properties.
 *
 * In general, we differentiate between composite components (e.g. devices) and
 * primitive components (e.g. parts). Hence, the Component class is abstract.
 *
 * @author Ernst Oberortner
 */
public abstract class Component
        extends NamedElement {

    private static final long serialVersionUID = -7841606660339005799L;

    protected String sequence;
    protected Orientation orientation;
    protected ComponentType type;

    /**
     * Constructing a component of a specific type.
     *
     * @param type the type of the component
     * @param name the name of the component
     */
    public Component(ComponentType type, String name) {
        super(name);
        this.type = type;
        this.sequence = new String();
        this.orientation = Orientation.FORWARD;
    }

    /**
     *
     * @return the type of the component
     */
    public ComponentType getType() {
        return this.type;
    }

    /**
     *
     * @return the orientation of the component
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * setting the orientation of the component
     *
     * @param orientation the orientation of the component
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /*--------------------------
     * SEQUENCE-related METHODS
     *--------------------------*/
    /**
     * The getSequence/1 method returns the DNA sequence of the component. In
     * case of composite components, the sequence must be constructed based on
     * the sequences and orientations of the sub-components In case of primitive
     * components, the sequence is returned as it has been initialized.
     *
     * @return the sequence of the component.
     */
    public String getSequence() {
        return this.sequence;
    }

    public enum Orientation {
        FORWARD,
        REVERSE,
        UNDEFINED;
    }

}
