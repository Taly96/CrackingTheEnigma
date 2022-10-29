package dm.assignment;

import dto.assignment.AssignmentDTO;
import dto.candidates.CandidatesInfo;
import dto.codeconfig.CodeConfigInfo;
import dto.staticinfo.StaticMachineDTO;
import machine.EnigmaMachine;
import machine.codeconfiguration.CodeConfiguration;

import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static dm.utils.Utils.createStringCodeConfig;
import static dm.utils.Utils.fromBytesArrayToObject;

public class Assignment implements Runnable {

    private AssignmentDTO assignments = null;

    private BlockingQueue<CandidatesInfo> candidatesInfo = null;

    private EnigmaMachine machine = null;

    private StaticMachineDTO staticInventory = null;

    private String messageToProcess = null;

    private Integer[] indexInAbc = null;

    private CountDownLatch countDownLatch = null;

    private AtomicInteger totalAssignmentsWorkedOn = null;

    private String allyName = null;

    private AtomicLong candidatesFound = null;

    public Assignment(
            StaticMachineDTO staticMachineInventory,
            AssignmentDTO assignmentInfo,
            BlockingQueue<CandidatesInfo> candidates,
            String messageToProcess,
            byte[] serMachine,
            CountDownLatch countDownLatch,
            AtomicInteger totalAssignmentsWorkedOn,
            String alliesTeam,
            AtomicLong candidatesFound
    ) {
        this.assignments = assignmentInfo;
        this.candidatesInfo = candidates;
        this.machine = (EnigmaMachine) fromBytesArrayToObject(serMachine);
        this.messageToProcess = messageToProcess;
        this.countDownLatch = countDownLatch;
        this.staticInventory = staticMachineInventory;
        this.totalAssignmentsWorkedOn = totalAssignmentsWorkedOn;
        this.allyName = alliesTeam;
        this.candidatesFound = candidatesFound;
    }

    @Override
    public void run() {
        System.out.println("Working on " + messageToProcess);
        boolean isCandidate = true;
        StringBuilder res = new StringBuilder();
        String positions = this.assignments.getStartingPoint();
        Integer numOfRotors = this.assignments.getNumOfRotors();
        this.indexInAbc = new Integer[numOfRotors];
        String  abc = this.staticInventory.getAbc();
        boolean isDone = false;
        this.machine.setABC(abc);
        Integer index = 0;

        for(Character pos : this.assignments.getStartingPoint().toCharArray()){
            this.indexInAbc[index] = abc.indexOf(pos);
            index++;
        }

        while(!isDone){
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
                this.candidatesFound.incrementAndGet();
            }
            if(positions.equals(this.assignments.getFinishPoint())) {
                isDone = true;
            }
            isCandidate = true;
            res.delete(0, res.length());
            if(!isDone){
                positions = this.getNextPos(abc);
            }
        }
        this.totalAssignmentsWorkedOn.incrementAndGet();
        this.countDownLatch.countDown();
    }

    private String getNextPos(String abc) {
        int carry = 1;
        StringBuilder res = new StringBuilder();

        for(int i = 0; i < this.indexInAbc.length; i++){
            this.indexInAbc[i] += carry;
            carry =this.indexInAbc[i]/abc.length();
            if(carry != 0){
                this.indexInAbc[i] = this.indexInAbc[i] % abc.length();
            }
            res.append(abc.charAt(indexInAbc[i]));
        }

        return res.toString();
    }

    private void addCandidate(StringBuilder res, CodeConfigInfo codeConfigInfo) {
        try {
            String codeConfig =
                    createStringCodeConfig(codeConfigInfo);
            this.candidatesInfo.put(
                    new CandidatesInfo(
                            res.toString().trim(),
                            this.allyName,
                            codeConfig
                    )
            );
            System.out.println("Added candidate " + res.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
