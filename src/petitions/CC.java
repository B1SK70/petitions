package petitions;

import java.io.IOException;
import java.net.Socket;

class CC implements Runnable {

    Petitions petition;
    Connection connection;
    int port;
    String connTo;
    
    public CC(Petitions petition, Connection connection, String connTo) {
        this.petition = petition;
        this.connection = connection;
        this.port = petition.getPort();
        this.connTo = connTo;

    }

    @Override
    public void run() {
        while (true) {

            try {
                if (!connTo.equals("N"))connection.setSocket(new Socket(connTo, port));
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
