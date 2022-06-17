package practice1.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Product {
    private String name;
    private int amount;
    private double price;

    public boolean isProduct(String name) {
        return Objects.equals(this.name, name);
    }
}

