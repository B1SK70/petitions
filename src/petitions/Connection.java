package petitions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection implements Runnable {

    private Socket s = null;
    private Petitions parent;
    private boolean connOk = false;

    PrintWriter pr;
    BufferedReader reader;

    SC sc;
    CC cc;
    
    Thread scThread;
    Thread ccThread;

    private String connectedIP = null;
    private String connectedName = null;

    public Connection(Petitions parent, String connTo) throws IOException {
        this.parent = parent;
        sc = new SC(parent, this);
        cc = new CC(parent, this, connTo);

        scThread = new Thread(sc);
        scThread.start();
        
        ccThread = new Thread(cc);
        ccThread.start();
    }

    public boolean connected() {
        return connOk;
    }

    public synchronized void setSocket(Socket s) throws IOException {
        if (this.s == null) {
            this.s = s;
            System.out.println("socket setted");
            connOk = true;

            pr = new PrintWriter(s.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }

        }
    }

    public void begginSaludation() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        sendRequest("IP");
    }

    private synchronized void processReceived(String received) {

        Boolean isRequest = true;
        String headder = "";
        String body = "";

        //€ -> REQUEST
        //& -> RESPONSE
        if (received.contains("€")) {
            headder = received.split("€")[0];

        } else {
            isRequest = false;
            String[] responseParts = received.split("&");
            headder = responseParts[0];
            body = responseParts[1];

        }

        if (isRequest) {
            String responseValue;
            if (headder.equals("IP")) {
                responseValue = parent.getIp();
                sendResponse(headder, responseValue);

            } else if (headder.equals("NAME")) {
                responseValue = parent.whoIAm();
                sendResponse(headder, responseValue);

            } else if (headder.equals("OK") && (connectedName == null && connectedIP == null)) {
                sendRequest("IP");

            } else {
                sendResponse(headder, headder);
                System.out.println("All ok! All ok! All ok! All ok!");
                displayConnectionData();
            }

        } else {

            if (headder.equals("IP")) {
                connectedIP = body;
                sendRequest("NAME");

            } else if (headder.equals("NAME")) {
                connectedName = body;
                sendRequest("OK");

            } else if (headder.equals("OK")) {
                System.out.println("All ok! All ok! All ok! All ok!");
                displayConnectionData();

            } else {
                doSomethingWithData(received.split("DATA$")[1]);
            }
        }

    }

    private synchronized void sendRequest(String requestName) {
        String petition = requestName + "€";

        System.out.println("Request sent -> " + petition);

        pr.println(petition);
        pr.flush();
    }

    private synchronized void sendResponse(String requestName, String responseValue) {
        String petition = requestName + "&" + responseValue;

        System.out.println("Response sent -> " + petition);

        pr.println(petition);
        pr.flush();
    }

    public String getConnectedTo() {
        return connectedIP;
    }

    private void doSomethingWithData(String string) {
        System.out.println("Saludation finished");
    }

    private void displayConnectionData() {
        System.out.println(
                "----------\n"
                + parent.whoIAm() + ": I'm connected to " + connectedName + " with IP " + connectedIP
                + "\n----------"
        );
    }

    public void resetConnection() {
        connOk = false;
        s = null;
        
        String newConTo = (connectedIP == null)? "N" : connectedIP ;
        
        System.out.println("new conn to " + newConTo);
        
        sc = new SC(parent, this);
        cc = new CC(parent, this, newConTo);

        scThread = new Thread(sc);
        scThread.start();
        
        ccThread = new Thread(cc);
        ccThread.start();
        
        connectedIP = null;
        connectedName = null;
        
    }

    @Override
    public void run() {
        while (true) {
            if (!connOk) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            } else {

                String received;
                try {
                    if ((received = reader.readLine()) != null) {
                        System.out.println("Received <- " + received);
                        processReceived(received);
                    }
                } catch (Exception e) {
                    
                    System.out.println(e + "Reseting connection");
                    new HS(this);
                }

            }
        }
    }

}
