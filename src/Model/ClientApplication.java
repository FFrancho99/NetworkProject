package Model;

import java.awt.image.ImageObserver;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;

public class ClientApplication implements ClientObserver {
    private BufferedReader in;
    private String mess;
    private Boolean maj = false;
    private BigInteger key;
    private Socket socket;

    public ClientApplication() {
        try{
            int portNumber = 666;
            Socket socket = new Socket("localhost", portNumber);
            OutputStream output;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
                ClientApplicationThread threadListen = new ClientApplicationThread(in, this);
                threadListen.start();

                // loop
                System.out.println("Do you want to login or signup?");
                Scanner scanner = new Scanner(System.in);
                switch (scanner.nextLine()){ // Choice of login or Account creation
                    case "login": {
                        // Diffie Hellman key sharing to encrypt communications
                        DiffieHellman dh = new DiffieHellman(definePG()); // DiffieHellman class instanciation
                        BigInteger s = secretNumber(); // Secret number based on P and G determination
                        String DHdata = dh.getP() + ":" + dh.getG() + ":" + dh.determineMessage(s); // Data array instanciation with P, G and the secret message value
                        sendToServer(out,8, DHdata); // Data sharing with the host in clear
                        waiting();
                        this.maj = false;
                        key = dh.determineKey(new BigInteger(mess),s); // Key determination with the server message and personnal secret number

                        // Login password verification
                        ClientLogin cl = new ClientLogin(); // Client Login object instanciation
                        cl.setLogin(); // Ask the login to the user
                        cl.setPassword(); // Ask the password to the user
                        String data = cl.getLogin() + ":" + cl.getPassword(); // Set login password in a data array
                        String Crypteddata = AES.encrypt(data, String.valueOf(key)); // Encrypt the datastring thanks to the key
                        sendToServer(out, 1, Crypteddata); // Send the encrypted data to the server
                        waiting();
                        System.out.println("out of boucle");
                        while(mess.equals("False")){ // If login password is false ask agait
                            this.maj = false;
                            System.out.println("Wrong login or password");
                            cl.setLogin(); // Ask again for login
                            cl.setPassword(); // Ask again for password
                            data = cl.getLogin() + ":" + cl.getPassword(); // Set login password in a data array
                            Crypteddata = AES.encrypt(data, String.valueOf(key)); // Encrypt datastring thanks to the key
                            sendToServer(out, 1, Crypteddata); // Send crypted data to the server
                            while(!maj){} // Wait for the answer
                        }
                        System.out.println("You are succefully logged in");
                    }break;
                    case "signup": {
                        AccountCreator aC = new AccountCreator(); // Create an AccountCreator object
                        aC.setLogin(); // Ask for a new login
                        aC.setPassword(); // Ask for a new password
                        String data = aC.getLogin() + ":" + aC.getPassword(); // Set the informations in a datastring
                        String Crypteddata = AES.encrypt(data, String.valueOf(key)); // Encrypt the datastring
                        sendToServer(out,4,Crypteddata); // Send the crypted data to the server
                        while(!maj){
                            System.out.println("boucle2");
                        } // Wait for the answer
                        while (mess.equals("False")){ // If login already exist
                            this.maj = false;
                            System.out.println("This login is already used please chose another");
                            aC.setLogin(); // Ask again for login
                            aC.setPassword(); // Ask again for password
                            data = aC.getLogin() + ":" + aC.getPassword(); // Set the informations in a datastring
                            Crypteddata = AES.encrypt(data, String.valueOf(key)); // Encrypt the datastring
                            sendToServer(out,4,Crypteddata);  // Send the crypted data to the server
                            while (!maj){} // Wait for the answer
                        }
                        // Login using the new login password
                        ClientLogin cl = new ClientLogin(aC.getLogin(), aC.getPassword());
                        data = cl.getLogin() + ":" + cl.getPassword();
                        Crypteddata = AES.encrypt(data, String.valueOf(key));
                        sendToServer(out, 1, Crypteddata);
                    }break;
                }
                while (true) {
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
        String messageToSend = header + message;
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
                        "if you need help, type 'help'" +
                        "if you want to do something, type the command followed by ':' " +
                        "followed by the command arguments if there are any" +
                        "--------------------------------------------------\n" +
                        "--------------COMMAND: + arguments----------------\n" +
                        "--------------------------------------------------\n" +
                        "'help' ------------------------------------------- if you need help\n" +
                        "'to: + username of your recipient' --------------- to start a conversation with 'username'\n" +
                        "solo --------------------------------------------- to start talking with an imaginary friend\n" +
                        "send: + msg -------------------------------------- to send 'msg' to your recipient\n" +
                        "logout ------------------------------------------- to disconnect\n" +
                        "login -------------------------------------------- to login \n" +
                        "signup ------------------------------------------- to sign up\n");
                break;
            case "to":
                try{
                    String encryptedData = AES.encrypt(commandAndArgumentsArray[1],String.valueOf(key)); // Encryption of the data to send
                    sendToServer(out, 2, encryptedData);}
                catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("You must mention the username of the person you want to talk to!\n" +
                            "Try using the command as follows:\n" +
                            "to: myFriend\n" +
                            "Type 'help' if you're still lost\n");
                }
                break;
            case "solo":

            case "send":
                try{
                    String encryptedData = AES.encrypt(commandAndArgumentsArray[1],String.valueOf(key)); // Encryption of the data to send
                    sendToServer(out, 3, encryptedData);}
                catch (ArrayIndexOutOfBoundsException exception){
                    System.out.println("Are you sure you want to send an empty message? \n" +
                            "No judgment but this is a weird idea. \n" +
                            "Try using the command as follows:\n" +
                            "send: my message is no longer empty \n" +
                            "Type 'help' if you're still lost\n");
                }
                break;
            case "logout":

                break;
            case "imaginary friend":
                sendToServer(out, 6, null);
                break;
        }
    }

    public void launch(PrintWriter out) throws InterruptedException {
        // loop
        System.out.println("Do you want to login or signup?");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextLine()){
            case "login": {
                login(out);
            }
            case "signup":{
                // TODO: 18/12/2021
            }
        }
        while (true) {
            String[] commandAndArgumentsArray = readConsole(out);
            if (commandAndArgumentsArray.length != 0) { //check if stg has been written
                readCommand(out, commandAndArgumentsArray);
            }
        }

    }

    public void login(PrintWriter out) throws InterruptedException {
        ClientLogin cl = new ClientLogin();
        cl.setLogin();
        cl.setPassword();
        String data = cl.getLogin() + ":" + cl.getPassword();
        sendToServer(out, 1, data);
        System.out.println("sent to server");
        while(mess.equals("False")){
            System.out.println("Wrong login or password");
            cl.setLogin();
            cl.setPassword();
            data = cl.getLogin() + ":" + cl.getPassword();
            sendToServer(out, 1, data);
        }
    }

    public ArrayList<BigInteger> definePG(){
        BigInteger p = new BigInteger("23984923039409503948728493059487");
        BigInteger g = new BigInteger("5");
        ArrayList<BigInteger> PG = new ArrayList<BigInteger>(3);
        PG.add(0, p);
        PG.add(1, g);
        return PG;
    }

    public BigInteger secretNumber(){
        Random rand = new Random();
        int int_random = rand.nextInt(30); // Create a random secret number
        return new BigInteger(String.valueOf(int_random));
    }
    public synchronized void waiting(){
        while(!maj){
            try { //Just for the sake of compiling - you may think of a better solution
                System.out.println("waiting for server");
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // Wait for the response
    }

    @Override
    public void update(String s) {
        this.mess = s;
        this.maj = true;
    }
}
