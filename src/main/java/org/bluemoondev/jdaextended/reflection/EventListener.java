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
package org.bluemoondev.jdaextended.reflection;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> EventListener.java
 *
 * <p>
 * Marks a class as an event listener. All reflection is done on bot start up,
 * so it will not effect bot performance once the bot is logged in and ready to
 * use
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface EventListener {

}
