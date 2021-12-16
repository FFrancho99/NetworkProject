package Model;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

public class ServerThread extends Thread{
    private Socket socketSender;
    private HashMap<String, Socket> clientList;

    public ServerThread(Socket socket, HashMap<String,Socket> clientList){
        this.socketSender = socket;
        this.clientList = clientList;
    }
    @Override
    public void run() {
        try {
            //done once
            InputStream input = socketSender.getInputStream(); //to read data (at low level, bytes) sent from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(input)); //wrap the InputStream in a BufferedReader to read data as String
            // Diffie Hellman p,g values sharing
            String pValue = reader.readLine(); // Read the p value sent by the client
            String gValue = reader.readLine(); // Read the g value sent by the client
            BigInteger p = new BigInteger(pValue); // Switch the type from string to BigInt
            BigInteger g = new BigInteger(gValue); // Switch the type from string to BigInt
            DiffieHellman DH = new DiffieHellman(p,g); // Create DiffieHellman object of the server for a given client

            Random rand = new Random();
            int int_random = rand.nextInt(30); // Create a random secret number
            BigInteger a = new BigInteger(String.valueOf(int_random)); // Switch type from int to string to BigInt

            sendToClient(socketSender, String.valueOf(DH.determineMessage(a))); // Send message depending on secret number to client
            String mess = reader.readLine(); // Receive the number based on the secret number a of Client

            BigInteger m = new BigInteger(String.valueOf(mess)); // Switch type from int to string to BigInt
            BigInteger key = DH.determineKey(m, a); // Determine the common key between the Server and Client

            System.out.println(key);

            String userName = reader.readLine(); //reads the userName sent by the client
            System.out.println("name of the client: " + userName);
            clientList.put(userName, socketSender);//adds the userName and the corresponding socket to the clientList
            String recipient = reader.readLine();// reads the recipient name sent by the client
            if(clientList.containsKey(recipient)) { //if client connected
                Socket socketRecipient = clientList.get(recipient);
                String message = reader.readLine();
                sendToClient(socketRecipient,message);
                while (true) {//to stay connected, should add a condition
                    String data = reader.readLine();
                    System.out.println(data);
                    sendToClient(socketRecipient,data);
                }

            } else{
                sendToClient(socketSender, "The recipient is not connected" );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendToClient(Socket socket, String message ){
        OutputStream output = null;//to send the data to the client (low level)
        try {
            output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
            writer.println(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
