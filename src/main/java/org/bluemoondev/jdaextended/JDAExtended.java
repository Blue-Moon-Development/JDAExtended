/*
 * JDA-Extended
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
package org.bluemoondev.jdaextended;

import java.io.File;

import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.commands.RSSCommand;
import org.bluemoondev.jdaextended.commands.TwitchCommand;
import org.bluemoondev.jdaextended.handlers.CommandListener;
import org.bluemoondev.jdaextended.handlers.EventHandler;
import org.bluemoondev.jdaextended.rss.RSSCache;
import org.bluemoondev.jdaextended.settings.Config;
import org.bluemoondev.jdaextended.tables.CoreTable;
import org.bluemoondev.jdaextended.tables.RSSTable;
import org.bluemoondev.jdaextended.tables.TwitchTable;
import org.bluemoondev.jdaextended.twitch.TwitchRequester;
import org.bluemoondev.jdaextended.twitch.TwitchTicker;
import org.bluemoondev.simplesql.MySql;
import org.bluemoondev.simplesql.SQLite;
import org.bluemoondev.simplesql.SimpleSQL;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> JDAExtended.java
 * <p>
 * Controls the bot and api. Provides communication between the two
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class JDAExtended {

    private static final Log LOG = Log.get("JDAExtended", JDAExtended.class);

    /**
     * The default prefix used for commands
     */
    public static final String DEFAULT_PREFIX = ">";

    // API Data managers
    public static CoreTable   CORE_TABLE   = new CoreTable();
    public static TwitchTable TWITCH_TABLE = new TwitchTable();
    public static RSSTable    RSS_TABLE    = new RSSTable();

    private static Bot bot;

    private static Config cfg;

    private static TwitchRequester twitchRequester;

    private static long devId;

    /**
     * Initializes the bot and sets up objects before starting the application
     *
     * @param botApp Your main app class that extends
     *               {@link org.bluemoondev.jdaextended.BotApp BotApp}
     */
    public static void start(BotApp botApp) {

        Log.setOnErrorAction(() -> {
            LOG.trace("Shutting down JDAExtended");
            if (getBot().isLoggedIn()) { getBot().getJda().shutdownNow(); }
            System.exit(-1);
        });

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            LOG.warn(e, "Uncaught exception in thread %s", t.getName());
        });

        cfg = new Config("./settings.cfg");
        if (cfg.getString("database").equalsIgnoreCase("sqlite"))
            SimpleSQL.init(new SQLite(new File("./database.db")));
        else SimpleSQL.init(new MySql(cfg.getString("database.host"), cfg.getString("database.user"),
                                      cfg.getString("database.password"), cfg.getString("database.name"),
                                      cfg.getInt("database.port"),
                                      cfg.getString("database.timezone")));
        devId = cfg.getLong("dev.id");

        botApp.preinit();

        bot = new Bot(cfg.getString("bot.token"), botApp.getActivity());
        EventHandler.init(bot, botApp.getHandlersPackageName());
        CommandListener.init();
        bot.addEventListener(new CommandListener());
        if (Modules.isEnabled(Modules.TWITCH)) {
            twitchRequester = new TwitchRequester(cfg.getString("twitch.client_id"), cfg.getString("twitch.secret"));
            new TwitchTicker();
            CommandListener.addCommand(new TwitchCommand());
        }
        if (Modules.isEnabled(Modules.RSS)) { CommandListener.addCommand(new RSSCommand()); }
        RSSCache.init(cfg);

        botApp.init();

        bot.build();

        botApp.postInit();
    }

    /**
     * @return the bot
     */
    public static Bot getBot() { return bot; }

    /**
     * @return The twitch requester. Null if the module is not enabled
     */
    public static TwitchRequester getTwitchRequester() { return twitchRequester; }

    public static long getDevId() { return devId; }

    public static Config getConfig() { return cfg; }

}
