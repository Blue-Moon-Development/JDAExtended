package com.teivodov.jdaextended;

import org.bluemoondev.jdaextended.BotApp;
import org.bluemoondev.jdaextended.JDAExtended;
import org.bluemoondev.jdaextended.Modules;
import org.bluemoondev.jdaextended.rss.FeedFactory;
import org.bluemoondev.jdaextended.rss.FeedParser;

import com.teivodov.jdaextended.testmanagers.TestDataManager;

public class AppTest extends BotApp {

	public static TestDataManager TEST_MANAGER = new TestDataManager();

	public static void main(String[] args) {
		Modules.enable(Modules.TWITCH | Modules.RSS);
		JDAExtended.start(new AppTest());
		// FeedParser p = FeedFactory.createParser(1, 1,
		// "https://www.bungie.net/en/Rss/NewsByCategory?category=all&currentpage=1&itemsPerPage=8&FILENAME=NewsRss&LOCALE=en");
		// p.start(1, e -> {
		// if (p.newEntry(e))
		// System.err.println(e.getLink());
		// });
	}

	@Override
	public void preinit() {

	}

	@Override
	public void init() {}

	@Override
	public void postInit() {}

	@Override
	public String getCommandsPackageName() { return "org.bluemoondev.jdaextended.testcommands"; }

	@Override
	public String getHandlersPackageName() { return "org.bluemoondev.jdaextended.testhandlers"; }
}
