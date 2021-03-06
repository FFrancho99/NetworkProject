package Model;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ClientApplication implements ClientObserver {
    private BufferedReader in;
    private String mess;
    private Boolean maj = false;
    private BigInteger serverKey;
    private BigInteger clientKey;
    private BigInteger m2;
    private PrintWriter out;
    private int serverPortNumber;
    private String serverHostName;
    private InetAddress serverAddress;
    private Boolean newClient = false;
    private Boolean stay = true;

    public ClientApplication(String hostName, int portNumber) {
        this.serverPortNumber = portNumber;
        this.serverHostName = hostName;
        functionClientApplication();
    }
    public ClientApplication(InetAddress serverAddress, int portNumber){
        this.serverPortNumber = portNumber;
        this.serverAddress = serverAddress;
        functionClientApplication();
    }

    public void functionClientApplication(){
        try{
            Socket socket = new Socket(serverHostName, serverPortNumber);
            OutputStream output;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
                ClientApplicationThread threadListen = new ClientApplicationThread(in, this);
                threadListen.start();
                Scanner scanner = new Scanner(System.in);

                // Diffie Hellman key sharing to encrypt communications
                DiffieHellman dh = new DiffieHellman(definePG()); // DiffieHellman class instanciation
                BigInteger s = secretNumber(); // Secret number based on P and G determination
                String DHdata = dh.getP() + ":" + dh.getG() + ":" + dh.determineMessage(s); // Data array instanciation with P, G and the secret message value
                sendToServer(out,8, DHdata); // Data sharing with the host in clear
                waiting();
                serverKey = dh.determineKey(new BigInteger(mess),s); // Key determination with the server message and personnal secret number

                boolean control = false;
                while (!control){
                    System.out.println("Do you want to login or signup?");
                    switch (scanner.nextLine()){ // Choice of login or Account creation
                        case "login": {
                            // Login verification
                            ClientLogin cl = new ClientLogin(); // Client Login object instanciation
                            cl.setLogin(); // Ask the login to the user
                            String data = cl.getLogin(); // Set login password in a data array
                            String Crypteddata = AES.encrypt(data, String.valueOf(serverKey)); // Encrypt the datastring thanks to the key
                            sendToServer(out, 1, Crypteddata); // Send the encrypted data to the server
                            waiting();
                            control = true;
                            if(mess.equals("False")){ // If login doesn't exist
                                System.out.println("This login doesn't exist, please create an account");
                                control = false;
                                break;
                            }
                            String nonce = mess;
                            String cryptedNonce = AES.encrypt(nonce, String.valueOf(serverKey));//encrypter le nonce using key
                            sendToServer(out, 10, cryptedNonce);
                            waiting();
                            if(mess.equals("true")){
                                cl.setPassword();
                                data = cl.getLogin() + ":" + cl.getPassword();
                                String cryptedData = AES.encrypt(data, String.valueOf(serverKey));
                                sendToServer(out, 11, cryptedData);
                                waiting();
                                while(mess.equals("False")){
                                    System.out.println("Wrong password");
                                    cl.setPassword();
                                    data = cl.getLogin() + ":" + cl.getPassword();
                                    cryptedData = AES.encrypt(data, String.valueOf(serverKey));
                                    sendToServer(out, 11, cryptedData);
                                    waiting();
                                }
                            }
                            //envoyer le encrypted nonce
                            //if nonce not okay, print "authentication faile, please try again" + control = false;
                        }break;
                        case "signup": {
                            newClient = true;
                            AccountCreator aC = new AccountCreator(); // Create an AccountCreator object
                            aC.setLogin(); // Ask for a new login
                            aC.setPassword(); // Ask for a new password
                            String data = aC.getLogin() + ":" + aC.getPassword(); // Set the informations in a datastring
                            String Crypteddata = AES.encrypt(data, String.valueOf(serverKey)); // Encrypt the datastring
                            sendToServer(out,4,Crypteddata); // Send the crypted data to the server
                            waiting();
                            while (mess.equals("False")){ // If login already exist
                                this.maj = false;
                                System.out.println("This login is already used please chose another");
                                aC.setLogin(); // Ask again for login
                                aC.setPassword(); // Ask again for password
                                data = aC.getLogin() + ":" + aC.getPassword(); // Set the informations in a datastring
                                Crypteddata = AES.encrypt(data, String.valueOf(serverKey)); // Encrypt the datastring
                                sendToServer(out,4,Crypteddata);  // Send the crypted data to the server
                                waiting();
                            }
                            // Login using the new login password
                            ClientLogin cl = new ClientLogin(aC.getLogin(), aC.getPassword());
                            data = cl.getLogin() + ":" + cl.getPassword();
                            Crypteddata = AES.encrypt(data, String.valueOf(serverKey));
                            sendToServer(out, 1, Crypteddata);
                            control = true;
                        }break;
                        default:{
                            System.out.println("You are not logged in yet, please login or signup");
                        }
                    }
                }
                System.out.println("You can now use commands\n" +
                        "type 'help' if you need help");
                while (stay) {
                    String[] commandAndArgumentsArray = readConsole(out);
                    if (commandAndArgumentsArray.length != 0) { //check if stg has been written
                        readCommand(out, commandAndArgumentsArray);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public void sendToServer(PrintWriter out, int header, String message){
        String messageToSend = header + "-" + message;
        out.println(messageToSend);
    }

    public String[] readConsole(PrintWriter out){
        Scanner commandScan = new Scanner(System.in);
        String commandAndArguments = commandScan.nextLine();
        return commandAndArguments.split(":");
    }

    private void readCommand(PrintWriter out, String[] commandAndArgumentsArray){
        String command = commandAndArgumentsArray[0].trim(); //trim to remove spaces at start and end of string
        switch (command){
            case "help":
                System.out.println("Got confused with the chat app? Bob is here to help you.\n" +
                        "If you want to use the app, type one of the following command,\n" +
                        "followed by ':', followed by the command arguments if there are any\n" +
                        "--------------COMMAND: + arguments----------------\n" +
                        "--------------------------------------------------\n" +
                        "'help' ------------------------------------------- if you need help\n" +
                        "'to: + username of your recipient' --------------- to start a conversation with 'username'\n" +
                        "solo --------------------------------------------- to have a good laugh\n" +
                        "send: + msg -------------------------------------- to send 'msg' to your recipient\n" +
                        "logout ------------------------------------------- to disconnect\n" +
                        "login -------------------------------------------- to login \n" +
                        "signup ------------------------------------------- to sign up\n");
                break;
            case "to": // User wants to talk with another -> NEW DH key
                try{
                    // Who's the recipient
                    String encryptedData = AES.encrypt(commandAndArgumentsArray[1],String.valueOf(serverKey)); // Encryption of the data to send
                    sendToServer(out, 2, encryptedData);
                    // Client 1 DH agreement
                    DiffieHellman DH = new DiffieHellman(definePG());
                    BigInteger s = secretNumber(); // Secret number based on P and G determination
                    String DHdata = DH.getP() + ":" + DH.getG() + ":" + DH.determineMessage(s); // Data array instanciation with P, G and the secret message value
                    sendToServer(out,7,DHdata); // Send DH data to the recipient
                    waiting();
                    if(newClient){
                        waiting();
                    }
                    clientKey = DH.determineKey(new BigInteger(mess),s);
                    System.out.println("the server's ready, send your message using the command 'send:'");
                }
                catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("You must mention the username of the person you want to talk to!\n" +
                            "Try using the command as follows:\n" +
                            "to: myFriend\n" +
                            "Type 'help' if you're still lost\n");
                }
                break;
            case "solo":
                sendToServer(out, 6, null);
                break;
            case "send":
                try{
                    String encryptedData = AES.encrypt(commandAndArgumentsArray[1],String.valueOf(clientKey)); // Encryption of the data to send
                    sendToServer(out, 3, encryptedData);}
                catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("Are you sure you want to send an empty message? \n" +
                            "No judgment but this is a weird idea. \n" +
                            "Try using the command as follows:\n" +
                            "send: my message is no longer empty \n" +
                            "Type 'help' if you're still lost\n");
                }
            System.out.println("Message sent!");
                break;
            case "logout":
                System.out.println("Sorry to see you go :'(");
                new ClientApplication(this.serverAddress, this.serverPortNumber);
                break;
        }
    }

    public ArrayList<BigInteger> definePG(){
        BigInteger p = new BigInteger("23984923039409503948728493059487");
        BigInteger g = new BigInteger("5");
        ArrayList<BigInteger> PG = new ArrayList<>(3);
        PG.add(0, p);
        PG.add(1, g);
        return PG;
    }

    public BigInteger secretNumber(){
        Random rand = new Random();
        int int_random = rand.nextInt(30); // Create a random secret number
        return new BigInteger(String.valueOf(int_random));
    }
    public void waiting(){
        while(!maj){
            try { //Just for the sake of compiling - you may think of a better solution
                System.out.println("waiting for server");
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } maj=false;// Wait for the response
    }

    @Override
    public void update(String s) {
        this.mess = s;
        this.maj = true;
    }

    public BigInteger setClientKey(BigInteger p, BigInteger g, BigInteger m, String sender) {
        DiffieHellman Dh = new DiffieHellman(p,g);
        BigInteger s = secretNumber();
        this.m2 = Dh.determineMessage(s);
        clientKey = Dh.determineKey(m,s);
        String data = m2 + ":" + sender;
        sendToServer(out,9,data);
        return clientKey;
    }
}
