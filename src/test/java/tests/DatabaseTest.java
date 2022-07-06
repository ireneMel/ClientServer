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

    @Test
    public void deleteProduct() {
        expected.remove(3);
        expected.remove(1);
        storageDB.deleteProduct("Cucumber");
        storageDB.deleteProduct("Cucumber");
        storageDB.deleteProduct("Melon");
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()));
    }

    @Test
    public void deleteGroup() {
        storageDB.deleteGroup(groupNames[4]);
        storageDB.deleteGroup(groupNames[1]);

        expected.remove(0);
        expected.remove(0);
        expected.remove(1);
        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder().build()));
    }

    @Test
    public void readProduct() {
        Assertions.assertEquals(allProducts[0], storageDB.readProduct("Watermelon"));
        Assertions.assertNotEquals(allProducts[1], storageDB.readProduct("Watermelon"));
        Assertions.assertEquals(allProducts[2], storageDB.readProduct("Beef"));
    }

    @Test
    public void listByCriteriaProductName() {
        storageDB.createProduct("Beet", "Vegetables", 12.5, 150);
        storageDB.createProduct("Bean", "Vegetables", 5.5, 300);
        storageDB.createProduct("Blackberry", "Berry", 172.0, 100);
        storageDB.createProduct("Milk", "Diary", 25.6, 110);

        expected.clear();
        expected.add(new Product("Beef", groupNames[0], 0, 0));
        expected.add(new Product("Beet", groupNames[1], 150, 12.5));
        expected.add(new Product("Bean", groupNames[1], 300, 5.5));
        expected.add(new Product("Blackberry", groupNames[4], 100, 172.0));

        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder()
                .productNameQuery("B")
                .build()));
    }

    @Test
    public void listByCriteriaGroupName() {
        storageDB.createProduct("Beet", "Vegetables", 12.5, 150);
        storageDB.createProduct("Bean", "Vegetables", 5.5, 300);
        storageDB.createProduct("Blackberry", "Berry", 172.0, 100);

        expected.clear();
        expected.add(new Product("Cucumber", groupNames[1], 0, 0));
        expected.add(new Product("Beet", groupNames[1], 150, 12.5));
        expected.add(new Product("Bean", groupNames[1], 300, 5.5));

        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder()
                .groupNameQuery("Vegetables")
                .build()));
    }

    @Test
    public void listByCriteriaPrice() {
        storageDB.createProduct("Beet", "Vegetables", 12.5, 150);
        storageDB.createProduct("Bean", "Vegetables", 5.5, 300);
        storageDB.createProduct("Blackberry", "Berry", 172.0, 100);
        storageDB.createProduct("Test1", "Berry", 49.0, 100);
        storageDB.createProduct("Test2", "Berry", 17.0, 100);

        expected.clear();
        expected.add(new Product("Beet", groupNames[1], 150, 12.5));
        expected.add(new Product("Test1", groupNames[4], 100, 49.0));
        expected.add(new Product("Test2", groupNames[4], 100, 17.0));

        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder()
                .lowerBoundPrice(12.5)
                .upperBoundPrice(50.0)
                .build()));
    }

    @Test
    public void listByCriteriaAmount() {
        storageDB.createProduct("Beet", "Vegetables", 12.5, 150);
        storageDB.createProduct("Bean", "Vegetables", 5.5, 300);
        storageDB.createProduct("Blackberry", "Berry", 172.0, 100);
        storageDB.createProduct("Test1", "Berry", 49.0, 70);
        storageDB.createProduct("Test2", "Berry", 17.0, 80);

        expected.clear();
        expected.add(new Product("Beet", groupNames[1], 150, 12.5));
        expected.add(new Product("Blackberry", groupNames[4], 100, 172.0));
        expected.add(new Product("Test1", groupNames[4], 70, 49.0));
        expected.add(new Product("Test2", groupNames[4], 80, 17.0));

        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder()
                .lowerBoundAmount(70)
                .upperBoundAmount(150)
                .build()));
    }

    @Test
    public void listByCriteriaCompound() {
        storageDB.createProduct("Test1", "Berry1", 49.0, 70);
        storageDB.createProduct("Test2", "Berry", 15.0, 80);
        storageDB.createProduct("Test3", "Berry", 7.0, 90);
        storageDB.createProduct("Test4", "Berry", 127.0, 300);
        storageDB.createProduct("Test5", "Berry", 17.0, 90);

        expected.clear();
        expected.add(new Product("Test2", groupNames[4], 80, 15.0));
        expected.add(new Product("Test5", groupNames[4], 90, 17.0));

        Assertions.assertIterableEquals(expected, storageDB.filter(Criteria.builder()
                .productNameQuery("Test")
                .groupNameQuery("Berry")
                .lowerBoundAmount(80)
                .upperBoundAmount(150)
                .lowerBoundPrice(15.0)
                .build()));
    }

    @SneakyThrows
    @AfterEach
    public void deleteFile() {
        storageDB.deleteAll();
        storageDB.closeConnection();
        expected.clear();
    }

}

