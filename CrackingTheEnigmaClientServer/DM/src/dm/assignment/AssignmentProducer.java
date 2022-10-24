package dm.assignment;

import dm.utils.Permuter;
import dto.assignment.AssignmentDTO;
import dto.decipher.DecipherDTO;
import dm.utils.Utils;
import machine.codeconfiguration.CodeConfiguration;
import machine.inventory.MachineInventory;
import machine.reflector.Reflector;
import machine.rotor.Rotor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AssignmentProducer implements Runnable {
    private BlockingQueue<AssignmentDTO> assignments = null;

    private String level = null;

    private BigDecimal assignmentSize = null;

    private DecipherDTO info = null;

    private MachineInventory inventory = null;

    private AtomicBoolean hasMoreAssignments = null;

    private boolean hasMoreStartingPoints = true;

    private BigDecimal counter = null;

    private BigDecimal maxValue = null;

    private int[] startingPos = null;

    private AtomicReference<BigDecimal> createdAssignments = null;

    private Object queueLock = null;

    public AssignmentProducer(
            AtomicReference<BigDecimal> createdAssignments,
            BlockingQueue<AssignmentDTO> assignmentsQueue,
            String level,
            BigDecimal assignmentSize,
            DecipherDTO info,
            AtomicBoolean hasMoreAssignments,
            Object queueLock){
        this.createdAssignments = createdAssignments;
        this.assignments = assignmentsQueue;
        this.level = level;
        this.assignmentSize = assignmentSize;
        this.hasMoreAssignments = hasMoreAssignments;
        this.info = info;
        this.inventory =
                (MachineInventory) Utils.fromBytesArrayToObject(
                        this.info.getMachineInventory()
                );
        this.counter = new BigDecimal(0);
        this.maxValue = BigDecimal.valueOf(Math.pow(this.inventory.getStaticMachineInfo().getAbc().length(),
                this.inventory.getRotorsCount()
        ));
        this.queueLock = queueLock;
    }

    @Override
    public void run() {

        switch (level){
            case "Easy": {
                this.goEasy();
                break;
            }
            case "Medium": {
                this.goALittleHarder();
                break;
            }
            case "Hard": {
                this.goFullPower();
                break;
            }
            case "Impossible": {
                this.thisIsSparta();
                break;
            }
            default :{
                this.hasMoreAssignments.set(false);
                break;
            }
        }
        this.hasMoreAssignments.set(false);
    }

    private void thisIsSparta() {
        int numberOfAvailableRotors = this.inventory.getAvailableRotors().size();
        int numberOfNeededRotors = this.inventory.getRotorsCount();
        List<int[]> possibleRotorsPermutations = this.generateRotorsOrder(numberOfAvailableRotors, numberOfNeededRotors);

        for(int[] rotorsIDs : possibleRotorsPermutations){
            CodeConfiguration assignment = new CodeConfiguration();
            List<Rotor> rotorsForAssignment = new ArrayList<>();

            for(Integer rotorID : rotorsIDs){
                rotorsForAssignment.add(this.inventory.getRotor(rotorID + 1));
            }

            assignment.setRotorsOrder(rotorsForAssignment);
            this.info.setKnownComponents(Utils.fromObjectToByteArray(assignment));
            this.goFullPower();
        }
    }

    private void goFullPower() {
        CodeConfiguration knownComponents =
                (CodeConfiguration) Utils.fromBytesArrayToObject(
                        this.info.getKnownComponents()
                );
        int numberOfRotors = this.inventory.getRotorsCount();
        int[] rotorsOrder = new int[numberOfRotors];
        int index = 0;

        for(Rotor rotor : knownComponents.getRotorsOrder()){
            rotorsOrder[index] = rotor.getID();
            index++;
        }

        Permuter permuter = new Permuter(numberOfRotors);
        int[] permutation = permuter.getNext();

        while (permutation != null){
            List<Rotor> rotorsOrderForAssignment = new ArrayList<>();

            for(int i = 0; i < permutation.length; i++){
                rotorsOrderForAssignment.add(
                        this.inventory.getRotor(rotorsOrder[permutation[i]])
                );
            }

            CodeConfiguration assignment = new CodeConfiguration();
            assignment.setRotorsOrder(rotorsOrderForAssignment);
            this.info.setKnownComponents(Utils.fromObjectToByteArray(assignment));
            goALittleHarder();
            permutation = permuter.getNext();
        }
    }

    private void goALittleHarder() {
        CodeConfiguration assignment =
                (CodeConfiguration) Utils.fromBytesArrayToObject(
                        this.info.getKnownComponents()
                );

        for (Reflector reflector : this.inventory.getAvailableReflectors()) {
            assignment.setReflector(reflector);
            this.info.setKnownComponents(Utils.fromObjectToByteArray(assignment));
            this.goEasy();
            this.hasMoreStartingPoints = true;
            this.counter = BigDecimal.ZERO;
        }
    }

    private void addAssignmentToQueue(AssignmentDTO newAssignment) {
        synchronized (queueLock) {
            try {
                this.assignments.put(newAssignment);
                this.createdAssignments.set(this.createdAssignments.get().add(BigDecimal.ONE));
                System.out.println("Added assignment : " + newAssignment .getStartingPoint() + " " + newAssignment.getFinishPoint());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void goEasy() {
        System.out.println("Going easy.");
        int numOfRotors = this.inventory.getRotorsCount();
        this.startingPos = new int[numOfRotors];
        for(int i = 0; i < numOfRotors; i++){
            this.startingPos[i] = 0;
        }
        while (this.hasMoreStartingPoints) {
            this.addAssignmentToQueue(this.createAssignment(numOfRotors));
        }

        System.out.println("Done");
    }

    private AssignmentDTO createAssignment(Integer numOfRotors) {
        String abc = this.inventory.getStaticMachineInfo().getAbc();
        int abcSize = abc.length();

        String [] positions = new String[] {"",""};

        for(Integer index : this.startingPos){
            positions[0] += abc.charAt(index);
        }

        positions[1] = this.getNextPos(numOfRotors, abcSize, this.assignmentSize, abc);
        this.getNextPos(numOfRotors, abcSize, BigDecimal.ONE, abc);

        System.out.println("Start: " + positions[0] + " End: " + positions[1]);
        return new AssignmentDTO(
                positions[0],
                positions[1],
                this.info.getKnownComponents(),
                numOfRotors
        );
    }

    private String getNextPos(Integer numOfRotors, int abcSize, BigDecimal assignmentSize, String abc) {
        BigDecimal pos = assignmentSize.add(BigDecimal.valueOf(this.startingPos[0]));
        Integer nextCarry = pos.divide(BigDecimal.valueOf(abcSize),RoundingMode.FLOOR).intValue();
        String res = "";

        if(nextCarry != 0){
            this.startingPos[0] = pos.remainder(BigDecimal.valueOf(abcSize)).intValue();
        }
        else{
            this.startingPos[0] = pos.intValue();
        }
        for (int i = 1; i < numOfRotors; i++) {
            pos = BigDecimal.valueOf(this.startingPos[i] + nextCarry);
            nextCarry = pos.divide(BigDecimal.valueOf(abcSize), RoundingMode.FLOOR).intValue();
            if(nextCarry != 0){
                if(i == numOfRotors - 1){
                    this.hasMoreStartingPoints = false;
                    this.startingPos = new int[]{abcSize -1, abcSize - 1, abcSize - 1};
                }
                else {
                    this.startingPos[i] = pos.remainder(BigDecimal.valueOf(abcSize)).intValue();
                }
            }
            else{
                this.startingPos[i] = pos.intValue();
            }
        }

        for(Integer index : this.startingPos){
            res += abc.charAt(index);
        }

        return res;
    }

    private List<int[]> generateRotorsOrder(int availableRotors, int neededRotors) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[neededRotors];

        for (int i = 0; i < neededRotors; i++) {
            combination[i] = i;
        }

        while (combination[neededRotors - 1] < availableRotors) {
            combinations.add(combination.clone());
            int t = neededRotors - 1;
            while (t != 0 && combination[t] == availableRotors - neededRotors + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < neededRotors; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }
}