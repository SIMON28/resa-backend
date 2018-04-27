package com.asptt.resa.commons.exception;

public class AccessDeniedException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = 8594425596566770442L;

	public AccessDeniedException(AccessDenied error) {
		super(error.getValue());
	}

	public AccessDeniedException(AccessDenied error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 403;
	}
}
