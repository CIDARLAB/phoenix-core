package org.clothocad.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.clothocad.core.datums.ObjBase;

public class Parameter extends ObjBase {

	@NotNull
	@Getter
	@Setter
	private double value;
	
	@NotNull
	@Getter
	@Setter
	private Variable variable;
	
	@NotNull
	@Getter
	@Setter
	private Units units;
	
	public Parameter(String name, double value, Variable variable, Units units) {
		super(name);
		this.value = value;
		this.variable = variable;
		this.units = units;
	}
}
