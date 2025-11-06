import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/fxmlsViews/home.fxml"));
            Scene scene = new Scene(root);
            //scene1.getStylesheets().add(getClass().getResource("Scene1.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch(Exception e){
            e.printStackTrace();

        }
    }
}

