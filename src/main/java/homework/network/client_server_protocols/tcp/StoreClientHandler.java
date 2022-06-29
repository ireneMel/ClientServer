package homework.network.client_server_protocols.tcp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import homework.network.Processor;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class StoreClientHandler {
    private Socket clientSocket;
    private PrintWriter out;
    private DataInputStream in;

    public StoreClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @SneakyThrows
    public void start() {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new DataInputStream(clientSocket.getInputStream());
        int length = in.read();
        byte[] codedPacket = new byte[length];
        in.read(codedPacket);

        Package decoded = new PackageDecoder(codedPacket).getPackage();
//        String reply = new Processor().processMessage(decoded.getMessage());
//
//        if (reply.equals("Ok") || reply.equals("Not Ok"))
//            out.println(reply);
//        else
//            out.println("something went wrong"); //must not reach here

        stop();
    }

    @SneakyThrows
    public void stop() {
        in.close();
        out.close();
        clientSocket.close();
    }
}