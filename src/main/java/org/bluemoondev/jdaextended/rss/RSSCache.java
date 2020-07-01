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
package org.bluemoondev.jdaextended.rss;

import java.io.File;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.Modules;
import org.bluemoondev.jdaextended.settings.Config;
import org.bluemoondev.jdaextended.util.ActionUtil;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> RSSCache.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class RSSCache {

    public static void init(Config cfg) {
        if (!Modules.isEnabled(Modules.RSS)) return;

        File folder = new File("./feeds");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files.length != 0)
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.isFile() && file.getName().endsWith(".rfb")) {
                        long guildId = Long.parseLong(file.getName().substring(0, file.getName().indexOf('-')));
                        long channelId = Long.parseLong(file.getName().substring(file.getName().indexOf('-') + 1, file
                                                                                 .getName().indexOf('.')));
                        String link = JDAExtended.RSS_TABLE.getLink(guildId, channelId);

                        FeedParser parser = new FeedParser(guildId, channelId, link);
                        parser.start(cfg.getInt("rss.delay"), e -> {
                                 if (parser.newEntry(e))
                                     ActionUtil.sendMessageAndComplete(JDAExtended.getBot().getJda().getGuildById(guildId)
                                             .getTextChannelById(channelId), e.getLink());
                             });
                    }
                }
        }
    }
    

}
