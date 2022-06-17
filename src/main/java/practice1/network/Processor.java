package practice1.network;

import practice1.message.Message;
import practice1.storage.Storage;
import practice1.utils.Commands;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Клас, що в багато потоків приймає
перетворене повідомлення та формує відповідь.
Поки достатньо відповіді Ок.
 */
public class Processor {
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private Storage storage;

    //отримати команду
    //прочитати повідомлення - кількість продукту, тип продукту?
    //отримати дані зі складу
    //якщо можливо списати товар - списати (додати синхронайзд)
    public void processMessage(Message message) {
        executor.execute(() -> {
            switch (message.getCType()) {
                case (Commands.PRODUCT_GET):
                    int amount = storage.productAmount(Arrays.toString(message.getMessageBody()));
                    break;
                case (Commands.PRODUCT_DELETE):
//                    storage.deleteProduct()
                    break;
                case (Commands.PRODUCT_ADD):
                    break;
                case (Commands.PRODUCT_SET_PRICE):
                    break;
                case (Commands.GROUP_ADD):
                    break;
                case (Commands.GROUP_ADD_PRODUCT_NAME):
                    break;
            }

        });
    }

    /*
      cType: Commands.PRODUCT_GET, messageBody: productName
      cType: Commands.PRODUCT_DELETE, messageBody: product name (bit to divide?) amount
      cType: Commands.PRODUCT_ADD, messageBody: product name (bit to divide?) amount, price
      cType: Commands.PRODUCT_SET_PRICE, messageBody: product name (bit to divide?) new price
      cType: Commands.GROUP_ADD, messageBody: group fields
      cType: Commands.GROUP_ADD_PRODUCT_NAME, messageBody: group name (bit to divide?) product name
     */
}
