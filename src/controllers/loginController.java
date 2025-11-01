package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnection;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class loginController {

    @FXML
    private Button createButton;
    //@FXML
    //private Button loginButton;

    @FXML
    private Button lognButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label titlelabel;

    @FXML
    private Label msg;

    @FXML
    private TextField usernameField;

    @FXML
    void login(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            msg.setText("Please fill all the fields!");
            return;
        }

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                msg.setStyle("-fx-text-fill: green;");
                msg.setText("Login successful!");

                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(ev -> {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/fxmlsViews/home.fxml"));
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        System.err.println("Error loading the home.fxml file.");
                        ex.printStackTrace();
                    }
                });
                pause.play(); 

            } else {
                msg.setStyle("-fx-text-fill: red;");
                msg.setText("Login failed!");
            }

        } catch (SQLException ex) {
            msg.setText("Database Error. Please try again.");
            ex.printStackTrace();
        }
    }

    @FXML
    void signupp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlsViews/signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println("Error loading the signup.fxml file.");
            ex.printStackTrace();
        }
    }
}
