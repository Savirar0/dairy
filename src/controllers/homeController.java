package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

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

    public void setUsername(String username) {
        this.username = username;
        welcome.setText("Welcome, " + username + "!");
        setProg();
    }

    @FXML
    public void setProg() {
        String sql = "SELECT progress FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int progress = rs.getInt("progress");
                progbarLbl.setText("Your Progress (" + progress + "/108)");
                progressBar.setProgress((double) progress / 108.0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addEn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlsViews/entry.fxml"));
            Parent root = loader.load();

            controllers.entryController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println("Error loading the entry.fxml file.");
            ex.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlsViews/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("User logged out successfully ✅");

        } catch (IOException ex) {
            System.err.println("Error loading the login.fxml file during logout.");
            ex.printStackTrace();
        }
    }

    @FXML
void shareEn(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxmlsViews/shareEntries.fxml")
        );
        Parent root = loader.load();

        shareEntriesController controller = loader.getController();
        controller.setUsername(username);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    void viewEn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlsViews/viewEntries.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Successfully loaded view entries ✅");

        } catch (IOException ex) {
            System.err.println("Error loading the viewEntries.fxml file.");
            ex.printStackTrace();
        }
    }
}
