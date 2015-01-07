package org.clothocad.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


public class Influence {
	
	@NotNull
	@Getter
	@Setter
	private Feature influencingFeature;
	
	@NotNull
	@Getter
	@Setter
	private Feature influencedFeature;
	
	@NotNull
	@Getter
	@Setter
	private InfluenceType type;

	public Influence(Feature influencingFeature, Feature influencedFeature, InfluenceType type) {
		this.influencingFeature = influencingFeature;
		this.influencedFeature = influencedFeature;
		this.type = type;
	}
	
	// Feel free to add more of these
    public static enum InfluenceType {
    	REPRESSION, ACTIVATION;
    }
}
