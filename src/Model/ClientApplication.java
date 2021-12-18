package Model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {

    public ClientApplication(){
        try{
            int portNumber = 666;
            Socket socket = new Socket("localhost", portNumber);
            OutputStream output = null;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
                // loop
                while (true) {
                    //listenToServer(in);
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
        System.out.println(e.getStackTrace());
        }

    }
    private static void listenToServer(BufferedReader in) {
        String resp = null;//reads the server response
        try {
            resp = in.readLine();
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(resp);
    }

    public static void sendToServer(PrintWriter out, int header, String message){
        String messageToSend = header + message;
        out.println(messageToSend);
    }

    public static String[] readConsole(PrintWriter out){
        Scanner commandScan = new Scanner(System.in);
        String commandAndArguments = commandScan.nextLine();
        String[] commandAndArgumentsArray = commandAndArguments.split(":");
        return commandAndArgumentsArray;
    }
    private static void readCommand(PrintWriter out, String[] commandAndArgumentsArray){
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
            case "send":
                try{
                    sendToServer(out, 2, commandAndArgumentsArray[1]);}
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
            case "login":
                //TODO add fucntion which asks for username and password calls readconsole to read user inputs
                break;
            case "signup":
                break;
        }}
}
