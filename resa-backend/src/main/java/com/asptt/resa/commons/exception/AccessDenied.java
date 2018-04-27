package com.asptt.resa.commons.exception;

public enum AccessDenied {

	/**
	 * GENERIC
	 */
	GENERIC(new ErrorBean(0, "Access denied error"));

	private final ErrorBean value;

	AccessDenied(ErrorBean code) {
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
