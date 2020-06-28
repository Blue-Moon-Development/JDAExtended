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
package org.bluemoondev.jdaextended;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Modules.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class Modules {

	public static final int	TWITCH		= 0b0001;
	public static final int	AUDIT_LOGS	= 0b0010;
	public static final int	RSS			= 0b0100;
	public static final int	EVERYTHING	= 0b1111;

	private static int mods;
	
	public static void enable(int modules) {
		mods = modules;
	}
	

	public static boolean isEnabled(int module) {
		if(mods == EVERYTHING) return true;
		if(mods == module) return true;
		if((mods & module) == module) return true;
		return false;
	}

}
