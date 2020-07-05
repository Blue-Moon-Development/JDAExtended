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
import org.bluemoondev.jdaextended.Modules;
import org.bluemoondev.jdaextended.reflection.EventListener;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.Emojis;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> NewServerListener.java
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

		EmbedBuilder eb = new EmbedBuilder().setAuthor("Blue Moon Development", "https://bluemoondev.org")
				.setTitle(JDAExtended.getBot().getJda().getSelfUser().getName() + " Powered by JDA-Extended")
				.setDescription("Type `>help` for commands")
				.setColor(0xff0000);
		StringBuilder sb = new StringBuilder();
		if (Modules.isEnabled(Modules.TWITCH)) {
			sb.append("Set up the Twitch alerts channel with `>twitch alerts (name of channel)`\n");
			sb.append("Set up the Role to be mentioned for allowed alerts with `>twitch mention (role name)`\n");
			sb.append("For information on adding and removing Twitch alerts, see `>help twitch`\n");
		}
		
		if(Modules.isEnabled(Modules.AUDIT_LOGS))
			sb.append("Set up the audit logs channel with `>logs (channel name)`\n");

		eb.addField("Getting Started", sb.toString(), false);
		
		ActionUtil.sendEmbedAndComplete(event.getGuild().getSystemChannel(), eb.build());
		
		Debug.info("Joining new server with ID: " + guildId);
		String s = JDAExtended.CORE_TABLE.getPrefix(guildId);
		if (s == null || s == "null" || s.isEmpty())
			JDAExtended.CORE_TABLE.setPrefix(guildId, JDAExtended.DEFAULT_PREFIX);

	}

}
