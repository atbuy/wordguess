package wordguess;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sounds {
    // Sounds
    private static final String THEME_URL = "./src/wordguess/static/sounds/theme.mp3";
    private static final String SUCCESS_URL = "./src/wordguess/static/sounds/success.mp3";
    private static final String ERROR_URL = "./src/wordguess/static/sounds/error.mp3";

    // Media player has to be static here, otherwise it will be garbage collected
    // and the sound will not play
    private static final File THEME_FILE = new File(THEME_URL);
    private static final Media themeMedia = new Media(THEME_FILE.toURI().toString());
    private static final MediaPlayer themePlayer = new MediaPlayer(themeMedia);

    private static final File SUCCESS_FILE = new File(SUCCESS_URL);
    private static final Media successMedia = new Media(SUCCESS_FILE.toURI().toString());

    private static final File ERROR_FILE = new File(ERROR_URL);
    private static final Media errorMedia = new Media(ERROR_FILE.toURI().toString());

    public void startThemePlayer() {
        themePlayer.setOnEndOfMedia(() -> {
            themePlayer.seek(Duration.ZERO);
        });
        themePlayer.setVolume(0.2);
        themePlayer.play();
    }

    public void playError() {
        MediaPlayer errorPlayer = new MediaPlayer(errorMedia);
        errorPlayer.play();
    }

    public void playSuccess() {
        MediaPlayer successPlayer = new MediaPlayer(successMedia);
        successPlayer.play();
    }
}
