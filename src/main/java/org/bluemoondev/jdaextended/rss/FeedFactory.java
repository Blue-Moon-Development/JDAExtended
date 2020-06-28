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
package org.bluemoondev.jdaextended.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> FeedFactory.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FeedFactory {
	
	private static final List<FeedParser> parsers = new ArrayList<FeedParser>();
	
	public static FeedParser createParser(long guildId, long channelId, String url) {
		if(parsers.isEmpty()) {
			parsers.add(new FeedParser(guildId, channelId, url));
			return parsers.get(0);
		}
		
		for(FeedParser p : parsers) {
			if(p.guildId == guildId && p.channelId == channelId && p.url == url)
				return p;
		}
		
		FeedParser p = new FeedParser(guildId, channelId, url);
		parsers.add(p);
		
		return p;
	}

}
