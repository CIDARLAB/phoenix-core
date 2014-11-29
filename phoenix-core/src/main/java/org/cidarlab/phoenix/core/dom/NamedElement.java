package org.cidarlab.phoenix.core.dom;

import java.io.Serializable;

/**
 * The abstract NamedElement class is the top class of the 
 * Phoenix DOM's inheritance tree. 
 * 
 * Every object must have a name and an ID. The name serves 
 * for human representation and the ID is being utilized only 
 * for internal use (e.g. indexing). 
 * 
 * The ID is derived from the name of the NamedElement. 
 *  
 * @author Ernst Oberortner
 */
public abstract class NamedElement 
		implements Serializable {

	private static final long serialVersionUID = 7803721755204206476L;

	/*
	 * ID (automatically generated)
	 */
	private int id;
	
	/*
	 * NAME
	 */
	private String name;
	
	/**
	 * Constructor: every NamedElement MUST have a name
	 * @param name
	 */
	public NamedElement(String name) {

		if(null == name) {
			this.setName(String.valueOf(System.nanoTime()));
		} else {
			this.setName(name);
		}

		this.id = this.hashName();

	}
	
	/**
	 * the setName/1 method is used to set the name 
	 * of the NamedElement. 
	 * This method is used in the interpreter for 
	 * assignment statements, particularly if the 
	 * RHS of an assignment needs to be named as specified 
	 * on the LHS of an assignment.
	 * 
	 * @param name ... the name of the NamedElement object
	 */
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * The private hashName/0 method is being 
	 * utilized to convert the name of the NamedElement
	 * to an ID.
	 * 
	 * @return   the ID of the NamedElement
	 */
	private int hashName() {
		if(null != this.getName()) {
			int hash = this.getName().hashCode();
			if(hash < 0) {
				return hash * -1;
			}
			return hash;
		} 
		return 0;
	}
	
	/**
	 * The getID/1 method returns the ID of the NamedElement.
	 * The ID is generated automatically based on the NamedElement's 
	 * name.
	 * 
	 * @return  the ID as int
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * The getName/0 method returns the name of the element.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}	
}
