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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {



    @FXML
    private BorderPane mainPanal;

    @FXML
    private TextField searchField;

    Alert a = new Alert(Alert.AlertType.NONE);


    @FXML
    public void erorrWindow(String massage) {

        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText(massage);
        a.show();

    }

    @FXML
    public void textFieldError(TextField name){

        String text = name.getText();
        boolean match = text.matches("[a-zA-Z]+");
        if(match){
            erorrWindow("A number is required");
        }
    }

    @FXML
    private void showAddItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.initOwner(mainPanal.getScene().getWindow());
        dialog.setTitle("Add new Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("addItemsDialog.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            System.out.println("Coudym't load dialo");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try{
                AddItemController controler = fxmlLoader.getController();
                controler.addItem();
                listArtists();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }

    }


    @FXML
    private void showBaskerView(ActionEvent event) throws Exception{
        Parent changeScenParent = FXMLLoader.load(getClass().getResource("FXML/basket.fxml"));
        Scene Baket = new Scene(changeScenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(Baket);
        window.show();
    }

    @FXML
    private void showTransferView(ActionEvent event) throws Exception{
        Parent changeScenParent = FXMLLoader.load(getClass().getResource("FXML/transfer.fxml"));
        Scene Transfer = new Scene(changeScenParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(Transfer);
        window.show();
    }


    @FXML
    public void removeItem(){
        Item item;
        item = itemTable.getSelectionModel().getSelectedItem();

        Task<Item> task = new Task<Item>() {
            @Override
            protected Item call() throws Exception {
                return Datasource.getInstance().removeItem(item);
            }
        };
        task.setOnSucceeded(e ->{
            listArtists();
        });

        new Thread(task).start();
    }

    @FXML
    public void search(){
        String itemName = searchField.getText();

        Task<ObservableList<Item>> task = new Task<ObservableList<Item>>() {
            @Override
            protected ObservableList<Item> call() throws Exception {
                return FXCollections.observableArrayList(
                        Datasource.getInstance().findByItem(itemName, 1));

            }
        };
        itemTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

    }


    @FXML
    private TableView<Item> itemTable;

    public void listArtists() {
        Task<ObservableList<Item>> task = new GetAllItemsTask();
        itemTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listArtists();
    }
}

class GetAllItemsTask extends Task {

    @Override
    public ObservableList<Item> call()  {
        return FXCollections.observableArrayList
                (Datasource.getInstance().queryItems(1));
    }
}

