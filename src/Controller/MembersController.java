package Controller;

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

public class MembersController {

    public JFXTextField txtMemberID;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public TableView<MemberTM> tableMember;
    public ImageView icnBack;
    public JFXButton btnAdd;
    public AnchorPane memberPane;

    public void initialize(){

        tableMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tableMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));

        ObservableList<MemberTM> members = FXCollections.observableList(DB.members);
        tableMember.setItems(members);

        tableMember.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MemberTM>() {
            @Override
            public void changed(ObservableValue<? extends MemberTM> observable, MemberTM oldValue, MemberTM newValue) {
                MemberTM selectedItem = tableMember.getSelectionModel().getSelectedItem();
                txtMemberID.setText(selectedItem.getId());
                txtName.setText(selectedItem.getName());
                txtAddress.setText(selectedItem.getAddress());
                txtContact.setText(selectedItem.getContact());
                txtMemberID.setDisable(true);
                btnAdd.setText("Update");
            }
        });


    }

    public void btnNew_OnAction(ActionEvent actionEvent) {

        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        btnAdd.setText("Add");
        txtMemberID.setDisable(false);
        // Generate a new id
        int maxId = 0;
        for (MemberTM members : DB.members) {
            int id = Integer.parseInt(members.getId().replace("M", ""));
            if (id > maxId){
                maxId = id;
            }
        }
        maxId = maxId + 1;
        String id = "";
        if (maxId < 10){
            id = "M00" + maxId;
        }else if (maxId < 100){
            id = "M0" + maxId;
        }else{
            id = "M" + maxId;
        }
        txtMemberID.setText(id);


    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
        ObservableList<MemberTM> members = FXCollections.observableList(DB.members);

        if(txtMemberID.getText().isEmpty() || txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtContact.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please fill your details.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }

        if(btnAdd.getText().equals("Add")){
            members.add(new MemberTM(txtMemberID.getText(),txtName.getText(),txtAddress.getText(),txtContact.getText()));

        }else{
            for (int i = 0; i <members.size() ; i++) {
                if(txtMemberID.getText().equals(members.get(i).getId())){
                    members.set(i,new MemberTM(txtMemberID.getText(),txtName.getText(),txtAddress.getText(),txtContact.getText()));

                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Record updated!",
                            ButtonType.OK);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                }
            }
        }
        tableMember.setItems(members);
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        MemberTM selectedItem = tableMember.getSelectionModel().getSelectedItem();
        ObservableList<MemberTM> members = FXCollections.observableList(DB.members);
        if(tableMember.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please select a member.",
                    ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            return;
        }
        for (int i = 0; i <members.size() ; i++) {
            if(txtMemberID.getText().equals(members.get(i).getId())){
                members.remove(i);
                tableMember.refresh();
                txtMemberID.clear();
                txtName.clear();
                txtContact.clear();
                txtAddress.clear();

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
        Stage primaryStage= (Stage) this.memberPane.getScene().getWindow();
        primaryStage.setScene(scene);

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), scene.getRoot());
        tt.setFromX(-scene.getWidth());
        tt.setToX(0);
        tt.play();
    }
}
