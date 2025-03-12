package app.core.server;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private volatile ServerSocket socket;
    private volatile int port;
    private volatile boolean isClosing, isRunning;
    private volatile LinkToPath links;
    private Thread thread;


    public Server(int port, LinkToPath links) {
        this.port = port;
        isClosing = false;
        isRunning = false;
        this.links = links;
    }

    public void start() {
        thread = new Thread(this, "Thread for server on port: " + port);
        thread.start();
    }
    public void kill() {
        System.out.println("Server: Server is closing...");
        try {
            socket.close();
        } catch (Exception e) {
            handleException(e);
        }
        isClosing = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isClosing = false;
        isRunning = true;
        try {
            socket = new ServerSocket(port);
            ClientHandler clientHandler = new ClientHandler();
            while (!isClosing) {
                System.out.println("Server: Waiting for connection on port " + port + ".");
                Socket clientSocket = socket.accept();
                System.out.println("Server: connecting to client " + clientSocket.getInetAddress().getHostAddress());
                clientHandler.handleClient(clientSocket, links);
            }
        } catch (Exception e) {
            if (!isClosing && !e.getMessage().equals("Socket closed")) {
                handleException(e);
            }
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    // Exception handling
    @SuppressWarnings("unused")
    private void handleException(Exception e) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("Server: ERROR - " + e.getMessage());
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(Exception e, String msg) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("Server: ERROR - " + msg);
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(String msg) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("Server: ERROR - " + msg);
        Exception e = new Exception(msg);
        throw new RuntimeException(e.getMessage(), e);
    }
}
