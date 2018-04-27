package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public class ForbiddenException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = 8671604109238149753L;

	public ForbiddenException(Forbidden error) {
		super(error.getValue());
	}

	public ForbiddenException(Forbidden error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 403;
	}

}
