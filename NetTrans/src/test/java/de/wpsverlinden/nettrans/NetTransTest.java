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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class NetTransTest {

    @Test
    public void testFileTransfer() throws Exception {

        File source = generateTestFile("testinfile");
        File destination = File.createTempFile("testoutfile", "tmp");
        ServerThread srv = new ServerThread("8000", destination);
        ClientThread cli = new ClientThread("127.0.0.1", "8000", source);

        srv.start();
        Thread.sleep(100);
        cli.start();

        srv.join();
        cli.join();

        assertTrue(fileCheck(source.getAbsoluteFile(), destination));
        
        source.delete();
        destination.delete();
    }

    @Test
    public void testStreamTransfer() throws Exception {
        File source = generateTestFile("testinfile");
        File destination = File.createTempFile("testoutfile", "tmp");
        InputStream is = new FileInputStream(source);
        PrintStream os = new PrintStream(new FileOutputStream(destination));
        ServerThread srv = new ServerThread("8001", os);
        ClientThread cli = new ClientThread("127.0.0.1", "8001", is);

        srv.start();
        Thread.sleep(100);
        cli.start();

        srv.join();
        cli.join();

        assertTrue(fileCheck(source.getAbsoluteFile(), destination));
        
        source.delete();
        destination.delete();
    }

    private boolean fileCheck(File in, File out) throws IOException {

        if (in.length() == out.length()) {
            FileInputStream fis1 = new FileInputStream(in);
            FileInputStream fis2 = new FileInputStream(out);
            int temp = 0;
            while ((temp = fis1.read()) != -1) {
                if (temp != fis2.read()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private File generateTestFile(String filename) throws IOException {
        File testfile = File.createTempFile(filename, "tmp");
        FileWriter fw = new FileWriter(testfile);
        Random generator = new Random(System.currentTimeMillis());
        byte[] bytes = new byte[1024];

        for (int i = 0; i < 512; i++) {
            generator.nextBytes(bytes);
            fw.write(new String(bytes).toCharArray());
        }
        fw.close();
        return testfile;
    }
}