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
                // loop
                while (true) {
                System.out.println("Do you want to: 1) send a message 2) check your messages 3) exit");
                int choice = (new Scanner(System.in)).nextInt();
                switch (choice){
                    case 1:
                        System.out.println("Please enter the recipient name of the message");
                        sendToServer(out);// send the recipient name to the server
                        System.out.println("Please enter the message");
                        sendToServer(out);// send the message to the server
                        System.out.println("Enter new message");
                        while (!((new Scanner(System.in)).nextLine().equals("exit"))) {
                            System.out.println("Enter new message");
                            sendToServer(out);
                        }
                        break;
                    case 2:
                        System.out.println("Waiting for response");
                        String response = in.readLine();//reads the server response
                        System.out.println(response);

                        break;
                    case 3:
                        socket.close();
                        break;
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