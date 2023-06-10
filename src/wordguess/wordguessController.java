package wordguess;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileNotFoundException;
import java.io.File;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    Random random = new Random();
    int currentDuration = 0;
    int score = 0;
    int characterAmount = random.nextInt(4) + 4;
    int[] currentPositions = new int[characterAmount];
    Map<String, Label> labelMap;
    List<String> currentCharacters = new ArrayList<>();
    ObservableList<Words> wordlist = FXCollections.observableArrayList();
    List<String> foundWords = new ArrayList<>();

    // Load words from wordlist file
    List<String> words = new Wordlist().words;

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
                default:
                    break;
            }

            statusLabel.setTextFill(Color.color(0.941, 0.298, 0.254));
        }
    }

    private String[] getRandomCharacters() {
        // Select 2 to 3 random vowels
        String[] vowels = { "a", "e", "i", "o", "u" };
        int vowelAmount = random.nextInt(2) + 2;
        String[] selectedVowels = new String[vowelAmount];
        for (int i = 0; i < vowelAmount; i++) {
            int randomIndex = random.nextInt(vowels.length);
            selectedVowels[i] = vowels[randomIndex];
        }

        // Select the rest of the characters as consonants
        String[] consonants = {
                "b", "c", "d", "f", "g",
                "h", "j", "k", "l", "m",
                "n", "p", "q", "r", "s",
                "t", "v", "w", "x", "z"
        };
        int consonantAmount = characterAmount - vowelAmount;
        String[] selectedConsonants = new String[consonantAmount];
        for (int i = 0; i < consonantAmount; i++) {
            int randomIndex = random.nextInt(consonants.length);
            selectedConsonants[i] = consonants[randomIndex];
        }

        // Merge vowels and consonants into one array
        String[] selectedCharacters = new String[characterAmount];
        System.arraycopy(selectedVowels, 0, selectedCharacters, 0, vowelAmount);
        System.arraycopy(selectedConsonants, 0, selectedCharacters, vowelAmount, consonantAmount);
        return selectedCharacters;
    }

    private void displayCharacters(Map<String, Label> map, String[] characters) {
        // Generate random positions without duplicates.
        // Using HashSet, because order doesn't matter in this case.
        Set<Integer> positions = new HashSet<>();
        while (positions.size() < characterAmount) {
            positions.add(random.nextInt(30));
        }

        // Convert HashSet to array
        Integer[] pos = positions.toArray(new Integer[0]);

        // Display characters on the grid
        for (int i = 0; i < characterAmount; i++) {
            // Get label ID by position
            String label = String.format("label_%d", pos[i]);

            // Get label object and display character
            Label labelObject = map.get(label);
            labelObject.setText(characters[i]);
        }
    }

    private void clearCharacterGrid(Map<String, Label> map) {
        /// Clear all characters from the grid

        for (Map.Entry<String, Label> entry : map.entrySet()) {
            Label label = entry.getValue();
            label.setText("");
        }
    }

    private void setupCharacterGrid(Map<String, Label> map) {
        // Display random characters on the character grid
        String[] characters = getRandomCharacters();

        // Append character to accepted characters list
        for (int i = 0; i < characters.length; i++) {
            try {
                currentCharacters.set(i, characters[i]);
            } catch (IndexOutOfBoundsException e) {
                currentCharacters.add(characters[i]);
            }
        }

        // Show character on the grid
        displayCharacters(map, characters);
    }

    private void clearNotificationLabel() {
        statusLabel.setText("");
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        handleKeyboardEnter();
    }

    @FXML
    private void handleReloadButtonAction(ActionEvent event) {
        characterAmount = random.nextInt(3) + 3;
        clearCharacterGrid(labelMap);
        setupCharacterGrid(labelMap);
        clearTextArea();
        resetScore();
        clearScrollGrid();
        clearNotificationLabel();
        resetTimer();
    }

    private void clearScrollGrid() {
        // Clear scroll pane
        wordTable.getItems().clear();
    }

    private void resetTimer() {
        // Reset timer
        currentDuration = 0;
        timerLabel.setText("Time: 00:00");
    }

    private void resetScore() {
        // Reset score for new words
        score = 0;
        updateScore(0);
    }

    private void clearTextArea() {
        textArea.setText("");
    }

    private void updateScore(int value) {
        score += value;
        scoreLabel.setText(String.format("Score: %d", score));
    }

    private void addWordToFoundWords(String word) {
        wordlist.add(new Words(word));
        foundWords.add(word);
    }

    private void successMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setTextFill(Color.color(0.298, 0.686, 0.313));
    }

    public void handleKeyboardEnter() {
        // Parse text
        String text = textArea.getText().trim();

        // Check if word is a valid length
        if (text.length() != characterAmount) {
            throwInvalidTextError(TextError.IncorrectLength);
            return;
        }

        // Check if the word has only the accepted letters
        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));
            if (!currentCharacters.contains(character)) {
                throwInvalidTextError(TextError.IncorrectLetters);
                return;
            }
        }

        // Check if the word is a valid english word
        if (!words.contains(text)) {
            throwInvalidTextError(TextError.NotAWord);
            return;
        }

        // Check if the word has already been found
        if (foundWords.contains(text)) {
            throwInvalidTextError(TextError.WordAlreadyFound);
            return;
        }

        // Word has been found, update UI
        addWordToFoundWords(text);
        clearTextArea();
        updateScore(1);
        successMessage("Word found!");
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

        // Display random characters on the left grid
        setupCharacterGrid(labelMap);

        // Handle ENTER keypress on text area
        textArea.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                handleKeyboardEnter();
                keyEvent.consume();
            }
        });

        // Configure table view
        wordTable.setItems(wordlist);

        // Configure column for words found
        TableColumn<Words, String> wordColumn = new TableColumn<>("Words Found");
        wordColumn.setMinWidth(wordTable.getWidth());
        wordColumn.setCellValueFactory(cellData -> cellData.getValue().wordProperty());
        wordTable.getColumns().add(wordColumn);
        wordTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create timer to update time label every second
        Timer timer = new Timer();
        System.out.println(timerLabel);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    currentDuration += 1;
                    int minutes = currentDuration / 60;
                    int seconds = currentDuration % 60;
                    String timed = String.format("Time: %02d:%02d", minutes, seconds);
                    System.out.println(timed);
                    timerLabel.setText(timed);
                });

            }
        }, 1000, 1000);
    }
}
