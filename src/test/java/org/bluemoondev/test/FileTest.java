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
package org.bluemoondev.test;

import org.bluemoondev.jdaextended.util.io.BufferedFile;
import org.bluemoondev.jdaextended.util.io.FileFactory;
import org.bluemoondev.jdaextended.util.io.RemoteFile;

/**
 * <strong>Project:</strong> JDAExtended<br>
 * <strong>File:</strong> FileTest.java<br>
 * <p>
 * TODO: Add description
 * </p>
 *
 * @author <a href = "https://bluemoondev.org"> Matt</a>
 */
public class FileTest {
	
	public static void testFile() {
		BufferedFile f = FileFactory.createBufferedFile("./test.txt", false, () -> {
			return new String[] { "This is the default line" };
		});
		f.write("something something\n");
		f.write("something something 1\n");
		f.write("something something 2\n");
		f.write("something something 3\n");
		
		System.out.println(f.readLine(2));
		System.out.println(f.readLine(3));
		System.err.println(f.read());
		
		f.close();
	}
	
	
	public static void testRemote() {
		RemoteFile f = FileFactory.createRemoteFile("https://raw.githubusercontent.com/Blue-Moon-Development/JDAExtended/master/pom.xml");
		System.out.println(f.readLine(5));
		f.close();
	}
	
	public static void main(String[] args) {
		testFile();
		testRemote();
	}

}
