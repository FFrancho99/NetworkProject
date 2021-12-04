
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    private static ServerSocket serverSocket;
    public static void main(String[] args) {
        int portNumber = 666;
        serverSocket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            acceptClients();

        } catch (IOException e) {
            System.out.println("Failed");

        }
    }

    private static void acceptClients() {
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted");
                ServerThread serverThread = new ServerThread();
            }catch(IOException e){
                System.out.println("Failed");

            }

        }
    }
}