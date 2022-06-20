package homework.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
public class Group {
    private String groupName;
    private LinkedList<Product> products;

    public Group(String groupName) {
        this.groupName = groupName;
        products = new LinkedList<>();
    }
}
