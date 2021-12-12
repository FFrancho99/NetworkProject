import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        int portNumber = 666;
        try {
            Socket socket = new Socket("localhost", portNumber);
            OutputStream output = null;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
                /// login
                System.out.println("Please enter your username");
                sendToServer(out);// send the userName to the server
                //create switch case

                System.out.println("Do you want to send a message?");
                if ((new Scanner(System.in)).nextLine().equals("yes")){
                    System.out.println("Please enter the recipient name of the message");
                    sendToServer(out);// send the recipient name to the server
                    System.out.println("Please enter the message");
                    sendToServer(out);// send the message to the server
                    while (true) { //to stay connected, should add a condition
                        System.out.println("enter new message");
                        sendToServer(out);
                        // String response = in.readLine();//reads the server response
                        //System.out.println(response);
                    }
                }else{
                    System.out.println("Waiting for response");
                    while (true) { //to stay connected, should add a condition
                        String response = in.readLine();//reads the server response
                        System.out.println(response);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    public static void sendToServer(PrintWriter out){
        Scanner userName = new Scanner(System.in);
        String userNameClient = userName.nextLine();
        out.println(userNameClient);

    }
}