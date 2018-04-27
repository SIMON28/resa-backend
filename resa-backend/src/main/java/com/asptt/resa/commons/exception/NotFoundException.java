package com.asptt.resa.commons.exception;

public class NotFoundException extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = -7586495485968286604L;

	public NotFoundException(NotFound error) {
		super(error.getValue());
	}

	public NotFoundException(NotFound error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 404;
	}

}
