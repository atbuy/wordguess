package wordguess;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Words {
    private final StringProperty word = new SimpleStringProperty(this, "word");

    public final void setWord(String value) {
        this.word.set(value);

    }

    public final String getWord() {
        return word.get();
    }

    public final StringProperty wordProperty() {
        return word;
    }

    public Words() {
    }

    public Words(String word) {
        setWord(word);
    }
}
