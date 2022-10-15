package assignment;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AssignmentConsumer implements Runnable {

    private BlockingQueue<CandidatesStats> candidates = null;

    private Consumer<CandidatesStats> addNewAgent = null;

    private AtomicBoolean hasMoreAssignments = null;

    public AssignmentConsumer(
            BlockingQueue<CandidatesStats> candidates,
            Consumer<CandidatesStats> addNewAgent,
            AtomicBoolean hasMoreAssignments){
        this.candidates = candidates;
        this.addNewAgent = addNewAgent;
        this.hasMoreAssignments = hasMoreAssignments;
    }

    @Override
    public void run() {
        try {
            while(this.hasMoreAssignments.get()) {
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
                CandidatesStats candidate = this.candidates.take();
                this.addNewAgent.accept(candidate);
            }
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }
}
