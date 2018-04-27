package com.asptt.resa.commons.exception;

public class NotImplementedException  extends ApiExceptionAbstractImpl {

	private static final long serialVersionUID = -7586495485968286604L;

	public NotImplementedException(NotImplemented error) {
		super(error.getValue());
	}

	public NotImplementedException(NotImplemented error, String message) {
		super(error.getValue(), message);
	}

	@Override
	public int getCategory() {
		return 501;
	}

}
