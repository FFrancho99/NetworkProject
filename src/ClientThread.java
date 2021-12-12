import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread {

    private Socket socket;
    public ClientThread(Socket socket){
        this.socket = socket;

        OutputStream output = null;
        try {
            output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
            PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server

            System.out.println("Please enter your username");
            Scanner userName = new Scanner(System.in);
            String userNameClient = userName.nextLine();
            out.println(userNameClient);// send the userName to the server

            System.out.println("Do you want to send a message?");
            if ((new Scanner(System.in)).nextLine().equals("yes")){
                System.out.println("Please enter the recipient name of the message");
                Scanner recipientName = new Scanner(System.in);
                String recipientNameClient = recipientName.nextLine();
                out.println(recipientNameClient);// send the recipient name to the server

                System.out.println("Please enter the message");
                Scanner message = new Scanner(System.in);
                String messageToSend = message.nextLine();
                out.println(messageToSend);// send the message to the server
            }else{
                System.out.println("Waiting for response");

            }

            while (true) { //to stay connected, should add a condition
                String response = in.readLine();//reads the server response
                System.out.println(response);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
