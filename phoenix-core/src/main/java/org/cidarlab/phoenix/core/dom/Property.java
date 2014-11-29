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
 * In Phoenix, Types of genetic components can have Properties.
 * Properties have two characteristics: a name and a type. ???
 * 
 *  
 * @author Ernst Oberortner
 */
public class Property 
	extends NamedElement {

	private static final long serialVersionUID = -608427454031681028L;
	
	// the type of the property
	protected PropertyType type;

	/**
	 * Constructor for a named and typed Property.
	 * 
	 * @param name   the Property's name
	 * @param type   the Property's type
	 */
	public Property(String name, PropertyType type) {
		super(name);
		this.type = type;
	}

	/**
	 * Constructor for non-typed Property
	 * 
	 * @param name  ... the name of the Property
	 */
	public Property(String name) {
		super(name);
		this.type = null;
	}

	/**
	 * The getType/0 method returns the type of 
	 * the Property.
	 * 
	 * @return  the Property's type.
	 */
	public PropertyType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Property ").append(this.getName()).append("(")
				.append(this.type).append(")");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		} else if(!(o instanceof Property)) {
			return false;
		}
		
		return this.hashCode() == ((Property)o).hashCode();
	}
	
	@Override
	public int hashCode() {
		return this.getName().hashCode() + this.getType().hashCode();
	}

}