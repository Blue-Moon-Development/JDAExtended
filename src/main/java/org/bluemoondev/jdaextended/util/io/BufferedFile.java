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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bluemoondev.blutilities.debug.Log;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> BufferedFile.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class BufferedFile extends IOFile {
    
    private static final Log LOG = Log.get("JDAExtended", BufferedFile.class);

	private BufferedWriter	writer;
	private File			file;
	private boolean			newlyCreated;
	private boolean			append;

	protected BufferedFile() {}

	@Override
	protected void init(String path) {
		file = new File(path);
		try {
			if (!file.exists()) {
				newlyCreated = true;
				file.createNewFile();
			}

			reader = new BufferedReader(new FileReader(file));
			reader.mark(MAX_STREAM_SIZE);
			writer = new BufferedWriter(new FileWriter(file, append));
		} catch (IOException ex) {
			LOG.error(ex);
		}
	}

	public boolean isNewlyCreated() { return this.newlyCreated; }

	public void write(String contents) {
		write(contents, false);
	}

	public void write(String contents, boolean newLine) {
		try {
			writer.write(contents);
			if (newLine) writer.write("\n");
		} catch (IOException ex) {
			LOG.error(ex, "Failed to write to file");
		}
	}
	
	public void append(String contents, boolean newLine) {
		StringBuilder sb = new StringBuilder(read());
		if(newLine) sb.append("\n");
		sb.append(contents);
		write(sb.toString());
	}
	
	public void append(String contents) {
		append(contents, false);
	}

	public void shouldAppend(boolean append) {
		this.append = append;
	}

	@Override
	protected BufferedWriter getWriter() { return writer; }

}
