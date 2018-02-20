/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asptt.resa.commons.utils;

import org.hashids.Hashids;

import com.asptt.resa.commons.exception.NotFound;
import com.asptt.resa.commons.exception.NotFoundException;

/**
 * @author clxq1935
 */
public final class HashIdUtils {

	private final static Hashids HASHIDS = new Hashids("aGFuc2hvdGZpcnN0", 10);

	public static String encode(final long... id) {
		return HASHIDS.encode(id);
	}

	public static long[] multiDecode(final String hashId) {
		return HASHIDS.decode(hashId);
	}

	public static long decode(final String hashId) {
		final long[] ids = multiDecode(hashId);
		if (ids == null || ids.length < 1) {
			throw new NotFoundException(NotFound.GENERIC);
		}
		return ids[0];
	}

}
