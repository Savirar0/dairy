package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class homeController {

    @FXML
    private Button addEntry;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label progbarLbl;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button share;

    @FXML
    private Label titlelabel;

    @FXML
    private Button viewEntries;

    @FXML
    private Label welcome;

    private String username;

    @FXML
    void setUsername(String username){
      this.username = username;
      titlelabel.setText("Welcome, "+username);
    }

    @FXML
    void addEn(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void shareEn(ActionEvent event) {

    }

    @FXML
    void viewEn(ActionEvent event) {

    }

}
