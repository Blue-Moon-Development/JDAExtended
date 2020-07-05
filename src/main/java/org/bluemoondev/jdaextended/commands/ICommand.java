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

import org.bluemoondev.jdaextended.util.collections.PairedValues;

import com.github.twitch4j.graphql.command.BaseCommand;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> ICommand.java
 *
 * <p>
 * An interface for creating commands. It is recommended to extend
 * {@link SubCommand} or {@link BaseCommand} rather than implementing this
 * interface directly
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public interface ICommand extends Comparable<ICommand>{

    public static final char REQUIRED_ARGUMENT_MARKER = '<';
    public static final String REQUIRED_USER_MENTION = "<@user>";

    /**
     * Runs the command
     *
     * @param message The message containing the command
     * @param args The command arguments
     */
    public void run(Message message, String[] args);

    /**
     * Runs the command
     *
     * @param message The message containing the command
     * @param subCmd The sub command
     * @param args The command arguments
     */
    public void run(Message message, String subCmd, String[] args);

    /**
     * @return The name of the command
     */
    public String getName();

    /**
     * @return A brief description of the command
     */
    public String getDescription();

    /**
     * @return A detailed description of the command
     */
    public String getDetailedDescription();

    /**
     * @return An array of sub commands that this command accepts
     */
    public String[] getSubCommands();

    /**
     * @return An array of usages for the command and sub commands. The first
     * usage should be the base command usage. The following usages correspond
     * to the sub commands array. usages[1] =&rsaquo; sub_commands[0] and onwards.
     * <p>
     * <code>[sub command] = required sub command<br>&lsaquo;arg name&rsaquo; =
     * required argument<br>
     * &lsaquo;@user&rsaquo; = required argument as a user mention<br>
     * |arg name| = optional argument</code>
     * </p>
     * <strong>Example, for a command 'rank' which takes 2 sub commands,
     * 'create' and 'delete'</strong>
     * <p>
     * <code>return new String[] { "[sub command]", "{@code <rank name>} |rank
     * color|", "&lsaquo;rank name&rsaquo;" };</code><br>
     * <i>This would correspond to the array given by
     * {@link org.bluemoondev.jdaextended.commands.ICommand#getSubCommands() getSubCommands()}
     * <code>{ "create", "delete" }</code></i>
     * </p>
     * <strong>Example, for a command 'kick' which takes 2 arguments and no sub
     * commands</strong>
     * <p>
     * <code>return new String[] { "&lsaquo;@user&rsaquo; &lsaquo;reason&rsaquo;" };</code><br>
     * <i>This would correspond to a null array returned by
     * {@link org.bluemoondev.jdaextended.commands.ICommand#getSubCommands() getSubCommands()}</i>
     * </p>
     */
    public String[] getUsages();

    /**
     * @param guildId The ID of the guild
     * @return The required permission, role, or both for this command to
     * operate
     */
    public PairedValues<Permission, String> getPermissions(String guildId);
    
    public int getPriority();

}
