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
package org.bluemoondev.jdaextended.test.testmanagers;

import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.simplesql.SQLTable;
import org.bluemoondev.simplesql.columns.LongColumn;
import org.bluemoondev.simplesql.columns.SQLColumn;
import org.bluemoondev.simplesql.columns.StringColumn;
import org.bluemoondev.simplesql.exceptions.SSQLException;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> TestDataManager.java
 * <p>
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class TestDataManager extends SQLTable {

	public final static SQLColumn<Long>		GUILD_ID	= new LongColumn("GUILD_ID", 0L, true);
	public final static SQLColumn<String>	GUILD_NAME	= new StringColumn("GUILD_NAME", null, 100);

	/**
	 * @param database
	 * @param tableName
	 */
	public TestDataManager() {
		super("TEST2");
	}

	public void setName(long guildId, String newName) {
		try {
			update(guildId, GUILD_NAME.name, newName);
		} catch (SSQLException ex) {
			Debug.error(ex);
		}
	}

	public String getName(long guildId) {
		try {
			return getString(guildId, GUILD_NAME.name);
		} catch (SSQLException ex) {
			Debug.error(ex);
			return null;
		}
	}

}
