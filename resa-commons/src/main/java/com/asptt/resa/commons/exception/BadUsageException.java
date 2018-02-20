package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public class BadUsageException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = -1426446676227142366L;

	public BadUsageException(BadUsage error) {
		super(error.getValue());
	}

	public BadUsageException(BadUsage error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 400;
	}

}
