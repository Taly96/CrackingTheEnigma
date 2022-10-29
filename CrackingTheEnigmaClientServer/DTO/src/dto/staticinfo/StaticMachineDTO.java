package dto.staticinfo;

import java.io.Serializable;
import java.util.Set;

public class StaticMachineDTO implements Serializable {

    private Set<String> words = null;

    private String abc = null;

    public StaticMachineDTO(
            String abc,
            Set<String> words
    ){
        this.abc = abc;
        this.words = words;
    }

    public Set<String> getWords() {
        return this.words;
    }

    public String getAbc() {
        return this.abc;
    }

    public void clear() {
        this.words.clear();
        this.abc = "";
    }
}
