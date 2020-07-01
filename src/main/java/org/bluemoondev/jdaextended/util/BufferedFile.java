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
package org.bluemoondev.jdaextended.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> BufferedFile.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class BufferedFile {

	private static final int MAX_STREAM_SIZE = 1024 * 1024;

	private String			filePath;
	private BufferedWriter	writer;
	private BufferedReader	reader;
	private File			file;
	private boolean			newlyCreated;

	public BufferedFile(String filePath) {
		this(filePath, 0, null);
	}

	public BufferedFile(String filePath, int maxLines, FileSupplier<String[]> supplier) {
		this.filePath = filePath;
		file = new File(filePath);
		String[] lines = null;
		try {
			if (!file.exists()) {
				newlyCreated = true;
				file.createNewFile();
				if (supplier != null) { lines = supplier.supply(); }
			}
			reader = new BufferedReader(new FileReader(file));
			reader.mark(MAX_STREAM_SIZE);
			writer = new BufferedWriter(new FileWriter(file, true));

			if (supplier != null && newlyCreated) {
				write(lines[0]);
				for (int i = 1; i < lines.length; i++) {
					if (lines[i] == null) break;
					append(lines[i]);
				}
			}

		} catch (IOException ex) {
			Debug.error(ex);
		}
	}

	public boolean isNewlyCreated() { return this.newlyCreated; }

	public String read() {
		StringBuilder src = new StringBuilder();
		String line;
		try {
			writer.flush();
			reader.reset();
			while ((line = reader.readLine()) != null) { src.append(line).append("\n"); }
			reader.reset();
		} catch (IOException ex) {
			Debug.error("Failed to read file", ex);
		}

		return src.toString();
	}

	public String[] readLines() {
		String[] results = new String[0];
		String line;
		int i = 0;
		try {
			writer.flush();
			results = new String[(int) reader.lines().count()];
			reader.reset();
			while ((line = reader.readLine()) != null) {
				System.err.println(i);
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

	public void write(String contents) {
		try {
			if (!file.exists())
				file.createNewFile();
			writer.write(contents);
		} catch (IOException ex) {
			Debug.error("Failed to write to file", ex);
		}

	}

	public void append(String contents, boolean newLine) {
		StringBuilder sb = new StringBuilder(read());
		if (newLine) sb.append("\n");
		sb.append(contents);

		write(sb.toString());
	}

	public void append(String contents) {
		append(contents, true);
	}

	public void close() {
		try {
			reader.close();
			writer.close();
		} catch (IOException ex) {
			Debug.error("Failed to close buffered file", ex);
		}
	}

	public String getPath() { return filePath; }

	public boolean exists() {
		return file.exists();
	}

	@FunctionalInterface
	public interface FileSupplier<T> {
		public T supply();
	}

}
