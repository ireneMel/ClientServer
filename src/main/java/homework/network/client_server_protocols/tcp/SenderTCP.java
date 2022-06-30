package homework.network.client_server_protocols.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SenderTCP {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public Future<?> sendPackage(byte[] packet, DataOutputStream stream) {
        return executorService.submit(()->{
            try {
                stream.writeInt(packet.length);
                stream.write(packet);
                stream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendServerEnabled(int expectedLength, DataOutputStream stream){
        executorService.submit(()->{
            try {
                stream.writeInt(expectedLength);
                stream.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

