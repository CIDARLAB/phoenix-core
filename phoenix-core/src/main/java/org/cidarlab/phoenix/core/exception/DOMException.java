package org.cidarlab.phoenix.core.exception;

/**
 * The DOMException class is being utilized if instances 
 * of the Phoenix DOM do not comply with DOM rules.
 * For example, assignment of property values to components 
 * whose type does not have this property.
 * 
 * @author Ernst Oberortner
 */
public class DOMException 
	extends Exception {

	private static final long serialVersionUID = 2257633121957956400L;
	
	public DOMException(String msg) {
		super(msg);
	}
}
