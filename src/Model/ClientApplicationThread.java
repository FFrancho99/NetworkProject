package Model;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientApplicationThread extends Thread {

    private final BufferedReader in;

    public  ClientApplicationThread(BufferedReader in){
        this.in = in;

    }
    @Override
    public void run() {
        System.out.println("in run");
        listenToServer(in);

    }
    private void listenToServer(BufferedReader in) {
        String resp = null;//reads the server response
        try {
            resp = in.readLine();
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

