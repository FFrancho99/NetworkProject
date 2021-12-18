
package Model;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ServerThreadNew extends Thread{
    private Socket socketSender;
    private HashMap<String,Socket[]> clientList;
    private String sender;
    private String recipient;
    private Socket socketOfRecipient;

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
                String header = String.valueOf(data.charAt(0));
                String dataContent = data.substring(1);



            switch (header){
                case "1": //login
                    System.out.println("name of the client: " + dataContent);
                    sender = dataContent;
                    clientList.put(sender, new Socket[]{socketSender, null});   //adds the userName and the corresponding socket to the clientList

                    break;
                case "2"://to
                    System.out.println("name of the recipient: " + dataContent);
                    recipient = dataContent;
                    Socket[] socketRecipient = clientList.get(recipient);
                    socketOfRecipient = socketRecipient[0];
                    clientList.put(sender, new Socket[]{socketSender, socketOfRecipient});
                    sendToClient(socketSender, "you can now send a message");
                    break;
                case "3"://send
                    sendToClient(socketOfRecipient, dataContent);
                    System.out.println("send message");
                    break;

                case "4":
                    break;

            }


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
