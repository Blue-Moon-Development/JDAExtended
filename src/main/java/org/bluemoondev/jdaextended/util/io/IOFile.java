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
package org.bluemoondev.jdaextended.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import org.bluemoondev.jdaextended.util.Debug;
import org.bluemoondev.jdaextended.util.exceptions.JDAIOException;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> IOFile.java<br>
 * <p>
 * Common code for both the remote and buffered file types
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public abstract class IOFile {
	
	public static final int MAX_STREAM_SIZE = 1024 * 1024;

	protected BufferedReader reader;
	
	
	protected abstract void init(String path);
	protected abstract BufferedWriter getWriter();
	
	public String read() {
		if(reader == null) throw new JDAIOException("BufferedReader object is null. Create this in the init method");
		StringBuilder src = new StringBuilder();
		String line;
		try {
			if(getWriter() != null) getWriter().flush();
			reader.reset();
			while ((line = reader.readLine()) != null) { src.append(line).append("\n"); }
			reader.reset();
		} catch (IOException ex) {
			Debug.error("Failed to read file", ex);
		}

		return src.toString();
	}
	
	public String[] readLines() {
		if(reader == null) throw new JDAIOException("BufferedReader object is null. Create this in the init method");
		String[] results = new String[0];
		String line;
		int i = 0;
		try {
			if(getWriter() != null) getWriter().flush();
			reader.reset();
			results = new String[(int) reader.lines().count()];
			reader.reset();
			while ((line = reader.readLine()) != null) {
				results[i] = line;
				i++;
			}
			reader.reset();
		} catch (IOException ex) {
			Debug.error("Failed to read lines", ex);
		}

		return results;
	}

	public String readLine(int lineNumber) {
		if (lineNumber < 0 || lineNumber >= readLines().length) {
			Debug.error("Attempted to read a non existent line", new IllegalArgumentException());
			return null;
		}
		return readLines()[lineNumber];
	}
	
	public void close() {
		if(reader == null) throw new JDAIOException("BufferedReader object is null. Create this in the init method");
		try {
			reader.close();
			if(getWriter() != null) getWriter().close();
		} catch (IOException ex) {
			Debug.error("Failed to close buffered file", ex);
		}
	}

}
