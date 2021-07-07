package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class AddItemController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantiotiField;


    public void addItem() throws SQLException {

        Item newItem = new Item();
        Controller controller = new Controller();

        if(nameField.getText().isEmpty()){
            controller.erorrWindow("enter a name");
        }else if(priceField.getText().isEmpty()) {
            controller.erorrWindow("enter price");
        }else if(quantiotiField.getText().isEmpty()){
            controller.erorrWindow("enter the amount");
        }else {

            controller.textFieldError(priceField);
            controller.textFieldError(quantiotiField);

            newItem.setName(nameField.getText());
            newItem.setPrice(Double.parseDouble(priceField.getText()));
            newItem.setQuantiti(Integer.parseInt(quantiotiField.getText()));

            List<Item> name = Datasource.getInstance().findByItem(newItem.getName(), 1);
            boolean ans = name.isEmpty();
            if(!ans) {
                Item item = name.get(0);
                if(item.getName().matches(newItem.getName()) && item.getPrice() == newItem.getPrice()){
                    Datasource.getInstance().upDataQuantiti(item.getQuantiti() + newItem.getQuantiti(), newItem.getName());
                }else if(item.getPrice() != newItem.getPrice()){
                    controller.erorrWindow("this item already exists");
                }
            }

            Datasource.getInstance().insertItem(newItem);

        }
    }

}


