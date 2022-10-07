package dto.loadedmachine;

import dto.battlefield.BattleFieldDTO;
import dto.staticinfo.StaticMachineDTO;

import java.util.List;

public class LoadedMachineDTO {

    private String abc = null;

    private int rotorsCount = 0;


    private List<Integer> availableRotors = null;


    private List<String> availableReflectors = null;

    private BattleFieldDTO battleFieldDTO = null;

    private StaticMachineDTO staticMachineDTO = null;

    public LoadedMachineDTO(
            String abc,
            int rotorsCount,
            List<Integer> availableRotors,
            List<String> availableReflectors,
            BattleFieldDTO battleFieldDTO,
            StaticMachineDTO staticMachineDTO
    ){
        this.abc = abc;
        this.availableReflectors =  availableReflectors;
        this.availableRotors = availableRotors;
        this.rotorsCount = rotorsCount;
        this.battleFieldDTO = battleFieldDTO;
        this.staticMachineDTO = staticMachineDTO;
    }

    public String getAbc() {
        return this.abc;
    }

    public int getRotorsCount() {
        return this.rotorsCount;
    }

    public List<Integer> getAvailableRotors() {
        return this.availableRotors;
    }

    public List<String> getAvailableReflectors() {
        return this.availableReflectors;
    }

    public BattleFieldDTO getBattleFieldDTO() {
        return this.battleFieldDTO;
    }

    public StaticMachineDTO getStaticMachineDTO() {
        return this.staticMachineDTO;
    }
}
