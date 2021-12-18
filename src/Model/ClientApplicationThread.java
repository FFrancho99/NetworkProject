package Model;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientApplicationThread extends Thread {

    private final BufferedReader in;
    private final ClientApplication clientApplication;

    public ClientApplicationThread(BufferedReader in, ClientApplication clientApplication){
        this.in = in;
        this.clientApplication = clientApplication;
    }
    @Override
    public void run() {
        System.out.println("in run");
        listenToServer(in);
    }
    private synchronized void listenToServer(BufferedReader in) {
        String resp;//reads the server response
        try {
            resp = in.readLine();
            clientApplication.setData(resp);
            System.out.println("dataset");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}