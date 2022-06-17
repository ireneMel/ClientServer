package practice1.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Getter
@Setter
@AllArgsConstructor
public class Group {
    private String groupName;
    private String productType;
    private LinkedList<Product> products;

}
