
import java.io.*;
import java.net.*;
import java.util.*;

public class Server_Side {
    private static final int PORT = 20380;
    private static final double THRESHOLD = 2.5;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is listening on port " + PORT);

        // Map to count occurrences of data packets
        Map<String, Integer> packetCounts = new HashMap<>();

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket, packetCounts)).start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private Map<String, Integer> packetCounts;

        public ClientHandler(Socket socket, Map<String, Integer> packetCounts) {
            this.socket = socket;
            this.packetCounts = packetCounts;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String packet;
                while ((packet = in.readLine()) != null) {
                    packetCounts.put(packet, packetCounts.getOrDefault(packet, 0) + 1);
                    System.out.println("Packet received: "+packet);
                }

                // Compute entropy and check for DDoS attack
                double entropy = computeEntropy(packetCounts);
                // System.out.println("Packet : " + packetCounts);
                System.out.println("Current Entropy: " + entropy);

                if (entropy > THRESHOLD) {
                    System.out.println("Potential DDoS Attack Detected!");
                } else {
                    System.out.println("No DDoS Attack Detected.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private double computeEntropy(Map<String, Integer> counts) {
            double totalPackets = counts.values().stream().mapToInt(Integer::intValue).sum();
            double entropy = 0.0;

            for (int count : counts.values()) {
                double probability = (double) count / totalPackets;
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }

            return entropy;
        }
    }
}