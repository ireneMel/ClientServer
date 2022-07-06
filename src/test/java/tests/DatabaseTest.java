package tests;


import homework.database.Criteria;
import homework.database.StorageDB;
import homework.storage.Product;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class DatabaseTest {
    private static StorageDB storageDB = new StorageDB();
    private List<Product> expected;
    String[] groupNames = new String[]{"Meat", "Vegetables", "Fruits", "Grains", "Berry"};

    Product[] allProducts = new Product[]{
            new Product("Watermelon", groupNames[4], 0, 0),
            new Product("Melon", groupNames[4], 0, 0),
            new Product("Beef", groupNames[0], 0, 0),
            new Product("Cucumber", groupNames[1], 0, 0),
            new Product("Apple", groupNames[2], 0, 0)
    };

    @BeforeEach
    public void init() {
        storageDB.initialization("OurStorage");

        expected = new LinkedList<>(List.of(allProducts));
        storageDB.createProduct("Watermelon", groupNames[4]);
        storageDB.createProduct("Melon", groupNames[4]);
        storageDB.createProduct("Beef", groupNames[0]);
        storageDB.createProduct("Cucumber", groupNames[1]);
        storageDB.createProduct("Apple", groupNames[2]);
    }

    @Test
    public void createProduct() {
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()));
    }

    @Test
    public void createAndUpdateProduct() {
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()));

        expected.get(0).setProductName("Lime");
        expected.get(1).setProductName("Lemon");
        expected.get(2).setProductName("Pork");

        //update name
        storageDB.updateProduct("Watermelon", "Lime");
        storageDB.updateProduct("Melon", "Lemon");
        storageDB.updateProduct("Beef", "Pork");
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()), "Wrong name");

        expected.get(3).setAmount(100);
        expected.get(4).setAmount(5);
        expected.get(2).setAmount(71);
        //update amount
        storageDB.updateProduct("Cucumber", 100);
        storageDB.updateProduct("Apple", 5);
        storageDB.updateProduct("Pork", 71);
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()), "Wrong amount");

        expected.get(3).setPrice(11.3);
        expected.get(4).setPrice(45.1);
        expected.get(2).setPrice(190.6);
        //update price
        storageDB.updateProduct("Cucumber", 11.3);
        storageDB.updateProduct("Apple", 45.1);
        storageDB.updateProduct("Pork", 190.6);
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()), "Wrong price");
    }

    @Test
    public void updateGroup() {
        storageDB.updateGroup(groupNames[4], "Vegan");
        expected.get(0).setGroupName("Vegan");
        expected.get(1).setGroupName("Vegan");
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()));
    }

    @SneakyThrows
    @AfterEach
    public void deleteFile() {
        storageDB.deleteAll();
        storageDB.closeConnection();
        expected.clear();
    }

}

