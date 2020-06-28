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
package org.bluemoondev.jdaextended.listeners;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.reflection.EventListener;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.Emojis;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> NewServerListener.java
 * <p>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@EventListener
public class NewServerListener extends ListenerAdapter {

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		long guildId = event.getGuild().getIdLong();
		ActionUtil.sendMessageAndComplete(event.getGuild()
				.getSystemChannel(), "*This bot is powered by JDA-Extended %s, "
										+ "an API in alpha stages by Teivodov. "
										+ "Type `>help` to get started*", Emojis.MARK_DOUBLE_EXCLAMATION);
		Debug.info("Joining new server with ID: " + guildId);
		String s = JDAExtended.CORE_TABLE.getPrefix(guildId);
		if (s == null || s == "null" || s.isEmpty())
			JDAExtended.CORE_TABLE.setPrefix(guildId, JDAExtended.DEFAULT_PREFIX);

	}

}
