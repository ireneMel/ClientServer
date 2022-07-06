package homework.database;

import homework.storage.Product;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/*
Create
Read
Update
Delete
List by criteria
 */
public class StorageDB {
    private Connection connection;

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void initialization(String name) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + name);
            PreparedStatement st = connection.prepareStatement("create table if not exists 'storage' " +
                    "('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "'productName' text, " +
                    "'productPrice' double, " +
                    "'productAmount' int," +
                    "'productGroup' text)");
            int result = st.executeUpdate();
        } catch (ClassNotFoundException e) {
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Неправильний SQL запит");
            e.printStackTrace();
        }
    }

    public void createProduct(String productName, String groupName, double price, int amount) {
        if (price < 0) throw new RuntimeException("Price must be above zero");
        if (amount < 0) throw new RuntimeException("Amount must be above zero");
        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO storage VALUES (?, ?, ?, ?, ?)");
            st.setString(2, productName);
            st.setDouble(3, price);
            st.setInt(4, amount);
            st.setString(5, groupName);
            int res = st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createProduct(String productName, String groupName) {
        createProduct(productName, groupName, 0, 0);
    }

    public void createProduct(String productName, String groupName, double price) {
        createProduct(productName, groupName, price, 0);
    }

    public void deleteAll() {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM storage");
            st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(String name) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM storage WHERE productName=(?)");
            st.setString(1, name);
            st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroup(String name) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM storage WHERE productGroup=(?)");
            st.setString(1, name);
            st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product readProduct(String name) {
        Product product = null;
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM storage WHERE productName=?");
            st.setString(1, name);
            ResultSet res = st.executeQuery();

            product = getProduct(res);

            res.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Неправильний SQL запит на вибірку даних");
            e.printStackTrace();
        }
        return product;
    }

    private void update(PreparedStatement st, Consumer<PreparedStatement> setter) throws SQLException {
        final boolean oldAutoCommit = st.getConnection().getAutoCommit();
        st.getConnection().setAutoCommit(false);
        try {
            setter.accept(st);
            st.execute();
            st.close();
        } catch (Exception e) {
            st.getConnection().rollback();
        } finally {
            st.getConnection().commit();
            st.getConnection().setAutoCommit(oldAutoCommit);
        }
    }

    public void updateProduct(String name, String newName) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE storage SET productName=? WHERE productName=?");
            update(st, (statement -> {
                try {
                    statement.setString(1, newName);
                    statement.setString(2, name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(String name, double newPrice) {
        if(newPrice < 0) throw new RuntimeException("Price must be above zero");
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE storage SET productPrice=? WHERE productName=?");
            update(st, (statement -> {
                try {
                    statement.setDouble(1, newPrice);
                    statement.setString(2, name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(String name, int newAmount) {
        if(newAmount < 0) throw new RuntimeException("Price must be above zero");
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE storage SET productAmount=? WHERE productName=?");
            update(st, (statement -> {
                try {
                    statement.setInt(1, newAmount);
                    statement.setString(2, name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(String name, String newName) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE storage SET productGroup=? WHERE productGroup=?");
            update(st, (statement -> {
                try {
                    statement.setString(1, newName);
                    statement.setString(2, name);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getString("productName"),
                resultSet.getString("productGroup"),
                resultSet.getInt("productAmount"),
                resultSet.getDouble("productPrice"));
    }

    public List<Product> filter(Criteria criteria) {
        LinkedList<Product> productList = new LinkedList<>();
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM storage WHERE" +
                    " (productGroup LIKE ?) AND " +
                    " (productName LIKE ?) AND" +
                    " (productPrice BETWEEN ? AND ?) AND " +
                    " (productAmount BETWEEN ? AND ?)"
            );

            st.setString(1, criteria.getGroupNameQuery() + "%");
            st.setString(2, criteria.getProductNameQuery() + "%");
            st.setDouble(3, criteria.getLowerBoundPrice());
            st.setDouble(4, criteria.getUpperBoundPrice());
            st.setInt(5, criteria.getLowerBoundAmount());
            st.setInt(6, criteria.getUpperBoundAmount());

            ResultSet res = st.executeQuery();

            while (res.next()) {
                productList.add(getProduct(res));
            }

            st.execute();
            res.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

}