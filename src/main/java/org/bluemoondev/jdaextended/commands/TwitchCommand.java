/*
 * Copyright (C) 2020 Blue Moon Development
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
package org.bluemoondev.jdaextended.commands;

import java.util.List;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.reflection.Command;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.PermissionUtil;
import org.bluemoondev.jdaextended.util.Util;
import org.bluemoondev.jdaextended.util.collections.PairedValues;
import org.bluemoondev.simplesql.exceptions.SSQLException;
import org.bluemoondev.simplesql.utils.DataSet;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> TwitchCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command
public class TwitchCommand extends SubCommand {

	public static final String	ALERTS_CHANNEL	= "alerts";
	public static final String	ALERTS_MENTION	= "mention";
	public static final String	ADD				= "add";
	public static final String	REMOVE			= "remove";

	@Override
	public void run(Message message, String subCmd, String[] args) {
		switch (subCmd) {
			case ALERTS_CHANNEL:
				List<TextChannel> channels = message.getGuild().getTextChannelsByName(args[0], true);
				if (!channels.isEmpty()) {
					long channelId = channels.get(0).getIdLong();
					JDAExtended.CORE_TABLE.setAlertsChannel(message.getGuild().getIdLong(), channelId);
					ActionUtil.sendMessageAndComplete(	message, "%s %s has been set as the Twitch Alerts channel",
														Emojis.SUCCESS, args[0]);
					return;
				} else {
					ActionUtil.sendMessageAndComplete(message, "%s No such channel exists", Emojis.ERROR);
				}
			break;
			case ALERTS_MENTION:
				List<Role> roles = message.getGuild().getRolesByName(args[0], true);
				if (!roles.isEmpty()) {
					long roleId = roles.get(0).getIdLong();
					JDAExtended.CORE_TABLE.setAlertsMention(message.getGuild().getIdLong(), roleId);
					ActionUtil.sendMessageAndComplete(	message, "%s %s has been set as the Twitch Alerts mentioned role",
														Emojis.SUCCESS, args[0]);
					return;
				} else {
					ActionUtil.sendMessageAndComplete(message, "%s No such role exists", Emojis.ERROR);
				}
			break;
			case ADD:
				boolean flag = (args.length > 2 && args[2].equalsIgnoreCase("true")) ? true : false;
				long twitchId = JDAExtended.getTwitchRequester().getIdFromName(args[1]);
				if (twitchId == -1L) {
					ActionUtil.sendMessageAndComplete(	message, "%s The twitch channel %s cannot be found", Emojis.ERROR,
														args[1]);
					return;
				}
				
				JDAExtended.TWITCH_TABLE.setUser(	message.getGuild().getIdLong(),
													Long.parseLong(Util.getIdFromMention(args[0])),
													JDAExtended.getTwitchRequester().getIdFromName(args[1]), flag);
				ActionUtil.sendMessageAndComplete(	message, "%s %s has been added as a streamer. Gets mentions: %b",
													Emojis.SUCCESS, args[1], flag);
			break;
			case REMOVE:
				twitchId = JDAExtended.getTwitchRequester().getIdFromName(args[1]);
				if (twitchId == -1L) {
					ActionUtil.sendMessageAndComplete(	message, "%s The twitch channel %s cannot be found", Emojis.ERROR,
														args[1]);
					return;
				}

				try {
					JDAExtended.TWITCH_TABLE
							.delete(new DataSet(JDAExtended.TWITCH_TABLE.GUILD_ID.name, message.getGuild().getIdLong()),
									new DataSet(JDAExtended.TWITCH_TABLE.USER_ID.name,
												Long.parseLong(Util.getIdFromMention(args[0]))),
									new DataSet(JDAExtended.TWITCH_TABLE.TWITCH_ID.name, twitchId));
				} catch (SSQLException ex) {
					Debug.error(ex);
				}
				ActionUtil.sendMessageAndComplete(	message, "%s %s has been removed from the Twitch alerts system",
													Emojis.SUCCESS, args[1]);
			break;
			default:
				ActionUtil.sendMessageAndComplete(message, "%s COMMAND ERROR %s", Emojis.ERROR, Emojis.ERROR);

		}
	}

	@Override
	public String getName() { return "twitch"; }

	@Override
	public String getDescription() { return "Manages the twitch alerts system"; }

	@Override
	public String getDetailedDescription() {
		return "Manages the twitch alerts system. Set up alerts channel and streamers";
	}

	@Override
	public String[] getSubCommands() { return new String[] { ALERTS_CHANNEL, ALERTS_MENTION, ADD, REMOVE }; }

	@Override
	public String[] getUsages() {
		return new String[] {	"[sub command]",
								"<channel name>",
								"<role name>",
								"<@user> <twitch channel> |mentions-flag|",
								"<@user> <twitch channel>" };
	}

	@Override
	public PairedValues<Permission, String> getPermissions(String guildId) {
		return PermissionUtil.ADMIN_PERM;
	}

	@Override
	public int getPriority() { return 1; }

}
