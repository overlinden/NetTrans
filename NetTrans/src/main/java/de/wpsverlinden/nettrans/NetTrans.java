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

public class NetTrans {

    static class HelpAndExitException extends Exception {

        private static final long serialVersionUID = 1L;
    }

    public static void main(String[] args) {
        if (args.length == 0 || !"stdin".equals(args[args.length - 1]) && !"stdout".equals(args[args.length - 1])) {
            System.out.println("NetTrans 1.0 - written by Oliver Verlinden (http://wps-verlinden.de)\n");
        }
        try {
            if (args.length < 3) {
                throw new HelpAndExitException();
            }
            if ("server".equals(args[0])) {
                Server srv;
                if (args.length == 2) {
                    srv = new Server(args[1], System.out);
                } else if (args.length == 3) {
                    srv = new Server(args[1], new File(args[2]));
                } else {
                    throw new HelpAndExitException();
                }
                srv.run();
            } else if ("client".equals(args[0])) {
                Client cli;
                if (args.length == 3) {
                    cli = new Client(args[1], args[2], System.in);
                } else if (args.length == 4) {
                    cli = new Client(args[1], args[2], new File(args[3]));
                } else {
                    throw new HelpAndExitException();
                }
                cli.run();
            } else {
                throw new HelpAndExitException();
            }
            System.exit(0);
        } catch (HelpAndExitException e) {
            printHelp();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Usage as server:");
        System.out.println("  Command: nettrans server <port> [<outfile>]");
        System.out.println("    <port>    : port to listen on");
        System.out.println("    <outfile> : output file name, output to stdout if blanc\n");

        System.out.println("Usage as client:");
        System.out.println("  Command: nettrans client <ip> <port> [<infile>]");
        System.out.println("    <ip>      : ip addess to connect to");
        System.out.println("    <port>    : port to connect to");
        System.out.println("    <infile>  : input file name, input from stdin if blanc");
        System.exit(0);
    }
}