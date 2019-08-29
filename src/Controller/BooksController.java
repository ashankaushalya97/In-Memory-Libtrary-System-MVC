package Controller;

import Model.BookTM;
import Model.DB;
import Model.MemberTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class BooksController {

    public AnchorPane bookPane;
    public JFXTextField txtBookID;
    public JFXTextField txtTitle;
    public JFXTextField txtAuthor;
    public TableView<BookTM> tableBooks;
    public JFXButton btnAdd;
    public JFXTextField txtStatus;

    public void initialize(){
        tableBooks.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableBooks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
        tableBooks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tableBooks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<BookTM> books = FXCollections.observableList(DB.books);
        tableBooks.setItems(books);

        tableBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookTM>() {
            @Override
            public void changed(ObservableValue<? extends BookTM> observable, BookTM oldValue, BookTM newValue) {
                BookTM selectedItem = tableBooks.getSelectionModel().getSelectedItem();
                txtBookID.setDisable(true);
                txtBookID.setText(selectedItem.getId());
                txtTitle.setText(selectedItem.getTitle());
                txtAuthor.setText(selectedItem.getAuthor());
                txtStatus.setText(selectedItem.getStatus());
                btnAdd.setText("Update");
            }
        });
    }

    public void btnNew_OnAction(ActionEvent actionEvent) {

        btnAdd.setText("Add");
        txtStatus.setText("Available");
        txtStatus.setDisable(true);
        txtBookID.setDisable(false);
        txtAuthor.clear();
        txtTitle.clear();

        // Generate a new id
        int maxId = 0;
        for (BookTM books : DB.books) {
            int id = Integer.parseInt(books.getId().replace("B", ""));
            if (id > maxId){
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10){
            id = "B00" + maxId;
        }else if (maxId < 100){
            id = "B0" + maxId;
        }else{
            id = "B" + maxId;
        }
        txtBookID.setText(id);

    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {

        ObservableList<BookTM> books = FXCollections.observableList(DB.books);

        if(txtBookID.getText().isEmpty() || txtTitle.getText().isEmpty() || txtAuthor.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill your details.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }

        if(btnAdd.getText().equals("Add")){
            books.add(new BookTM(txtBookID.getText(),txtTitle.getText(),txtAuthor.getText(),txtStatus.getText()));

        }else{
            for (int i = 0; i <books.size() ; i++) {
                if(txtBookID.getText().equals(books.get(i).getId())){
                    books.set(i,new BookTM(txtBookID.getText(),txtTitle.getText(),txtAuthor.getText(),txtStatus.getText()));


                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Record updated!",
                            ButtonType.OK);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                }
            }
        }
        tableBooks.setItems(books);
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        BookTM selectedItem = tableBooks.getSelectionModel().getSelectedItem();
        ObservableList<BookTM> books = FXCollections.observableList(DB.books);
        if(tableBooks.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a member.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }
        for (int i = 0; i <books.size() ; i++) {
            if(txtBookID.getText().equals(books.get(i).getId())){
                books.remove(i);
                tableBooks.refresh();
                txtBookID.clear();
                txtTitle.clear();
                txtAuthor.clear();
                txtStatus.clear();
                txtStatus.setDisable(false);

                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Record deleted!",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
            }
        }
    }

    @FXML
    private void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
        }
    }

    @FXML
    private void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView){
            ImageView icon = (ImageView) event.getSource();


            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void icnBack_OnAction(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/View/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage= (Stage) this.bookPane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
