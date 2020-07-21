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
package org.bluemoondev.jdaextended.util;

import java.util.List;
import java.util.stream.Collectors;

import org.bluemoondev.blutilities.debug.Log;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> ActionUtil.java
 * <p>
 * A utility class providing methods for dealing with
 * {@link net.dv8tion.jda.api.requests.RestAction RestActions.} Helps avoid the
 * need to remember using <code>queue, complete, and submit</code>
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class ActionUtil {
    
    private static final Log LOG = Log.get("JDAExtended", ActionUtil.class);

	private static MessageAction sendMessage(MessageChannel channel, String format, Object... values) {
		return channel.sendMessage(String.format(format, values));
	}

	private static MessageAction sendMessage(MessageChannel channel, MessageEmbed embed) {
		return channel.sendMessage(embed);
	}
	
	//TODO add queue and submit methods

	/**
	 * Completes a formatted message to be sent in a given channel
	 * 
	 * @param channel The channel to send the message in
	 * @param format  The format to send the message in
	 * @param values  A list of values to plug into the format's tokens
	 */
	public static void sendMessageAndComplete(MessageChannel channel, String format, Object... values) {
		sendMessage(channel, format, values).complete();
	}

	/**
	 * Completes a formatted message to be sent in the same channel as the receieved
	 * message
	 * 
	 * @param received The message received to respond to
	 * @param format   The format to send the message in
	 * @param values   A list of values to plug into the format's tokens
	 */
	public static void sendMessageAndComplete(Message received, String format, Object... values) {
		sendMessageAndComplete(received.getChannel(), format, values);
	}

	/**
	 * Completes an embedded message to be sent in the same channel as a received
	 * message
	 * 
	 * @param received The message received and will be responded to
	 * @param embed  The message to be sent
	 */
	public static void sendEmbedAndComplete(Message received, MessageEmbed embed) {
		sendEmbedAndComplete(received.getChannel(), embed);
	}

	/**
	 * Completes an embedded message to be sent in the given channel
	 * 
	 * @param channel The channel to send the message in
	 * @param embed The message embed to send
	 */
	public static void sendEmbedAndComplete(MessageChannel channel, MessageEmbed embed) {
		sendMessage(channel, embed).complete();
	}

	public static void kickAndComplete(Guild guild, Member member, String reason) {
		guild.kick(member, reason).complete();
	}

	public static void banAndComplete(Guild guild, Member member, String reason) {
		banAndComplete(guild, member, reason, 0);
	}

	public static void banAndComplete(Guild guild, Member member, String reason, int daysToDelete) {
		guild.ban(member, daysToDelete, reason).complete();
	}


	public static boolean sendDMAndComplete(User user, String msg) {
		boolean success = true;
		try {
		user.openPrivateChannel().complete().sendMessage(msg).complete();
		}catch(ErrorResponseException ex) {
			LOG.warn(ex, "Failed to send DM to user " + user.getName());
			success = false;
		}

		return success;
	}

	public static void addReactionToLastAndQueue(MessageChannel channel, String emoji) {
		channel.getHistory().retrievePast(1).queue(success -> {
			List<Message> list = success.stream().collect(Collectors.toList());
			list.forEach(m -> {
				m.addReaction(emoji);
			});
		});
	}

}
