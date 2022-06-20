package homework.network;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.storage.Storage;
import homework.utils.Commands;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків приймає
перетворене повідомлення та формує відповідь.
Поки достатньо відповіді Ок.
 */
public class Processor {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private final Storage storage = new Storage();
    private final Encryptor encryptor;

    public Processor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    //отримати команду
    //прочитати повідомлення - кількість продукту, тип продукту?
    //отримати дані зі складу
    //якщо можливо списати товар - списати (додати синхронайзд)
    //has to return bytes?
    public void processMessage(Package packet) {
        Message message = packet.getMessage();
        ByteBuffer messageBody = ByteBuffer.wrap(message.getMessageBody());
        executor.execute(() -> {
            String reply = "Not Ok";
            switch (message.getCType()) {
                case (Commands.PRODUCT_GET):
                    int amount = storage.getProductAmount(String.valueOf(messageBody));
                    if (amount != -1) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_DELETE):
                    boolean res_decrease = storage.decreaseProductAmount(String.valueOf(messageBody.getInt()), messageBody.getInt());
                    if (res_decrease) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_ADD):
                    storage.addProductName(String.valueOf(messageBody));
                    reply = "Ok";
                    break;
                case (Commands.PRODUCT_SET_PRICE):
                    boolean res_price = storage.setPrice(String.valueOf(messageBody.getInt()), messageBody.getInt());
                    if (res_price) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.GROUP_ADD):
                    storage.addGroup(String.valueOf(messageBody));
                    reply = "Ok";
                    break;
                case (Commands.GROUP_ADD_PRODUCT_NAME):
                    storage.addProductToGroup(String.valueOf(messageBody.getInt()), String.valueOf(messageBody.getInt()));
                    break;
            }
            byte[] bodyBytes = reply.getBytes();
            Message mes = new Message(packet.getMessage().getCType(), packet.getMessage().getUserId(), bodyBytes);
            Package new_pack = new Package(packet.getBSrc(),
                    packet.getPacketId(), bodyBytes.length, mes);
            encryptor.encrypt(new_pack);
        });
    }

    /*
      cType: Commands.PRODUCT_GET, messageBody: productName - - 8 bytes
      cType: Commands.PRODUCT_DELETE, messageBody: product name - 8 bytes, amount - 8 bytes
      cType: Commands.PRODUCT_ADD, messageBody: product name - 8 bytes
      cType: Commands.PRODUCT_SET_PRICE, messageBody: product name, new price - all 8 bytes
      cType: Commands.GROUP_ADD, messageBody: group name - - 8 bytes
      cType: Commands.GROUP_ADD_PRODUCT_NAME, messageBody: group name, product name - all - 8 bytes
     */
}
