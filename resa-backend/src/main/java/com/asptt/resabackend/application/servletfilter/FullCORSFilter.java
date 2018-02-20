package com.asptt.resabackend.application.servletfilter;

import javax.servlet.FilterConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.asptt.resa.commons.filter.CORSFilterAbstractImpl;

@Component
public class FullCORSFilter extends CORSFilterAbstractImpl {

	@Value("${cors.allowed.origins}")
	String allowedOriginsString;

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(this.allowedOriginsString);
	}

}
