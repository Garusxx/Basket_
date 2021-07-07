package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item implements Comparable<Item> {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleIntegerProperty quantiti;

    public Item() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty();
        this.quantiti = new SimpleIntegerProperty();
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantiti() {
        return quantiti.get();
    }

    public SimpleIntegerProperty quantitiProperty() {
        return quantiti;
    }

    public void setQuantiti(int quantiti) {
        this.quantiti.set(quantiti);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || ! (otherObject instanceof Item)) {
            return false;
        }

        Item item = (Item) otherObject;

        if (Double.compare(item.getPrice(), getPrice()) != 0) {
            return false;
        }
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode();
    }

    @Override
    public int compareTo(Item other) {
        if (other == null) {
            return 1;
        }
        int comparision = this.getName().compareTo(other.getName());
        if (comparision != 0) {
            return comparision;
        }
        return Double.compare(this.getPrice(), other.getPrice());
    }


}
