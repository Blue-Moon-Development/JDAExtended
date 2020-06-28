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

import javax.security.auth.login.LoginException;

import org.bluemoondev.jdaextended.util.Debug;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> Bot.java
 * <p>
 * Essentially a wrapper for {@link net.dv8tion.jda.api.JDA JDA} and
 * {@link net.dv8tion.jda.api.JDABuilder}, basically a
 * simplified version. Example as follows:<br>
 * <br>
 *
 * <pre>
 * public void start() {
 * 	Bot bot = new Bot("token", Activity.playing("Status msg"));
 * 	bot.addEventListener(new MyListener());
 * 	bot.build();
 * }
 * </pre>
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public final class Bot {

	private JDA			jda;
	private JDABuilder	builder;
	private int			numServers;
	private boolean		loggedIn;

	// TODO: Sharding?
	/**
	 * Constructs the Bot object. Should only be needed by JDA-Extended itself
	 *
	 * @param token    The authentication for the bot. See
	 *                 {@link org.bluemoondev.jdaextended.reflection.BotInfo BotApp}
	 * @param activity The status displayed by the bot. Can be
	 *                 <code>Acitivty.playing("msg")</code> or
	 *                 <code>watching, streaming, listening</code>
	 */
	public Bot(String token, Activity activity) {
		builder = new JDABuilder(token).setActivity(activity);

	}

	/**
	 * Creates the JDA and logs into Discord. Should only be needed by JDA-Extended
	 * itself
	 */
	public void build() {
		try {
			Debug.info("Bot logging in");
			jda = builder.build();
		} catch (LoginException e) {
			Debug.error("Unable to login", e);
		}

		loggedIn = true;
		Debug.info("Bot logged in");
	}

	/**
	 * Adds an event listener to the JDA. Should only be needed by JDA-Extended
	 * itself as listeners are added by
	 * JDA-Extended automatically if created properly
	 *
	 * @param listener The listener to add
	 */
	public void addEventListener(ListenerAdapter listener) {
		builder.addEventListeners(listener);
	}

	/**
	 * @return the jda instance
	 */
	public JDA getJda() { return jda; }

	/**
	 * @return the number of servers the bot is in
	 */
	public int getNumServers() { return numServers; }

	/**
	 * @return True if the bot is logged in, false otherwise
	 */
	public boolean isLoggedIn() { return loggedIn; }

}
