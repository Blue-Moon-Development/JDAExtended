/*
 * jdaextended
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
package org.bluemoondev.jdaextended.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bluemoondev.blutilities.debug.Log;

/**
 * <strong>Project:</strong> jdaextended <br>
 * <strong>File:</strong> FileUtil.java
 * <p>
 * A utility class which provides methods for working with files
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FileUtil {
    
    private static final Log LOG = Log.get("JDAExtended", FileUtil.class);
	
	//TODO: Should really not be a static class
	//TODO: Add remote file capability

	/**
	 * Writes a file
	 *
	 * @param file     The path of the file to write
	 * @param contents The contents which to put in the file
	 */
	public static void write(String file, String contents) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			writer.write(contents);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void append(String file, String contents) {
		StringBuilder sb = new StringBuilder(read(file));
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			sb.append(contents);
			writer.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads an entire file
	 *
	 * @param file The path of the file to read
	 * @return The contents of the file
	 */
	public static String read(String file) {
		String contents = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) { sb.append(line).append("\n"); }
			contents = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents;
	}

	/**
	 * Reads the first line from a file
	 *
	 * @param file The path to the file to read
	 * @return The first line in the file
	 */
	public static String readLine(String file) {
		String contents = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			StringBuilder sb = new StringBuilder();
			line = reader.readLine();
			if (line != null)
				sb.append(line);
			contents = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents;
	}

	public static List<String> readLines(String file) {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			line = reader.readLine();
			if (line != null)
				lines.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return lines;
	}

	/**
	 * Reads one line from a file
	 *
	 * @param file       The path to the file to read
	 * @param lineNumber Which line number to read
	 * @return The line that exists at the supplied lineNumber in the file
	 */
	public static String readLine(String file, int lineNumber) {
		String contents = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			StringBuilder sb = new StringBuilder();
			int count = 0;
			while ((line = reader.readLine()) != null) {
				count++;
				if (count == lineNumber) {
					sb.append(line);
					break;
				}
			}
			contents = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents;
	}

	public static boolean addUniqueLine(String file, String line) {
		File f = new File(file);
		if (!f.exists())
			write(file, line);

		Scanner scanner;
		PrintWriter writer = null;
		boolean lineAdded = false;
		try {
			scanner = new Scanner(f);
			writer = new PrintWriter(new FileWriter(f, true));
			boolean found = false;
			while (scanner.hasNextLine()) {
				String lineInFile = scanner.nextLine();
				// Contains instead of equals just incase the lineInFile contains the new line
				// character
				if (lineInFile.contains(line)) {
					found = true;
					break;
				}
			}

			if (!found) {
				writer.append("\n" + line);
				lineAdded = true;
			}
		} catch (IOException ex) {
			LOG.error(ex, "Error while attemping to add line to file: " + file);
		} finally {
			if (writer != null)
				writer.close();
		}

		return lineAdded;
	}

}
