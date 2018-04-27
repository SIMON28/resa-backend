package com.asptt.resa.commons.exception;

public class UnauthorizedException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = 8594425596566770442L;

	public UnauthorizedException(Unauthorized error) {
		super(error.getValue());
	}

	public UnauthorizedException(Unauthorized error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 401;
	}

}
