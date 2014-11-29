package org.cidarlab.phoenix.core.dom;

import java.util.ArrayList;
import java.util.List;

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

	/*
	 * LIST OF PROPERTIES
	 */
	protected List<Property> properties; 
	
	public ComponentType(String name) {
		super(name);
		this.properties = new ArrayList<Property>();
	}
	
	public ComponentType(String name, List<Property> properties) {
		super(name);
		this.properties = properties;
	}
	
	public List<Property> getProperties() {
		return this.properties;
	}

	/**
	 * The getProperty/1 method returns the Property 
	 * of the specified name.
	 * 
	 * @param sPropertyName  ... the name of the property
	 * 
	 * @return the Property of the given name
	 */
	public Property getProperty(String sPropertyName) {
		for (Property p : this.getProperties()) {
			if (p.getName().equals(sPropertyName)) {
				return p;
			}
		}
		
		return (Property) null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Type ").append(this.getName()).append("(");
		for(int i=0; i<this.getProperties().size(); i++) {
			sb.append(".").append(this.getProperties().get(i).getName()).append("(")
				.append(this.getProperties().get(i).getType()).append(")");
			
			if(i < this.getProperties().size() -1 ) {
				sb.append(", ");
			}
		}
		sb.append(");");
		return sb.toString();
	}
}
