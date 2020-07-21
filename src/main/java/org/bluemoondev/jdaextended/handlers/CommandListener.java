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
package org.bluemoondev.jdaextended.handlers;

import java.util.Arrays;
import java.util.Set;

import org.bluemoondev.blutilities.commands.CommandHandler;
import org.bluemoondev.blutilities.commands.ICommand;
import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.blutilities.errors.Errors;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.commands.DiscordCommand;
import org.bluemoondev.jdaextended.reflection.EventListener;
import org.bluemoondev.jdaextended.util.ActionUtil;
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
 * <strong>File:</strong> CommandListener.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@EventListener
public class CommandListener extends ListenerAdapter {

    private static final Log LOG = Log.get("JDAExtended", CommandListener.class);

    private static CommandHandler handler;

    /**
     * Initializes the command handler. This is done so by JDA-Extended.
     */
    public static void init() {
        LOG.info("Registering all marked commands");
        handler = new CommandHandler();
        Set<Class<? extends DiscordCommand>> classes = ReflectionUtil.getAllSubClasses(DiscordCommand.class);
        for (Class<? extends DiscordCommand> clazz : classes) {
            try {
                LOG.trace("Registering %s as a Discord Command", clazz.getName());
                handler.addCommand(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                LOG.error(ex);
            }
            LOG.trace("Discord command class %s has been registered", clazz.getName());
        }

    }
    
    public static void addCommand(ICommand cmd) {
        LOG.trace("Registering %s as a new Discord module command", cmd.getClass());
        handler.addCommand(cmd);
        LOG.trace("Discord module class %s has been registered", cmd.getClass());
    }

    private static boolean canRunCommand(String guildId, Member member, DiscordCommand cmd) {
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

        if (!message.getContentRaw().startsWith(prefix)) return;

        String[] msgArray = Util.splitButKeepQuotes(message.getContentRaw());
        String cmd = msgArray[0];
        String[] args = Arrays.copyOfRange(msgArray, 1, msgArray.length);

        String name = cmd.substring(prefix.length());
        Errors error = handler.execute(cmd, args,
                                       canRunCommand(event.getGuild().getId(), event.getMember(),
                                                     (DiscordCommand) handler.getCommand(cmd)),
                                       (sub, arr) -> {
                                           ((DiscordCommand) handler.getCommand(cmd)).run(message, sub);
                                       });
        switch (error) {
            case COMMAND_PARSER_NO_PERMISSION:
                ActionUtil.sendMessageAndComplete(message, "%s You do not have permission for this command %s",
                                                  Emojis.ERROR, Emojis.ERROR);
            break;
            default:

        }
        // CommandListener.runCommand(message, name, subCmd, args);
    }

}
