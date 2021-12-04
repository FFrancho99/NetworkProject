
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ServerSocket serverSocket;
    public static void main(String[] args) {
        int portNumber = 666;
        serverSocket = null;
        ArrayList<ServerThread>  threadArrayList = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(portNumber);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                ServerThread serverThread = new ServerThread(socket, threadArrayList);
                serverThread.start();
                threadArrayList.add(serverThread);
                }

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
