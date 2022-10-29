package agent.decipher;


import dm.assignment.Assignment;
import dto.agents.AgentsInfo;
import dto.assignment.AssignmentDTO;
import dto.assignment.AssignmentDTOList;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import dto.staticinfo.StaticMachineDTO;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import machine.EnigmaMachine;

import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static agent.http.Configuration.ASSIGNMENTS;
import static dm.utils.Utils.fromBytesArrayToObject;
import static dm.utils.Utils.fromObjectToByteArray;
import static httpcommon.constants.Constants.*;
import static httpcommon.constants.Constants.SC_OK;
import static httpcommon.utils.HttpClientUtil.GSON_INSTANCE;
import static httpcommon.utils.HttpClientUtil.runAsync;
import static httpcommon.utils.Utils.showErrors;


public class DecipherTask extends Task<Boolean> {

    private AgentsInfo agentInfo = null;

    private byte[] serMachine = null;

    private BlockingQueue<CandidatesInfo> candidates = null;

    private BlockingQueue<AssignmentDTOList> assignments = null;
    private AssignmentDTOList assignmentInfo = null;

    private ExecutorService threads = null;

    private String messageToProcess = null;

    private AtomicBoolean hasMoreAssignments = null;

    private StaticMachineDTO staticMachineInventory = null;

    private CountDownLatch countDownLatch = null;

    private Consumer<CandidatesDTO> candidatesDTOConsumer = null;

    private AtomicInteger totalAssignmentsWorkedOn = null;

    private AtomicLong candidatesFound = null;

    private AtomicReference<Integer> totalCompletedAssignments = null;

    private AtomicReference<Long> totalCandidatesFound = null;

    private AtomicReference<Integer> currentlyAwaitingAssignments = null;

    private AtomicReference<Integer> totalAssignments = null;

    private IntegerProperty totalCompletedAssignmentsProperty = null;

    private LongProperty totalCandidatesFoundProperty = null;

    private IntegerProperty awaitingAssignmentsProperty = null;

    private IntegerProperty totalDrawnAssignmentsProperty = null;

    private Object assignmentsLock = null;

    public DecipherTask(
            AgentsInfo agentsInfo,
            String messageToProcess,
            byte[] serStaticMachineInventory,
            Consumer<CandidatesDTO> candidatesDTOConsumer
    ){
        this.assignmentsLock = new Object();
        this.agentInfo = agentsInfo;
        this.candidatesFound = new AtomicLong(0);
        this.totalAssignmentsWorkedOn = new AtomicInteger(0);
        this.threads = Executors.newFixedThreadPool(this.agentInfo.getNumberOfThreads());
        EnigmaMachine machine = new EnigmaMachine();
        this.serMachine = fromObjectToByteArray(machine);
        this.messageToProcess = messageToProcess;
        this.candidatesDTOConsumer = candidatesDTOConsumer;
        this.assignments = new LinkedBlockingQueue<>();
        this.hasMoreAssignments = new AtomicBoolean(true);
        this.candidates = new LinkedBlockingQueue<>();
        this.totalCompletedAssignments = new AtomicReference<>();
        this.totalCandidatesFound = new AtomicReference<>();
        this.currentlyAwaitingAssignments  = new AtomicReference<>();
        this.totalAssignments = new AtomicReference<>();
        this.awaitingAssignmentsProperty = new SimpleIntegerProperty(this,"awaitingAssignments", 0);
        this.totalCandidatesFoundProperty = new SimpleLongProperty(this,"totalCandidates", 0);
        this.totalCompletedAssignmentsProperty = new SimpleIntegerProperty(this, "totalCompleted", 0);
        this.totalDrawnAssignmentsProperty = new SimpleIntegerProperty(this, "totalDrawn", 0);
        this.staticMachineInventory = (StaticMachineDTO) fromBytesArrayToObject(serStaticMachineInventory);
    }

    @Override
    protected Boolean call() throws InterruptedException {
        CandidatesUpdater candidatesUpdater =
                new CandidatesUpdater(
                        this.candidatesDTOConsumer,
                        this.candidates,
                        this.hasMoreAssignments
                );
        Thread thread = new Thread(candidatesUpdater, "Candidates Updater");
        thread.start();
        while(this.hasMoreAssignments.get()){
            if(isCancelled() || Thread.currentThread().isInterrupted()){
                this.threads.shutdown();
                Thread.currentThread().join();
            }
            this.getAssignments();
            this.assignmentInfo =
                    this.assignments.take();
            this.agentInfo.setDrawn(assignmentInfo.getAssignments().size());
            this.updateTotalDrawn(this.totalDrawnAssignmentsProperty.get() + assignmentInfo.getAssignments().size());
            if(this.assignmentInfo.getAssignments().size() != 0) {
                countDownLatch = new CountDownLatch(assignmentInfo.getAssignments().size());
                this.updateInfo();
                for (AssignmentDTO assignmentDTO : this.assignmentInfo.getAssignments()) {
                    this.threads.execute(
                            new Assignment(
                                    this.staticMachineInventory,
                                    assignmentDTO,
                                    this.candidates,
                                    this.messageToProcess,
                                    this.serMachine,
                                    this.countDownLatch,
                                    this.totalAssignmentsWorkedOn,
                                    this.agentInfo.getAlliesTeam(),
                                    this.candidatesFound
                            )
                    );
                    this.updateCurrentlyAwaiting((int) this.countDownLatch.getCount());
                }

                if(isCancelled() || Thread.currentThread().isInterrupted()){
                    this.updateInfo();
                    this.threads.shutdown();
                    Thread.currentThread().join();
                }
                this.countDownLatch.await();
            }
            else{
                if(!this.assignmentInfo.hasMore()){
                    this.hasMoreAssignments.set(false);
                    this.updateInfo();
                }
            }
        }

        this.updateCurrentlyAwaiting((int) this.countDownLatch.getCount());
        this.updateTotalCompleted(this.totalAssignmentsWorkedOn.get());
        this.updateTotalCandidates(this.candidatesFound.get());
        return Boolean.TRUE;
    }

    private void updateInfo() {
        this.updateCurrentlyAwaiting((int) this.countDownLatch.getCount());
        this.agentInfo.setLeft((int) this.countDownLatch.getCount());
        this.updateTotalCompleted(this.totalAssignmentsWorkedOn.get());
        this.agentInfo.setCompleted(this.totalAssignmentsWorkedOn.get());
        this.updateTotalCandidates(this.candidatesFound.get());
        this.agentInfo.setTotalFound(this.candidatesFound.get());
    }


    private void getAssignments() {
        String finalUrl = HttpUrl
                .parse(REFRESH_DATA)
                .newBuilder()
                .addQueryParameter(DATA, ASSIGNMENTS)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> showErrors(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException{
                String body = response.body().string();
                if (response.code() == SC_OK) {
                    try {
                        synchronized (assignmentsLock){
                            AssignmentDTOList assignmentDTOList =
                                    GSON_INSTANCE.fromJson(
                                            body,
                                            AssignmentDTOList.class
                                    );
                            if(assignmentDTOList != null) {
                                assignments.put(assignmentDTOList);
                            }
                            assignmentsLock.notifyAll();
                        }
                    } catch (InterruptedException ignored) {}
                } else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }

    public IntegerProperty totalCompletedAssignmentsProperty() {
        return totalCompletedAssignmentsProperty;
    }

    public LongProperty totalCandidatesFoundProperty() {
        return totalCandidatesFoundProperty;
    }

    public IntegerProperty awaitingAssignmentsProperty() {
        return awaitingAssignmentsProperty;
    }

    public IntegerProperty totalDrawnAssignmentsProperty() {
        return totalDrawnAssignmentsProperty;
    }

    protected void updateTotalDrawn(Integer drawn) {
        if (Platform.isFxApplicationThread()) {
            this.totalDrawnAssignmentsProperty.set(drawn);
        } else {
            if (this.totalAssignments.getAndSet(drawn) == null) {
                Platform.runLater(() -> {
                    final Integer total = totalAssignments.getAndSet(null);
                    DecipherTask.this.totalDrawnAssignmentsProperty.set(total);
                });
            }
        }
    }

    protected void updateCurrentlyAwaiting(Integer awaiting) {
        if (Platform.isFxApplicationThread()) {
            this.awaitingAssignmentsProperty.set(awaiting);
        } else {
            if (this.currentlyAwaitingAssignments.getAndSet(awaiting) == null) {
                Platform.runLater(() -> {
                    final Integer total = currentlyAwaitingAssignments.getAndSet(null);
                    DecipherTask.this.awaitingAssignmentsProperty.set(total);
                });
            }
        }
    }

    protected void updateTotalCandidates(Long candidates) {
        if (Platform.isFxApplicationThread()) {
            this.totalCandidatesFoundProperty.set(candidates);
        } else {
            if (this.totalCandidatesFound.getAndSet(candidates) == null) {
                Platform.runLater(() -> {
                    final Long total = totalCandidatesFound.getAndSet(null);
                    DecipherTask.this.totalCandidatesFoundProperty.set(total);
                });
            }
        }
    }

    protected void updateTotalCompleted(Integer completed) {
        if (Platform.isFxApplicationThread()) {
            this.totalCompletedAssignmentsProperty.set(completed);
        } else {
            if (this.totalCompletedAssignments.getAndSet(completed) == null) {
                Platform.runLater(() -> {
                    final Integer total = totalCompletedAssignments.getAndSet(null);
                    DecipherTask.this.totalCompletedAssignmentsProperty.set(total);
                });
            }
        }
    }
}
