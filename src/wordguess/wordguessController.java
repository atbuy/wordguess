package wordguess;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileNotFoundException;
import java.io.File;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

enum TextError {
    IncorrectLength,
    IncorrectLetters,
    NotAWord,
    WordAlreadyFound,
    TooManyUsesOfSameLetter,
    NoError,
}

class Wordlist {

    public List<String> words = new ArrayList<>();

    public Wordlist() {
        try {
            // Open file and setup scanner
            File file = new File("./src/wordguess/static/wordlist_en.txt");

            // Read all words from text file and append them to public variable
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine()) {
                    String word = reader.nextLine();
                    words.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find wordlist file.");
        }
    }
}

public class wordguessController implements Initializable {

    // Define all labels in the left grid by their ID
    @FXML
    private Label label_0;
    @FXML
    private Label label_1;
    @FXML
    private Label label_2;
    @FXML
    private Label label_3;
    @FXML
    private Label label_4;
    @FXML
    private Label label_5;
    @FXML
    private Label label_6;
    @FXML
    private Label label_7;
    @FXML
    private Label label_8;
    @FXML
    private Label label_9;
    @FXML
    private Label label_10;
    @FXML
    private Label label_11;
    @FXML
    private Label label_12;
    @FXML
    private Label label_13;
    @FXML
    private Label label_14;
    @FXML
    private Label label_15;
    @FXML
    private Label label_16;
    @FXML
    private Label label_17;
    @FXML
    private Label label_18;
    @FXML
    private Label label_19;
    @FXML
    private Label label_20;
    @FXML
    private Label label_21;
    @FXML
    private Label label_22;
    @FXML
    private Label label_23;
    @FXML
    private Label label_24;
    @FXML
    private Label label_25;
    @FXML
    private Label label_26;
    @FXML
    private Label label_27;
    @FXML
    private Label label_28;
    @FXML
    private Label label_29;

    @FXML
    private TextArea textArea;
    @FXML
    private Label scoreLabel;
    @FXML
    private TableView<Words> wordTable;
    @FXML
    private Label statusLabel;
    @FXML
    private Label timerLabel;

    int score = 0;
    int currentDuration = 0;
    Map<String, Label> labelMap;

    Game game = new Game();
    Sounds sounds = new Sounds();
    CharacterGrid characterGrid;

    private void throwInvalidTextError(TextError error) {
        if (error != null) {
            switch (error) {
                case IncorrectLength:
                    statusLabel.setText("Error: Invalid character length");
                    break;
                case IncorrectLetters:
                    statusLabel.setText("Error: Invalid characters in word");
                    break;
                case NotAWord:
                    statusLabel.setText("Error: Not a valid english word");
                    break;
                case WordAlreadyFound:
                    statusLabel.setText("Error: Word already found");
                    break;
                case TooManyUsesOfSameLetter:
                    statusLabel.setText("Error: Too many uses of the same letter");
                    break;
                default:
                    break;
            }

            statusLabel.setTextFill(Color.color(0.941, 0.298, 0.254));
            sounds.playError();
        }
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        handleKeyboardEnter();
    }

    @FXML
    private void handleReloadButtonAction(ActionEvent event) {
        // Clear text area input
        textArea.setText("");

        // Reset score
        score = 0;
        updateScore(0);

        // Remove words from the table
        wordTable.getItems().clear();

        // Remove any notifications already shown
        statusLabel.setText("");

        // Reset timer to 0 seconds
        currentDuration = 0;
        timerLabel.setText("Time: 00:00");

        // Reload objects
        game.reload();
        characterGrid.reload();
    }

    private void clearTextArea() {
        textArea.setText("");
    }

    private void updateScore(int value) {
        score += value;
        scoreLabel.setText(String.format("Score: %d", score));
    }

    private void successMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setTextFill(Color.color(0.298, 0.686, 0.313));
    }

    public void handleKeyboardEnter() {
        // Parse word and validate it against the current characters
        String word = textArea.getText().toLowerCase().trim();
        TextError result = game.validWord(word, characterGrid.currentCharacters);

        // The input had some error, so we need to display it
        if (result != TextError.NoError) {
            throwInvalidTextError(result);
            return;
        }

        // Word has been found, update UI
        clearTextArea();
        updateScore(1);
        successMessage("Word found!");

        // Scroll to last word in the table
        int lastIndex = wordTable.getItems().size() - 1;
        wordTable.scrollTo(lastIndex);

        sounds.playSuccess();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelMap = new HashMap<String, Label>() {
            {
                put("label_0", label_0);
                put("label_1", label_1);
                put("label_2", label_2);
                put("label_3", label_3);
                put("label_4", label_4);
                put("label_5", label_5);
                put("label_6", label_6);
                put("label_7", label_7);
                put("label_8", label_8);
                put("label_9", label_9);
                put("label_10", label_10);
                put("label_11", label_11);
                put("label_12", label_12);
                put("label_13", label_13);
                put("label_14", label_14);
                put("label_15", label_15);
                put("label_16", label_16);
                put("label_17", label_17);
                put("label_18", label_18);
                put("label_19", label_19);
                put("label_20", label_20);
                put("label_21", label_21);
                put("label_22", label_22);
                put("label_23", label_23);
                put("label_24", label_24);
                put("label_25", label_25);
                put("label_26", label_26);
                put("label_27", label_27);
                put("label_28", label_28);
                put("label_29", label_29);
            }
        };

        // Setup character grid
        characterGrid = new CharacterGrid(labelMap);
        characterGrid.setup();

        // Link found words table with wordlist
        wordTable.setItems(game.wordlist);

        // Handle ENTER keypress on text area
        textArea.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                handleKeyboardEnter();
                keyEvent.consume();
            }
        });

        // Configure column for found words table
        TableColumn<Words, String> wordColumn = new TableColumn<>("Words Found");
        wordColumn.setMinWidth(wordTable.getWidth());
        wordColumn.setCellValueFactory(cellData -> cellData.getValue().wordProperty());
        wordTable.getColumns().add(wordColumn);
        wordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create timer to update time label every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentDuration += 1;
                    int minutes = currentDuration / 60;
                    int seconds = currentDuration % 60;
                    String timed = String.format("Time: %02d:%02d", minutes, seconds);
                    timerLabel.setText(timed);
                });

            }
        }, 1000, 1000);
    }
}
