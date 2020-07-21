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
import java.util.Set;

import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.Bot;
import org.bluemoondev.jdaextended.reflection.EventListener;
import org.bluemoondev.jdaextended.util.ReflectionUtil;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> EventHandler.java
 *
 * <p>
 * Registers all event listeners that are marked by the
 * {@link org.bluemoondev.jdaextended.reflection.EventListener @EventListener}
 * annotation in the JDA api and in the package specifed by
 * {@link org.bluemoondev.jdaextended.BotApp#getHandlersPackageName getHandlersPackageName}
 * of the bot's main class
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class EventHandler {
    
    private static final Log LOG = Log.get("JDAExtended", EventHandler.class);

    public static void init(Bot bot, String packageName) {
        LOG.info("Registering all marked event listeners");

        // Register all JDA-Extended event handlers
        Set<Class<?>> classes = ReflectionUtil.getClassesAnnotatedBy("org.bluemoondev.jdaextended.listeners",
                EventListener.class);
        for (ListenerAdapter la : parseClasses(classes)) {
            bot.addEventListener(la);
        }

        // Register all user created event handlers
        if (packageName == "disabled") {
            return;
        }
        Set<Class<?>> botClasses = ReflectionUtil.getClassesAnnotatedBy(packageName, EventListener.class);
        for (ListenerAdapter la : parseClasses(botClasses)) {
            bot.addEventListener(la);
        }
    }

    private static ArrayList<ListenerAdapter> parseClasses(Set<Class<?>> classes) {
        ArrayList<ListenerAdapter> list = new ArrayList<ListenerAdapter>();
        for (Class<?> c : classes) {
            LOG.trace("Attempting to load event listener class [" + c.getName() + "]");
            if (c.getSuperclass() == ListenerAdapter.class) {
                try {
                    ListenerAdapter la = (ListenerAdapter) c.newInstance();
                    list.add(la);
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("Reflection error", e);
                }
                LOG.trace("Event handler class [" + c.getName() + "] successfully loaded");
            } else {
                LOG.error("[" + c.getName() + "] must be a sub type of [net.dv8tion.jda.api.hooks.ListenerAdapter]");
            }
        }

        return list;
    }

}
