package homework.network.interfaces_impl;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;
import homework.network.interfaces.Generator;
import homework.network.interfaces.Receiver;
import homework.utils.Commands;
import homework.utils.GeneratorUtils;
import homework.utils.GeneratorUtils.*;

import java.nio.ByteBuffer;
import java.util.Random;

import static homework.utils.GeneratorUtils.*;

public class GeneratorImpl implements Generator {
    protected final Random random = new Random(System.currentTimeMillis());
    protected final Receiver receiver;

    protected String getProduct(int groupId) {
        return productTemplates[groupId][random.nextInt(productTemplates[groupId].length)];
    }

    protected String getProduct() {
        return getProduct(getGroupId());
    }

    protected String getGroup() {
        return groupTemplates[getGroupId()];
    }

    protected int getGroupId() {
        return random.nextInt(groupTemplates.length);
    }

    public GeneratorImpl(Receiver receiver) {
        this.receiver = receiver;
    }

    protected int getAmount() {
        return random.nextInt(10);
    }

    protected int getMessageType() {
        return random.nextInt(Commands.COMMAND_SIZE);
    }

    protected byte[] createMessageBody(int type) {
        switch (type) {
            case Commands.PRODUCT_GET:
            case Commands.PRODUCT_ADD_NAME:
                return GeneratorUtils.createMessageBody(getProduct());
            case Commands.PRODUCT_INCREASE:
            case Commands.PRODUCT_DECREASE:
            case Commands.PRODUCT_SET_PRICE:
                return GeneratorUtils.createMessageBody(getProduct(), getAmount());
            case Commands.GROUP_ADD:
                return GeneratorUtils.createMessageBody(getGroup());
            case Commands.GROUP_ADD_PRODUCT_NAME:
                return GeneratorUtils.createMessageBody(getGroup(), getProduct());
        }
        return null;
    }

    protected Message createMessage() {
        int type = getMessageType();
        return new Message(type, template.getMessage().getUserId(), createMessageBody(type));
    }

    private long availablePacketId = 0;
    protected byte[] packMessage(Message message) {
        Package p = new Package(template.getBSrc(), availablePacketId++, message.getMessageBody().length + 8, message);
        return new PackageEncoder(p).getBytes();
    }

    @Override
    public void generateMessage() {
        receiver.receivePackage(packMessage(createMessage()));
    }

}
