package homework.network.client_server_protocols.tcp;

import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import homework.homework1.packet.PackageEncoder;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

public class StoreClientTCP {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }
    public void sendMessage(Package packet) throws IOException {
        //encode here and send
        byte[] coded = new PackageEncoder(packet).getBytes();
        out.writeInt(coded.length); //int
        out.flush();
        while (!checkServerAvailable(coded.length));
        future = null;
        out.write(coded);
        out.flush();
    }

    Future<Boolean> future = null;
    private boolean checkServerAvailable(int checkSize){

        try {
            if (future == null)
                future = service.submit(() -> {
                    int responseSize = in.readInt();
                    return responseSize == checkSize;
                });
            return future.get(1, TimeUnit.SECONDS);
        }
        catch (TimeoutException e){
            return false;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private byte[] buffer = new byte[256];

    public String readMessage() throws Exception {
        int size = in.readInt();
        size = in.read(buffer,0,size);
        ByteBuffer wrap = ByteBuffer.allocate(size);
        wrap.put(buffer, 0, size);
        Package packet = new PackageDecoder(wrap.array()).getPackage();
        return new String(packet.getMessage().getMessageBody());
    }

    public void stopConnection() {
        try {
            out.writeInt(-1);
            out.flush();
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}