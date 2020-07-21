/*
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bluemoondev.jdaextended.commands;

import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> ShutdownCommand.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "shutdown", allowNoArgs = true)
public class ShutdownCommand extends DiscordCommand {

    /** Utilize Blutilities4j Logger */
    private static final Log LOG = Log.get("JDAExtended", ShutdownCommand.class);

    @Override
    public void preRun(String arg0, CommandParser arg1) {}

    @Override
    public void run(Message message, String subCmd) {
        if (message.getAuthor().getIdLong() != JDAExtended.getDevId()) {
            ActionUtil.sendMessageAndComplete(message, "%s This is a developer only command! %s", Emojis.ERROR,
                                              Emojis.ERROR);
            return;
        }

        LOG.trace("Shutting down...");
        ActionUtil.sendMessageAndComplete(message, "%s Shutting down... %s", Emojis.MARK_DOUBLE_EXCLAMATION,
                                          Emojis.MARK_DOUBLE_EXCLAMATION);
        JDAExtended.getBot().getJda().shutdown();
        System.exit(0);
    }

    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return null;
    }

}
