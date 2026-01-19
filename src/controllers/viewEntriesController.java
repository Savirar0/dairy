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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class viewEntriesController {

    @FXML
    private Label questionIndexLbl;

    @FXML
    private Label questionLbl;

    @FXML
    private TextArea answerTxt;

    @FXML
    private Button prevBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private TextField jumpField;

    private String username;
    private int currentQuestion = 1;
    private static final int TOTAL_QUESTIONS = 108;

    // =========================
    // SET USER
    // =========================
    public void setUsername(String username) {
        this.username = username;
        loadQuestion();
    }

    // =========================
    // LOAD QUESTION + ANSWER
    // =========================
    private void loadQuestion() {
        String sql = """
            SELECT q.question, e.answer
            FROM questions q
            LEFT JOIN entries e
                ON q.question_id = e.question_id
                AND e.username = ?
            WHERE q.question_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, currentQuestion);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                questionLbl.setText(rs.getString("question"));

                String answer = rs.getString("answer");
                if (answer == null || answer.trim().isEmpty()) {
                    answerTxt.setText("(Not answered yet)");
                } else {
                    answerTxt.setText(answer);
                }
            }

            questionIndexLbl.setText(
                "Question " + currentQuestion + " / " + TOTAL_QUESTIONS
            );

            prevBtn.setDisable(currentQuestion <= 1);
            nextBtn.setDisable(currentQuestion >= TOTAL_QUESTIONS);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // NEXT
    // =========================
    @FXML
    void next(ActionEvent event) {
        if (currentQuestion < TOTAL_QUESTIONS) {
            currentQuestion++;
            loadQuestion();
        }
    }

    // =========================
    // PREVIOUS
    // =========================
    @FXML
    void prev(ActionEvent event) {
        if (currentQuestion > 1) {
            currentQuestion--;
            loadQuestion();
        }
    }

    // =========================
    // JUMP TO QUESTION
    // =========================
    @FXML
    void jump(ActionEvent event) {
        try {
            int q = Integer.parseInt(jumpField.getText().trim());

            if (q >= 1 && q <= TOTAL_QUESTIONS) {
                currentQuestion = q;
                loadQuestion();
                jumpField.clear();
            } else {
                jumpField.setText("");
            }

        } catch (NumberFormatException e) {
            jumpField.setText("");
        }
    }

    // =========================
    // BACK TO HOME
    // =========================
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlsViews/home.fxml"));
            Parent root = loader.load();

            homeController controller = loader.getController();
            controller.setUsername(username);
            controller.setProg();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
