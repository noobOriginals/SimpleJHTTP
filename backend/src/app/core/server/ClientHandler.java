package app.core.server;

import java.net.Socket;

import app.core.util.File;
import app.core.util.ParallelExecution;

public class ClientHandler {

    public void handleClient(Socket clientSocket, LinkToPath links) {
        new ParallelExecution(() -> {
            System.out.println("ClientHandler: Handling client on separate thread");

            try {
                ClientInput in = new ClientInput(clientSocket);
                ClientOutput out = new ClientOutput(clientSocket, "My Server");

                System.out.println("ClientHandler: Sending info to client.");
                String fileExt = links.getFileExt(in.getLink());
                File fileIn = links.getSourceFile(in.getLink());
                String output;
                if (fileIn == null) {
                    output = null;
                } else {
                    output = fileIn.toString();
                }
                out.sendAnswer(in.getProtocolVersion(), fileExt, output);
            } finally {
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    System.out.println("ClientHandler: ERROR - " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return null;
        }).execute();
    }
}
