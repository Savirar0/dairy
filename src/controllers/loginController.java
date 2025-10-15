package controllers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    @FXML
    private Button createButton;

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
        String pass=passwordField.getText();
        if(user.isEmpty() || pass.isEmpty()){
            msg.setText("Please fill all the fields!");
            return;
        }
        String sql = "select * from users where username=? and password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, user);
                pstmt.setString(2, pass);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    msg.setStyle("-fx-text-fill: green;");
                     msg.setText("Login successful!");
                    //TODO : to switch to main page

                }else{
                    msg.setStyle("-fx-text-fill: red;");
                    msg.setText("Login failed!");
                }

                
             } catch (SQLException e) {
            msg.setText("Database Error. Please try again.");
            e.printStackTrace(); 
        }
    }

    @FXML
    void signup(ActionEvent event) {

    }

}
