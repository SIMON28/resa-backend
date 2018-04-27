package com.asptt.resa.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.LoggerFactory;

/**
 * <code>DateAdapter</code> is an {@link XmlAdapter} implementation that
 * (un)marshals dates between <code>String</code> and <code>Date</code> representations.
 * All date strings meet <a href="http://en.wikipedia.org/wiki/ISO_8601">ISO
 * 8601</a> basic format
 */

public class XmlDateAdapter extends XmlAdapter<String, Date> {

	private static final org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(XmlDateAdapter.class);
	private DateFormat iso_8601;

	public XmlDateAdapter() {
		iso_8601 = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.getDefault());
		iso_8601.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public String marshal(Date d) throws Exception {
		LOGGER.debug(d.toString());

		try {
			return iso_8601.format(d);
		} catch (Exception e) {
			LOGGER.warn(String.format("Failed to format date %s", d.toString()), e);
			return null;
		}
	}

	@Override
	public Date unmarshal(String d) throws Exception {
		LOGGER.debug(d);

		if (d == null) {
			return null;
		}

		try {
			return iso_8601.parse(d);
		} catch (ParseException e) {
			LOGGER.warn(String.format("Failed to parse string %s", d), e);
			return null;
		}
	}

}