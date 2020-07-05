/*
 * jdaextended
 * Copyright (C) 2019 Matt
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 * Contact: matt@bluemoondev.org
 */
package org.bluemoondev.jdaextended.util;

import org.bluemoondev.jdaextended.JDAExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Debug.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Debug {

	private static final Logger	LOG			= LoggerFactory.getLogger("JDA-Extended");
	private static final Logger	ERROR_LOG	= LoggerFactory.getLogger("JDA-ExtendedERROR");

	public static void info(String msg) {
		LOG.info(msg);
	}

	public static void debug(String msg) {
		LOG.debug(msg);
	}

	public static void trace(String msg) {
		LOG.trace(msg);
	}

	public static void warn(String msg) {
		ERROR_LOG.warn(msg);
	}

	public static void error(String msg) {
		ERROR_LOG.error(msg);
		if (JDAExtended.getBot().isLoggedIn()) { JDAExtended.getBot().getJda().shutdownNow(); }
		System.exit(-1);
	}

	public static void warn(String msg, Throwable t) {
		ERROR_LOG.warn(msg, t);
	}

	public static void error(String msg, Throwable t) {
		ERROR_LOG.error(msg, t);
		if (JDAExtended.getBot().isLoggedIn()) { JDAExtended.getBot().getJda().shutdownNow(); }
		System.exit(-1);
	}

	public static void error(Throwable t) {
		error(t.getMessage(), t);
	}

	public static Logger getLogger() { return LOG; }
	public static Logger getErrorLogger() { return ERROR_LOG; }

}
