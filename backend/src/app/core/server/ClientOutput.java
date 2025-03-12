package app.core.server;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientOutput {
    private BufferedWriter out;
    private String serverName;

    public ClientOutput(Socket socket, String serverName) {
        this.serverName = serverName;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e) {
            out = null;
            handleException(e, "No valid output provided.");
        }
    }

    // TODO: Implement error support
    public void sendAnswer(String protoclVersion, String fileExt, String output) {
        if (fileExt == null) {
            fileExt = "";
        }
        if (output == null) {
            output = "";
        }
        writeln(protoclVersion + " 200 OK");
        writeln("Date: " + LocalDateTime.now());
        writeln("Server: " + serverName);
        writeln("Content-Type: " + getContentType(fileExt));
        writeln("Content-Length: " + output.length());
        writeln();
        writeln(output);
        flush();
    }

    public void writeln() {
        try {
            out.write("\r\n");
        } catch (Exception e) {
            handleException(e, "Failed to write info to output stream.");
        }
    }
    public void writeln(String str) {
        try {
            out.write(str + "\r\n");
        } catch (Exception e) {
            handleException(e, "Failed to write info to output stream.");
        }
    }
    public void write(String str) {
        try {
            out.write(str);
        } catch (Exception e) {
            handleException(e, "Failed to write info to output stream.");
        }
    }
    public void flush() {
        try {
            out.flush();
        } catch (Exception e) {
            handleException(e, "Failed to write info to output stream.");
        }
    }

    private String getContentType(String fileExt) {
        switch (fileExt) {
            case ".html":
                return "text/html";
            case ".css":
                return "text/css";

            default:
                System.out.println("ClientOutput: No file extension found. Sending HTML.");
                return "text/html";
        }
    }

    // Exception handling
    @SuppressWarnings("unused")
    private void handleException(Exception e) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("ClientOutput: ERROR - " + e.getMessage());
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(Exception e, String msg) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("ClientOutput: ERROR - " + msg);
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(String msg) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println("ClientOutput: ERROR - " + msg);
        Exception e = new Exception(msg);
        throw new RuntimeException(e.getMessage(), e);
    }
}
