package homework.network.client_server_protocols.tcp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class StoreClientTCP {
    private Socket clientSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @SneakyThrows
    public void sendMessage(Package packet) {
        //encode here and send
        byte[] coded = new PackageEncoder(packet).getBytes();
        out.write(coded.length); //int
        out.write(coded);
        out.flush();
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}