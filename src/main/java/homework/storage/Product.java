package homework.storage;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Product {
    private String productName;
    private String groupName;
    private int amount;
    private double price;

    public Product(String productName) {
        this.productName = productName;
    }
}

