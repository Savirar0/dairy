import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            
            URL fxmlUrl = getClass().getResource("/fxmlsViews/login.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("ERROR: login.fxml not found in /fxmlsViews/");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Scene scene = new Scene(root);

            //css
            URL cssUrl = getClass().getResource("/CSS/theme.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("WARNING: theme.css not found");
            }

            
            stage.setTitle("Memento");

            //icon
            URL iconUrl = getClass().getResource("/assets/icon.ico");
            if (iconUrl != null) {
                stage.getIcons().add(new Image(iconUrl.toExternalForm()));
            } else {
                System.err.println("WARNING: icon.ico not found");
            }

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            // CRITICAL
            System.err.println("APPLICATION FAILED TO START");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
