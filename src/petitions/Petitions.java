package petitions;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class Petitions implements Runnable {

    Scanner scanner = new Scanner(System.in);

    String username;
    int port;

    ArrayList<Connection> openConnections;

    public Petitions(String username, int port, String connTo) throws IOException {
        this.username = username;
        this.port = port;
        openConnections = new ArrayList<Connection>();
        Connection conn = new Connection(this, connTo);
        openConnections.add(conn);
        new Thread(conn).start();
    }

    private void connsStatus() {
        for (Connection openConnection : openConnections) {
            System.out.println(whoIAm() + ": " + openConnection.connected());
        }
    }

    public int getPort() {
        return port;
    }

    public String whoIAm() {
        return username;
    }

    public String getIp() {
        String ip = "NA";

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {

                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.getHostAddress().contains("172.16.")) {
                        ip = i.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println(ex);
        }

        return ip;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
            //connsStatus();

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }

    }

}
