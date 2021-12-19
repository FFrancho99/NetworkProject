package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;

public class ClientApplicationThread extends Thread {

    private final BufferedReader in;
    private final ClientApplication clientApplication;
    private String data;

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
            System.out.println("réponse du server " + resp);
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
        }else if(header.equals("DH")){
            System.out.println("data DH reçu " + dataContent);
            clientApplication.setClientKey(new BigInteger(dataContent[1]),new BigInteger(dataContent[2]),new BigInteger(dataContent[3]),dataContent[4]);
        }else if(header.equals("DH2")){
            System.out.println("DH2 received");
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
        else{
            this.data = dataContent[1];
            clientApplication.update(this.data);
        }
    }
}

