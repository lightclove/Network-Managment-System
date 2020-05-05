package ceeport.tools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Pinger {

    public static void main(String[] args) throws IOException, InterruptedException {
        
        // Testing:
        //System.out.println(_ping("10.168.2.50"));
        System.out.println(_ping("10.168.2.112"));
        //System.out.println(ping("10.168.2.50")); //ok
        //System.out.println(ping("10.168.2.50")); //ok
        
        

    }
    // Use this method to ping hosts on Windows or other platforms:
    public static boolean _ping(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows ? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();
        int returnVal = proc.waitFor();
        return returnVal == 0;
    }

    /**
     * Connect using layer3
     *
     * @param hostAddress
     * @return delay if the specified host responded, -1 if failed
     */
    public static long ping(String hostAddress){
        InetAddress inetAddress = null;
        Date start, stop;

        try {
            inetAddress = InetAddress.getByName(hostAddress);
        } catch (UnknownHostException e) {
            System.out.println("Problem, unknown host:");
            return -1;
        }

        try {
            start = new Date();
            if (inetAddress.isReachable(5000)) {
                stop = new Date();
                return (stop.getTime() - start.getTime());
            }

        } catch (IOException e1) {
            System.out.println("Problem, a network error has occurred:");
            return -1;
        } catch (IllegalArgumentException e1) {
            System.out.println("Problem, timeout was invalid:");
            return -1;
        }

        return -1; // to indicate failure

    }

    /**
     * Connect using layer4 (sockets)
     *
     * @param
     * @return delay if the specified host responded, -1 if failed
     */
    public static long ping(String hostAddress, Integer port) {
        InetAddress inetAddress = null;
        InetSocketAddress socketAddress = null;
        SocketChannel sc = null;
        long timeToRespond = -1;
        Date start, stop;

        try {
            inetAddress = InetAddress.getByName(hostAddress);
        } catch (UnknownHostException e) {
            
            System.out.println("Problem, unknown host:");
            return -1;
        }

        try {
            socketAddress = new InetSocketAddress(inetAddress, port);
        } catch (IllegalArgumentException e) {
            System.out.println("Problem, port may be invalid:");
            return -1;
        }

        // Open the channel, set it to non-blocking, initiate connect
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(true);
            start = new Date();
            if (sc.connect(socketAddress)) {
                stop = new Date();
                timeToRespond = (stop.getTime() - start.getTime());
            }
        } catch (IOException e) {
            System.out.println("Problem, connection could not be made:");
            return -1;
        }

        try {
            sc.close();
        } catch (IOException e) {
        }

        return timeToRespond;
    }
    

}

