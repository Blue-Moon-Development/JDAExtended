/*
 * Copyright (C) 2020 Blue Moon Development
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bluemoondev.jdaextended.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bluemoondev.blutilities.debug.Log;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> RemoteFile.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class RemoteFile extends IOFile {
    
    private static final Log LOG = Log.get("JDAExtended", RemoteFile.class);

	private InputStreamReader inStreamReader;
	
	protected RemoteFile() {}
	
	@Override
	protected void init(String path) {
		try {
			URLConnection conn = new URL(path).openConnection();
			inStreamReader = new InputStreamReader(conn.getInputStream());
			reader = new BufferedReader(inStreamReader);
			reader.mark(MAX_STREAM_SIZE);
		} catch (IOException ex) {
			LOG.error(ex);
		}

	}

	@Override
	public void close() {
		super.close();
		try {
			if (inStreamReader != null)
				inStreamReader.close();
		} catch (IOException ex) {
			LOG.error(ex);
		}
	}

	@Override
	protected BufferedWriter getWriter() { return null; }

}
