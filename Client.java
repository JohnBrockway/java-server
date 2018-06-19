import java.net.*;
import java.io.*;
import java.nio.charset.*;

public class Client {

    public static void main(String args[]) {
        String serverAddress = args[0];
        int nPort = Integer.parseInt(args[1]);
        int reqCode = Integer.parseInt(args[2]);
        String msg = args[3];

        try {
            // Negotiation stage
            Socket negotiationSocket = new Socket(serverAddress, nPort);
 
            // Send request code
            PrintWriter negotiationOut = new PrintWriter(negotiationSocket.getOutputStream(), true);
            negotiationOut.println(reqCode);

            // Receive port for negotiation, or null if the server rejected due to incorrect request code
            BufferedReader negotiationInput = new BufferedReader(new InputStreamReader(negotiationSocket.getInputStream()));
            String rPortString = negotiationInput.readLine();
            
            negotiationOut.close();
            negotiationInput.close();
            negotiationSocket.close();

            if (rPortString != null) {
                int rPort = Integer.parseInt(rPortString);

                // Transport stage
                DatagramSocket transportSocket = new DatagramSocket();
                InetAddress serverInetAddress = InetAddress.getByName(serverAddress);

                // Encode and send message
                byte [] msgAsBytes = msg.getBytes(Charset.forName("UTF-8"));
                DatagramPacket msgPacket = new DatagramPacket(msgAsBytes, msgAsBytes.length, serverInetAddress, rPort);
                transportSocket.send(msgPacket);

                // Receive server response
                byte[] returnMsgAsBytes = new byte[1024];
                DatagramPacket returnMsgPacket = new DatagramPacket(returnMsgAsBytes, returnMsgAsBytes.length);
                transportSocket.receive(returnMsgPacket);

                returnMsgAsBytes = trimByteArray(returnMsgPacket.getData(), returnMsgPacket.getLength());
                String returnMsg = new String(returnMsgAsBytes, Charset.forName("UTF-8"));
                System.out.println(returnMsg);

                transportSocket.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] trimByteArray(byte[] input, int length) {
        byte[] output = new byte[length];

        for (int i = 0 ; i < length ; i++) {
            output[i] = input[i];
        }

        return output;
    }
}