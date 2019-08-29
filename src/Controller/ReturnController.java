package Controller;

import Model.BookTM;
import Model.DB;
import Model.IssueTM;
import Model.ReturnTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ReturnController {
    public AnchorPane ReturnPane;
    public JFXTextField txtIssuedDate;
    public JFXTextField txtFine;
    public TableView<ReturnTM> tableReturned;
    public ImageView icnBack;
    public JFXComboBox comboIssueID;
    public JFXDatePicker dateReturnedDate;
    public JFXButton btnSetInventory;

    public void initialize(){

        tableReturned.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableReturned.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        tableReturned.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        tableReturned.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("fine"));

        ObservableList<IssueTM> issued = FXCollections.observableList(DB.issued);
        ObservableList<ReturnTM> returned = FXCollections.observableList(DB.returned);
        tableReturned.setItems(returned);

        //set issue id
        ObservableList combIssued = comboIssueID.getItems();
        for (int i = 0; i <issued.size(); i++) {
            combIssued.add(issued.get(i).getIssueId());
        }

        comboIssueID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(comboIssueID.getSelectionModel().getSelectedItem()==null){
                    return;
                }
                for (int i = 0; i <issued.size() ; i++) {
                    if (comboIssueID.getSelectionModel().getSelectedItem().equals(issued.get(i).getIssueId())){
                        txtIssuedDate.setText(issued.get(i).getDate());
                        txtIssuedDate.setDisable(true);
                    }
                }
            }
        });
        dateReturnedDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                if(dateReturnedDate.getValue().toString().equals(null)){
                    return;
                }

                LocalDate returned = dateReturnedDate.getValue();
                LocalDate issued =  LocalDate.parse(txtIssuedDate.getText());

                Date date1 = Date.valueOf(issued);
                Date date2 = Date.valueOf(returned);

                long diff = date2.getTime() - date1.getTime();

                System.out.println(TimeUnit.MILLISECONDS.toDays(diff));
                int dateCount = (int) TimeUnit.MILLISECONDS.toDays(diff);
                float fine = 0;

                if(dateCount>14){
                    fine=dateCount*15;
                }

                txtFine.setText(Float.toString(fine));
            }
        });
    }

    public void btnSetInventory_OnAction(ActionEvent actionEvent) {


        if(comboIssueID.getSelectionModel().isEmpty() ||
        txtIssuedDate.getText().isEmpty() ||
                dateReturnedDate.getValue().toString().isEmpty() ||
                txtFine.getText().isEmpty()
        ){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill your details.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            System.out.println("You have empty fields!");
            return;
        }
        ObservableList<ReturnTM> returned = FXCollections.observableList(DB.returned);
        ObservableList<BookTM> book = FXCollections.observableList(DB.books);
        ObservableList<IssueTM> issued = FXCollections.observableList(DB.issued);

        String issueID= (String) comboIssueID.getSelectionModel().getSelectedItem();

        returned.add(new ReturnTM(issueID,txtIssuedDate.getText(),dateReturnedDate.getValue().toString(),Float.parseFloat(txtFine.getText())));
        tableReturned.setItems(returned);

        for (int i = 0; i <issued.size() ; i++) {
            if(issueID.equals(issued.get(i).getIssueId())){
                for (int j = 0; j <book.size() ; j++) {
                    if(issued.get(i).getBookId().equals(book.get(j).getId())){
                        book.get(j).setStatus("Available");

                        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                "Book status updated to Available",
                                ButtonType.OK);
                        Optional<ButtonType> buttonType = alert.showAndWait();
                        System.out.println("Book status updated to Available");
                        break;
                    }
                }
            }
        }
        comboIssueID.getItems().remove(issueID);
        comboIssueID.getSelectionModel().clearSelection();
        txtIssuedDate.clear();
        txtFine.clear();
        dateReturnedDate.getEditor().clear();

    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
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
        Stage primaryStage= (Stage) this.ReturnPane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    public void btnNew_OnAction(ActionEvent actionEvent) {

    }
}
