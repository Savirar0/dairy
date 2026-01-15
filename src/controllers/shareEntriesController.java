package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class shareEntriesController {

    @FXML
    private Label statusLbl;

    @FXML
    private Button downloadBtn;

    private String username;
    private int answeredCount = 0;

    // =========================
    // SET USER & CHECK ANSWERS
    // =========================
    public void setUsername(String username) {
        this.username = username;
        checkAnsweredEntries();
    }

    private void checkAnsweredEntries() {
        String sql = """
            SELECT COUNT(*) AS total
            FROM entries
            WHERE username = ?
              AND answer IS NOT NULL
              AND TRIM(answer) != ''
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                answeredCount = rs.getInt("total");

                if (answeredCount > 0) {
                    statusLbl.setText(
                        "You have answered " + answeredCount + " questions."
                    );
                    downloadBtn.setDisable(false);
                } else {
                    statusLbl.setText(
                        "You haven't answered any questions yet."
                    );
                    downloadBtn.setDisable(true);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DOWNLOAD PDF
    // =========================
    @FXML
    void downloadPdf(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Journal PDF");
        chooser.setInitialFileName(username + "_journal.pdf");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = chooser.showSaveDialog(
            ((Node) event.getSource()).getScene().getWindow()
        );

        if (file == null) return;

        String sql = """
            SELECT q.question, e.answer
            FROM entries e
            JOIN questions q ON q.question_id = e.question_id
            WHERE e.username = ?
              AND e.answer IS NOT NULL
              AND TRIM(e.answer) != ''
            ORDER BY e.question_id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileOutputStream fos = new FileOutputStream(file)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            Document doc = new Document();
            PdfWriter.getInstance(doc, fos);
            doc.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font qFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font aFont = new Font(Font.HELVETICA, 12);

            doc.add(new Paragraph("Journal of " + username, titleFont));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Answered Questions: " + answeredCount));
            doc.add(new Paragraph(" "));

            int i = 1;
            while (rs.next()) {
                doc.add(new Paragraph(
                    i + ". " + rs.getString("question"), qFont
                ));
                doc.add(new Paragraph(
                    rs.getString("answer"), aFont
                ));
                doc.add(new Paragraph(" "));
                i++;
            }

            doc.close();
            statusLbl.setText("PDF downloaded successfully ✔");

        } catch (Exception e) {
            e.printStackTrace();
            statusLbl.setText("Failed to create PDF ❌");
        }
    }

    // =========================
    // BACK TO HOME
    // =========================
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxmlsViews/home.fxml")
            );
            Parent root = loader.load();

            homeController controller = loader.getController();
            controller.setUsername(username);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
