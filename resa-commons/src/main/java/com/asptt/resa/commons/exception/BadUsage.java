package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public enum BadUsage {

	/**
	 * BAD_USAGE
	 */
	GENERIC(new ErrorBean(0, "Bad Usage")),
	/**
	 *
	 */
	SEARCH_QUERY(new ErrorBean(1, "Search query is not valid")),
	/**
	 *
	 */
	FLOW_TRANSITION(new ErrorBean(2, "Workflow, state transition is not valid")),
	/**
	 *
	 */
	FLOW_UNKNOWN_STATE(new ErrorBean(3, "Workflow, unknown state")),
	/**
	 *
	 */
	MANDATORY_FIELDS(new ErrorBean(4, "Missing mandatory field")),
	/**
	 *
	 */
	UNKNOWN_VALUE(new ErrorBean(5, "Unknown value")),
	/**
	 *
	 */
	OPERATOR(new ErrorBean(6, "Wrong operator")),
	/**
	 *
	 */
	FORMAT(new ErrorBean(7, "Wrong format")),
	/**
	 *
	 */
	MISSING_QUERY(new ErrorBean(8, "Missing query parameter"));

	private final ErrorBean value;

	BadUsage(ErrorBean code) {
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
