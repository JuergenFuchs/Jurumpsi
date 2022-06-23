import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SPIEL extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("test.fxml")); 
        primaryStage.setTitle("FXML-Beispiel");
        primaryStage.setScene(new Scene(root, 100, 57));
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}