
import java.io.*;
import java.net.*;
public class Sender_Side {
    // private static final String SERVER_ADDRESS = "192.168.106.112";
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 20380; 
    private static final int NUM_PACKETS = 50; 
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to server...");
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); 
            System.out.println("Connected to server.");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            for (int i = 0; i < NUM_PACKETS; i++) {
                String packet = generateRandomPacket();
                System.out.println("Packet Send: "+packet);
                out.println(packet);
            }
            System.out.println("Packets sent.");
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage()); 
        }
    }
    static int n=0;
    private static String generateRandomPacket() {
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            System.out.println(e);
        }
        return "Packet_" + String.valueOf(n++); 
    }
}