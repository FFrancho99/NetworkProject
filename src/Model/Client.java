package Model;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        int portNumber = 666;
        // Diffie Hellman values for key exchange
        BigInteger p = new BigInteger("23");
        BigInteger g = new BigInteger("5");
        DiffieHellman DH = new DiffieHellman(p,g);

        try {
            Socket socket = new Socket("localhost", portNumber);
            OutputStream output = null;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server

                // Diffie-Hellman key sharing
                out.println(DH.getP()); // Send p to the server
                out.println(DH.getG()); // Send g to the server

                Random rand = new Random();
                int int_random = rand.nextInt(30); // Create a random secret number
                BigInteger b = new BigInteger(String.valueOf(int_random)); // Switch type from int to string to BigInt

                out.println(String.valueOf(DH.determineMessage(b))); // Send number to server based on the secret number

                String message = in.readLine(); // Receive the number based on the secret number a of Server
                BigInteger m = new BigInteger(String.valueOf(message)); // Switch type from int to string to BigInt
                BigInteger key = DH.determineKey(m, b); // Determine the common key between the Server and Client

                System.out.println(key);
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }
    public static void sendToServer(PrintWriter out){
        Scanner userName = new Scanner(System.in);
        String userNameClient = userName.nextLine();
        out.println(userNameClient);
    }
}