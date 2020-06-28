/*
*	jdaextended
*	Copyright (C) 2019 Matt
*
*	This program is free software: you can redistribute it and/or modify
*	it under the terms of the GNU General Public License as published by
*	the Free Software Foundation, either version 3 of the License, or
*	(at your option) any later version.
*
*	This program is distributed in the hope that it will be useful,
*	but WITHOUT ANY WARRANTY; without even the implied warranty of
*	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*	GNU General Public License for more details.
*
*	You should have received a copy of the GNU General Public License
*	along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*	Contact: matt@bluemoondev.org
*/
package org.bluemoondev.jdaextended.commands;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.reflection.Command;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> PrefixCommand.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command
public class PrefixCommand extends BaseCommand {

	@Override
	public void run(Message message, String[] args) {
		if (args[0].length() > 5) {
			ActionUtil.sendMessageAndComplete(message, "%s Prefix can be at most 5 characters %s", Emojis.WARNING,
					Emojis.WARNING);
			return;
		}

		JDAExtended.CORE_TABLE.setPrefix(message.getGuild().getIdLong(), args[0]);
		ActionUtil.sendMessageAndComplete(message, "%s The prefix has been changed %s", Emojis.SUCCESS, Emojis.SUCCESS);
	}

	@Override
	public String getName() {
		return "prefix";
	}

	@Override
	public String getDescription() {
		return "Changes the prefix used by commands";
	}

	@Override
	public String getDetailedDescription() {
		return "Changes the prefix used by commands. The prefix can be at most length 5 characters";
	}

	@Override
	public PairedValues<Permission, String> getPermissions(String guildId) {
		return new PairedValues<Permission, String>(Permission.ADMINISTRATOR, null);
	}

	@Override
	public String getUsage() {
		return "<prefix>";
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
