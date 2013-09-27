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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client {

	private String ip;
	private File inFile;
	private InputStream inStream;
	private int port;
	private boolean msgOutput;

	public Client(String ip, String port, File infile) {
		this.ip = ip;
		this.port = Integer.parseInt(port);
		this.inFile = infile;
		this.msgOutput = true;
	}

	public Client(String ip, String port, InputStream inStream) {
		this.ip = ip;
		this.port = Integer.parseInt(port);
		this.inStream = inStream;
		this.msgOutput = false;
	}

	public void run() throws IOException {

		BufferedInputStream bis;
		if (msgOutput) {
			System.out.println("Client mode... connecting to [" + ip + ":" + port + "]");
			FileInputStream fis = new FileInputStream(inFile);
			bis = new BufferedInputStream(fis);

		} else {
			bis = new BufferedInputStream(inStream);
		}
		Socket csock = new Socket(ip, port);
		if (msgOutput) {
			System.out.println("Connection established. Sending data...");
		}
		sendBytes(csock, bis);
		bis.close();
	}

	private void sendBytes(Socket sock, InputStream source) throws IOException {
		byte[] buf = new byte[1536];
		int totalBytes = 0;
		String msg;
		
		int count = source.read(buf);
		while (count >= 0) {
			sock.getOutputStream().write(buf, 0, count);
			totalBytes += count;
			if (msgOutput) {
				System.out.print("\r");
				msg = "sent " + totalBytes + " bytes of data ... ";
				System.out.print(msg);
			}
			count = source.read(buf);
		}
		
		if (msgOutput) {
			System.out.println("Data transfer completed.");
		}
		sock.shutdownOutput();
		sock.close();
	}
}
