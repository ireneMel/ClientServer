package practice1.storage;

import java.util.LinkedList;

public class Storage {
    private LinkedList<Group> groupList;
    private LinkedList<Product> productList;

    private int findId(String name) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).isProduct(name))
                return i;
        }
        return -1;
    }

    public int productAmount(String name) {
        int id = findId(name);
        if (id != -1) return productList.get(id).getAmount();
        return -1;
    }

    public boolean deleteProduct(String name, int amount) {
        if (amount <= 0) return false;
        int id = findId(name);
        if (id == -1) return false;
        if (productList.get(id).getAmount() <= amount) productList.remove(id);
        else productList.get(id).setAmount(productList.get(id).getAmount() - amount);
        return true;

    }

    public void addProduct(String name, int amount, double price) {
        if (price <= 0 || amount <= 0) return;
        productList.add(new Product(name, amount, price));
    }

    public void setPrice(String name, double price) {
        if (price <= 0) return;

        int id = findId(name);
        if (id == -1) return;
        productList.get(id).setPrice(price);
    }

    public void addGroup() {

    }

    public void addProductToGroup() {

    }
}


