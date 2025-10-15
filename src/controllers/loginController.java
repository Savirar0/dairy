package controllers;
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
    }

    @FXML
    void signup(ActionEvent event) {

    }

}
