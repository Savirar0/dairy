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
        loadLastProgress(); // ✅ Load their last question when username is set
    }

    private void loadLastProgress() {
        String sql = "SELECT progress FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int prog = rs.getInt("progress");
                currentQuestion = (prog > 0 && prog < 108) ? prog + 1 : 1; // continue from last unanswered
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadCurrentQuestion(); // ✅ load question & saved answer
    }

    private void loadCurrentQuestion() {
        String qSql = "SELECT question FROM questions WHERE question_id = ?";
        String aSql = "SELECT answer FROM entries WHERE username = ? AND question_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement qStmt = conn.prepareStatement(qSql);
            qStmt.setInt(1, currentQuestion);
            ResultSet qRs = qStmt.executeQuery();

            if (qRs.next()) {
                questionLbl.setText(qRs.getString("question"));
            } else {
                questionLbl.setText("🎉 You’ve completed all 108 questions!");
                nextBtn.setDisable(true);
                return;
            }

            PreparedStatement aStmt = conn.prepareStatement(aSql);
            aStmt.setString(1, username);
            aStmt.setInt(2, currentQuestion);
            ResultSet aRs = aStmt.executeQuery();

            if (aRs.next()) {
                ansTxt.setText(aRs.getString("answer"));
            } else {
                ansTxt.clear();
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
    void prev(ActionEvent event){
        
    }

    @FXML
    void next(ActionEvent event) {
        if (username == null || currentQuestion <= 0) {
            System.out.println("⚠️ Username or question number missing!");
            return;
        }

        if (currentQuestion >= 108) {
            questionLbl.setText("🎉 You’ve completed all questions!");
            nextBtn.setDisable(true);
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

            currentQuestion++; // ✅ go to next question
            loadCurrentQuestion(); // ✅ load next question + saved answer

            System.out.println("✅ Answer saved, progress updated, and moved to question " + currentQuestion);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
