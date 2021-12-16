package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static ServerSocket serverSocket;
    public static void main(String[] args) {
        int portNumber = 666;
        serverSocket = null;
        ArrayList<ServerThreadNew>  threadArrayList = new ArrayList<>();
        HashMap<String, Socket[]> clientList = new HashMap<>(); //HashMap contains the userNames (key) with the corresponding socket (value)
        try {
            serverSocket = new ServerSocket(portNumber); //creates the socket of the server
            System.out.println("Model.Server started");
        }catch (IOException e) {
            System.out.println("Unable to request port");
        }
        try {
            while(true){
                System.out.println("Waiting for a new client");
                Socket socket = serverSocket.accept(); //creates a socket for each new client accepted
                System.out.println("Model.Client accepted");
                ServerThreadNew serverThread = new ServerThreadNew(socket, clientList); //creates the thread for the new client
                serverThread.start();
                threadArrayList.add(serverThread);
            }
            //serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
