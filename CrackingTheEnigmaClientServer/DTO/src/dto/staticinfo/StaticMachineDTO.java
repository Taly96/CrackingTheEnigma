package dto.staticinfo;

import java.io.Serializable;
import java.util.Set;

public class StaticMachineDTO implements Serializable {

    private Set<String> words = null;

    private String abc = null;

    private String wordsToExclude = null;

    public StaticMachineDTO(
            String abc,
            Set<String> words,
            String wordsToExclude
    ){
        this.abc = abc;
        this.words = words;
        this.wordsToExclude = wordsToExclude;
    }

    public Set<String> getWords() {
        return this.words;
    }

    public String getAbc() {
        return this.abc;
    }

    public String getWordsToExclude() {
        return this.wordsToExclude;
    }
}
