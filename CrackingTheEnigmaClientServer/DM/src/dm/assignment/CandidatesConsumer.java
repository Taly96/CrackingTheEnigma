package dm.assignment;


import dto.candidates.CandidatesInfo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CandidatesConsumer implements Runnable {

    private BlockingQueue<CandidatesInfo> candidates = null;

    private Consumer<CandidatesInfo> addNewAgent = null;

    private AtomicBoolean hasMoreAssignments = null;

    public CandidatesConsumer(
            BlockingQueue<CandidatesInfo> candidates,
            Consumer<CandidatesInfo> addNewAgent,
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
                CandidatesInfo candidate = this.candidates.take();
                this.addNewAgent.accept(candidate);
            }
            Thread.currentThread().join();
        } catch (InterruptedException ignored) {}
    }
}
