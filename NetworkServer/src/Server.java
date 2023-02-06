import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server class for building up a server.
 * 
 * @author 200010781
 *
 */
public class Server {

    /**
     * A ServerSocket object for server socket in connection.
     */
    private ServerSocket ss;
    /**
     * A string variable referring to root directory path.
     */
    private String url;

    /**
     * A constructor for creating a server.
     * 
     * @param url  The root directory path.
     * @param port The port number in socket connection.
     */
    public Server(String url, int port) {

        try {
            this.url = url;
            ss = new ServerSocket(port);
            System.out.println("Server started ...");

            Help.createLogFile(); // create and clean log file

            while (true) {

                Socket conn = ss.accept(); // build connection
                System.out.println("Server got new connection request from " + conn.getInetAddress());

                ConnectionHandler handler = new ConnectionHandler(conn, url);
                handler.start(); // start handling the client request

            }
        } catch (IOException ioe) {
            Help.closeLogFile(); // close log file
            System.out.println("Something went wrong: " + ioe.getMessage());
        }
    }
}
