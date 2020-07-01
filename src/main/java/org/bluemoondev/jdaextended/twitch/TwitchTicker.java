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
package org.bluemoondev.jdaextended.twitch;

import java.awt.Color;
import java.util.List;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.Modules;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;

import com.github.philippheuer.events4j.reactor.ReactorEventHandler;
import com.github.twitch4j.events.ChannelGoLiveEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> TwitchTicker.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class TwitchTicker {

	public TwitchTicker() {
		if (Modules.isEnabled(Modules.TWITCH) && JDAExtended.getTwitchRequester() != null) {

			List<Guild> guilds = JDAExtended.getBot().getJda().getGuilds();
			if (null == guilds || guilds.isEmpty()) return;

			for (long l : JDAExtended.TWITCH_TABLE.getTwitchIds())
				JDAExtended.getTwitchRequester().getClient().getClientHelper()
						.enableStreamEventListener(JDAExtended.getTwitchRequester().getNameFromId(l));
			ReactorEventHandler eventHandler = new ReactorEventHandler();
			JDAExtended.getTwitchRequester().getClient().getEventManager().registerEventHandler(eventHandler);
			eventHandler.onEvent(ChannelGoLiveEvent.class, subscriber -> {
				run(subscriber);
			});
		}
	}

	public void run(ChannelGoLiveEvent event) {
		if (!Modules.isEnabled(Modules.TWITCH) || JDAExtended.getTwitchRequester() == null) return;

		TwitchRequester twitch = JDAExtended.getTwitchRequester();
		List<Guild> guilds = JDAExtended.getBot().getJda().getGuilds();
		if (guilds == null || guilds.isEmpty()) return;

		for (long guildId : JDAExtended.TWITCH_TABLE.getGuilds()) {
			for (Guild g : guilds) {
				long gid = g.getIdLong();
				if (guildId == gid) {
					if (JDAExtended.TWITCH_TABLE.getTwitchIds(guildId)
							.contains(Long.parseLong(event.getChannel().getId()))) {
						String name = event.getChannel().getName();
						EmbedBuilder eb = new EmbedBuilder().setTitle(name + " is live!").setColor(Color.MAGENTA)
								.addField("Title", event.getStream().getTitle(), false)
								.addField(name + " is playing", twitch.getGame(event.getStream().getGameId()), false)
								.addField("", twitch.getURL(event.getChannel().getName()), false)
								.setImage(twitch.getImage(event.getChannel().getId()));

						long channelId = JDAExtended.CORE_TABLE.getAlertsChannel(guildId);
						MessageChannel channel = g.getTextChannelById(channelId);
						if (channel == null) return;
						if (JDAExtended.TWITCH_TABLE
								.doesUserGetMentions(guildId, Long.parseLong(event.getChannel().getId()))) {
							Member m = g.getMemberById(JDAExtended.TWITCH_TABLE
									.getUser(guildId, Long.parseLong(event.getChannel().getId())));
							ActionUtil.sendMessageAndComplete(	channel, "%s %s is %s %s", Emojis.RED_DOT,
																m.getAsMention(), Emojis.stringToEmojiRef("live!"),
																Emojis.RED_DOT);
						}

						ActionUtil.sendEmbedAndComplete(channel, eb.build());
						ActionUtil.addReactionToLastAndQueue(channel, Emojis.THUMB_UP);
						return;
					}
				}
				break;
			}
		}
	}

}
