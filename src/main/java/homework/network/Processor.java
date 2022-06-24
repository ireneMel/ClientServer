package homework.network;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.storage.Storage;
import homework.utils.Commands;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/*
Клас, що в багато потоків приймає
перетворене повідомлення та формує відповідь.
Поки достатньо відповіді Ок.
 */
public class Processor {
    private ExecutorService executor;
    private final Storage storage = new Storage();
    private Encryptor encryptor;

    public Processor(ExecutorService executor, Encryptor encryptor) {
        this.encryptor = encryptor;
        this.executor = executor;
    }

    public Processor() {

    }

    public String processMessage(Message message) {
        byte[] messageBody = message.getMessageBody();
        switch (message.getCType()) {
            case (Commands.PRODUCT_GET):
                int amount = storage.getProductAmount(new String(messageBody));
                if (amount != -1) return "Ok";
                return "Not Ok";
            case (Commands.PRODUCT_DECREASE):
                Map.Entry<String, Integer> d = splitBytes(messageBody);
                boolean res_decrease = storage.decreaseProductAmount(d.getKey(), d.getValue());
                if (res_decrease) return "Ok";
                else return "Not Ok";
            case (Commands.PRODUCT_INCREASE):
                Map.Entry<String, Integer> i = splitBytes(messageBody);
                boolean res_increase = storage.increaseProductAmount(i.getKey(), i.getValue());
                if (res_increase) return "Ok";
                else return "Not Ok";
            case (Commands.PRODUCT_SET_PRICE):
                Map.Entry<String, Integer> p = splitBytes(messageBody);
                boolean res_price = storage.setPrice(p.getKey(), p.getValue());
                if (res_price) return "Ok";
                else return "Not Ok";
            case (Commands.PRODUCT_ADD_NAME):
                storage.addProductName(new String(messageBody));
                return "Ok";
            case (Commands.GROUP_ADD):
                storage.addGroup(new String(messageBody));
                return "Ok";
            case (Commands.GROUP_ADD_PRODUCT_NAME):
                Map.Entry<String, String> ptg = splitBytesToStrings(messageBody);
                storage.addProductToGroup(ptg.getKey(), ptg.getValue());
                return "Ok";
        }
        return "Not Ok";
    }

    public void processMessage(Package packet) {
        Message message = packet.getMessage();
        byte[] messageBody = message.getMessageBody();
        executor.execute(() -> {
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
            Package new_pack = new Package(packet.getBSrc(),
                    packet.getPacketId(), bodyBytes.length, mes);
            encryptor.encrypt(new_pack);
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
