package practice1;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageDecoder;
import homework.homework1.packet.PackageEncoder;
import homework.network.Decryptor;
import homework.network.Encryptor;
import homework.network.Processor;
import homework.network.interfaces.Generator;
import homework.network.interfaces.Receiver;
import homework.network.interfaces.Sender;
import homework.network.interfaces_impl.GeneratorImpl;
import homework.network.interfaces_impl.ReceiverImpl;
import homework.network.interfaces_impl.SenderImpl;
import homework.utils.Commands;
import homework.utils.GeneratorUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static homework.utils.GeneratorUtils.*;

public class GeneratorTest {
    ExecutorService service;
    Sender sender;
    Encryptor encryptor;
    Processor processor;
    Decryptor decryptor;
    Receiver receiver;
    Generator generator;

    public void setup() {
        service = Executors.newFixedThreadPool(20);
        encryptor = new Encryptor(service, sender);
        processor = new Processor(service, encryptor);
        decryptor = new Decryptor(service, processor);
        receiver = new ReceiverImpl(decryptor);
        generator = new GeneratorImpl(receiver);
    }

    @Test
    @SneakyThrows
    public void adding() {
        Map<Long, String> actualResponses = new HashMap<>();
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                actualResponses.put(decodePacket.getPacketId(), ans);
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Message message = template.getMessage();

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(0, message));

        Thread.sleep(10);

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
        receiver.receivePackage(GeneratorUtils.packMessage(1, message));

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(2, message));

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][1], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(3, message));

        Map<Long, String> expectedResponses = new HashMap<>();
        expectedResponses.put(0L, "Ok");
        expectedResponses.put(1L, "Ok");
        expectedResponses.put(2L, "Ok");
        expectedResponses.put(3L, "Not Ok");

        service.awaitTermination(1, TimeUnit.SECONDS);
        service.shutdown();

        Assertions.assertIterableEquals(expectedResponses.entrySet(), actualResponses.entrySet());
    }

    @Test
    @SneakyThrows
    public void increaseAndDecrease() {
        Map<Long, String> actualResponses = new HashMap<>();
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                actualResponses.put(decodePacket.getPacketId(), ans);
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Map<Long, String> expectedResponses = new HashMap<>();
        Message message = template.getMessage();

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(0, message));
        expectedResponses.put(0L, "Ok");

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(1, message));
        expectedResponses.put(1L, "Ok");

        Thread.sleep(10);

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
        receiver.receivePackage(GeneratorUtils.packMessage(2, message));
        expectedResponses.put(2L, "Ok");

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(3, message));
        expectedResponses.put(3L, "Ok");

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(4, message));
        expectedResponses.put(4L, "Not Ok");

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(5, message));
        expectedResponses.put(5L, "Ok");

        service.awaitTermination(200, TimeUnit.MILLISECONDS);
        service.shutdown();

        Assertions.assertIterableEquals(expectedResponses.entrySet(), actualResponses.entrySet());
    }

    @Test
    @SneakyThrows
    public void decreaseAndIncrease() {
        Map<Long, String> actualResponses = new HashMap<>();
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                actualResponses.put(decodePacket.getPacketId(), ans);
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Map<Long, String> expectedResponses = new HashMap<>();
        Message message = template.getMessage();

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(0, message));
        expectedResponses.put(0L, "Ok");

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(1, message));
        expectedResponses.put(1L, "Ok");

        Thread.sleep(10);

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(4, message));
        expectedResponses.put(4L, "Not Ok");

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(5, message));
        expectedResponses.put(5L, "Not Ok");

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
        receiver.receivePackage(GeneratorUtils.packMessage(2, message));
        expectedResponses.put(2L, "Ok");

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(3, message));
        expectedResponses.put(3L, "Ok");

        service.awaitTermination(200, TimeUnit.MILLISECONDS);
        service.shutdown();

        Assertions.assertIterableEquals(expectedResponses.entrySet(), actualResponses.entrySet());
    }

    @Test
    @SneakyThrows
    public void decreaseAndIncrease2() {
        Map<Long, String> actualResponses = new HashMap<>();
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                actualResponses.put(decodePacket.getPacketId(), ans);
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Map<Long, String> expectedResponses = new HashMap<>();
        Message message = template.getMessage();

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(0, message));
        expectedResponses.put(0L, "Ok");

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(1, message));
        expectedResponses.put(1L, "Ok");

        Thread.sleep(10);

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(3, message));
        expectedResponses.put(3L, "Ok");

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(4, message));
        expectedResponses.put(4L, "Not Ok");

        message = message.withCType(Commands.PRODUCT_DECREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[1][0], 20));
        receiver.receivePackage(GeneratorUtils.packMessage(5, message));
        expectedResponses.put(5L, "Ok");

        message = message.withCType(Commands.PRODUCT_INCREASE);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0], 10));
        receiver.receivePackage(GeneratorUtils.packMessage(2, message));
        expectedResponses.put(2L, "Ok");


        service.awaitTermination(200, TimeUnit.MILLISECONDS);
        service.shutdown();

        Assertions.assertIterableEquals(expectedResponses.entrySet(), actualResponses.entrySet());
    }

    @Test
    @SneakyThrows
    public void synchronizedDecreaseAndIncreaseForLoop() {
        Map<Long, String> actualResponses = new HashMap<>();
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                actualResponses.put(decodePacket.getPacketId(), ans);
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Map<Long, String> expectedResponses = new HashMap<>();
        Message message = template.getMessage();

        message = message.withCType(Commands.PRODUCT_ADD_NAME);
        message = message.withMessageBody(createMessageBody(productTemplates[0][0]));
        receiver.receivePackage(GeneratorUtils.packMessage(0, message));
        expectedResponses.put(0L, "Ok");

        Thread.sleep(10);
        for (int i = 1; i <= 20; ++i) {
            message = message.withCType(Commands.PRODUCT_INCREASE);
            message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
            receiver.receivePackage(GeneratorUtils.packMessage(i * 2L, message));
            expectedResponses.put(i * 2L, "Ok");

            Thread.sleep(10);
            message = message.withCType(Commands.PRODUCT_DECREASE);
            message = message.withMessageBody(createMessageBody(productTemplates[0][0], 20));
            receiver.receivePackage(GeneratorUtils.packMessage(i * 2L + 1, message));
            expectedResponses.put(i * 2L + 1, "Ok");
            Thread.sleep(10);
        }

        service.awaitTermination(1500, TimeUnit.MILLISECONDS);
        service.shutdown();

        Assertions.assertIterableEquals(expectedResponses.entrySet(), actualResponses.entrySet());
    }

    @Test
    @SneakyThrows
    public void decreaseAndIncreaseForLoop() {
        Map<Long, String> actualResponses = new HashMap<>();
        ExecutorService service = Executors.newFixedThreadPool(4);
        sender = packet -> {
            try {
                Package decodePacket = new PackageDecoder(packet).getPackage();
                Message message = decodePacket.getMessage();
                byte[] body = message.getMessageBody();
                String ans = new String(body);
                synchronized (actualResponses) {
                    actualResponses.put(decodePacket.getPacketId(), ans);
                }
            } catch (Exception e) {
                Assertions.fail("Exception!!!");
            }
        };
        setup();
        Map<Long, String> expectedResponses = new HashMap<>();
        final Message message = template.getMessage();

        for (int i = 0; i < 4; ++i) {
            int finalI = i;
            service.submit(() -> {
                receiver.receivePackage(GeneratorUtils.packMessage(finalI,
                        message.withCType(Commands.PRODUCT_ADD_NAME)
                        .withMessageBody(createMessageBody(productTemplates[0][finalI]))
                ));
                expectedResponses.put((long) finalI, "Ok");
            });
        }
        service.submit(() -> {
            receiver.receivePackage(GeneratorUtils.packMessage(5,
                    message.withCType(Commands.GROUP_ADD)
                            .withMessageBody(createMessageBody(groupTemplates[0]))
            ));
            expectedResponses.put(5L, "Ok");
        });

        Thread.sleep(50);
        for (int i = 1; i <= 25; ++i) {
            int finalI = i;
            service.submit(() -> {
                for (int j = 0; j < 50; ++j) {
                    int finalJ = j;
                    service.submit(() -> {
                        receiver.receivePackage(GeneratorUtils.packMessage(finalI * 10000L + finalJ,
                                message.withCType(Commands.PRODUCT_ADD_NAME)
                                        .withMessageBody(createMessageBody(productTemplates[finalI % 3][finalJ % 4]))
                        ));
                        expectedResponses.put(finalI * 10000L + finalJ, "Ok");
                    });
                    service.submit(() -> {
                        receiver.receivePackage(GeneratorUtils.packMessage(finalI * 1000000L + finalJ,
                                message.withCType(Commands.GROUP_ADD)
                                        .withMessageBody(createMessageBody(groupTemplates[finalJ % 3]))
                        ));
                        expectedResponses.put(finalI * 1000000L + finalJ, "Ok");
                    });
                    service.submit(() -> {
                        receiver.receivePackage(GeneratorUtils.packMessage(finalI * 100000000L + finalJ,
                                message.withCType(Commands.GROUP_ADD_PRODUCT_NAME)
                                        .withMessageBody(createMessageBody(groupTemplates[0],productTemplates[0][finalJ % 4]))
                        ));
                        expectedResponses.put(finalI * 100000000L + finalJ, "Ok");
                    });
                }
            });
            for (int j = 0; j < 4; ++j) {
                int finalJ = j;
                service.submit(() -> {
                    receiver.receivePackage(GeneratorUtils.packMessage(finalI * 12L + finalJ,
                            message.withCType(Commands.PRODUCT_INCREASE)
                            .withMessageBody(createMessageBody(productTemplates[0][finalJ], 20))
                    ));
                    expectedResponses.put(finalI * 12L + finalJ, "Ok");
                });
            }
            Thread.sleep(50);
            for (int j = 0; j < 4; ++j) {
                int finalJ = j;
                service.submit(() -> {
                    receiver.receivePackage(GeneratorUtils.packMessage(finalI * 12L + finalJ + 4,
                            message.withCType(Commands.PRODUCT_DECREASE)
                                    .withMessageBody(createMessageBody(productTemplates[0][finalJ], 30))
                    ));
                    expectedResponses.put(finalI * 12L + finalJ + 4, "Not Ok");
                });
            }
            Thread.sleep(50);
            for (int j = 0; j < 4; ++j) {
                int finalJ = j;
                service.submit(() -> {
                    receiver.receivePackage(GeneratorUtils.packMessage(finalI * 12L + finalJ + 8,
                            message.withCType(Commands.PRODUCT_DECREASE)
                            .withMessageBody(createMessageBody(productTemplates[0][finalJ], 20))
                    ));
                    expectedResponses.put(finalI * 12L + finalJ + 8, "Ok");
                });
            }
            Thread.sleep(50);
        }

        service.awaitTermination(4000, TimeUnit.MILLISECONDS);
        service.shutdown();

        expectedResponses.forEach((id, response) -> {
            Assertions.assertEquals(response, actualResponses.get(id), "Package id: " + id);
        });
    }
}
