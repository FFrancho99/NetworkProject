import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        int portNumber = 666;
        try {
            Socket socket = new Socket("localhost", portNumber);
            ClientThread clientThread = new ClientThread(socket);

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }

    }
}