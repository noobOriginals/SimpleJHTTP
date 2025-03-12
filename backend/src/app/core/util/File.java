package app.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class File {
    public static final int READ = 0x0;
    public static final int WRITE = 0x1;
    public static final int APPEND = 0x2;

    private BufferedReader in;
    private BufferedWriter out;
    private String path;
    private int operation;
    private boolean canRead;

    public File(String path, int operation) {
        this.path = path;
        this.operation = operation;
        switch (operation) {
            case READ:
                canRead = true;
                initReader();
                break;
            case WRITE:
                canRead = false;
                initWriter(false);
                break;
            case APPEND:
                canRead = false;
                initWriter(true);
                break;

            default:
                handleException("The provided operation is invalid or unknown.");
        }
    }

    public String readln() {
        if (!canRead) {
            handleException("This file is not readable.");
            return null;
        }
        try {
            return in.readLine();
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }
    public char read() {
        if (!canRead) {
            handleException("This file is not readable.");
            return 0;
        }
        try {
            return (char) in.read();
        } catch (Exception e) {
            handleException(e);
            return 0;
        }
    }
    public void writeln(String str) {
        if (canRead) {
            handleException("Cannot write to this file.");
        }
        try {
            out.write(str + "\n");
        } catch (Exception e) {
            handleException(e);
        }
    }
    public void write(String str) {
        if (canRead) {
            handleException("Cannot write to this file.");
        }
        try {
            out.write(str);
        } catch (Exception e) {
            handleException(e);
        }
    }
    public void flush() {
        if (!canRead) {
            try {
                out.flush();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    @Override
    public String toString() {
        if (!canRead) {
            handleException("This file is not readable.");
            return null;
        }
        try {
            BufferedReader read = new BufferedReader(new FileReader(path));
            String line = in.readLine(), file = "";
            while (line != null) {
                file += line + "\n";
                line = in.readLine();
            }
            read.close();
            return file;
        } catch (Exception e) {
            handleException(e);
            return null;
        }
    }

    public void close() {
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilepath() {
        return path;
    }
    public int getOperation() {
        return operation;
    }

    private void initReader() {
        try {
            in = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            in = null;
            handleException(e);
        }
    }
    private void initWriter(boolean append) {
        try {
            out = new BufferedWriter(new FileWriter(path, append));
        } catch (Exception e) {
            out = null;
            handleException(e);
        }
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
        if (out != null) {
            try {
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("File: ERROR - " + e.getMessage());
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
        if (out != null) {
            try {
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.err.println("File: ERROR - " + msg);
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
        if (out != null) {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.err.println("File: ERROR - " + msg);
        Exception e = new Exception(msg);
        throw new RuntimeException(e.getMessage(), e);
    }
}
