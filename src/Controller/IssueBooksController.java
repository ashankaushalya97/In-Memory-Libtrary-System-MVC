package Controller;

import Model.BookTM;
import Model.DB;
import Model.IssueTM;
import Model.MemberTM;
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
import java.time.LocalDate;
import java.util.Optional;

public class IssueBooksController {
    public AnchorPane issuePane;
    public JFXTextField txtIssueID;
    public TableView<IssueTM> tableIssue;
    public JFXButton btnAdd;
    public JFXComboBox comboMemberID;
    public JFXComboBox comboBookID;
    public JFXTextField txtMemberName;
    public JFXTextField txtBookTitle;
    public JFXDatePicker datePicker;

    public void initialize(){
            tableIssue.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("issueId"));
            tableIssue.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
            tableIssue.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("memberId"));
            tableIssue.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("bookId"));

        ObservableList<IssueTM> issued = FXCollections.observableList(DB.issued);
        ObservableList<MemberTM> members = FXCollections.observableList(DB.members);
        ObservableList<BookTM> books = FXCollections.observableList(DB.books);
        tableIssue.setItems(issued);

        //set Member ID
        ObservableList cmbmembers = comboMemberID.getItems();
        for (int i = 0; i <members.size() ; i++) {
            cmbmembers.add(members.get(i).getId());
        }

        //set Member ID
        ObservableList cmbbooks = comboBookID.getItems();
        for (int i = 0; i <books.size() ; i++) {
            cmbbooks.add(books.get(i).getId());
        }

        comboMemberID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if(comboMemberID.getSelectionModel().getSelectedItem()!=null){
                    Object selectedItem = comboMemberID.getSelectionModel().getSelectedItem();
                    if(selectedItem.equals(null) || comboMemberID.getSelectionModel().isEmpty()){
                        return;
                    }
                    for (int i = 0; i <members.size() ; i++) {
                        if(selectedItem.equals(members.get(i).getId())){
                            txtMemberName.setText(members.get(i).getName());
                        }
                    }
                }

            }
        });

        comboBookID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(comboBookID.getSelectionModel().getSelectedItem()!=null){
                    Object selectedItem = comboBookID.getSelectionModel().getSelectedItem();

                    for (int i = 0; i <books.size() ; i++) {
                        if(selectedItem.equals(books.get(i).getId())){
                            if(books.get(i).getStatus().equals("Available")){
                                txtBookTitle.setText(books.get(i).getTitle());
                            }else{
                                Alert alert = new Alert(Alert.AlertType.ERROR,
                                        "Book is not available!",
                                        ButtonType.OK);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                                return;
                            }
                        }
                    }
                }
            }
        });

        tableIssue.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<IssueTM>() {
            @Override
            public void changed(ObservableValue<? extends IssueTM> observable, IssueTM oldValue, IssueTM newValue) {
                IssueTM selectedItem = tableIssue.getSelectionModel().getSelectedItem();
                if(selectedItem.getIssueId()!=null){
                    txtIssueID.setText(selectedItem.getIssueId());
                    datePicker.setValue(LocalDate.parse(selectedItem.getDate()));
                    comboMemberID.getSelectionModel().select(selectedItem.getIssueId());
                    comboBookID.getSelectionModel().select(selectedItem.getBookId());
                    for (int i = 0; i <members.size() ; i++) {
                        if (selectedItem.getMemberId().equals(members.get(i).getId())){
                            txtMemberName.setText(members.get(i).getName());
                        }
                    }
                    for (int i = 0; i <books.size() ; i++) {
                        if(selectedItem.getBookId().equals(books.get(i).getId())){
                            txtBookTitle.setText(books.get(i).getTitle());
                        }
                    }
                }
            }
        });
    }

    public void btnNew_OnAction(ActionEvent actionEvent) {

        txtBookTitle.clear();
        txtMemberName.clear();
        comboMemberID.getSelectionModel().clearSelection();
        comboBookID.getSelectionModel().clearSelection();
        datePicker.setPromptText("Issue Date");




        // Generate a new id
        int maxId = 0;
        for (IssueTM issued : DB.issued) {
            int id = Integer.parseInt(issued.getIssueId().replace("I", ""));
            if (id > maxId){
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10){
            id = "I00" + maxId;
        }else if (maxId < 100){
            id = "I0" + maxId;
        }else{
            id = "I" + maxId;
        }
        txtIssueID.setText(id);
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        ObservableList<IssueTM>  issued = FXCollections.observableList(DB.issued);
        ObservableList<BookTM> books = FXCollections.observableList(DB.books);

        if(txtIssueID.getText().isEmpty() ||
                comboBookID.getSelectionModel().getSelectedItem().equals(null) ||
                comboMemberID.getSelectionModel().getSelectedItem().equals(null)
                || datePicker.getValue().toString().equals(null)){

            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill your details.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            System.out.println("You have empty fields!");
            return;
        }
        String memberId = (String) comboMemberID.getSelectionModel().getSelectedItem();
        String bookId = (String) comboBookID.getSelectionModel().getSelectedItem();
        issued.add(new IssueTM(txtIssueID.getText(),datePicker.getValue().toString(),memberId,bookId));
        for (int i = 0; i <books.size() ; i++) {
            if(books.get(i).getId().equals(bookId)){
                books.get(i).setStatus("Unavailable");
                System.out.println("Status updated!");

                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Status updated.",
                        ButtonType.OK);
                Optional<ButtonType> buttonType = alert.showAndWait();
            }
        }
        tableIssue.setItems(issued);
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {

        ObservableList<IssueTM> issued = FXCollections.observableList(DB.issued);
        if(tableIssue.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a member.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }
        for (int i = 0; i <issued.size() ; i++) {
            if(txtIssueID.getText().equals(issued.get(i).getIssueId())){
                issued.remove(i);
                tableIssue.refresh();

                txtBookTitle.clear();
                txtMemberName.clear();
                comboMemberID.getSelectionModel().clearSelection();
                comboBookID.getSelectionModel().clearSelection();
                datePicker.setPromptText("Issue Date");
                txtIssueID.clear();


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
        Stage primaryStage= (Stage) this.issuePane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
