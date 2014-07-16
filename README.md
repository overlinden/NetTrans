NetTrans [![Build Status](https://drone.io/github.com/overlinden/NEtTrans/status.png)](https://drone.io/github.com/overlinden/NetTrans/latest)
========

File transfer made easy  


nettrans is a tiny application to easily transfer data from a source machine to a target machine. 
nettrans is either used for the sending and for the receiving side.
On the server and the client side, the user can decide whether to use a file as input/output or 
if the default pipes stdin/stdout should be used. The transfer is performed via TCP.

Start the application (server mode):  

    Command: nettrans server <port> [<outfile>]
        <port>      : port to listen on
        <outfile>   : output file name, output to stdout if blanc

Start the application (client mode):  

    Command: nettrans client <ip> <port> [<infile>]
        <ip>        : ip addess to connect to
        <port>      : port to connect to
        <infile>    : input file name, input from stdin if blanc



All stable builds can be downloaded here: http://wps-verlinden.de/drone.io/NetTrans/
