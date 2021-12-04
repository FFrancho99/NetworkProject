import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        int portNumber = 666;
        try {
            socket = new Socket("localhost", portNumber);

            ClientThread clientThread = new ClientThread(socket);


        } catch (IOException e) {

        }

    }
}