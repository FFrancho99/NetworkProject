import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
    private Socket socket;
    private ArrayList<ServerThread> threadArrayList;

    public ServerThread(Socket socket, ArrayList<ServerThread> threadArrayList){
        this.socket = socket;
        this.threadArrayList = threadArrayList;


    }
}
