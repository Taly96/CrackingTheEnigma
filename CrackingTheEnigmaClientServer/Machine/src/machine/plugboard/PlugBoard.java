package machine.plugboard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Serializable {
    private final Map<Character, Character> plugs = new HashMap<>();

    public boolean addPlug (char from, char to) {
        if(this.plugs.containsKey(from) || this.plugs.containsKey(to)){

            return false;
        }
        else {
            this.plugs.put(from, to);
            this.plugs.put(to, from);

            return true;
        }
    }

    public boolean isExistingPlug(char from){
        return this.plugs.containsKey(from);
    }

    public char encrypt(char from){

        return this.plugs.get(from);
    }

    public void clear() {
        plugs.clear();
    }

}
