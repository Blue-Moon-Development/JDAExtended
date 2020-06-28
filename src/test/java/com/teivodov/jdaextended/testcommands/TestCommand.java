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
package com.teivodov.jdaextended.testcommands;

import org.bluemoondev.jdaextended.commands.BaseCommand;
import org.bluemoondev.jdaextended.reflection.Command;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> TestCommand.java
 *
 * <p>
 * TODO: Describe class here
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
@Command
public class TestCommand extends BaseCommand {

    @Override
    public void run(Message message, String[] args) {
        System.err.println("Someone sent a msg! From: " + message.getAuthor().getName() + ", contents: "
                + message.getContentRaw());
    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "This is a test";
    }

    @Override
    public String getDetailedDescription() {
        return "This is a test";
    }

    @Override
    public PairedValues<Permission, String> getPermissions(String guildId) {
        return new PairedValues<Permission, String>(null, "469915383428808705");
    }

    @Override
    public String getUsage() {
        return "<required arg> |optional arg|";
    }

	@Override
	public int getPriority() {
		return -1;
	}

}
