package practice1.storage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Storage {
    private final List<Group> groupList = Collections.synchronizedList(new LinkedList<>());
    private final List<Product> productList = Collections.synchronizedList(new LinkedList<>());

    private int findId(String name) {
        synchronized (productList) {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).isProduct(name))
                    return i;
            }
        }
        return -1;
    }

    public int getProductAmount(String name) {
        int id = findId(name);
        if (id != -1) return productList.get(id).getAmount();
        return -1;
    }

    public boolean increaseProductAmount(String name, int amount) {
        int id = findId(name);
        if (id == -1) return false;
        Product product = productList.get(id);

        return setProductAmount(product, product.getAmount() + amount);
    }

    public boolean decreaseProductAmount(String name, int amount) {
        int id = findId(name);
        if (id == -1) return false;
        Product product = productList.get(id);

        return setProductAmount(product, product.getAmount() - amount);
    }

    private boolean setProductAmount(Product product, int amount) {
        if (amount < 0) return false;
        product.setAmount(amount);
        return true;
    }

    public void addProductName(String name) {
        productList.add(new Product(name));
    }

    public void setPrice(String name, double price) {
        if (price <= 0) return;
        int id = findId(name);
        if (id == -1) return;
        productList.get(id).setPrice(price);
    }

    public void addGroup(String name) {
        groupList.add(new Group(name));
    }

    public void addProductToGroup(String groupName, String productName) {

    }
}


