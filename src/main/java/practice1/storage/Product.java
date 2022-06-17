package practice1.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

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

    public boolean isProduct(String name) {
        return Objects.equals(this.name, name);
    }
}

