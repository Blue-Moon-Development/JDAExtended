/*
 * Copyright (C) 2020 Matt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.jdaextended.commands;

import org.bluemoondev.blutilities.annotations.Argument;
import org.bluemoondev.blutilities.annotations.Command;
import org.bluemoondev.blutilities.commands.CommandParser;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.rss.FeedFactory;
import org.bluemoondev.jdaextended.rss.FeedParser;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> RSSCommand.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command(name = "rss", subCmds = true)
public class RSSCommand extends ModuleCommand {
    
    @Argument(name = "rsslink", shortcut = "l", desc = "The RSS Feed link", cmd = "link")
    private String link;

    @Override
    public void run(Message message, String subCmd) {
        long guildId = message.getGuild().getIdLong();
        long channelId = message.getChannel().getIdLong();
        switch(subCmd){
            case "link":
                JDAExtended.RSS_TABLE.setLink(link, guildId, channelId);
                FeedParser parser = FeedFactory.createParser(guildId, channelId, link);
                parser.start(JDAExtended.getConfig().getInt("rss.delay"), e -> {
                    if(JDAExtended.RSS_TABLE.getLink(guildId, channelId) == null) return;
                    if(parser.newEntry(e)){
                        ActionUtil.sendMessageAndComplete(message.getChannel(), e.getLink());
                    }
                });
                ActionUtil.sendMessageAndComplete(message, "%s RSS feed has been linked to this channel %s", Emojis.SUCCESS, Emojis.SUCCESS);
                break;
            case "unlink":
                JDAExtended.RSS_TABLE.setLink(null, guildId, channelId);
                ActionUtil.sendMessageAndComplete(message, "%s RSS feed has been unlinked from this channel %s", Emojis.SUCCESS, Emojis.SUCCESS);
                break;
        }
    }



    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return new PairedValues<Permission, String>(Permission.MANAGE_CHANNEL, null);
    }

    @Override
    public void preRun(String sub, CommandParser parser) {
        if(sub == "link")
            link = parser.get("rsslink");
    }

}
