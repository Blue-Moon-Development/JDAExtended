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

import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.simplesql.SQLTable;
import org.bluemoondev.simplesql.columns.LongColumn;
import org.bluemoondev.simplesql.columns.SQLColumn;
import org.bluemoondev.simplesql.columns.StringColumn;
import org.bluemoondev.simplesql.exceptions.SSQLException;

/**
 * <strong>Project:</strong> jdaextended<br>
 * <strong>File:</strong> CoreTable.java<br>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class CoreTable extends SQLTable {

	public static final SQLColumn<Long>		GUILD_ID		= new LongColumn("guild_id", 0L, true);
	public static final SQLColumn<Long>		ALERTS_CHANNEL	= new LongColumn("alerts_channel", null);
	public static final SQLColumn<Long>		ALERTS_MENTION	= new LongColumn("alerts_mention", null);
	public static final SQLColumn<Long>		LOGS_CHANNEL	= new LongColumn("logs_channel", null);
	public static final SQLColumn<String>	PREFIX			= new StringColumn("prefix", ">", 5);

	public CoreTable() {
		super("jda_core_settings");
	}

	public void setLogsChannel(long guildId, long channel) {
		try {
			update(guildId, LOGS_CHANNEL.name, channel);
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

	public void setAlertsChannel(long guildId, long channel) {
		try {
			update(guildId, ALERTS_CHANNEL.name, channel);
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

	public void setAlertsMention(long guildId, long roleId) {
		try {
			update(guildId, ALERTS_MENTION.name, roleId);
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

	public void setPrefix(long guildId, String prefix) {
		if (prefix.length() > 5) return;
		try {
			update(guildId, PREFIX.name, prefix);
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

	public long getLogsChannel(long guildId) {
		try {
			return getLong(guildId, LOGS_CHANNEL.name);
		} catch (SSQLException ex) {
			Debug.error(ex);
			return -1L;
		}
	}

	public long getAlertsChannel(long guildId) {
		try {
			return getLong(guildId, ALERTS_CHANNEL.name);
		} catch (SSQLException ex) {
			Debug.error(ex);
			return -1L;
		}
	}

	public long getAlertsMention(long guildId) {
		try {
			return getLong(guildId, ALERTS_MENTION.name);
		} catch (SSQLException ex) {
			Debug.error(ex);
			return -1L;
		}
	}

	public String getPrefix(long guildId) {
		try {
			return getString(guildId, PREFIX.name);
		} catch (SSQLException ex) {
			Debug.error(ex);
			return null;
		}
	}

}
