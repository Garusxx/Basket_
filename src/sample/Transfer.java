package sample;

import javafx.collections.FXCollections;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Transfer implements Initializable {
    @FXML
    private TableView<Item> transferOneTable;

    @FXML
    private TableView<Item> transferTwoTable;

    @FXML
    private TextField transferQuantiti;

    Controller controller = new Controller();

    public void listtransfeOneItems() {
        Task<ObservableList<Item>> task = new GetAllItemsTask();
        transferOneTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    public void listtransfeTwoItems() {
        Task<ObservableList<Item>> task = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().queryItems(2));

            }
        };
        transferTwoTable.itemsProperty().bind(task.valueProperty());

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

    @FXML
    private void transferItemIn() {

        List<Item> name = Datasource.getInstance().findByItem(transferOneTable.getSelectionModel().getSelectedItem().getName(), 1);
        List<Item> name2 = Datasource.getInstance().findByItem(transferOneTable.getSelectionModel().getSelectedItem().getName(), 2);

        boolean ans = name2.isEmpty();

        if(ans) {
            Item transferItem = transferOneTable.getSelectionModel().getSelectedItem();
            transferItem.setQuantiti(0);
            Datasource.getInstance().insertItemTwo(transferItem);
            name2.add(transferItem);
        }

        Item item = name.get(0);
        Item item2 = name2.get(0);

        if (Integer.parseInt(transferQuantiti.getText()) <= 0) {
            controller.erorrWindow(String.format("Illegal quantity %d!", Integer.parseInt(transferQuantiti.getText())));
            throw new IllegalArgumentException(String.format("Illegal quantity %d!", Integer.parseInt(transferQuantiti.getText())));
        }else if (Integer.parseInt(transferQuantiti.getText()) > item.getQuantiti()){
            controller.erorrWindow("There is no that many items to add");
            throw new IllegalStateException("There is no that many items to add");
        }

        Datasource.getInstance().upDataQuantiti((item.getQuantiti() - Integer.parseInt(transferQuantiti.getText())), item.getName());
        item.setQuantiti((item.getQuantiti() - Integer.parseInt(transferQuantiti.getText())));
        listtransfeOneItems();

        Datasource.getInstance().upDataQuantitiTwo((item2.getQuantiti() + Integer.parseInt(transferQuantiti.getText())), item2.getName());
        item2.setQuantiti((item2.getQuantiti() + Integer.parseInt(transferQuantiti.getText())));
        listtransfeTwoItems();

    }

    @FXML
    private void transferItemOut() {

        List<Item> name = Datasource.getInstance().findByItem(transferTwoTable.getSelectionModel().getSelectedItem().getName(), 1);
        List<Item> name2 = Datasource.getInstance().findByItem(transferTwoTable.getSelectionModel().getSelectedItem().getName(), 2);

        boolean ans = name.isEmpty();

        if(ans) {
            Item transferItem = transferTwoTable.getSelectionModel().getSelectedItem();
            transferItem.setQuantiti(0);
            Datasource.getInstance().insertItem(transferItem);
            name.add(transferItem);
        }

        Item item = name.get(0);
        Item item2 = name2.get(0);

        if (Integer.parseInt(transferQuantiti.getText()) <= 0) {
            controller.erorrWindow(String.format("Illegal quantity %d!", Integer.parseInt(transferQuantiti.getText())));
            throw new IllegalArgumentException(String.format("Illegal quantity %d!", Integer.parseInt(transferQuantiti.getText())));
        }else if (Integer.parseInt(transferQuantiti.getText()) > item2.getQuantiti()){
            controller.erorrWindow("There is no that many items to add");
            throw new IllegalStateException("There is no that many items to add");
        }

        Datasource.getInstance().upDataQuantiti((item.getQuantiti() + Integer.parseInt(transferQuantiti.getText())), item.getName());
        item.setQuantiti((item.getQuantiti() - Integer.parseInt(transferQuantiti.getText())));
        listtransfeOneItems();

        Datasource.getInstance().upDataQuantitiTwo((item2.getQuantiti() - Integer.parseInt(transferQuantiti.getText())), item2.getName());
        item2.setQuantiti((item2.getQuantiti() + Integer.parseInt(transferQuantiti.getText())));
        listtransfeTwoItems();

    }

    @FXML
    private void removeItem(){
        Item item;
        item = transferTwoTable.getSelectionModel().getSelectedItem();

        Task<Item> task = new Task<Item>() {
            @Override
            protected Item call() throws Exception {
                return Datasource.getInstance().removeItemFromTwo(item);
            }
        };
        task.setOnSucceeded(e ->{
            listtransfeOneItems();
            listtransfeTwoItems();

        });

        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listtransfeOneItems();listtransfeTwoItems();
    }
}
