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
package org.bluemoondev.jdaextended.tables;

import java.util.List;

import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.simplesql.SQLTable;
import org.bluemoondev.simplesql.columns.BoolColumn;
import org.bluemoondev.simplesql.columns.LongColumn;
import org.bluemoondev.simplesql.columns.SQLColumn;
import org.bluemoondev.simplesql.columns.StringColumn;
import org.bluemoondev.simplesql.exceptions.SSQLException;
import org.bluemoondev.simplesql.utils.DataSet;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> TwitchTable.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class TwitchTable extends SQLTable {

    private static final Log LOG = Log.get("JDAExtended", TwitchTable.class);

    public static final SQLColumn<Long>    GUILD_ID      = new LongColumn("guild_id", 0L);
    public static final SQLColumn<String>  USER_ID       = new StringColumn("user_id", null, 128);
    public static final SQLColumn<Long>    TWITCH_ID     = new LongColumn("twitch_id", 0L);
    public static final SQLColumn<Boolean> MENTIONS_FLAG = new BoolColumn("mentions_flag", false);

    public TwitchTable() {
        super("jda_twitch");
    }

    public List<Long> getTwitchIds() {
        try {
            return getLongs(TWITCH_ID.name);
        } catch (SSQLException ex) {
            return null;
        }
    }

    public List<Long> getTwitchIds(long guildId) {
        try {
            return getLongs(GUILD_ID.name, guildId, TWITCH_ID.name);
        } catch (SSQLException ex) {
            return null;
        }
    }

    public List<Long> getGuilds() {
        try {
            return getLongs(GUILD_ID.name);
        } catch (SSQLException ex) {
            return null;
        }
    }

    public void setUser(long guildId, long twitchId, boolean mention) {
        try {
            update(TWITCH_ID.name, twitchId, new DataSet(GUILD_ID.name, guildId),
                   new DataSet(USER_ID.name, JDAExtended.getTwitchRequester().getNameFromId(twitchId)));
            update(MENTIONS_FLAG.name, mention, new DataSet(GUILD_ID.name, guildId),
                   new DataSet(USER_ID.name,
                               new DataSet(USER_ID.name, JDAExtended.getTwitchRequester().getNameFromId(twitchId))));
        } catch (SSQLException ex) {
        }
    }

    public String getUser(long guildId, long twitchId) {
        try {
            return getString(USER_ID.name, new DataSet(GUILD_ID.name, guildId), new DataSet(TWITCH_ID.name, twitchId));
        } catch (SSQLException ex) {
            return null;
        }
    }

    public boolean doesUserGetMentions(long guildId, long twitchId) {
        try {
            return getBool(MENTIONS_FLAG.name, new DataSet(GUILD_ID.name, guildId),
                           new DataSet(TWITCH_ID.name, twitchId));
        } catch (SSQLException ex) {
            return false;
        }
    }

    public void remove(long guildId, long twitchId) {
        try {
            delete(new DataSet(GUILD_ID.name, guildId), new DataSet(TWITCH_ID.name, twitchId),
                   new DataSet(USER_ID.name, JDAExtended.getTwitchRequester().getNameFromId(twitchId)));
        } catch (SSQLException ex) {
            LOG.error(ex);
        }
    }

}
