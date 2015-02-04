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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cidarlab.phoenix.core.exception.DOMException;

/**
 * In Phoenix, Components are a super-type of genetic elements (such as parts).
 * A Component has three predefined characteristics:
 * - a sequence (represented as String), 
 * - an orientation (represented as an enum of pre-defined orientations), and
 * - a type (represented as instance of ComponentType)
 * 
 * Besides, components can have user-defined characteristics, which are 
 * in Phoenix terminology, Properties.
 * 
 * In general, we differentiate between composite components (e.g. devices) and 
 * primitive components (e.g. parts). Hence, the Component class is abstract.
 * 
 * @author Ernst Oberortner
 */
public abstract class Component 
	extends NamedElement {

	private static final long serialVersionUID = -7841606660339005799L;

	/*
	 * SEQUENCE
	 */
	protected String sequence;
	
	/*
	 * ORIENTATION
	 */
	protected Orientation orientation;
	
	/*
	 * TYPE OF THE COMPONENT
	 */
	protected ComponentType type;
	
	/*
	 * PROPERTY VALUES
	 */
	private HashMap<String, PropertyValue> hmPropertiesValues;


	/**
	 * Constructing a component of a specific type.
	 *  
	 * @param type   the type of the component
	 * @param name   the name of the component
	 */
	public Component(ComponentType type, String name) {
		super(name);
		this.type = type;
		this.sequence = new String();
		this.orientation = Orientation.FORWARD;
		this.hmPropertiesValues = new HashMap<String, PropertyValue>();
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
	 * @param orientation  the orientation of the component
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	
	/*--------------------------
	 * SEQUENCE-related METHODS
	 *--------------------------*/
	
	/**
	 * The getSequence/1 method returns the DNA sequence of the component.
	 * In case of composite components, the sequence must be constructed 
	 * based on the sequences and orientations of the sub-components
	 * In case of primitive components, the sequence is returned as it has
	 * been initialized.
	 *  
	 * @return  the sequence of the component.
	 */
	public String getSequence() {
		return this.sequence;
	}
	
	/*--------------------------
	 * PROPERTY-related METHODS
	 *-------------------------- 
	 */
	/**
	 * The getProperties/0 method returns a list of 
	 * all properties of the component's type.
	 * 
	 * @return   a list of properties
	 */
	public List<Property> getProperties() {
		return this.getType().getProperties();
	}

	/**
	 * The getProperties/1 method returns the property 
	 * of the specified name.
	 * 
	 * @param sPropertyName   the name of the desired property
	 * 
	 * @return  the property if it exists, NULL otherwise
	 */
	public Property getProperty(String sPropertyName) {

		// if the type has some properties
		if(null != this.getType().getProperties() &&
				!this.getType().getProperties().isEmpty()) {
			
			// then we ask the type to get us the desired 
			// property
			return this.getType().getProperty(sPropertyName);
		}
		
		// the property does not exist.
		return (Property) null;
	}
	
	public Map<String, PropertyValue> getPropertyValues() {
		return this.hmPropertiesValues;
	}
	public PropertyValue getPropertyValue(String sPropertyName) {
		return this.hmPropertiesValues.get(sPropertyName);
	}
	
	public void setPropertyValue(PropertyValue pv) 
			throws DOMException {
		
		// if the component does not have a property of that 
		// the property value is type of, then we throw an exception
		if(!this.getProperties().contains(pv.getProperty())) {
			throw new DOMException("Components of type " + this.getType() + 
					" do not have a " + pv.getProperty() + " property!");
		}
		
		// otherwise, we set the property to the given property value
		// if the property has already a value, then it will be overwritten
		this.hmPropertiesValues.put(
				pv.getProperty().getName(), 
				pv);
	}

		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (this.getType() != null) {
			sb.append(this.getType().getName()).append(" ");
		} else {
			sb.append("Component ");
		}

		sb.append(this.getName()).append(" (");

		if (null != this.getPropertyValues() && !this.getPropertyValues().isEmpty()) {
			Iterator<String> it = this.getPropertyValues().keySet().iterator();
			while (it.hasNext()) {
				String sPropertyName = it.next();
				PropertyValue objValue = this.getPropertyValues().get(sPropertyName);
				sb.append(".").append(sPropertyName).append("(");

				if (objValue != null) {
					sb.append(objValue.toString());
				}
				sb.append(")");

				if (it.hasNext()) {
					sb.append(",");
				}
			}
		}

		sb.append(");");
		return sb.toString();
	}
}
