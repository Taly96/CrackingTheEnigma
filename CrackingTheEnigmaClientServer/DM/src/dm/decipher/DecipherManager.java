package dm.decipher;

import dm.assignment.AssignmentProducer;
import dto.assignment.AssignmentDTO;
import dto.decipher.DecipherDTO;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DecipherManager {

    private BigDecimal totalNumberOfAssignments  = null;

    private BigDecimal createdAssignments = null;

    private BigDecimal sizeOfAssignment = null;

    private AssignmentProducer assignmentProducer = null;

    private BlockingQueue<AssignmentDTO> assignments = null;

    private Thread assignmentProdThread = null;

    private AtomicBoolean hasMoreAssignments = null;

    private String level = null;

    //private DecipherDTO decipherDTO = null;

    public DecipherManager(String level, String assignmentSize, String totalAssignments){
        this.assignments = new LinkedBlockingQueue<>(1000);
        this.createdAssignments = new BigDecimal(0);
        this.hasMoreAssignments = new AtomicBoolean(true);
        this.totalNumberOfAssignments = new BigDecimal(totalAssignments);
        this.sizeOfAssignment = new BigDecimal(assignmentSize);
        this.level = level;
    }

    public void startProducingAssignments(
            BigDecimal totalNumberOfAssignments,
            BigDecimal sizeOfAssignment,
            DecipherDTO decipherDTO
    ){
        this.sizeOfAssignment = sizeOfAssignment;
        this.totalNumberOfAssignments = totalNumberOfAssignments;
        this.assignmentProdThread =
                new Thread(
                        new AssignmentProducer(
                                this.assignments,
                                this.level,
                                this.sizeOfAssignment,
                                decipherDTO,
                                this.hasMoreAssignments
                        )
                );
        this.assignmentProdThread.setName("Assignment Producer");
        this.assignmentProdThread.start();
    }
}