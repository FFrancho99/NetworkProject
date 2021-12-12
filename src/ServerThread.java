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
            InputStream input = socketSender.getInputStream(); //to read data (at low level, bytes) sent from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(input)); //wrap the InputStream in a BufferedReader to read data as String
            String userName = reader.readLine(); //reads the userName sent by the client

            System.out.println("name of the client: " + userName);
            clientList.put(userName, socketSender);//adds the userName and the corresponding socket to the clientList

            while (true) {//to stay connected, should add a condition
                String recipient = reader.readLine();// reads the recipient name sent by the client
                if(clientList.containsKey(recipient)) {
                    Socket socketRecipient = clientList.get(recipient);
                    OutputStream outputRecipient = socketRecipient.getOutputStream();//to send the data to the client (low level)
                    PrintWriter writerRecipient = new PrintWriter(outputRecipient, true);// wrap it in a PrintWriter to send data in text format
                    String message = reader.readLine();
                    writerRecipient.println(message);
                } else{
                    OutputStream outputSender = socketSender.getOutputStream();//to send the data to the client (low level)
                    PrintWriter writerSender = new PrintWriter(outputSender, true);// wrap it in a PrintWriter to send data in text format
                    writerSender.println("The recipient is not connected");
                    
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    }
