package com.asptt.resa.commons.resource.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asptt.resa.commons.exception.TechnicalException;

/**
 *
 * @author maig7313
 */
@Provider
public class TechnicalMapper implements ExceptionMapper<TechnicalException> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TechnicalMapper.class);

	@Override
	public Response toResponse(TechnicalException ex) {
		LOGGER.error(
				"Do not panic, this is a handled error, but you have to fix this",
				ex.getMessage());
		LOGGER.error(ex.getCode().toString());
		return ErrorResponse.build(ex);
	}

}
