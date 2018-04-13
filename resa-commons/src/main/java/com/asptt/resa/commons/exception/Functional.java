package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public enum Functional {

	GENERIC(new ErrorBean(0, "Functional error")),

	REGISTRATION(new ErrorBean(1, "Registration error")),

	AUTHENTICATION(new ErrorBean(2, "Authentication error")),

	REGISTRATION_HEURE_OUVERTURE(new ErrorBean(3, "Registration error. Waiting days and opening hour")),

	REGISTRATION_JOUR_HO(new ErrorBean(4, "Registration error. Waiting opening hour")),
	
	REGISTRATION_NB_MINI_PLONGEUR(new ErrorBean(5, "Registration error. Not enough divers ")),

	REGISTRATION_PLONGEE_FERMEE(new ErrorBean(6, "Registration error. closed dive "));

	private final ErrorBean value;

	Functional(ErrorBean code) {
		this.value = code;
	}

	@Override
	public String toString() {
		String out = String.format("%1$ - %2$", this.name(),
				this.getValue().toString());
		return out;
	}

	/**
	 *
	 * @return
	 */
	public ErrorBean getValue() {
		return this.value;
	}
}
