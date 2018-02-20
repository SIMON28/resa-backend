package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public enum Technical {

	/**
	 * GENERIC
	 */
	GENERIC(new ErrorBean(0, "Technical error", "mailto:asptt.plongee@orange.fr?subject=[ERROR][ASPTT RESA]")),

	NOT_IMPLEMENTED(new ErrorBean(1, "Operation not Implemented"));

	private final ErrorBean value;

	Technical(ErrorBean code) {
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
		return value;
	}
}
