package com.asptt.resa.commons.exception;

import com.asptt.resa.commons.exception.ErrorBean;

/**
 *
 * @author maig7313
 */
public enum Unauthorized {

	/**
	 * GENERIC
	 */
	GENERIC(new ErrorBean(0, "Forbidden error"));

	private final ErrorBean value;

	Unauthorized(ErrorBean code) {
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
