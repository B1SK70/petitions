package petitions;

import java.io.IOException;

public class MyTask {

    public static void main(String[] args) throws IOException {

        int port = 10001;

        Petitions p1 = new Petitions("Arnau", port, "localhost");
          new Thread(p1).start();
        
        Petitions p2 = new Petitions("Jandro", port);
        new Thread(p2).start();
        
        
        //Petitions p2 = new Petitions("Jandro", port, addressBook);
        //new Thread(p2).start();

    }

}
