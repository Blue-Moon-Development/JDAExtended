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
public class RSSCommand extends SubCommand {

    @Override
    public void run(Message message, String subCmd, String[] args) {
        long guildId = message.getGuild().getIdLong();
        long channelId = message.getChannel().getIdLong();
        switch(subCmd){
            case "link":
                JDAExtended.RSS_TABLE.setLink(args[0], guildId, channelId);
                FeedParser parser = FeedFactory.createParser(guildId, channelId, args[0]);
                parser.start(Integer.parseInt(JDAExtended.getConfig().getOption("rss.delay")), e -> {
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
    public String getName() {
        return "rss";
    }

    @Override
    public String getDescription() {
        return "Sets up an RSS feed for the channel this command is used in";
    }

    @Override
    public String getDetailedDescription() {
        return "Sets up or unpairs an RSS feed for the channel this command is used in. Only one RSS feed per channel.";
    }

    @Override
    public String[] getSubCommands() {
        return new String[] {"link", "unlink"};
    }

    @Override
    public String[] getUsages() {
        return new String[] {"[sub command]", "<link>", ""};
    }

    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return new PairedValues<Permission, String>(Permission.MANAGE_CHANNEL, null);
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
