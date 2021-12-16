/*
package Model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NewClient {

    public static void main(String[] args) {
        int portNumber = 666;
        try {
            Socket socket = new Socket("localhost", portNumber);
            OutputStream output = null;
            try {
                output = socket.getOutputStream(); //to send the data to the client (low level, bytes)
                PrintWriter out = new PrintWriter(output, true);// wrap it in a PrintWriter to send data in text format
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//to receive the data from the server
                // loop
                while (true) {
                    listenToServer(in);
                    String command = readConsole();
                    sendToServer();
                    // listenners should call sendToServer


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
        String messageToSend = header + message.nextLine();
        out.println(messageToSend);
    }

    public static String readConsole(){
        Scanner command = new Scanner(System.in);
        String commandUser = command.nextLine();
        return commandUser;
    }
}
*/
