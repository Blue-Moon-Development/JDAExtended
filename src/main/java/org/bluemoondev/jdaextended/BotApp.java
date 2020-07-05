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
package org.bluemoondev.jdaextended;

import net.dv8tion.jda.api.entities.Activity;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> BotApp.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public abstract class BotApp {

	public abstract void preinit();

	public abstract void init();

	public abstract void postInit();

	/**
	 * The package that stores commands using the Command annotation. These
	 * classes will automatically be added as commands to the bot.
	 *
	 * @return The package name containing the annotated commands
	 */
	public String getCommandsPackageName() { return "disabled"; }

	/**
	 * The package that stores handlers using the EventListener annotation.
	 * These classes will automatically be added as listeners to the bot
	 *
	 * @return The package name containing the annotated handlers/event
	 *         listeners
	 */
	public String getHandlersPackageName() { return "disabled"; }

	/**
	 * Command run when the bot restarts. A script, such as a shell script, is
	 * required to start the bot's jar. Example:
	 * <code>./start.sh &amp; disown</code>
	 *
	 * @return The command to run
	 */
	public String getStartCommand() { return "disabled"; }

	/**
	 * Sets the bot's activity message
	 *
	 * @return The bot's starting activity message
	 */
	public Activity getActivity() { return Activity.playing("default"); }
}
