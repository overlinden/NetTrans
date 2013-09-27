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

package de.wpsverlinden.nettrans.tests;

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

/**
 *
 * @author oliver
 */
public class NetTransTest {

    @Test
    public void testFileTransfer() {

        try {
            generateTestFile("./testinfile");
            ServerThread srv = new ServerThread("8000", new File("./testoutfile"));
            ClientThread cli = new ClientThread("127.0.0.1", "8000", new File("./testinfile"));

            srv.start();
            Thread.sleep(100);
            cli.start();

            srv.join();
            cli.join();

            assertTrue(fileCheck("./testinfile", "./testoutfile"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    private void deleteTestFiles() {
        File f = new File("./testinfile");
        if (f.exists()) {
            f.delete();
        }
        f = new File("./testoutfile");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void testStreamTransfer() {
        try {
            generateTestFile("./testinfile");
            InputStream is = new FileInputStream("./testinfile");
            PrintStream os = new PrintStream(new FileOutputStream("./testoutfile"));
            ServerThread srv = new ServerThread("8000", os);
            ClientThread cli = new ClientThread("127.0.0.1", "8000", is);

            srv.start();
            Thread.sleep(100);
            cli.start();

            srv.join();
            cli.join();

            assertTrue(fileCheck("./testinfile", "./testoutfile"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTestFiles();
        }
    }

    private boolean fileCheck(String file1, String file2) throws IOException {
        File in = new File(file1);
        File out = new File(file2);

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

    private void generateTestFile(String filename) throws IOException {
        File testfile = new File(filename);
        FileWriter fw = new FileWriter(testfile);
        Random generator = new Random(System.currentTimeMillis());
        byte[] bytes = new byte[1024];

        for (int i = 0; i < 10 * 1024; i++) {
            generator.nextBytes(bytes);
            fw.write(new String(bytes).toCharArray());
        }
    }
}