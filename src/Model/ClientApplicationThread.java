package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;

public class ClientApplicationThread extends Thread {

    private final BufferedReader in;
    private final ClientApplication clientApplication;
    private String data;
    private BigInteger key;

    public  ClientApplicationThread(BufferedReader in, ClientApplication clientApplication){
        this.in = in;
        this.clientApplication = clientApplication;
    }
    @Override
    public void run() {
        while(true){
            listenToServer(in);
        }
    }
    private void listenToServer(BufferedReader in) {
        String resp; //reads the server response
        try {
            resp = in.readLine();
            setData(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setData(String data) throws IOException {
        String[] dataContent = data.split(":");
        String header = dataContent[0];
        if(header.equals("D")){
            System.out.println(dataContent[1]);
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
        else if(header.equals("N")){
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
        else if(header.equals("DH")){
            key = clientApplication.setClientKey(new BigInteger(dataContent[1]),new BigInteger(dataContent[2]),new BigInteger(dataContent[3]),dataContent[4]);
        }
        else if(header.equals("DH2")){
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
        else if(header.equals("E")){
            String message = AES.decrypt(dataContent[1],String.valueOf(key));
            System.out.println(message);
        }
        else{
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
    }
}

