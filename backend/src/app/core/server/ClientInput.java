package app.core.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class ClientInput {private BufferedReader in;
    private String input, method, link, protocolVersion;
    private HashMap<String, String> headers;

    public ClientInput(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            in = null;
            handleException(e, "No valid input provided.");
        }
        method = null;
        link = null;
        protocolVersion = null;
        headers = new HashMap<>();
        try {
            String line = in.readLine();
            input = "";
            while (line != null && !line.isEmpty()) {
                input += line + "\n";
                line = in.readLine();
            }
            in = null;
            parseInput();
        } catch (Exception e) {
            input = null;
            method = null;
            link = null;
            protocolVersion = null;
            headers = new HashMap<>();
            handleException(e, "Could not parse input.");
        }
    }

    public String getInput() {
        return input;
    }
    public String getMethod() {
        return method;
    }
    public String getLink() {
        return link;
    }
    public String getProtocolVersion() {
        return protocolVersion;
    }
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    private void parseInput() {
        int lineStartIdx, lineEndIdx;
        String line;
        lineStartIdx = 0;
        lineEndIdx = input.indexOf('\n', lineStartIdx);
        line = input.substring(lineStartIdx, lineEndIdx);
        parseMethodLine(line);
        lineStartIdx = lineEndIdx + 1;
        lineEndIdx = input.indexOf('\n', lineStartIdx);
        while (lineEndIdx < input.length() - 1) {
            line = input.substring(lineStartIdx, lineEndIdx);
            parseLine(line);
            lineStartIdx = lineEndIdx + 1;
            lineEndIdx = input.indexOf('\n', lineStartIdx);
        }
    }
    private void parseMethodLine(String line) {
        int start, pathStart, protocolStart;
        start = 0;
        pathStart = line.indexOf('/');
        protocolStart = line.indexOf(' ', pathStart) + 1;
        method = line.substring(start, pathStart - 1);
        link = line.substring(pathStart, protocolStart - 1);
        protocolVersion = line.substring(protocolStart);
    }
    private void parseLine(String line) {
        int start, headerEnd;
        start = 0;
        headerEnd = line.indexOf(':');
        String header = line.substring(start, headerEnd);
        String value = line.substring(headerEnd + 2);
        headers.put(header, value);
    }

    // Exception handling
    @SuppressWarnings("unused")
    private void handleException(Exception e) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("ClientInput: ERROR - " + e.getMessage());
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(Exception e, String msg) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("ClientInput: ERROR - " + msg);
        throw new RuntimeException(e.getMessage(), e);
    }
    @SuppressWarnings("unused")
    private void handleException(String msg) {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println("ClientInput: ERROR - " + msg);
        Exception e = new Exception(msg);
        throw new RuntimeException(e.getMessage(), e);
    }
}
