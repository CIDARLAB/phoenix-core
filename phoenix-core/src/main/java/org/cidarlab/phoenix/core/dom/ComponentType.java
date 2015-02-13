package org.cidarlab.phoenix.core.dom;

/**
 * In Phoenix, every Component (either primitive or composite) MUST have 
 * a type. The ComponentType class serves this purpose. 
 * Every ComponentType CAN/MUST (?) have a list of Properties.
 * 
 * @author Ernst Oberortner
 */
public class ComponentType 
	extends NamedElement {

	private static final long serialVersionUID = 1242448556255896751L;
	
	public ComponentType(String name) {
		super(name);
	}
}