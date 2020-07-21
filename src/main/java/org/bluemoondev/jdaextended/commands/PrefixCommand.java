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
package org.bluemoondev.jdaextended.commands;

import org.bluemoondev.blutilities.annotations.Argument;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.PermissionUtil;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> PrefixCommand.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "prefix")
public class PrefixCommand extends DiscordCommand {

    @Argument(name = "new_prefix", shortcut = "np", desc = "The new prefix for the bot commands to use")
    private String newPrefix;

    @Override
    public void run(Message message, String subCmd) {
        if (newPrefix.length() > 5) {
            ActionUtil.sendMessageAndComplete(message, "%s Prefix can be at most 5 characters %s", Emojis.WARNING,
                                              Emojis.WARNING);
            return;
        }

        JDAExtended.CORE_TABLE.setPrefix(message.getGuild().getIdLong(), newPrefix);
        ActionUtil.sendMessageAndComplete(message, "%s The prefix has been changed %s", Emojis.SUCCESS, Emojis.SUCCESS);
    }

    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return PermissionUtil.ADMIN_PERM;
    }

    @Override
    public void preRun(String sub, CommandParser parser) {
        newPrefix = parser.get("new_prefix");
    }

}
