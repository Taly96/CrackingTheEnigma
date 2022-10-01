package enigma.machine.reflector;


import java.io.Serializable;

public class Reflector implements Serializable {
    private String ID = "";

    private int[] reflections = null;

    public  Reflector(String inputID, int numOfReflections){
        this.ID = inputID;
        this. reflections = new int[numOfReflections * 2];
    }

    public void addReflection(int input, int output){
        this.reflections[input - 1] = input - 1;
        this.reflections[output - 1] = input - 1;
    }

    public int reflect (int input){
        int toReflect = this.reflections[input];
        int index = -1;

        for (int i = 0; i < this.reflections.length; i++){
            if (this.reflections[i] == toReflect && input != i ){
                index = i;
            }
        }

        return index;
    }

    public String getID() {
        return this.ID;
    }
}
