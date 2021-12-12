import java.io.*;
import java.net.Socket;

public class ClientThread {

    private Socket socket;
    public ClientThread(Socket socket){
        this.socket = socket;
        // here client doesn't stay connected, should add a loop
        OutputStream output = null;//to send the data to the client (low level)
        try {
            output = socket.getOutputStream();
            PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Hello");
            String response = in.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
