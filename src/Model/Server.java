package Model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static ServerSocket serverSocket; // static bc only one server

    public static void main(String[] args) {
        int serverPortNumber = 666;
        serverSocket = null;
        ArrayList<ServerThreadNew>  threadArrayList = new ArrayList<>();
        HashMap<String, Socket[]> clientList = new HashMap<>(); //HashMap contains the userNames (key) with the corresponding socket (value)


        try {
            int serverBacklog = 0; // 0 for default value
            if (args.length == 0){ // if IP address is NOT provided when starting the server
                //InetAddress serverAddress = InetAddress.getLocalHost();
                //serverSocket = new ServerSocket(serverPortNumber, serverBacklog, serverAddress); //creates the socket of the server
                                                        // IP address is localhost as it is not precised
                serverSocket = new ServerSocket(serverPortNumber);
            }
            else {
                InetAddress serverAddress = InetAddress.getByName(args[0]);
                serverSocket = new ServerSocket(serverPortNumber, serverBacklog, serverAddress); //creates the socket of the server
            }

            System.out.println("Server started:" +
                    "Host="+ serverSocket.getInetAddress().getHostAddress()+
                    "   Port=" + serverSocket.getLocalPort() );
        }catch (IOException e) {
            System.out.println("Server couldn't start: unable to request port "+ serverSocket.getLocalPort());
        }
        try {
            while(true){
                System.out.println("Waiting for a new client");
                Socket clientSocket = serverSocket.accept(); //creates a socket for each new client accepted
                System.out.println("New Client connection: from "+ clientSocket.getInetAddress().getHostAddress());
                ServerThreadNew serverThread = new ServerThreadNew(clientSocket, clientList); //creates the thread for the new client
                serverThread.start();
                threadArrayList.add(serverThread);
            }
            //serverSocket.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
