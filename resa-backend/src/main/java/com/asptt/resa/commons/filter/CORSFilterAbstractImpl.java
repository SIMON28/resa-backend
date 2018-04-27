package com.asptt.resa.commons.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CORSFilterAbstractImpl implements Filter {

	private Set<String> origins = new HashSet<String>();;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CORSFilterAbstractImpl.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		if (req instanceof HttpServletRequest) {
			if (request.getHeader("Origin") != null) {
				String origin = request.getHeader("Origin");
				if (this.contains(origin)) {
					response.setHeader("Access-Control-Allow-Origin", origin);
					response.setHeader("Access-Control-Allow-Methods",
							"GET, POST, PUT, DELETE, OPTIONS");
					response.setHeader("Access-Control-Max-Age", "3600");
					response.setHeader("Access-Control-Allow-Credentials",
							"true");
					response.setHeader("Access-Control-Allow-Headers",
							"x-requested-with, Authorization, Content-Type");
				}
			}
		}
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			chain.doFilter(req, res);
		}
	}

	protected void init(String allowedOriginsString) {

		if (this.origins.isEmpty()) {

			CORSFilterAbstractImpl.LOGGER
					.info("Initializing CORS Filter with list of origins : "
							+ allowedOriginsString);

			String[] tab = allowedOriginsString.split(",");
			for (String element : tab) {
				this.origins.add(element);
			}
		}

	}

	public boolean contains(String origin) {
		return (this.origins.contains("*") || this.origins.contains(origin));
	}

}
