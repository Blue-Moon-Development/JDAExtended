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

import org.bluemoondev.jdaextended.util.Util;

import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> BaseCommand.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public abstract class BaseCommand implements ICommand {

    @Override
    public final void run(Message message, String subCmd, String[] args) {
        if (subCmd == null || subCmd == "") {
            run(message, args);
        } else {
            run(message, Util.joinArray(new String[]{subCmd}, args));
        }
    }

    @Override
    public final String[] getSubCommands() {
        return null;
    }

    @Override
    public final String[] getUsages() {
        if (getUsage() == null) {
            return new String[]{"|command|"};
        }
        return new String[]{getUsage()};
    }

    /**
     * @return The usage for this command. See {@link ICommand#getUsages}
     */
    public abstract String getUsage();
    
    @Override
    public int compareTo(ICommand o) {
    	return (getPriority() - o.getPriority());
    }
}
