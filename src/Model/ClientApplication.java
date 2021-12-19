package Model;

import java.awt.image.ImageObserver;
import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Arrays;
import java.util.Scanner;

public class ClientApplication implements ClientObserver {
    private BufferedReader in;
    private String mess;
    private Boolean maj = false;
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
                switch (scanner.nextLine()){
                    case "login": {
                        ClientLogin cl = new ClientLogin();
                        cl.setLogin();
                        cl.setPassword();
                        String data = cl.getLogin() + ":" + cl.getPassword();
                        sendToServer(out, 1, data);
                        while(!maj){
                        }
                        while(mess.equals("False")){
                            this.maj = false;
                            System.out.println("Wrong login or password");
                            cl.setLogin();
                            cl.setPassword();
                            data = cl.getLogin() + ":" + cl.getPassword();
                            sendToServer(out, 1, data);
                            while(!maj){
                            }
                        }
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
                    sendToServer(out, 2, commandAndArgumentsArray[1]);}
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
                    sendToServer(out, 3, commandAndArgumentsArray[1]);}
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
                sendToServer(out, 4, null);
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

    @Override
    public void update(String s) {
        this.mess = s;
        this.maj = true;
    }
}
