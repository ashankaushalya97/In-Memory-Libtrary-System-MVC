package Controller;

import Model.BookTM;
import Model.DB;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

public class BSearchController {
    public AnchorPane searchPane;
    public JFXTextField txtSearch;
    public TableView<BookTM> tableSearch;
    public ImageView icnBack;

    public void initialize(){

        tableSearch.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableSearch.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
        tableSearch.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));
        tableSearch.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<BookTM> books = FXCollections.observableList(DB.books);
        tableSearch.setItems(books);

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String searchText= txtSearch.getText();

                ObservableList<BookTM> tempSearch =  FXCollections.observableArrayList();

                for (BookTM book : books) {
                    if(book.getId().contains(searchText) || book.getTitle().contains(searchText) || book.getAuthor().contains(searchText) || book.getStatus().contains(searchText)){
                        tempSearch.add(book);
                    }
                }
                tableSearch.setItems(tempSearch);
            }
        });


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
        Stage primaryStage= (Stage) this.searchPane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }



}
