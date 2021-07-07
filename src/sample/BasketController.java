package sample;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.*;


public class BasketController implements Initializable {

    Basket basket = new Basket();

    @FXML
    private TextField quantitiField;
    @FXML
    private TextArea basketArea;
    @FXML
    private TableView<Item> itemBasketTable;

    Controller controller = new Controller();

    @FXML
    private void addItemBasket(){

        if (Integer.parseInt(quantitiField.getText()) <= 0) {
            controller.erorrWindow ("Illegal quantity " + quantitiField.getText() + "!");
        }else if (Integer.parseInt(quantitiField.getText()) > itemBasketTable.getSelectionModel().getSelectedItem().getQuantiti()){
            controller.erorrWindow ("There is no that many items to add");
        }

        basket.add(itemBasketTable.getSelectionModel().getSelectedItem(),Integer.parseInt(quantitiField.getText()));

        basketArea.setText(basket.toString());

        final Item item = (Item) itemBasketTable.getSelectionModel().getSelectedItem();

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().upDataQuantiti((item.getQuantiti() - Integer.parseInt(quantitiField.getText())), item.getName());
            }
        };

        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                item.setQuantiti((item.getQuantiti() - Integer.parseInt(quantitiField.getText())));
                itemBasketTable.refresh();
            }
        });

        new Thread(task).start();


    }

    @FXML
    private void removeItemBasket(){

        if (Integer.parseInt(quantitiField.getText()) <= 0) {
            controller.erorrWindow ("Illegal quantity " + quantitiField.getText() + "!");
        }else if (Integer.parseInt(quantitiField.getText()) > itemBasketTable.getSelectionModel().getSelectedItem().getQuantiti()){
            controller.erorrWindow ("There is no that many items to remove");
        }

        basket.remove((itemBasketTable.getSelectionModel().getSelectedItem()), Integer.parseInt(quantitiField.getText()));

        basketArea.setText(basket.toString());

        final Item item = (Item) itemBasketTable.getSelectionModel().getSelectedItem();

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().upDataQuantiti((item.getQuantiti() + Integer.parseInt(quantitiField.getText())), item.getName());
            }
        };

        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                item.setQuantiti((item.getQuantiti() + Integer.parseInt(quantitiField.getText())));
                itemBasketTable.refresh();
            }
        });

        new Thread(task).start();

    }

    @FXML
    private void showMenu(ActionEvent event) throws Exception{
        Parent changeScenParent = FXMLLoader.load(getClass().getResource("FXML/sample.fxml"));
        Scene Menu = new Scene(changeScenParent, 800, 600);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(Menu);
        window.show();

    }


    public void listBascketItems() {
        Task<ObservableList<Item>> task = new GetAllItemsTask();
        itemBasketTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listBascketItems();
    }
}

