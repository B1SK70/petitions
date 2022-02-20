package petitions;

public class HS {
    
    Connection conn;
    private String connectedIP;
    
    public HS(Connection conn) {
        this.conn = conn;
        connectedIP = conn.getConnectedTo();
        
        conn.resetConnection();
    } 

}
