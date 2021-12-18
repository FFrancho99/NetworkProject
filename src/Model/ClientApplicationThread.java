package Model;

import java.io.BufferedReader;
import java.io.IOException;

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
        System.out.println("in run");
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
    public void setData(String data){
        this.data = data;
        clientApplication.update(this.data);
    }
}

