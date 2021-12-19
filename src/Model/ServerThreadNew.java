
package Model;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ServerThreadNew extends Thread{
    private Socket socketSender;
    private HashMap<String,Socket[]> clientList;
    private BigInteger key;
    private String sender;
    private String recipient;
    private Socket socketOfRecipient;
    private int nonce;

    public ServerThreadNew(Socket socket, HashMap<String,Socket[]> clientList){
        this.socketSender = socket;
        this.clientList = clientList;
    }
    @Override
    public void run() {
        try {
            //done once
            InputStream input = socketSender.getInputStream(); //to read data (at low level, bytes) sent from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(input)); //wrap the InputStream in a BufferedReader to read data as String
            while(true){
                String data = reader.readLine(); //reads the userName sent by the client
                String[] dataC = data.split("-");
                String dataContent = dataC[1];
                String header = dataC[0];



            switch (header){
                case "1": //login
                    String DecryptedData = AES.decrypt(dataContent,String.valueOf(key)); // Decrypt the received datastring
                    String[] DecryptedDataArry = DecryptedData.split(":"); // Split the data into a list
                    sender = DecryptedDataArry[0]; // First element of the data is the login
                    ClientLogin clientLogin = new ClientLogin(DecryptedDataArry[0]); // Create a new ClientLogin object with login password received
                    if(clientLogin.checkLogin()){ // Verification of the login password
                        clientList.put(sender, new Socket[]{socketSender, null});   //adds the userName and the corresponding socket to the clientList
                        System.out.println(sender + " added to the clientList");
                        Random rand = new Random();
                        nonce = rand.nextInt(1000000);
                        data = "N:" + nonce;
                        sendToClient(socketSender, data); // Correct login
                    }
                    else{ // Incorect login
                        sendToClient(socketSender, "H:False");
                    }

                    break;
                case "2"://to
                    // Recipient of the conversation
                    String decryptedData = AES.decrypt(dataContent,String.valueOf(key));
                    recipient = decryptedData;
                    Socket[] socketRecipient = clientList.get(recipient);
                    socketOfRecipient = socketRecipient[0];
                    clientList.put(sender, new Socket[]{socketSender, socketOfRecipient});
                    clientList.put(recipient,new Socket[]{socketOfRecipient,socketSender});

                    // Clear the database
                    FileWriter cleanWriter = new FileWriter("CryptedConversations.txt");
                    cleanWriter.close();
                    break;
                case "3"://send
                    FileWriter fileWriter = new FileWriter("CryptedConversations.txt",true);

                    data = "E:" + dataContent + ":" + sender;

                    fileWriter.write(dataContent);
                    fileWriter.write("\n");
                    fileWriter.close();

                    sendToClient(clientList.get(sender)[1], data);
                    System.out.println("message sent");
                    break;

                case "4": // Sign up
                    String DecryptedData2 = AES.decrypt(dataContent,String.valueOf(key)); // Decryption of thje datastring
                    String[] newS = DecryptedData2.split(":"); // Split into a list
                    sender = newS[0];
                    AccountCreator aC = new AccountCreator(newS[0],newS[1]); // Account creator instanciation with the received data
                    if(aC.checkLogin()){ // Check if login already exist
                        System.out.println("This login is already taken choose another"); // login already exist
                        sendToClient(socketSender,"H:False");
                    }else{ // Login is unique
                        System.out.println("Registering succefful");
                        aC.saveLoginPassword(); // Save the login password in the database
                        sendToClient(socketSender,"D:Login Password saved");
                    }
                    break;
                case "5": // Quit
                    break;
                case "6": // Imaginary friend communication
                    soloMode(socketSender);
                    break;
                case "7": // Diffie Hellman key sharing Client-Client
                    data = "DH:" + dataContent + ":" + sender;
                    sendToClient(clientList.get(sender)[1], data);
                    break;
                case "8": // Diffie Hellman key sharing communication server-client
                    BigInteger s = secretNumber();
                    sender = dataContent;
                    String[] PG = dataContent.split(":");
                    DiffieHellman dh = new DiffieHellman(new BigInteger(PG[0]),new BigInteger(PG[1]));
                    data = "H:" + dh.determineMessage(s); // H header is to hide the information to the user
                    sendToClient(socketSender, data);
                    key = dh.determineKey(new BigInteger(PG[2]),s);
                    break;
                case "9":
                    String[] dataArray = dataContent.split(":");
                    data = "DH2:" + dataArray[0];
                    sendToClient(clientList.get(dataArray[1])[0],data);
                    break;
                case "10": //nonce reception
                    String receivedNonce = dataContent;
                    String decryptedNonce = AES.decrypt(receivedNonce, String.valueOf(key));
                    if(decryptedNonce.equals(String.valueOf(nonce))){
                        sendToClient(clientList.get(sender)[0], "H:true");
                    }else {
                        sendToClient(clientList.get(sender)[0], "H:false");
                    }break;
                case "11":
                    String decryptedData3 = AES.decrypt(dataContent, String.valueOf(key));
                    String[] arrayOfDecryptedData3 = decryptedData3.split(":");
                    ClientLogin cl = new ClientLogin(arrayOfDecryptedData3[0], arrayOfDecryptedData3[1]);
                    if(cl.checkPassword()){ // Verification of the login password
                        sendToClient(clientList.get(sender)[0], "D:login successful"); // Correct password
                    }
                    else{
                        sendToClient(clientList.get(sender)[0], "False");
                    }break;
            }
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
    public void sendToClient(Socket socket, String message){
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
    public BigInteger secretNumber(){
        Random rand = new Random();
        int int_random = rand.nextInt(30); // Create a random secret number
        return new BigInteger(String.valueOf(int_random));
    }

    private void soloMode(Socket socket) throws IOException {
        File listOfJokes = new File("src/Model/jokeDatabase");
        String joke = null;
        Random rand = new Random();
        int n = 0;
        Scanner scanner = new Scanner(listOfJokes);
        while (scanner.hasNext()) {
            ++n;
            String line = scanner.nextLine();
            if (rand.nextInt(n) == 0)
                joke = line;
        }
        scanner.close();
        sendToClient(clientList.get(sender)[0], "J:" + joke);

    }


}
