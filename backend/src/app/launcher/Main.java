package app.launcher;

import java.util.Scanner;

import app.core.server.LinkToPath;
import app.core.server.Server;

public class Main {
    public static void main(String[] args) {
        LinkToPath links = new LinkToPath("../frontend", "../frontend/links.txt");
        Server server = new Server(8080, links);
        server.start();
        Scanner in = new Scanner(System.in);
        while (!in.nextLine().equals("q")) {}
        in.close();
        server.kill();
    }
}
