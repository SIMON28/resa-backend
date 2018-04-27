package com.asptt.resa.commons.exception;

/**
 *
 * @author maig7313
 */
public interface ApiException {

	int getCategory();

	ErrorBean getCode();

	String getMessage();

}
