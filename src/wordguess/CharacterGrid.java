package wordguess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javafx.scene.control.Label;

public class CharacterGrid implements GameObject {
    private static Random random = new Random();
    public static final int characterAmount = random.nextInt(2) + 5;

    // Holds all the accepted characters for the current round
    private Map<String, Label> labelMap;
    public HashMap<String, Integer> currentCharacters = new HashMap<String, Integer>();

    public CharacterGrid(Map<String, Label> labelMap) {
        this.labelMap = labelMap;
    }

    public HashMap<String, Label> getLabelMap(HashMap<String, Label> labelMap) {
        return labelMap;
    }

    public void setLabelMap(HashMap<String, Label> labelMap) {
        this.labelMap = labelMap;
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

    public void display(Map<String, Label> map, String[] characters) {
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

    @Override
    public void setup() {
        // Display random characters on the character grid
        String[] characters = getRandomCharacters();

        // Append character to accepted characters map.
        // The map also contains the times the character can be used in a guess.
        for (int i = 0; i < characters.length; i++) {
            String character = characters[i];
            if (currentCharacters.containsKey(character)) {
                int currentAmount = currentCharacters.get(character);
                currentCharacters.put(character, currentAmount + 1);
            } else {
                currentCharacters.put(character, 1);
            }
        }

        // Show character on the grid
        display(labelMap, characters);
    }

    @Override
    public void clear() {
        /// Clear all characters from the grid

        for (Map.Entry<String, Label> entry : labelMap.entrySet()) {
            Label label = entry.getValue();
            label.setText("");
        }

        // Clear accepted characters to prepare for the next round
        currentCharacters.clear();
    }

    @Override
    public void reload() {
        // Clear the grid and setup a new round
        clear();
        setup();
    }

}
