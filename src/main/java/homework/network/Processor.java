package homework.network;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.storage.Storage;
import homework.utils.Commands;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/*
Клас, що в багато потоків приймає
перетворене повідомлення та формує відповідь.
Поки достатньо відповіді Ок.
 */
public class Processor {
    private final ExecutorService executor;
    private final Storage storage = new Storage();

    public Processor(ExecutorService executor) {
        this.executor = executor;
    }

    public Future<Package> processMessage(Package packet) {
        Message message = packet.getMessage();
        byte[] messageBody = message.getMessageBody();
        return executor.submit(() -> {
            String reply = "Not Ok";
            switch (message.getCType()) {
                case (Commands.PRODUCT_GET):
                    int amount = storage.getProductAmount(new String(messageBody));
                    if (amount != -1) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_DECREASE):
                    Map.Entry<String, Integer> d = splitBytes(messageBody);
                    boolean res_decrease = storage.decreaseProductAmount(d.getKey(), d.getValue());
                    if (res_decrease) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_INCREASE):
                    Map.Entry<String, Integer> i = splitBytes(messageBody);
                    boolean res_increase = storage.increaseProductAmount(i.getKey(), i.getValue());
                    if (res_increase) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_SET_PRICE):
                    Map.Entry<String, Integer> p = splitBytes(messageBody);
                    boolean res_price = storage.setPrice(p.getKey(), p.getValue());
                    if (res_price) reply = "Ok";
                    else reply = "Not Ok";
                    break;
                case (Commands.PRODUCT_ADD_NAME):
                    storage.addProductName(new String(messageBody));
                    reply = "Ok";
                    break;
                case (Commands.GROUP_ADD):
                    storage.addGroup(new String(messageBody));
                    reply = "Ok";
                    break;
                case (Commands.GROUP_ADD_PRODUCT_NAME):
                    Map.Entry<String, String> ptg = splitBytesToStrings(messageBody);
                    storage.addProductToGroup(ptg.getKey(), ptg.getValue());
                    reply = "Ok";
                    break;
            }
            byte[] bodyBytes = reply.getBytes();
            Message mes = new Message(packet.getMessage().getCType(), packet.getMessage().getUserId(), bodyBytes);
            return new Package(packet.getBSrc(),
                    packet.getPacketId() + 1, bodyBytes.length, mes);
        });
    }

    private Map.Entry<String, Integer> splitBytes(byte[] body) {
        return Map.entry(
                new String(body, 0, body.length - 4),
                ByteBuffer.wrap(body, body.length - 4, 4).getInt()
        );
    }

    private Map.Entry<String, String> splitBytesToStrings(byte[] body) {
        int splitIndex = 0;
        while (body[splitIndex++] != 0) ;
        return Map.entry(
                new String(body, 0, splitIndex - 1),
                new String(body, splitIndex + 1, body.length - splitIndex - 1)
        );
    }
}
