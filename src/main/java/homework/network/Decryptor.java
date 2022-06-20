package homework.network;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків розбирає,
дешифрує та перетворює пакет в об`єкт типу Message.
Після чого передає повідомлення в Processor
 */
public class Decryptor {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final Processor processor = new Processor();

    public void decrypt(byte[] packet) {
        executor.execute(() -> {
            try {
                Package decodedPackage = new PackageDecoder(packet).getPackage();
                String reply = processor.processMessage(decodedPackage.getMessage());

                byte[] bodyBytes = reply.getBytes();
                Message mes = new Message(decodedPackage.getMessage().getCType(), decodedPackage.getMessage().getUserId(), bodyBytes);
                Package new_pack = new Package(decodedPackage.getBSrc(),
                        decodedPackage.getPacketId(), bodyBytes.length, mes);

                //build new message using reply as body
                //put this message to encoder
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
