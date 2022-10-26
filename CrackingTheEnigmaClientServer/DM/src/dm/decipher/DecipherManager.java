package dm.decipher;

import dm.assignment.AssignmentProducer;
import dto.assignment.AssignmentDTO;
import dto.assignment.AssignmentDTOList;
import dto.decipher.DecipherDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DecipherManager {

    private BigDecimal totalNumberOfAssignments  = null;

    private BigDecimal sizeOfAssignment = null;

    private AssignmentProducer assignmentProducer = null;

    private BlockingQueue<AssignmentDTO> assignments = null;

    private Thread assignmentProdThread = null;

    private AtomicBoolean hasMoreAssignments = null;

    private AtomicReference<BigDecimal> createdAssignments;

    private DecipherDTO info = null;

    private Object queueLock = null;

    private String level = null;

    public DecipherManager(
            String level,
            String assignmentSize,
            String totalAssignments,
            DecipherDTO decipherDTO
    ){
        this.queueLock = new Object();
        this.assignments = new LinkedBlockingQueue<>(1000);
        this.info = decipherDTO;
        this.createdAssignments = new AtomicReference<>(new BigDecimal(0));
        this.hasMoreAssignments = new AtomicBoolean(true);
        this.totalNumberOfAssignments = new BigDecimal(totalAssignments);
        this.sizeOfAssignment = new BigDecimal(assignmentSize);
        this.level = level;
        this.assignmentProdThread =
                new Thread(
                        new AssignmentProducer(
                                this.createdAssignments,
                                this.assignments,
                                this.level,
                                this.sizeOfAssignment,
                                decipherDTO,
                                this.hasMoreAssignments,
                                this.queueLock
                        )
                );
        this.assignmentProdThread.setName("Assignment Producer");
    }

    public synchronized void startProducingAssignments(){
        System.out.println(Thread.currentThread().getName());
        this.assignmentProdThread.start();
    }

    public synchronized String getCreatedAssignments() {

        return this.createdAssignments.get().toString();
    }

    public synchronized AssignmentDTOList getAssignments(int agentsDraw){
        List<AssignmentDTO> assignments = new ArrayList<>();
       synchronized (queueLock){
           for(int i = 0; i < agentsDraw; i++){
               AssignmentDTO assignment =
                       this.assignments.poll();
               if(assignment != null){
                   assignments.add(assignment);
                   System.out.println("added assignment " + (i+1));
               }
               else{
                   break;
               }
           }
           queueLock.notifyAll();
       }


        System.out.println("Got assignments");
       return new AssignmentDTOList(assignments, this.hasMoreAssignments.get());
    }

    public void stopProducing() {
        this.hasMoreAssignments.set(false);
        try {
            this.assignmentProdThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}