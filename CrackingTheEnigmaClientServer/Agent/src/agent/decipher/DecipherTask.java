package agent.decipher;


import dm.assignment.Assignment;
import dto.agents.AgentsInfo;
import dto.assignment.AssignmentDTO;
import dto.candidates.CandidatesInfo;
import dto.staticinfo.StaticMachineDTO;
import machine.EnigmaMachine;

import javafx.concurrent.Task;

import java.util.List;
import java.util.concurrent.*;

import static dm.utils.Utils.fromObjectToByteArray;


public class DecipherTask extends Task<Boolean> {

    private AgentsInfo agentInfo = null;

    private byte[] serMachine = null;

    private StaticMachineDTO staticInventory = null;

    private BlockingQueue<CandidatesInfo> candidates = null;

    private List<AssignmentDTO> assignmentInfo = null;

    private ExecutorService threads = null;

    private String messageToProcess = null;

    public DecipherTask(
            List<AssignmentDTO> assignmentInfo,
            StaticMachineDTO staticInventory,
            AgentsInfo agentsInfo,
            BlockingQueue<CandidatesInfo> candidates,
            String messageToProcess

    ){
        this.agentInfo = agentsInfo;
        this.assignmentInfo = assignmentInfo;
        this.staticInventory = staticInventory;
        this.threads = Executors.newFixedThreadPool(this.agentInfo.getNumberOfThreads());
        EnigmaMachine machine = new EnigmaMachine();
        this.serMachine = fromObjectToByteArray(machine);
        this.candidates = candidates;
        this.messageToProcess = messageToProcess;
    }
    @Override
    protected Boolean call() {
        for(AssignmentDTO assignmentInfo : this.assignmentInfo ){
            this.threads.execute(
                    new Assignment(
                            assignmentInfo,
                            this.candidates,
                            this.serMachine,
                            this.staticInventory,
                            this.messageToProcess
                    )
            );
        }

        return Boolean.TRUE;
    }
}
