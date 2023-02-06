import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a help class for accomplishing some auxiliary functions.
 * 
 * @author 200010781
 *
 */
public class Help {

    /**
     * A variable to set the path of log file.
     */
    private static String filePath = "../log.txt";
    /**
     * A BufferedWriter to write data in log file.
     */
    private static BufferedWriter bw;

    /**
     * A method to check the validity of input.
     * 
     * @param args User input array.
     * @return If input is valid, returns true. Otherwise, returns false.
     */
    public static Boolean checkValid(String[] args) {

        // check if the length is 2
        // check if args[0] exists
        // check if args[1] is a valid number(not characters and a positive integer)
        if (args.length == 2 && isExists(args[0]) && isPosInt(args[1])) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method to check if root directory exists or not.
     * 
     * @param url Root directory path.
     * @return Check whether the root directory exists or not. If it exists, returns
     *         true.Otherwise, returns false.
     */
    public static Boolean isExists(String url) {
        File file = new File(url);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method to check the validity of input port number.
     * 
     * @param str Port number from user input.
     * @return Check whether the input is a positive integer or not.If it's, returns
     *         true. Otherwise, returns false.
     */
    public static Boolean isPosInt(String str) {
        try {
            int i = Integer.parseInt(str);

            if (i > 0) {
                return true;
            } else {
                return false;
            }

        } catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * A method to create a log file.
     */
    public static void createLogFile() {
        try {
            bw = new BufferedWriter(new FileWriter(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * A method to get the current time of a request.
     * 
     * @return The time string in a specific format.
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String dateStringParse = sdf.format(date);
        return dateStringParse;
    }

    /**
     * A method to update request in log file.
     * 
     * @param client The client which sends request.
     * @param time   The time when sending a request from a client.
     * @param type   The request type.
     * @param code   The response code from server.
     * @param length The content length of response body.
     * @throws InterruptedException throw a exception when there's an error.
     */
    public static void updateLogFile(InetAddress client, String time, String type, String code, String length)
            throws InterruptedException {
        try {

            bw.write("Request From: " + client + "\r\n");
            bw.write("Request Time: " + time + "\r\n");
            bw.write("Request Type: " + type + "\r\n");
            bw.write("Response Code: " + code + "\r\n");
            bw.write(length + "\r\n");
            bw.write("\r\n");

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to close the log file at the end.
     */
    public static void closeLogFile() {
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
