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

import org.bluemoondev.blutilities.annotations.Argument;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.PermissionUtil;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

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
@Command(name = "twitch", subCmds = true)
public class TwitchCommand extends DiscordCommand {

    private static final Log LOG = Log.get("JDAExtended", TwitchCommand.class);

    public static final String ALERTS_CHANNEL = "alerts";
    public static final String ALERTS_MENTION = "mention";
    public static final String ADD            = "add";
    public static final String REMOVE         = "remove";

    @Argument(name = "channel", shortcut = "c", cmd = ALERTS_CHANNEL,
              desc = "The text channel where Twitch alerts should be posted")
    private String alertsChannelName;

    @Argument(name = "mention", shortcut = "m", cmd = ALERTS_MENTION,
              desc = "The role that should get mentioned when an alert is posted and "
                      + "the streamer conditions are met. See `>twitch add` for more information")
    private String alertsMention;

    @Argument(name = "channel", shortcut = "c", cmd = ADD, desc = "The twitch channel to add to the alerts system")
    private String channelNameToAdd;

    @Argument(name = "mentionable", shortcut = "m", cmd = ADD, required = false, defaultValue = "false",
              desc = "Should the alert mention the defined role for this streamer?")
    private boolean channelGetsMention;

    @Argument(name = "channel", shortcut = "c", cmd = REMOVE,
              desc = "The twitch channel to remove from the alerts system")
    private String channelToRemove;

    @Override
    public void preRun(String sub, CommandParser parser) {
        switch (sub) {
            case ALERTS_CHANNEL:
                alertsChannelName = parser.get("channel");
            break;
            case ALERTS_MENTION:
                alertsMention = parser.get("mention");
            break;
            case ADD:
                channelNameToAdd = parser.get("channel");
                channelGetsMention = Boolean.parseBoolean(parser.get("mentionable"));
            break;
            case REMOVE:
                channelToRemove = parser.get("channel");
            break;
        }
    }

    @Override
    public void run(Message message, String subCmd) {
        switch (subCmd) {
            case ALERTS_CHANNEL:
                List<TextChannel> channels = message.getGuild().getTextChannelsByName(alertsChannelName, true);
                if (!channels.isEmpty()) {
                    long channelId = channels.get(0).getIdLong();
                    JDAExtended.CORE_TABLE.setAlertsChannel(message.getGuild().getIdLong(), channelId);
                    ActionUtil.sendMessageAndComplete(message, "%s %s has been set as the Twitch Alerts channel",
                                                      Emojis.SUCCESS, alertsChannelName);
                    return;
                } else {
                    ActionUtil.sendMessageAndComplete(message, "%s No such channel exists", Emojis.ERROR);
                }
            break;
            case ALERTS_MENTION:
                List<Role> roles = message.getGuild().getRolesByName(alertsMention, true);
                if (!roles.isEmpty()) {
                    long roleId = roles.get(0).getIdLong();
                    JDAExtended.CORE_TABLE.setAlertsMention(message.getGuild().getIdLong(), roleId);
                    ActionUtil.sendMessageAndComplete(message, "%s %s has been set as the Twitch Alerts mentioned role",
                                                      Emojis.SUCCESS, alertsMention);
                    return;
                } else {
                    ActionUtil.sendMessageAndComplete(message, "%s No such role exists", Emojis.ERROR);
                }
            break;
            case ADD:
                long twitchId = JDAExtended.getTwitchRequester().getIdFromName(channelNameToAdd);
                if (twitchId == -1L) {
                    ActionUtil.sendMessageAndComplete(message, "%s The twitch channel %s cannot be found", Emojis.ERROR,
                                                      channelNameToAdd);
                    return;
                }

                JDAExtended.TWITCH_TABLE.setUser(message.getGuild().getIdLong(),
                                                 twitchId, channelGetsMention);
                ActionUtil.sendMessageAndComplete(message, "%s %s has been added as a streamer. Gets mentions: %b",
                                                  Emojis.SUCCESS, channelNameToAdd, channelGetsMention);
            break;
            case REMOVE:
                twitchId = JDAExtended.getTwitchRequester().getIdFromName(channelToRemove);
                if (twitchId == -1L) {
                    ActionUtil.sendMessageAndComplete(message, "%s The twitch channel %s cannot be found", Emojis.ERROR,
                                                      channelToRemove);
                    return;
                }
                if (!JDAExtended.TWITCH_TABLE.getTwitchIds(message.getGuild().getIdLong()).contains(twitchId)) {
                    ActionUtil.sendMessageAndComplete(message,
                                                      "%s %s was never part of the alerts system, no need to remove",
                                                      Emojis.ERROR, channelToRemove);
                    return;
                }

                LOG.info("Removing %s (twitch ID: %i) from the database and alerts system", channelToRemove, twitchId);

                JDAExtended.TWITCH_TABLE.remove(message.getGuild().getIdLong(), twitchId);
                ActionUtil.sendMessageAndComplete(message, "%s %s has been removed from the Twitch alerts system",
                                                  Emojis.SUCCESS, channelToRemove);
                LOG.trace("%s (twitch ID: %i) has been removed from the database and alerts system", channelToRemove,
                          twitchId);
            break;
            default:
                ActionUtil.sendMessageAndComplete(message, "%s COMMAND ERROR %s", Emojis.ERROR, Emojis.ERROR);

        }
    }

    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return PermissionUtil.ADMIN_PERM;
    }

}
