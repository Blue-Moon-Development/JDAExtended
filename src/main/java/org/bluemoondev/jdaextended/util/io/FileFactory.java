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

import java.net.MalformedURLException;

import org.bluemoondev.jdaextended.util.Debug;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> FileFactory.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FileFactory {

	public static BufferedFile createBufferedFile(String path, boolean append, FileSupplier<String[]> supplier) {
		BufferedFile buf = new BufferedFile();
		buf.shouldAppend(append);
		buf.init(path);
		if (buf.isNewlyCreated()) {
			if (supplier != null) {
				String[] lines = supplier.supply();
				for (int i = 0; i < lines.length; i++) {
					if (lines[i] == null) break;
					buf.write(lines[i], true);
				}
			}
		}
		return buf;
	}

	public static BufferedFile createBufferedFile(String path, FileSupplier<String[]> supplier) {
		return createBufferedFile(path, true, supplier);
	}

	public static RemoteFile createRemoteFile(String url) {
		if (!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
			Debug.error("The supplied url <" + url + "> was malformatted", new MalformedURLException());
			return null;
		} else {
			Debug.debug("Supplied url <" + url + "> is valid");
		}
		RemoteFile rf = new RemoteFile();
		rf.init(url);
		return rf;
	}

	@FunctionalInterface
	public interface FileSupplier<T> {
		public T supply();
	}

}
