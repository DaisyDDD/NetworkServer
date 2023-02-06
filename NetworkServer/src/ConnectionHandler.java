import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * A class to handle various requests from clients.
 * 
 * @author 200010781
 *
 */
public class ConnectionHandler extends Thread {
    /**
     * A Socket object for server connection.
     */
    private Socket conn;
    /**
     * InputStream for getting data from client.
     */
    private InputStream is;
    /**
     * OutputStream for sending data back to the client.
     */
    private OutputStream os;
    /**
     * A string variable referring to root directory path.
     */
    private String url; // root directory
    /**
     * BufferedReader for reading data.
     */
    private BufferedReader br;
    /**
     * A File object referring to the file the client is requesting.
     */
    private File content; // response content
    /**
     * A string variable refers to client request type.
     */
    private String type; // request type
    /**
     * A string variable refers to the content size that the client is requesting.
     */
    private String contentLength; // response body length
    /**
     * A string variable to record response code.
     */
    private String responseCode; // response code
    /**
     * A string variable to record request time.
     */
    private String requestTime; // request time
    /**
     * A string variable to record protocol version.
     */
    private String protVersion; // request protocol Version

    /**
     * A constructor of ConnectionHandler.
     * 
     * @param conn Socket Connection.
     * @param url  Root directory path.
     */
    public ConnectionHandler(Socket conn, String url) {
        try {

            this.conn = conn;
            this.url = url;
            is = conn.getInputStream();
            os = conn.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));

        } catch (IOException e) {
            System.out.println("ConnectionHandler: " + e.getMessage());
        }
    }

    /**
     * A run method in threading to handle client request.
     */
    public void run() {
        System.out.println("new ConnectionHandler constructed .... ");

        try {
            handleRequest();

        } catch (Exception e) {
            System.out.println("ConnectionHandler.handleClientRequest: " + e.getMessage());
            cleanup(); // cleanup and exit
        }
    }

    /**
     * A method to handle various clients' request.
     * 
     * @throws Exception When there is IO exception or other exception.
     */
    public void handleRequest() throws Exception {
        while (true) {
            System.out.println("Get request...... ");
            String requestHeader = br.readLine(); // get data from client over socket

            if (requestHeader == null || requestHeader.equals("null") || requestHeader.equals("exits")) {
                throw new Exception(" Request is null ... OR ... Client exists");
            }
            this.requestTime = Help.getCurrentTime();

            int begin = requestHeader.indexOf("/");
            int end = requestHeader.indexOf("HTTP/") - 1;

            this.type = requestHeader.substring(0, begin - 1); // Request type:GET/HEAD
            this.content = new File(url + requestHeader.substring(begin, end)); // Request resource
            this.protVersion = requestHeader.substring(end + 1) + " "; // Request protocol version

            if (!content.exists()) {
                handleNotFoundRequest(); // Not Found File
            } else {

                InputStream f = new FileInputStream(content);
                this.contentLength = "Content-Length: " + String.valueOf(f.available()); // Content Length

                if (type.equals("GET")) {
                    handleGetRequest(); // Get Request
                } else if (type.equals("HEAD")) {
                    handleHeadRequest(); // Head Request
                } else {
                    handleNotimplementRequest(); // Request not implemented
                }
            }
            // Update log file
            Help.updateLogFile(conn.getInetAddress(), requestTime, type, responseCode, contentLength);

        }
    }

    /**
     * A method for handling HEAD request (write header).
     * 
     */
    public void handleHeadRequest() {
        // OK Response for Header
        this.responseCode = "200 OK";
        System.out.println("handleHeadRequest......");
        writeHeader();

    }

    /**
     * A method to handle GET request.
     */
    public void handleGetRequest() {

        handleHeadRequest(); // Write header
        writeContent(); // Write content body

    }

    /**
     * A method to response Not Found file and send back a not found page.
     */
    public void handleNotFoundRequest() {
        // Not Found File
        this.responseCode = "404 Not Found";

        try {
            // response a not found page(../Exception/NotFound.html)
            this.content = new File("../Exception/NotFound.html");
            InputStream f = new FileInputStream(content);
            this.contentLength = "Content-Length: " + String.valueOf(f.available()); // Content Length
            writeHeader();
            writeContent();

        } catch (IOException e) {
            System.out.println("ConnectionHandler.handleNotFoundRequest: " + e.getMessage());

        }
    }

    /**
     * A method to response Not Implement request and send back a not implement
     * page.
     */
    public void handleNotimplementRequest() {
        // request type not implemented
        this.responseCode = "501 Not Implemented";
        try {
            // response a not implemented page(../Exception/NotImplemented.html)
            this.content = new File("../Exception/NotImplemented.html");
            InputStream f = new FileInputStream(content);
            this.contentLength = "Content-Length: " + String.valueOf(f.available()); // Content Length
            writeHeader();
            writeContent();
        } catch (IOException e) {
            System.out.println("ConnectionHandler.handleNotimplementRequest: " + e.getMessage());
        }
    }

    /**
     * A method to send back header to client.
     */
    private void writeHeader() {

        try {
            os.write(protVersion.getBytes());
            os.write(responseCode.getBytes());
            os.write("\r\n".getBytes());
            os.write("Server: Simple Java Http Server\r\n".getBytes());
            os.write("Content-Type: text/html\r\n".getBytes());
            os.write(contentLength.getBytes());
            os.write("\r\n".getBytes());
            os.write("\r\n".getBytes());
        } catch (IOException e) {
            System.out.println("ConnectionHandler.writeHeader: " + e.getMessage());
        }
    }

    /**
     * A method to send back content body to client.
     */
    private void writeContent() {
        try {
            InputStream fin = new FileInputStream(content);
            int len = 0;
            byte[] buf = new byte[2];
            while ((len = fin.read(buf)) != -1) {
                os.write(buf, 0, len);
            }

            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println("ConnectionHandler.writeContent: " + e.getMessage());
        }
    }

    /**
     * A method to close connection and IO method and clean up.
     */
    private void cleanup() {
        System.out.println("Exiting ... ");
        try {
            br.close();
            is.close();
            os.close();
            conn.close();
        } catch (IOException e) {
            System.out.println("ConnectionHandler:cleanup " + e.getMessage());
        }
    }

}
