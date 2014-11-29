package org.cidarlab.phoenix.core.dom;


import java.util.List;
import java.util.ArrayList;

import org.cidarlab.phoenix.core.dom.Participation.Role;

/**
 * The Interaction class represents functional interactions and relationships
 * among genetic components in a biological system's design.
 * 
 * An Interaction consists of multiple participants which have certain 
 * roles in the interaction (see Participation class).
 * 
 * @author Ernst Oberortner
 */
public class Interaction 
		extends NamedElement {

	private static final long serialVersionUID = -5603391336825177408L;

	// An Interaction has a pre-defined type
	private InteractionType type;
	// list of participants
	private List<Participation> participants;
	
	public enum InteractionType {
		INDUCES  {
			@Override
			public List<Participation> initializeParticipants(NamedElement lhs, NamedElement rhs) {
				List<Participation> lop = new ArrayList<Participation>();
				lop.add(new Participation(Role.INDUCER, lhs));
				lop.add(new Participation(Role.INDUCEE, rhs));
				return lop;
			}
		}, 
		DRIVES {
			@Override
			public List<Participation> initializeParticipants(NamedElement lhs, NamedElement rhs) {
				List<Participation> lop = new ArrayList<Participation>();
//				lop.add(new Participation(Role.INDUCER, lhs));
//				lop.add(new Participation(Role.INDUCER, lhs));
				return lop;
			}
		}, 		
		REPRESSES {
			@Override
			public List<Participation> initializeParticipants(NamedElement lhs, NamedElement rhs) {
				List<Participation> lop = new ArrayList<Participation>();
				lop.add(new Participation(Role.REPRESSOR, lhs));
				lop.add(new Participation(Role.REPRESSEE, rhs));
				return lop;
			}
		};
		
		
		// every type of interaction must have a ``constructor'' 
		// which takes as input the participants of the interaction.
		// the number of participants depends on the arity of 
		// the interaction.
		public abstract List<Participation> initializeParticipants(NamedElement lhs, NamedElement rhs);
	}
	
	/**
	 * Constructor for construction a binary interaction.
	 * 
	 * @param name
	 * @param lhs
	 * @param type
	 * @param rhs
	 */
	public Interaction(String name, NamedElement lhs, InteractionType type, NamedElement rhs) {
		super(name);
		this.type = type;
		this.participants = this.type.initializeParticipants(lhs, rhs);
	}
	
	/**
	 * The getType/0 method returns the type of the Interaction.
	 * 
	 * @return  the Interaction's type
	 */
	public InteractionType getType() {
		return this.type;
	}

	/**
	 * The getParticipations/0 method returns a list of 
	 * all participants in the Interaction.
	 * 
	 * @return  a list of the particpants in the Interaction
	 */
	public List<Participation> getParticipations() {
		return this.participants;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// name of the interaction
		sb.append("INTERACTION ").append(this.getName())
		// type of the interaction
			.append(" TYPE ").append(this.getType()).append("[");
		
		// list of participants
		for(Participation participation : this.getParticipations()) {
			sb.append(participation).append(", ");
		}
		
		sb.append("]");
		return sb.toString();
	}
}
