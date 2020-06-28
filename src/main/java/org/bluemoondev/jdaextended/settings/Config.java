/*
*	jdaextended
*	Copyright (C) 2019 Matt
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*	Contact: matt@bluemoondev.org
*/
package org.bluemoondev.jdaextended.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.bluemoondev.jdaextended.util.Checks;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.FileUtil;
import org.bluemoondev.jdaextended.util.exceptions.JDAConfigurationException;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Config.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Config {
	
	//TODO Properties is a lazy day. Should make an actualy mapped system, that way a default config could be created if needed
	
	private Properties file;
	
	private Map<String, String> options;
	private List<String> lines;
	
	/**
	 * Constructs a configuration object
	 * @param fileName The name of the file. Must be pre-existing
	 */
	public Config(String fileName) {
		options = new HashMap<String, String>();
		try {
			file = new Properties();
			File f = new File(fileName);
			if(!f.exists()) {
				f.createNewFile();
			}
			file.load(getClass().getClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			Debug.error("Failed to load the configuration file", e);
		}
		lines = FileUtil.readLines(fileName);
		
		for(String line : lines) {
			if(StringUtils.isBlank(line)) continue;
			if(line.charAt(0) == '#') continue;
			String formattedLine = line.substring(line.indexOf('=') - 1, line.indexOf('=') + 2).replaceAll(" ", "");
			String k = formattedLine.substring(0, line.indexOf('='));
			String v = formattedLine.substring(line.indexOf('=') + 1);
			options.put(k, v);
		}
	}
	
	public String getOption(String key) {
		return file.getProperty(key);
	}
	
	public int getInt(String key) {
		if(Checks.isNumber(options.get(key))) return Integer.parseInt(options.get(key));
		Debug.warn("Option [" + key + "] was expected to be an integer, but it was not", new JDAConfigurationException());
		return -1;
	}
	
	//TODO: Add more
	
	
	
}
