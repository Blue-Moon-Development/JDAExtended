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
package org.bluemoondev.jdaextended.twitch;

import java.util.ArrayList;
import java.util.List;

import org.bluemoondev.jdaextended.util.Debug;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.User;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> TwitchRequester.java
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class TwitchRequester {

	private TwitchClient client;

	public TwitchRequester(String clientId, String credential) {
		Debug.info("Initializing twitch client");
		client = TwitchClientBuilder.builder().withEnableHelix(true).withClientId(clientId).withClientSecret(credential)
				.build();
		Debug.info("Twitch client connected");
	}

	public TwitchClient getClient() { return client; }

	public long getFollowers(long channelId) {
		return client.getHelix().getFollowers(null, null, channelId, null, 100).execute().getTotal();
	}

	public String getGame(String gameId) {
		List<String> gameIds = new ArrayList<String>();
		gameIds.add(gameId);
		List<Game> games = client.getHelix().getGames(null, gameIds, null).execute().getGames();
		if (games.isEmpty()) return "Unknown Game";
		return games.get(0).getName();
	}

	public String getURL(long channelId) {
		return getURL(getNameFromId(channelId));
	}

	public String getImage(long channelId) {
		return getImage(getNameFromId(channelId));
	}

	public long getIdFromName(String channelName) {
		List<String> channelNames = new ArrayList<String>();
		channelNames.add(channelName);
		List<User> users = client.getHelix().getUsers(null, null, channelNames).execute().getUsers();
		if (users.isEmpty()) return -1L;
		return users.get(0).getId();
	}

	public long getFollowers(String channelName) {
		return getFollowers(getIdFromName(channelName));
	}

	public String getURL(String channelName) {
		return "https://twitch.tv/" + channelName;
	}

	public String getImage(String channelName) {
		List<String> channelNames = new ArrayList<String>();
		channelNames.add(channelName);
		List<User> users = client.getHelix().getUsers(null, null, channelNames).execute().getUsers();
		if (users.isEmpty()) return null;
		return users.get(0).getProfileImageUrl();
	}

	public boolean doesExist(String channelName) {
		List<String> channelNames = new ArrayList<String>();
		channelNames.add(channelName);
		List<User> users = client.getHelix().getUsers(null, null, channelNames).execute().getUsers();
		return !users.isEmpty();
	}

	public String getNameFromId(long streamer) {
		List<Long> channelIds = new ArrayList<Long>();
		channelIds.add(streamer);
		List<User> users = client.getHelix().getUsers(null, channelIds, null).execute().getUsers();
		if (users.isEmpty()) return null;
		return users.get(0).getDisplayName();
	}
}
