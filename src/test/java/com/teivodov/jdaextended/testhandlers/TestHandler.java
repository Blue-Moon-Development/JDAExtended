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
package com.teivodov.jdaextended.testhandlers;

import org.bluemoondev.jdaextended.reflection.EventListener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> TestHandler.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@EventListener
public class TestHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("A message was sent by someone: " + event.getMessage().getContentDisplay());
    }

}
