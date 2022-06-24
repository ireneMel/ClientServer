package homework.utils;

import homework.homework1.message.Message;
import homework.homework1.packet.Package;
import homework.homework1.packet.PackageEncoder;

import java.nio.ByteBuffer;

public class GeneratorUtils {
    public static final Package template = new Package((byte) 0, 10, 0, new Message(0, 0, new byte[0]));
    public static final String[][] productTemplates = new String[][]{
            new String[]{"Apple", "Orange", "Banana", "Peach"},
            new String[]{"Cabbage", "Cucumber", "Tomato", "Garlic"},
            new String[]{"Wheat", "Rice", "Buckwheat", "Oatmeal"}
    };
    public static final String[] groupTemplates = new String[]{
            "Fruits", "Vegetables", "Grains"
    };

    public static byte[] createMessageBody(String name){
        return name.getBytes();
    }

    public static byte[] createMessageBody(String name, int amount){
        ByteBuffer buffer = ByteBuffer.allocate(name.getBytes().length + 4);
        buffer.put(name.getBytes());
        buffer.putInt(amount);
        return buffer.array();
    }

    public static byte[] createMessageBody(String group, String product){
        ByteBuffer buffer = ByteBuffer.allocate(group.getBytes().length + product.getBytes().length + 1);
        buffer.put(group.getBytes());
        buffer.put((byte) 0);
        buffer.put(product.getBytes());
        return buffer.array();
    }

    public static byte[] packMessage(long id, Message message) {
        Package p = new Package(template.getBSrc(), id, message.getMessageBody().length + 8, message);
        return new PackageEncoder(p).getBytes();
    }

    /**
     * does not encode message. just creates it
     * @param id
     * @param message
     * @return
     */
    public static Package packMessageWithoutEncoding(long id, Message message) {
        return new Package(template.getBSrc(), id, message.getMessageBody().length + 8, message);
    }
}
