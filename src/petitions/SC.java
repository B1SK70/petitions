package petitions;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SC implements Runnable {

    Petitions petition;
    Connection connection;
    ServerSocket ss;
    int port;

    public SC(Petitions petition, Connection connection) {
        this.petition = petition;
        this.connection = connection;
        this.port = petition.getPort();

    }

    @Override
    public void run() {
        while (true) {

            try {

                ss = new ServerSocket(port);
                connection.setSocket(ss.accept());
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
                
                connection.begginSaludation();
                break;

            } catch (IOException ex) {
                try {
                    Thread.sleep(300);
                    continue;

                } catch (InterruptedException e) {
                    System.out.println(e);
                }

            }

        }
    }
}
