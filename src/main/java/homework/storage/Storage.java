package homework.storage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Storage {
    private final List<Group> groupList = Collections.synchronizedList(new LinkedList<>());
    private final List<Product> productList = Collections.synchronizedList(new LinkedList<>());

    public int getProductAmount(String name) {
        int id = findProductId(name);
        if (id != -1) return productList.get(id).getAmount();
        return -1;
    }

    public boolean increaseProductAmount(String name, int amount) {
        int id = findProductId(name);
        if (id == -1) return false;
        Product product = productList.get(id);
        return setProductAmount(product, product.getAmount() + amount);
    }

    public boolean decreaseProductAmount(String name, int amount) {
        int id = findProductId(name);
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

    public boolean setPrice(String name, double price) {
        if (price <= 0) return false;
        int id = findProductId(name);
        if (id == -1) return false;
        productList.get(id).setPrice(price);
        return true;
    }

    public void addGroup(String name) {
//        if (findGroupId(name) == -1)
        groupList.add(new Group(name));
    }

    public void addProductToGroup(String groupName, String productName) {
        int groupId = findGroupId(groupName);
        int productId = findProductId(productName);

        if (groupId == -1)
            groupList.add(new Group(groupName));

        if (productId == -1)
            productList.add(new Product(productName));

        groupList.get(groupId).getProducts().add(new Product(productName));
    }

    private int findProductId(String name) {
        synchronized (productList) {
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getName().equals(name))
                    return i;
            }
        }
        return -1;
    }

    private int findGroupId(String name) {
        synchronized (groupList) {
            for (int i = 0; i < groupList.size(); i++) {
                if (groupList.get(i).getGroupName().equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }
}


