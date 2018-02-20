package com.asptt.resa.commons.exception;

public class FunctionalException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = -1286318486967465054L;

	public FunctionalException(Functional error) {
		super(error.getValue());
	}

	public FunctionalException(Functional error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 422;
	}

}
