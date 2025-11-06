package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class entryController {

    @FXML
    private TextArea ansTxt;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Button prevBtn;

    @FXML
    private Label questionLbl;

    @FXML
    private Button saveBtn;

    @FXML
    private Label titlelabel;

    private String username;
    private int currentQuestion = 1; 
    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuestion(int question) {
        this.currentQuestion = question;
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
    void next(ActionEvent event) {
        // (to be implemented later)
    }

    @FXML
    void prev(ActionEvent event) {
        // (to be implemented later)
    }

    @FXML
    private void save() {
        if (username == null || currentQuestion <= 0) {
            System.out.println("⚠️ Username or question number missing!");
            return;
        }

        String answer = ansTxt.getText();

        String insertSQL = """
            INSERT INTO entries (username, question_id, answer)
            VALUES (?, ?, ?)
            ON CONFLICT(username, question_id)
            DO UPDATE SET answer = excluded.answer;
        """;

        String progressSQL = """
            UPDATE users
            SET progress = (
                SELECT COUNT(*) FROM entries 
                WHERE username = ? AND answer IS NOT NULL AND TRIM(answer) != ''
            )
            WHERE username = ?;
        """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, username);
            pstmt.setInt(2, currentQuestion);
            pstmt.setString(3, answer);
            pstmt.executeUpdate();

            PreparedStatement progStmt = conn.prepareStatement(progressSQL);
            progStmt.setString(1, username);
            progStmt.setString(2, username);
            progStmt.executeUpdate();

            System.out.println("✅ Answer saved & progress updated for " + username);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
