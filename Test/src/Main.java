import dto.agents.AgentsDTO;
import dto.agents.AgentsInfo;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;

public class Main {

    public final static String BASE_URL = "http://localhost:8080/BattleFieldServer_Web_exploded";

    public static void main(String[] args) throws InterruptedException {
        String abc = "ABCD";
        int abcSize = 4;
        int numOfRotors = 1;
        int assignmentSize = 3;
        int[] pos = {0,0};
        boolean hasMoreStartingPoints = true;

        String positionsABC = "";
        for(Integer inABC : pos){
            positionsABC += abc.charAt(inABC);
        }
        System.out.println(positionsABC);
        positionsABC = "";

        while (hasMoreStartingPoints) {
            int index = pos[0] + assignmentSize;
            int nextCarry = index / abcSize;

            if(nextCarry != 0){
                pos[0] = index % abcSize;
            }
            else{
                pos[0] = index;
            }
//            for(Integer inABC : pos){
//                positionsABC += abc.charAt(inABC);
//            }
//            System.out.println(positionsABC);
//            positionsABC = "";

            for (int i = 1; i < numOfRotors; i++) {
                index = pos[i] + nextCarry;
                nextCarry = index / abcSize;

                if(i == numOfRotors - 1 && nextCarry != 0){
                    hasMoreStartingPoints = false;
                    pos = new int[]{abcSize -1, abcSize - 1};
                } else if(nextCarry != 0){
                    pos[i] = index % abcSize;
                }
                else{
                    pos[i] = index;
                }

                for(Integer inABC : pos){
                    positionsABC += abc.charAt(inABC);
                }
                System.out.println(positionsABC);
                positionsABC = "";
            }
        }




    }

}
