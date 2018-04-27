package com.asptt.resa.commons.exception;

public enum NotImplemented {
	/**
	 * TECHNICAL
	 */
	GENERIC(new ErrorBean(0, "Not implemented method"));

	private final ErrorBean value;

	NotImplemented(ErrorBean code) {
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
