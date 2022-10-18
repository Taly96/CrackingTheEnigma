package dm.assignment;

import dto.assignment.AssignmentDTO;
import dto.candidates.CandidatesInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.staticinfo.StaticMachineDTO;
import machine.EnigmaMachine;
import machine.codeconfiguration.CodeConfiguration;

import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;

import static dm.utils.Utils.fromBytesArrayToObject;
import static dm.utils.Utils.fromObjectToByteArray;

public class Assignment implements Runnable {

    private AssignmentDTO assignments = null;

    private BlockingQueue<CandidatesInfo> candidatesInfo = null;

    private EnigmaMachine machine = null;

    private StaticMachineDTO staticInventory = null;

    private String messageToProcess = null;

    private Integer[] indexInAbc = null;
    public Assignment(
            AssignmentDTO assignmentInfo,
            BlockingQueue<CandidatesInfo> candidates,
            byte[] serMachine,
            StaticMachineDTO staticInventory,
            String messageToProcess
    ) {
        this.assignments = assignmentInfo;
        this.candidatesInfo = candidates;
        this.machine = (EnigmaMachine) fromBytesArrayToObject(serMachine);
        this.staticInventory = staticInventory;
        this.machine.setABC(this.staticInventory.getAbc());
        this.messageToProcess = messageToProcess;
    }

    @Override
    public void run() {
        boolean isCandidate = true;
        StringBuilder res = new StringBuilder();
        String positions = this.assignments.getStartingPoint();
        Integer numOfRotors = this.assignments.getNumOfRotors();
        this.indexInAbc = new Integer[numOfRotors];
        String  abc = this.staticInventory.getAbc();
        Integer index = 0;

        for(Character pos : this.assignments.getStartingPoint().toCharArray()){
            this.indexInAbc[index] = abc.indexOf(pos);
        }
        while(positions.equals(this.assignments.getFinishPoint())){
            CodeConfiguration knownComponents =
                    (CodeConfiguration) fromBytesArrayToObject(
                            this.assignments.getKnownComponents()
                    );
            knownComponents.setRotorsPositions(positions);
            CodeConfigInfo configInfo =
                    this.machine.setCodeConfig(knownComponents);
            String processedWords =
                    this.machine.process(this.messageToProcess);

            String word = "";
            StringTokenizer st = new StringTokenizer(processedWords, " ");
            while(st.hasMoreTokens()){
                word = st.nextToken();
                if (!this.staticInventory.getWords().contains(word)) {
                    isCandidate = false;
                    break;
                } else {
                    res.append(word + " ");
                }
            }

            if (isCandidate && !res.equals("")) {
                this.addCandidate(res, configInfo);
            }
            isCandidate = true;
            res.delete(0, res.length());
            positions = this.getNextPos(abc);
        }


    }

    private String getNextPos(String abc) {

        Integer carry = 1;
        String res = "";
        for(Integer index: this.indexInAbc){
            this.indexInAbc[index] += carry;
            carry =this.indexInAbc[index]/abc.length();
            if(carry != 0){
                this.indexInAbc[index] = this.indexInAbc[index] % abc.length();
            }
            res += abc.charAt(index);
        }

        return res;
    }

    private void addCandidate(StringBuilder res, CodeConfigInfo codeConfigDTO) {
        try {
            this.candidatesInfo.put(
                    new CandidatesInfo(
                            res.toString().trim(),
                            Thread.currentThread().getName(),
                            fromObjectToByteArray(codeConfigDTO)
                    )
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
