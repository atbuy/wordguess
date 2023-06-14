package wordguess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Game implements GameObject {
    // Keep track of the found words for each round
    public ObservableList<Words> wordlist = FXCollections.observableArrayList();
    public List<String> foundWords = new ArrayList<>();

    // Load english wordlist
    List<String> words = new Wordlist().words;

    public TextError validWord(String word, HashMap<String, Integer> currentCharacters) {
        // Check if word is a valid length
        if (word.length() > CharacterGrid.characterAmount) {
            return TextError.IncorrectLength;
        }

        // Check if the word has only the accepted letters
        // and that the letters are not used more times than they appear in the grid
        HashMap<String, Integer> currentCharactersCopy = new HashMap<String, Integer>(currentCharacters);
        for (int i = 0; i < word.length(); i++) {
            String character = String.valueOf(word.charAt(i));

            // The character is not in the grid
            if (!currentCharactersCopy.containsKey(character)) {
                return TextError.IncorrectLetters;
            }

            // Check if the character has been used more times than it appears in the grid
            int currentAmount = currentCharactersCopy.get(character);
            if (currentAmount == 0) {
                return TextError.TooManyUsesOfSameLetter;
            }

            // Update the amount of times the character can be used
            currentCharactersCopy.put(character, currentAmount - 1);
        }

        // Check if the word is a valid english word
        if (!words.contains(word)) {
            return TextError.NotAWord;
        }

        // Check if the word has already been found
        if (foundWords.contains(word)) {
            return TextError.WordAlreadyFound;
        }

        // Word has been found, save it in memory
        foundWords.add(word);
        wordlist.add(new Words(word));
        return TextError.NoError;
    }

    public void setup() {

    }

    public void clear() {
        // Clear the found words list
        foundWords.clear();
    }

    public void reload() {
        // Clear the found words list and setup a new round
        clear();
        setup();
    }
}
