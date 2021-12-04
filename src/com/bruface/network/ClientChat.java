package com.bruface.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientChat {

    private Socket socket;


    public ClientChat(String serverName, int serverPort) {
        System.out.println("Connection");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);

        }
        catch(UnknownHostException uhe)
        {  System.out.println("Host unknown: " + uhe.getMessage()); }
        catch(IOException ioe)
        {  System.out.println("Unexpected exception: " + ioe.getMessage()); }
    }
}