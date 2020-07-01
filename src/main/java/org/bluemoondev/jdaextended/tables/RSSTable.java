/*
 * Copyright (C) 2020 Matt
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

import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.simplesql.SQLTable;
import org.bluemoondev.simplesql.columns.LongColumn;
import org.bluemoondev.simplesql.columns.SQLColumn;
import org.bluemoondev.simplesql.columns.StringColumn;
import org.bluemoondev.simplesql.exceptions.SSQLException;
import org.bluemoondev.simplesql.utils.DataSet;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> RSSTable.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class RSSTable extends SQLTable {

	public static final SQLColumn<Long>		GUILD_ID	= new LongColumn("guild_id", 0L);
	public static final SQLColumn<Long>		CHANNEL_ID	= new LongColumn("channel_id", 0L);
	public static final SQLColumn<String>	LINK		= new StringColumn("link", null, 512);

	public RSSTable() {
		super("jda_rss_settings");
	}

	public String getLink(long guildId, long channelId) {
		try {
			return getString(LINK.name, new DataSet(GUILD_ID.name, guildId), new DataSet(CHANNEL_ID.name, channelId));
		} catch (SSQLException ex) {
			Debug.error(ex);
			return null;
		}
	}

	public void setLink(String link, long guildId, long channelId) {
		try {
			update(LINK.name, link, new DataSet(GUILD_ID.name, guildId), new DataSet(CHANNEL_ID.name, channelId));
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

}
