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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.handlers.CommandHandler;
import org.bluemoondev.jdaextended.reflection.Command;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> HelpCommand.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command
public class HelpCommand extends BaseCommand {

	@Override
	public void run(Message message, String[] args) {
		String cmd = null;
		if (args.length >= 1) cmd = args[0];
		String prefix = JDAExtended.CORE_TABLE.getPrefix(message.getGuild().getIdLong());
		List<ICommand> sorted = new ArrayList<ICommand>(CommandHandler.getCommands().values());
		Collections.sort(sorted);
		if (cmd == null) {
			StringBuilder sb = new StringBuilder("\t\t**Help** - Command List\n*For further help with a command, "
					+ "supply it as an argument to the help command*\n");
			sorted.forEach(command -> {
				String str = command.getName();
				if (CommandHandler.canRunCommand(message.getGuild().getId(), message.getMember(), command)) {
					sb.append("• `").append(prefix).append(str).append('`').append(" - ")
							.append(command.getDescription()).append("\n");
				}
			});

			ActionUtil.sendMessageAndComplete(message, sb.toString());
			return;
		}

		if (!sorted.contains(CommandHandler.getCommands().get(cmd))) {
			ActionUtil.sendMessageAndComplete(message, "%s %s is not a command %s", Emojis.WARNING, cmd, Emojis.WARNING);
			return;
		}

		StringBuilder sb = new StringBuilder("**Help** - ").append(cmd).append(
				"\n*Usage key: [sub command] = required subcommand, <arg name> = required argument, |arg name| = optional argument*\n");
		ICommand command = CommandHandler.getCommands().get(cmd);
		sb.append("• **Description:** ").append(command.getDetailedDescription()).append("\n• **Usage:** ");

		sb.append("`").append(prefix).append(cmd).append(" ").append(command.getUsages()[0]).append("`\n\t");
		if (command instanceof SubCommand) {
			for (int i = 1; i < command.getUsages().length; i++) {
				String s = command.getUsages()[i];
				sb.append("`").append(prefix).append(cmd).append(" ").append(command.getSubCommands()[i - 1])
						.append(" ").append(s).append("`\n\t");
			}
		}

		ActionUtil.sendMessageAndComplete(message, sb.toString());

	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Provides a list of help for commands";
	}

	@Override
	public String getDetailedDescription() {
		return "Lists commands if no argument is given, along with their brief descriptions. "
				+ "If a command is supplied as an argument, it will provide a detailed description and the "
				+ "usage(s) for that command";
	}

	@Override
	public PairedValues<Permission, String> getPermissions(String guildId) {
		return null;
	}

	@Override
	public String getUsage() {
		return "|command|";
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
