package decipher;

import dto.candidates.CandidatesInfo;
import javafx.concurrent.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DecipherTask extends Task<Boolean> {

    private String agentName = null;

    private Integer numOfThreads = 0;

    private Integer numberOfAssignmentsToPull = 0;





    @Override
    protected Boolean call() throws Exception {
        ThreadFactory costumeThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix(this.agentName + ".").build();
        BlockingQueue<Runnable> assignments = new LinkedBlockingQueue<>(1000);
        BlockingQueue<CandidatesInfo> candidates = new LinkedBlockingQueue<>();

        PausableThreadPoolExecutor assignmentsExecutor =
                new PausableThreadPoolExecutor(
                        this.numOfThreads,
                        this.numOfThreads,
                        0,
                        TimeUnit.SECONDS,
                        assignments,
                        costumeThreadFactory
                );

        assignmentsExecutor.prestartAllCoreThreads();

        return Boolean.TRUE;

    }
}
