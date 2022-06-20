package homework.network.interfaces_impl;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import homework.network.interfaces.Generator;
import homework.network.interfaces.Receiver;
import homework.utils.Commands;

import java.nio.ByteBuffer;
import java.util.Random;

public class GeneratorImpl implements Generator {
    private final Random random = new Random(System.currentTimeMillis());
    private final Receiver receiver;
    private final Package template = new Package((byte) 0, 10, 0, new Message(0, 0, new byte[0]));
    private static final String[][] productTemplates = new String[][]{
            new String[]{"Apple", "Orange", "Banana", "Peach"},
            new String[]{"Cabbage", "Cucumber", "Tomato", "Garlic"},
            new String[]{"Wheat", "Rice", "Buckwheat", "Oatmeal"}
    };
    private static final String[] groupTemplates = new String[]{
            "Fruits", "Vegetables", "Grains"
    };

    private String getProduct(int groupId) {
        return productTemplates[groupId][random.nextInt(productTemplates[groupId].length)];
    }

    private String getProduct() {
        return getProduct(getGroupId());
    }

    private String getGroup() {
        return groupTemplates[getGroupId()];
    }

    private int getGroupId() {
        return random.nextInt(groupTemplates.length);
    }

    public GeneratorImpl(Receiver receiver) {
        this.receiver = receiver;
    }

    private int getAmount() {
        return random.nextInt(10);
    }

    private int getMessageType() {
        return random.nextInt(Commands.COMMAND_SIZE);
    }

    private byte[] createMessageBody(int type) {
        byte[] ret = new byte[0];
        ByteBuffer buffer;
        switch (type) {
            case Commands.PRODUCT_GET:
            case Commands.PRODUCT_ADD_NAME:
                ret = getProduct().getBytes();
                break;
            case Commands.PRODUCT_INCREASE:
            case Commands.PRODUCT_DECREASE:
            case Commands.PRODUCT_SET_PRICE:
                buffer = ByteBuffer.wrap(getProduct().getBytes());
                buffer.putInt(getAmount());
                ret = buffer.array();
                break;
            case Commands.GROUP_ADD:
                ret = getGroup().getBytes();
                break;
            case Commands.GROUP_ADD_PRODUCT_NAME:
                int groupId = getGroupId();
                buffer = ByteBuffer.wrap(groupTemplates[groupId].getBytes());
                buffer.putChar((char) 0);
                buffer.put(getProduct(groupId).getBytes());
                ret = buffer.array();
                break;
        }
        return ret;
    }

    private Message createMessage() {
        int type = getMessageType();
        return new Message(type, template.getMessage().getUserId(), createMessageBody(type));
    }

    @Override
    public void generateMessage() {
        Message message = createMessage();
        Package p = new Package(template.getBSrc(), template.getPacketId(), message.getMessageBody().length + 8, message);
        receiver.receivePackage(new PackageEncoder(p).getBytes());
    }

}
