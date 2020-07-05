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

import org.bluemoondev.jdaextended.util.exceptions.JDAIOException;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> FileBuilder.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FileBuilder {

	private boolean			append;
	private FileConsumer	defaultContents;
	private FileType		type;
	private String			path;
	private boolean			isUrl;

	/**
	 * Creates a FileBuilder to be used to create either a BufferedFile or a
	 * RemoteFile.
	 * It will attempt to validate whether or not the supplied path is a url and
	 * automatically set the FileType to REMOTE.
	 * If this fails, set the type manually {@link #setType(FileType)}
	 * 
	 * @param path The path or url to the file
	 */
	public FileBuilder(String path) {
		this.path = path;
		type = FileType.LOCAL;
		isUrl = path.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		if (isUrl) type = FileType.REMOTE;
	}

	/**
	 * Sets the type of the file to build.
	 * 
	 * @param  type The type of file.
	 *              {@link org.bluemoondev.jdaextended.util.io.FileType
	 *              FileType.LOCAL or FileType.REMOTE}
	 * @return      this
	 */
	public FileBuilder setType(FileType type) {
		this.type = type;
		return this;
	}

	public FileBuilder setAppendable(boolean append) {
		this.append = append;
		return this;
	}

	public FileBuilder setHasDefaultContents(FileConsumer defaultContents) {
		this.defaultContents = defaultContents;
		return this;
	}

	public IOFile build() {
		IOFile file = null;
		switch (type) {
			case LOCAL:
				file = new FileInstance.LocalInstance().create();
				if (append) ((BufferedFile) file).shouldAppend(append);
			break;
			case REMOTE:
				file = new FileInstance.RemoteInstance().create();
			break;
		}

		file.init(path);
		if (file instanceof BufferedFile && defaultContents != null && ((BufferedFile) file).isNewlyCreated())
			defaultContents.consume((BufferedFile) file);
		return file;
	}

	public BufferedFile buildLocal() {
		return (BufferedFile) build();
	}

	public RemoteFile buildRemote() {
		if(type != FileType.REMOTE) {
			throw new JDAIOException("Attempted to build a RemoteFile, but the file type is set to LOCAL");
		}
		
		if(!isUrl) {
			throw new JDAIOException("Attempted to build a RemoteFile, but the URL ( " + path + " ) is malformed");
		}
		return (RemoteFile) build();
	}

	@FunctionalInterface
	public interface FileConsumer {
		public void consume(BufferedFile buf);
	}

}

abstract class FileInstance {
	public abstract IOFile create();

	public static class LocalInstance extends FileInstance {

		@Override
		public IOFile create() {
			return new BufferedFile();
		}
	}

	public static class RemoteInstance extends FileInstance {

		@Override
		public IOFile create() {
			return new RemoteFile();
		}

	}
}
