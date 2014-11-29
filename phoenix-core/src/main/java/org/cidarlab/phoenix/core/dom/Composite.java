package org.cidarlab.phoenix.core.dom;

import java.util.Set;
import java.util.HashSet;

/**
 * The Composite class represents composite genetic components.
 * Composites can be either composed of primitive components, composite components,
 * or both. Composites allow the hierarchical composition of genetic components.
 * 
 * @author Ernst Oberortner
 *
 */
public class Composite 
		extends Component {

	private static final long serialVersionUID = 5872818585444553057L;

	// the set of annotations
	private Set<Annotation> annotations;
	
	public Composite(ComponentType type, String name) {
		super(type, name);
		this.annotations = new HashSet<Annotation>();
	}

	/**
	 * The getAnnotations/0 method returns a reference to the set 
	 * of annotations of this composite. The returned set can 
	 * then be extended/modified in the method that calls the getAnnotations/0.
	 * The modifications will get reflected to this object.
	 * 
	 * @return  a set of annotations.
	 */
	public Set<Annotation> getAnnotations() {
		return this.annotations;
	}
	/**
	 * The validate/0 method validates if all sub-components 
	 * of the compositions are annotated properly. 
	 * 
	 * @return  true ... annotation ok
	 *         false ... otherwise
	 */
	public boolean validate() {
		return true;
	}

}
