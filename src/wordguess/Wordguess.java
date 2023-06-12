package wordguess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Wordguess extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("wordguess.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Start background music
        Sounds sounds = new Sounds();
        sounds.startThemePlayer();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
