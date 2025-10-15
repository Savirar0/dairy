package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DatabaseConnection;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class signupController {

    @FXML
    private Label msg;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordagain;

    @FXML
    private Button signupButton;

    @FXML
    private Label titlelabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    boolean isValid = false;

    @FXML
    void login(ActionEvent event) {
        String user = usernameField.getText();
        String pass=passwordField.getText();
        String pass2=passwordagain.getText();
        if(user.isEmpty() || pass.isEmpty() || pass2.isEmpty()){
            msg.setText("Please fill all the fields!");
            return;
        }
        if(!pass.equals(pass2)){
            msg.setText("Passwords do not match!");
            return;
        }
        String sql = "insert into users(username,password) values(?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.executeUpdate();
            msg.setStyle("-fx-text-fill: green;");
            msg.setText("Signup successful! Click login to proceed.");

        } catch (SQLException e) {
            msg.setText("Database Error. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    void loginPage(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlsViews/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
        System.err.println("Error loading the signup.fxml file.");
        e.printStackTrace();
    }

    }

}
