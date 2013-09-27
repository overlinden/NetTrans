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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private File outFile;
	private PrintStream outStream;
	private int port;
	private boolean msgOutput;

	public Server(String port, File outfile) {
		this.port = Integer.parseInt(port);
		this.outFile = outfile;
		this.msgOutput = true;
	}

	public Server(String port, PrintStream outStream) {
		this.port = Integer.parseInt(port);
		this.outStream = outStream;
		this.msgOutput = false;
	}

	public void run() throws IOException {
		BufferedOutputStream bos;
		ServerSocket ssock = new ServerSocket(port);
		if (msgOutput) {
			System.out.println("Server mode... listening on port " + port);
			FileOutputStream fos = new FileOutputStream(outFile);
			bos = new BufferedOutputStream(fos);
		} else {
			bos = new BufferedOutputStream(outStream);
		}
		
		Socket csock = ssock.accept();
		if (msgOutput) {
			System.out.println("Connection established. Waiting for data...");
		}
		receiveBytes(csock, bos);
		bos.close();
	}

	private void receiveBytes(Socket sock, OutputStream dest) throws IOException {
		byte[] buf = new byte[1536];
		int totalBytes = 0;
		String msg;
		
		int count = sock.getInputStream().read(buf);
		while (count >= 0) {
			if (msgOutput) {
				System.out.print("\r");
				msg = "received " + totalBytes + " bytes of data ... ";
				System.out.print(msg);
			}
			dest.write(buf, 0, count);
			totalBytes += count;
			count = sock.getInputStream().read(buf);
		}
		
		if (msgOutput) {
			System.out.println("Data transfer completed.");
		}
		
		sock.shutdownInput();
		sock.close();
	}
}
