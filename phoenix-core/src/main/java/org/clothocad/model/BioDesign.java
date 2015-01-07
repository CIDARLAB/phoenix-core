package org.clothocad.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.clothocad.core.datums.SharableObjBase;


public class BioDesign extends SharableObjBase {
	
	@Getter
	@Setter
	private Module module;
	
	@Getter
	@Setter
	private Set<Parameter> parameters;
	
	@Getter
	@Setter
	
	private Set<Part> parts;
	
	@Getter
	@Setter
	private Set<Polynucleotide> polynucleotides;
	
	@Getter
	@Setter
	private Set<Strain> strains;
	
	@Getter
	@Setter
	private Set<Medium> media;
	
	public BioDesign(String name, String description, Module module, Set<Parameter> parameters,
			Set<Part> parts, Set<Polynucleotide> polynucleotides, Set<Strain> strains, Set<Medium> media,
			Person author) {
		super(name, author, description);
		constructBioDesign(module, parameters, parts, polynucleotides, strains, media);
	}
	
	public BioDesign(String name, Module module, Set<Parameter> parameters, 
			Set<Part> parts, Set<Polynucleotide> polynucleotides, Set<Strain> strains, Set<Medium> media,
			Person author) {
		super(name, author);
		constructBioDesign(module, parameters, parts, polynucleotides, strains, media);
	}
	
	private void constructBioDesign(Module module, Set<Parameter> parameters,
			Set<Part> parts, Set<Polynucleotide> polynucleotides, Set<Strain> strains, Set<Medium> media) {
		this.module = module;
		if (parameters == null)
			this.parameters = new HashSet<Parameter>();
		else
			this.parameters = parameters;
		if (parts == null)
			this.parts = new HashSet<Part>();
		else
			this.parts = parts;
		if (polynucleotides == null)
			this.polynucleotides = new HashSet<Polynucleotide>();
		else
			this.polynucleotides = polynucleotides;
		if (strains == null)
			this.strains = new HashSet<Strain>();
		else
			this.strains = strains;
		if (media == null)
			this.media = new HashSet<Medium>();
		else
			this.media = media;
	}
	
	
}
