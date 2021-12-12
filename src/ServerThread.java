import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream(); //to read data (at low level, bytes) sent from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(input)); //wrap the InputStream in a BufferedReader to read data as String
            String line = reader.readLine();    // reads a line of text
            System.out.println(line);
            OutputStream output = socket.getOutputStream();//to send the data to the client (low level)
            PrintWriter writer = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
            writer.println("Hello too");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    }
