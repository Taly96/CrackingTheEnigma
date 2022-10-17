import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ !?'";
        int abcSize = 30;
        int sizeOfMission = 30;
        int counter = 0;
        int numOfRotors = 3;
        double maxValue = Math.pow(abcSize, numOfRotors);
        boolean haMore = true;
        String sPos = "";
        int[] positions = new int[]{0,0,0};
        //int i = 0;
        //List<Integer> rotorsPos = new ArrayList<>(numOfRotors);


        for (Integer index : positions) {
            sPos+=abc.charAt(index);
        }
        System.out.println(sPos);
        sPos = "";

        while(haMore) {
            Integer pos = positions[0] + sizeOfMission;
            Integer nextCarry = pos / abcSize;
            if(nextCarry != 0){
                positions[0] = pos % abcSize;
            }
            else{
                positions[0] = pos;
            }
            for (int i = 1; i < numOfRotors; i++) {
                pos = positions[i] + nextCarry;
                nextCarry = pos / abcSize;
                if(nextCarry != 0){
                    if(i == numOfRotors - 1){
                        haMore = false;
                        positions = new int[]{abcSize -1, abcSize - 1, abcSize - 1};
                    }
                    else {
                        positions[i] = pos % abcSize;
                    }
                }
                else{
                    positions[i] = pos;
                }
            }

            for (Integer index : positions) {
                sPos+=abc.charAt(index);
            }
            System.out.println(sPos);
            sPos = "";
        }
    }

}
