/*
 * Copyright (C) 2020 Blue Moon Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.jdaextended.commands;

import org.bluemoondev.blutilities.commands.ICommand;
import org.bluemoondev.blutilities.debug.Log;
import org.bluemoondev.jdaextended.util.collections.PairedValues;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> ModuleCommand.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public abstract class ModuleCommand implements ICommand {

    private static final Log LOG = Log.get("JDAExtended", ModuleCommand.class);
    
    public abstract void run(Message message, String subCmd);
    
    public abstract PairedValues<Permission, String> getPermissions(String guildId);
}
