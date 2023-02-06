/**
 * A class for setting up main function.
 * 
 * @author 200010781
 *
 */
public class WebServerMain {

    /**
     * A main function for getting user input and start the server.
     * 
     * @param args User input arguments.args[0] refers to root directory and args[1]
     *             refers to the port number.
     */
    public static void main(String[] args) {

        Boolean isValid = Help.checkValid(args); // check whether input args is valid or not

        if (isValid) {
            String url = args[0];
            int port = Integer.valueOf(args[1]);

            new Server(url, port);

        } else {
            System.out.print("Usage: java WebServerMain <document_root> <port>");
        }
    }
}
