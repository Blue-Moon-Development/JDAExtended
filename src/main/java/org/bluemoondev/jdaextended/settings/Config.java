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
package org.bluemoondev.jdaextended.settings;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bluemoondev.jdaextended.util.Checks;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.exceptions.JDAConfigurationException;
import org.bluemoondev.jdaextended.util.io.BufferedFile;
import org.bluemoondev.jdaextended.util.io.FileFactory;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Config.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Config {


	private Map<String, String>	options;
	private String[]			lines;

	/**
	 * Constructs a configuration object
	 * 
	 * @param fileName The name of the file. Must be pre-existing
	 */
	public Config(String fileName) {
		options = new HashMap<String, String>();
		BufferedFile f = FileFactory.createBufferedFile(fileName, () -> {
			return setupDefaultOptions();
		});

		if (f.isNewlyCreated()) {
			f.close();
			Debug.info("Configuration file created. Please fill out the details before running again");
			System.exit(0);
		}
		lines = f.readLines();
		f.close();

		for (String line : lines) {
			if (StringUtils.isBlank(line)) continue;
			StringUtils.strip(line);
			if (line.charAt(0) == '#') continue;
			String formattedLine = line.replaceAll(" = ", "=");
			String k = formattedLine.substring(0, formattedLine.indexOf('='));
			String v = formattedLine.substring(formattedLine.indexOf('=') + 1);
			options.put(k, v);
		}
	}

	private String[] setupDefaultOptions() {
		String[] contents = new String[24];
		contents[0] = "# Bot settings";
		contents[1] = "bot.token = <insert discord bot token here>";
		contents[2] = "";
		contents[3] = "# Database settings";
		contents[4] = "database = <either \'mysql\' or \'sqlite\'>";
		contents[5] = "# If using mysql, fill out the following details, if using sqlite, comment out the lines with \'#\'";
		contents[6] = "database.user = <your database username>";
		contents[7] = "database.password = <your database password>";
		contents[8] = "database.host = <the host IP address of the database>";
		contents[9] = "# The standard default port is 3306, change this only if needed";
		contents[10] = "database.port = 3306";
		contents[11] = "database.name = <the name of the database WHICH MUST ALREADY BE CREATED>";
		contents[12] = "database.timezone = <the timezone of the server the database is hosted on. For example, if EST, then put EST5EDT>";
		contents[13] = "";
		contents[14] = "# Twitch settings. Uncomment and fill out details if using the Twitch module";
		contents[15] = "#twitch.client_id = <applications client id>";
		contents[16] = "#twitch.secret = <applications secret. KEEP THIS SECRET (duh)>";
		contents[17] = "";
		contents[18] = "# RSS Settings. Uncomment and fillout if using the RSS module";
		contents[19] = "#rss.delay = <hourly interval>";
		contents[20] = "# For example, if rss.delay = 4, the bot will check for new rss feeds every 4 hours";
		contents[21] = "";
		contents[22] = "# Developer ID - Used for developer commands, such as <shutdown> which lets you close the bot from a discord server";
		contents[23] = "dev.id = <enter your discord user ID>";
		return contents;
	}

	public int getInt(String key) {
		if (Checks.isNumber(options.get(key))) return Integer.parseInt(options.get(key));
		Debug.warn(	"Option ["	+ key + "] was expected to be an integer, but it was not",
					new JDAConfigurationException());
		return -1;
	}

	public long getLong(String key) {
		if (Checks.isLongNumber(options.get(key))) return Long.parseLong(options.get(key));
		Debug.warn("Option [" + key + "] was expected to be an long, but it was not", new JDAConfigurationException());
		return -1L;
	}

	public String getString(String key) {
		return options.get(key);
	}

	public boolean getBool(String key) {
		return Boolean.parseBoolean(options.get(key));
	}

	// TODO: Add more

}
