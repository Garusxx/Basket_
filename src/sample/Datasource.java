package sample;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "items.db";

    public static final String CONNECTION_STRING = "jdbc:sqlserver:C:\\Users\\Rafal\\IdeaProjects\\Basket3\\src\\" + DB_NAME;

    public static final String TABLE_ITEMS = "ItemsInStok";
    public static final String TABLE_ITEMS_TWO = "ItemsInStokTwo";

    public static final String COLUMN_ITEMS_ID = "id";
    public static final String COLUMN_ITEMS_NAME = "name";
    public static final String COLUMN_ITEMS_PRICE = "price";
    public static final String COLUMN_ITEMS_QUANTITY = "quantity";

    public static final String QUERY_ITEM = "SELECT " + COLUMN_ITEMS_ID + " FROM " +
            TABLE_ITEMS + " WHERE " + COLUMN_ITEMS_NAME + " = ?";

    public static final String INSERT_ITEM = "INSERT INTO " + TABLE_ITEMS +
            '(' + COLUMN_ITEMS_NAME + ", " + COLUMN_ITEMS_PRICE + ", " + COLUMN_ITEMS_QUANTITY +
            ") VALUES(?, ?, ?)";

    public static final String INSERT_ITEM_TWO = "INSERT INTO " + TABLE_ITEMS_TWO +
            '(' + COLUMN_ITEMS_NAME + ", " + COLUMN_ITEMS_PRICE + ", " + COLUMN_ITEMS_QUANTITY +
            ") VALUES(?, ?, ?)";

    public static final String REMOVE_ITEM = "DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEMS_NAME + " = ?";
    public static final String REMOVE_ITEM_TWO = "DELETE FROM " + TABLE_ITEMS_TWO + " WHERE " + COLUMN_ITEMS_NAME + " = ?";

    public static final String FIND_BY_NAME = "SELECT * FROM " + TABLE_ITEMS + " WHERE name LIKE " +
            COLUMN_ITEMS_NAME + " = ?" ;

    public static final String UPDATA_QUANTITI = "UPDATE " + TABLE_ITEMS + " SET " + COLUMN_ITEMS_QUANTITY + "=? WHERE " +
            COLUMN_ITEMS_NAME + "=?";

    public static final String UPDATA_QUANTITI_TWO = "UPDATE " + TABLE_ITEMS_TWO + " SET " + COLUMN_ITEMS_QUANTITY + "=? WHERE " +
            COLUMN_ITEMS_NAME + "=?";


    private PreparedStatement insertIntoItem;
    private PreparedStatement insertIntoItemTwo;
    private PreparedStatement queryRemoveItem;
    private PreparedStatement queryRemoveItemTwo;
    private PreparedStatement queryFindByName;
    private PreparedStatement queryItem;
    private PreparedStatement updataQuantiti;
    private PreparedStatement updataQuantitiTwo;

    private Connection conn;

    private static final Datasource instance = new Datasource();

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoItem = conn.prepareStatement(INSERT_ITEM, Statement.RETURN_GENERATED_KEYS);
            insertIntoItemTwo = conn.prepareStatement(INSERT_ITEM_TWO, Statement.RETURN_GENERATED_KEYS);
            queryRemoveItem = conn.prepareStatement(REMOVE_ITEM);
            queryRemoveItemTwo = conn.prepareStatement(REMOVE_ITEM_TWO);
            queryFindByName = conn.prepareStatement(FIND_BY_NAME);
            queryItem = conn.prepareStatement(QUERY_ITEM);
            updataQuantiti = conn.prepareStatement(UPDATA_QUANTITI);
            updataQuantitiTwo = conn.prepareStatement(UPDATA_QUANTITI_TWO);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close(){

        try{
            if (conn != null) {
                conn.close();
            }
            if(insertIntoItem != null){
                insertIntoItem.close();
            }
            if(insertIntoItemTwo != null){
                insertIntoItemTwo.close();
            }
            if(queryItem != null){
                queryItem.close();
            }
            if(queryRemoveItem != null){
                queryRemoveItem.close();
            }
            if(queryFindByName != null){
                queryFindByName.close();
            }
            if(updataQuantiti != null){
                updataQuantiti.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Item> queryItems(int tableNum){

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        if(tableNum == 1){
        sb.append(TABLE_ITEMS);
        } else if(tableNum == 2){
            sb.append(TABLE_ITEMS_TWO);
        }else{
            System.out.println("Choose the right one");
        }


        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Item> items = new ArrayList<>();
            while (results.next()) {

                Item item = new Item();

                item.setName(results.getString(COLUMN_ITEMS_NAME));
                item.setPrice(results.getInt(COLUMN_ITEMS_PRICE));
                item.setQuantiti(results.getInt(COLUMN_ITEMS_QUANTITY));
                item.setId(results.getInt(COLUMN_ITEMS_ID));

                items.add(item);

            }
            return items;


        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;

        }

    }


    public Item insertItem(Item item){

        try {
            conn.setAutoCommit(false);

            insertIntoItem.setString(1, item.getName());
            insertIntoItem.setDouble(2, item.getPrice());
            insertIntoItem.setInt(3, item.getQuantiti());
            int affectedRows = insertIntoItem.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The item insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert item exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
        return item;
    }

    public Item insertItemTwo(Item item){

        try {
            conn.setAutoCommit(false);

            insertIntoItemTwo.setString(1, item.getName());
            insertIntoItemTwo.setDouble(2, item.getPrice());
            insertIntoItemTwo.setInt(3, item.getQuantiti());
            int affectedRows = insertIntoItemTwo.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The item insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert item exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
        return item;
    }

    public Item removeItem(Item item) {


        try {
            conn.setAutoCommit(false);

            queryRemoveItem.setString(1, item.getName());

            int affectedRows = queryRemoveItem.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The item insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert item exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
        return item;
    }

    public Item removeItemFromTwo(Item item) {

        try {
            conn.setAutoCommit(false);

            queryRemoveItemTwo.setString(1, item.getName());

            int affectedRows = queryRemoveItemTwo.executeUpdate();
            if(affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("The item insert failed");
            }

        } catch(Exception e) {
            System.out.println("Insert item exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch(SQLException e2) {
                System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                conn.setAutoCommit(true);
            } catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }

        }
        return item;
    }

    public boolean upDataQuantiti (int quantiti, String name){

        try{
            updataQuantiti.setInt(1, quantiti);
            updataQuantiti.setString(2, name);
            int affectedRocords = updataQuantiti.executeUpdate();
            return affectedRocords == 1;

        }catch (SQLException e){
            System.out.println("Updata feil : " + e.getMessage());
            return false;
        }
    }

    public boolean upDataQuantitiTwo (int quantiti, String name){
        try{
            updataQuantitiTwo.setInt(1, quantiti);
            updataQuantitiTwo.setString(2, name);
            int affectedRocords = updataQuantitiTwo.executeUpdate();
            return affectedRocords == 1;

        }catch (SQLException e){
            System.out.println("Updata feil : " + e.getMessage());
            return false;
        }
    }

    public List<Item> findByItem(String  itemName, int tableNum) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        if(tableNum == 1){
            sb.append(TABLE_ITEMS);
        } else if(tableNum == 2){
            sb.append(TABLE_ITEMS_TWO);
        }else{
            System.out.println("Choose the right one");
        }
        sb.append(" WHERE ");
        sb.append(COLUMN_ITEMS_NAME);
        sb.append(" LIKE ");
        sb.append("'%");
        sb.append(itemName);
        sb.append("%'");


        System.out.println("SQL statement = " + sb);

        try (Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())) {

            List<Item> serchItems = new ArrayList<>();
            while(results.next()) {
                Item serchItem = new Item();

                serchItem.setName(results.getString(COLUMN_ITEMS_NAME));
                serchItem.setPrice(results.getInt(COLUMN_ITEMS_PRICE));
                serchItem.setQuantiti(results.getInt(COLUMN_ITEMS_QUANTITY));
                serchItem.setId(results.getInt(COLUMN_ITEMS_ID));

                serchItems.add(serchItem);

            }

            System.out.println(serchItems);

            return serchItems;
        } catch(SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}
