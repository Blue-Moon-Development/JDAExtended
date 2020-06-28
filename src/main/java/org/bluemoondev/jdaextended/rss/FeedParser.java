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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.FileUtil;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> FeedParser.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FeedParser {

	protected final String	url;
	protected final String	fileName;
	protected final long	guildId;
	protected final long	channelId;
	
	private static final Timer timer = new Timer();

	//TODO: Need a way to make this static. Maybe PairedValues<PairedValue<String, String>, List<String>>
	// Where its PVs<PV<guildId, url>, links>
	// Or make a static list that will contain parsers. No need to create a new one if its in the list already?
	// See equals method
	private List<String> links = new ArrayList<String>();

	protected FeedParser(long guildId, long channelId, String url) {
		this.guildId = guildId;
		this.channelId = channelId;
		this.url = url;
		fileName = "./feeds/" + guildId + "-" + channelId + ".rfb";
		File f = new File(fileName);
		if (!f.exists()) {
			try {
				File dir = new File("./feeds");
				dir.mkdir();
				f.createNewFile();
			} catch (IOException e) {
				Debug.warn("Could not create feeds file: " + fileName, e);
			}
		}else {
			for(String link : FileUtil.readLines(fileName))
				links.add(link);
		}

	}

	public SyndEntry getLatestFeed() {
		try (CloseableHttpClient client = HttpClients.createMinimal()) {
			HttpUriRequest request = new HttpGet(url);
			try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity()
					.getContent()) {
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed feed = input.build(new XmlReader(stream));
				if (feed.getEntries() == null || feed.getEntries().isEmpty()) return null;
				return feed.getEntries().get(0);
			} catch (IOException | IllegalArgumentException | FeedException e) {
				Debug.warn("Failed to get feed from url: " + url, e);
			}
		} catch (IOException e) {
			Debug.warn("Failed to create http client from url: " + url, e);
		}
		return null;
	}

	public List<SyndEntry> getFeeds() {
		try (CloseableHttpClient client = HttpClients.createMinimal()) {
			HttpUriRequest request = new HttpGet(url);
			try (CloseableHttpResponse response = client.execute(request); InputStream stream = response.getEntity()
					.getContent()) {
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed feed = input.build(new XmlReader(stream));
				if (feed.getEntries() == null || feed.getEntries().isEmpty()) return null;
				return feed.getEntries();
			} catch (IOException | IllegalArgumentException | FeedException e) {
				Debug.warn("Failed to get feed from url: " + url, e);
			}
		} catch (IOException e) {
			Debug.warn("Failed to create http client from url: " + url, e);
		}
		return null;
	}

	public void update(FeedConsumer consumer) {
		List<SyndEntry> entries = getFeeds();
		if (entries == null) {
			Debug.warn("No feed entries for " + url);
			return;
		}

		for (SyndEntry e : entries) {
			if (links.contains(e.getLink())) return;
			consumer.consume(e);
			links.add(e.getLink());
			
		}


	}
        
        public boolean newEntry(SyndEntry e){
            return FileUtil.addUniqueLine(fileName, e.getLink());
        }
	
	/**
	 * Starts the ticker to catch feeds
	 * @param time How many hours between updates?
	 * @param consumer The feed consumer
	 */
	public void start(int time, FeedConsumer consumer) {
		timer.schedule(new TimerTask() {
			public void run() {
				update(consumer);
			}
		}, 5000, time * 3600000); // 3600000 ms per hour
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FeedParser)) return false;
		FeedParser other = (FeedParser) obj;
		return url == other.url && guildId == other.guildId && channelId == other.channelId;
	}


	@FunctionalInterface
	public interface FeedConsumer {
		public void consume(SyndEntry entry);
	}


}
