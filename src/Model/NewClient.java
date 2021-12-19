
package Model;

import java.io.IOException;
import java.net.InetAddress;

public class NewClient {

    public static void main(String[] args) throws IOException {
        Integer serverPortNumber = 666;
        if(args.length==0){ // no args has been given
            String serverAddress = "localhost";
            ClientApplication clientApplication = new ClientApplication(serverAddress, serverPortNumber);
        }
        else{
            InetAddress serverAddress = InetAddress.getByName(args[0]);
            ClientApplication clientApplication = new ClientApplication(serverAddress, serverPortNumber);
        }

    }
}

