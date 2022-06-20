package homework.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {
    private String name;
    private int amount;
    private double price;

    public Product(String name) {
        this.name = name;
    }
}

