import java.io.*;
import java.net.Socket;
import java.util.HashMap;

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
