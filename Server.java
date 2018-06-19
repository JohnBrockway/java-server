import java.net.*;
import java.io.*;
import java.nio.charset.*;

public class Server {

    public static void main(String args[]) {
        int reqCode = Integer.parseInt(args[0]);

        try {
            ServerSocket srvr = new ServerSocket(0);
            System.out.println("SERVER_PORT=" + srvr.getLocalPort());

            while (true) {
                // Negotiation stage
                Socket negotiationSocket = srvr.accept();
                BufferedReader negotiationInput = new BufferedReader(new InputStreamReader(negotiationSocket.getInputStream()));
                int inputReqCode = Integer.parseInt(negotiationInput.readLine());

                // Close connection if given an incorrect request code
                if (inputReqCode != reqCode) {
                    negotiationInput.close();
                    negotiationSocket.close();
                    continue;
                }

                // Send transport port through negotiation channel
                DatagramSocket transportSocket = new DatagramSocket();
                PrintWriter negotiationOut = new PrintWriter(negotiationSocket.getOutputStream(), true);
                negotiationOut.println(transportSocket.getLocalPort());

                // Transport stage
                // Receive message
                byte[] msgAsBytes = new byte[1024];
                DatagramPacket msgPacket = new DatagramPacket(msgAsBytes, msgAsBytes.length);
                transportSocket.receive(msgPacket);

                msgAsBytes = trimByteArray(msgPacket.getData(), msgPacket.getLength());
                String msg = new String(msgAsBytes, Charset.forName("UTF-8"));
                
                // Send response message
                byte [] returnMsgAsBytes = reverseString(msg).getBytes(Charset.forName("UTF-8"));
                DatagramPacket returnMsgPacket = new DatagramPacket(returnMsgAsBytes, returnMsgAsBytes.length, msgPacket.getAddress(), msgPacket.getPort());
                transportSocket.send(returnMsgPacket);

                transportSocket.close();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static String reverseString(String input) {
        String output = "";

        for (int i = input.length() - 1 ; i >= 0 ; i--) {
            output += input.charAt(i);
        }

        return output;
    }

    public static byte[] trimByteArray(byte[] input, int length) {
        byte[] output = new byte[length];

        for (int i = 0 ; i < length ; i++) {
            output[i] = input[i];
        }

        return output;
    }
}