package org.cidarlab.phoenix.core.dom;

/**
 * In Phoenix, components can be composed into more complex components.
 * The Annotation class enables to specify such composition 
 * either through absolute positions (``indexing'') or relative positions 
 * of the components to each other in a composition (``precedes'').
 * 
 * The Annotation class is implemented in such a way that absolute and relative 
 * positioning can be mixed. If the sub-components of a composed components 
 * are annotated correctly, is validated in the Composite class.
 * 
 * An Annotation MUST have a reference to a sub-component. 
 * 
 * @author Ernst Oberortner
 */
public class Annotation {
	
	// for absolute positioning
	private Integer idx;
	
	// for relative positioning
	private Annotation precedes;
	
	// the reference to the sub-component
	private Component subComponent;
	
	/**
	 * The constructor for an ``absolute'' annotation. 
	 * I.e. the subComponent appears at a fixed position 
	 * in the composition.
	 * 
	 * @param idx   ... the index of the subComponent
	 * @param subComponent  ... the subComponent
	 */
	public Annotation(int idx, Component subComponent) {
		this.idx = idx;
		this.precedes = null;
		this.subComponent = subComponent;
	}
	
	/**
	 * The Annotation constructor for ``relative'' positioning 
	 * of components in a composition.
	 * 
	 * @param precedes  ... a reference to the preceding annotation
	 * @param subComponent   ... a reference to the sub-component
	 */
	public Annotation(Annotation precedes, Component subComponent) {
		this.idx = null;
		this.precedes = precedes;
		this.subComponent = subComponent;
	}
	
	/**
	 * 
	 * @return  true ... if the annotation is relative to another annotation
	 *         false ... otherwise
	 */
	public boolean isRelative() {
		return this.precedes != null;
	}

	/**
	 * 
	 * @return  true ... if the annotation refers to an index
	 *         false ... otherwise
	 */
	public boolean isAbsolute() {
		return this.idx != null;
	}
	
	/**
	 * 
	 * @return  the index of the position the annotation's sub-component appears
	 *          if the annotation is absolute
	 */
	public int getPosition() {
		return this.idx;
	}
	
	/**
	 * 
	 * @return the preceding annotation if the annotation is relative
	 */
	public Annotation getPrecedes() {
		return this.precedes;
	}
	
	/**
	 * the getComponent/0 method returns the component of the composition 
	 * that the the Annotation is annotating.
	 * 
	 * @return  the Annotation's component
	 */
	public Component getComponent() {
		return this.subComponent;
	}
}
