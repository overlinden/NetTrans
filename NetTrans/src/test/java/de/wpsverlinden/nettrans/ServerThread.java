/*
 *   NetTrans - File transfer made easy
 *   Copyright (C) 2012  Oliver Verlinden (http://wps-verlinden.de)
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.wpsverlinden.nettrans;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;


public class ServerThread extends Thread {

	private String port;
	private File outFile;
	private PrintStream outStream;

	public ServerThread(String port, File outfile) {
		this.port = port;
		this.outFile = outfile;
	}
	
	public ServerThread(String port, PrintStream outStream) {
		this.port = port;
		this.outStream = outStream;
	}

	@Override
	public void run() {
		Server srv = null;
		if (outFile != null) {
			srv = new Server(port, outFile);	
		}
		if (outStream != null) {
			srv = new Server(port, outStream);
		}
		
		try {
			if (srv != null) {
				srv.run();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
