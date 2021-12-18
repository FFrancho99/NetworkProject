package Model;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientApplication {
    private Socket socket;

    public ClientApplication(){
        try{
            int portNumber = 666;
            socket = new Socket("localhost", portNumber);
            launch();
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
        String[] commandAndArgumentsArray = commandAndArguments.split(":");
        return commandAndArgumentsArray;
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
            case "login":
                sendToServer(out, 1, commandAndArgumentsArray[1]);
                //TODO add fucntion which asks for username and password calls readconsole to read user inputs
                break;
            case "signup":
                break;
        }
    }

    public void launch() throws IOException {
        OutputStream output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
        PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
        ClientApplicationThread threadListen = new ClientApplicationThread(in);
        threadListen.start();
        // loop
        while (true) {
            String[] commandAndArgumentsArray = readConsole(out);
            if (commandAndArgumentsArray.length != 0) { //check if stg has been written
                readCommand(out, commandAndArgumentsArray);
            }
        }
    }


}
