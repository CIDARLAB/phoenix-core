package org.cidarlab.phoenix.core.dom;

/**
 * A Primitive represents a genetic basic component, such as a part.
 * 
 * @author Ernst Oberortner
 *
 */
public class Primitive extends Component {

	private static final long serialVersionUID = -4837390880130494083L;

	public Primitive(ComponentType type, String name) {
		super(type, name);
	}
	
	/**
	 * It is possible to set the sequence of a primitive component.
	 *  
	 * @param sequence   the sequence of the primitive component
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
