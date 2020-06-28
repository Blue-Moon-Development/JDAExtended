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
package org.bluemoondev.jdaextended.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.commands.BaseCommand;
import org.bluemoondev.jdaextended.commands.ICommand;
import org.bluemoondev.jdaextended.commands.SubCommand;
import org.bluemoondev.jdaextended.reflection.Command;
import org.bluemoondev.jdaextended.reflection.EventListener;
import org.bluemoondev.jdaextended.util.ActionUtil;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.Emojis;
import org.bluemoondev.jdaextended.util.ReflectionUtil;
import org.bluemoondev.jdaextended.util.Util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> CommandHandler.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@EventListener
public class CommandHandler extends ListenerAdapter {

	private static Map<String, ICommand> commands = new HashMap<String, ICommand>();

	/**
	 * Initializes the command handler. This is done so by JDA-Extended.
	 *
	 * @param packageName The bot's package where Command classes annotated by
	 *                    {@link org.bluemoondev.jdaextended.reflection.Command @Command} are stored
	 */
	public static void init(String packageName) {
		Debug.info("Registering all marked commands");
		Set<Class<?>> classes = ReflectionUtil.getClassesAnnotatedBy("org.bluemoondev.jdaextended.commands",
				Command.class);
		for (ICommand c : parseClasses(classes)) {
			add(c);
		}

		if (packageName == "disabled") { return; }
		Set<Class<?>> botClasses = ReflectionUtil.getClassesAnnotatedBy(packageName, Command.class);
		for (ICommand c : parseClasses(botClasses)) {
			add(c);
		}

	}

	private static ArrayList<ICommand> parseClasses(Set<Class<?>> classes) {
		ArrayList<ICommand> list = new ArrayList<>();
		for (Class<?> c : classes) {
			Debug.trace("Attempting to load event command class [" + c.getName() + "]");
			if (c.getSuperclass() == BaseCommand.class) {
				try {
					BaseCommand bc = (BaseCommand) c.newInstance();
					list.add(bc);
				} catch (InstantiationException | IllegalAccessException e) {
					Debug.error("Reflection error", e);
				}
				Debug.trace("Command class [" + c.getName() + "] successfully loaded");
			} else if (c.getSuperclass() == SubCommand.class) {
				try {
					SubCommand sc = (SubCommand) c.newInstance();
					list.add(sc);
				} catch (InstantiationException | IllegalAccessException e) {
					Debug.error("Reflection error", e);
				}
				Debug.trace("Command class [" + c.getName() + "] successfully loaded");
			} else {
				Debug.error("[" + c.getName() + "] must be a sub type of either BaseCommand or SubCommand");
			}
		}

		return list;
	}

	/**
	 * Adds a command to JDA-Extended's command handler. The command will also automatically be added to the 'help' page
	 *
	 * @param cmd The command to add
	 */
	public static void add(ICommand cmd) {
		commands.put(cmd.getName(), cmd);
	}

	/**
	 * Runs a command
	 *
	 * @param message The message sent by a user
	 * @param name    The name of the command the user tried using
	 * @param subCmd  The sub command, if any, used by the user. If the command takes no sub commands, this becomes
	 *                <code>args[0]</code> later
	 * @param args    The arguments of the command
	 */
	public static void runCommand(Message message, String name, String subCmd, String[] args) {
		ICommand cmd = commands.get(name);
		if (cmd == null) { return; }
		if (cmd.getUsages().length == 0) {
			Debug.warn("Command <" + name + "> class must not have an empty usages array");
			return;
		}
		String[] raw = message.getContentRaw().split(" ");
		String primaryUsage = cmd.getUsages()[0];
		boolean hasSub = primaryUsage.contains("[sub command]");

		if (hasSub) {
			if (raw.length < 2) {
				ActionUtil.sendMessageAndComplete(message,
						"%s You must supply a sub command. See `>help %s` for more info %s", Emojis.ERROR, name,
						Emojis.ERROR);
				Debug.warn("cmd requires sub cmd");
				return;
			}

			String usage = null;
			boolean validSubCmd = false;
			// Loop through the usages, ignoring the first usage which denotes a sub command
			for (int i = 1; i < cmd.getUsages().length; i++) {
				String sub = cmd.getSubCommands()[i - 1]; // Get corresponding sub command to the current usage
				if (sub.equalsIgnoreCase(subCmd)) {
					usage = cmd.getUsages()[i];
					validSubCmd = true;
				}
			}

			if (!validSubCmd) {
				ActionUtil.sendMessageAndComplete(message, "%s Sub command is invalid. See `>help %s` for more info %s",
						Emojis.ERROR, name, Emojis.ERROR);
				return;
			}

			char[] chars = usage.toCharArray();
			int numArgsRequired = 0;
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == ICommand.REQUIRED_ARGUMENT_MARKER) {
					numArgsRequired++;
				}
			}

			if (args.length < numArgsRequired) {
				ActionUtil.sendMessageAndComplete(message,
						"%s Command requires %d arguments. See `>help %s` for more info %s", Emojis.ERROR,
						numArgsRequired, name, Emojis.ERROR);
				return;
			}

			// TODO: Make <@user> just require at least one mention, and make it so command can handle mutlple mentions
			// message.getMentionedUsers() -> returns a list
			if (usage.contains(ICommand.REQUIRED_USER_MENTION)) {

				if (message.getMentionedMembers().isEmpty()) {
					ActionUtil.sendMessageAndComplete(message,
							"%s Command requires a user mention. See `>help %s` for more info %s", Emojis.ERROR, name,
							Emojis.ERROR);
					return;
				}
			}
		} else {
			String[] newArgs = Util.joinArray(subCmd != null ? new String[] { subCmd } : new String[] {}, args);
			char[] chars = primaryUsage.toCharArray();
			int numArgsRequired = 0;
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == ICommand.REQUIRED_ARGUMENT_MARKER) {
					numArgsRequired++;
				}
			}

			if (newArgs.length < numArgsRequired) {
				ActionUtil.sendMessageAndComplete(message,
						"%s Command requires %d arguments. See `>help %s` for more info %s", Emojis.ERROR,
						numArgsRequired, name, Emojis.ERROR);
				return;
			}
		}

		String subcmdtext = subCmd == null ? "" : !hasSub ? "" : " " + subCmd;

		if (canRunCommand(message.getGuild().getId(), message.getMember(), cmd)) {
			Debug.info("Running command " + name + subcmdtext + " with args: " + Util.formatArray(args));
			cmd.run(message, subCmd, args);
			// TODO: Audit logs
		} else {
			ActionUtil.sendMessageAndComplete(message, "%s You do not have permission for this command %s", Emojis.WARNING,
					Emojis.WARNING);
			Debug.warn("No permission for this command");
		}
	}

	public static boolean canRunCommand(String guildId, Member member, ICommand cmd) {
		//@formatter:off
        return member.hasPermission(Permission.ADMINISTRATOR)
                || // If a member is an admin, they have permission
                (cmd.getPermissions(guildId) == null
                || // If no permission was specified, they have permission
                (cmd.getPermissions(guildId) != null
                && // If a permission was specified AND...
                (cmd.getPermissions(guildId).first != null && member.hasPermission(cmd.getPermissions(guildId).first))
                || // The member has the specified permission, they can run the command
                (cmd.getPermissions(guildId).second != null && Util.hasRole(member, cmd.getPermissions(guildId).second)))); // AND/OR if they have the specified role they can run the command
        //@formatter:on
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.isFromType(ChannelType.PRIVATE) || event.getAuthor().isBot())
			return;

		Message message = event.getMessage();
		String prefix = JDAExtended.CORE_TABLE.getPrefix(event.getGuild().getIdLong());
		// TODO account for >help

		if (!message.getContentRaw().startsWith(prefix)) return;

		String[] msgArray = Util.splitButKeepQuotes(message.getContentRaw());
		String cmd = msgArray[0];
		String subCmd = null;
		String[] args = new String[0];
		if (msgArray.length == 2) {
			subCmd = msgArray[1];
		} else if (msgArray.length > 2) {
			args = new String[msgArray.length - 2];
			subCmd = msgArray[1];

			for (int i = 2; i < msgArray.length; i++) {
				args[i - 2] = msgArray[i];
			}
		}

		String name = cmd.substring(prefix.length());
		CommandHandler.runCommand(message, name, subCmd, args);
	}

	/**
	 * @return the commands
	 */
	public static Map<String, ICommand> getCommands() {
		return commands;
	}


}
