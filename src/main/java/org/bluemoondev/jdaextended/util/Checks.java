/*
 * Copyright (C) 2020 Matt
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.jdaextended.util;

/**
 * <strong>Project:</strong> JDA-Extended<br>
 * <strong>File:</strong> Checks.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Checks {

	public static boolean isNumber(String input) {
		try {
			int a = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isHexNumber(String input) {
		try {
			int a = Util.getFromHex(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
