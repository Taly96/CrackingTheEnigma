package agent.decipher;


import dm.assignment.Assignment;
import dto.agents.AgentsInfo;
import dto.assignment.AssignmentDTO;
import dto.assignment.AssignmentDTOList;
import dto.candidates.CandidatesDTO;
import dto.candidates.CandidatesInfo;
import dto.staticinfo.StaticMachineDTO;
import javafx.application.Platform;
import machine.EnigmaMachine;

import javafx.concurrent.Task;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    public DecipherTask(
            AgentsInfo agentsInfo,
            String messageToProcess,
            byte[] serStaticMachineInventory,
            Consumer<CandidatesDTO> candidatesDTOConsumer
    ){
        this.agentInfo = agentsInfo;
        this.threads = Executors.newFixedThreadPool(this.agentInfo.getNumberOfThreads());
        EnigmaMachine machine = new EnigmaMachine();
        this.serMachine = fromObjectToByteArray(machine);
        this.messageToProcess = messageToProcess;
        this.candidatesDTOConsumer = candidatesDTOConsumer;
        this.assignments = new LinkedBlockingQueue<>();
        this.hasMoreAssignments = new AtomicBoolean(true);
        this.candidates = new LinkedBlockingQueue<>();
        this.staticMachineInventory = (StaticMachineDTO) fromBytesArrayToObject(serStaticMachineInventory);
    }

    @Override
    protected Boolean call() throws InterruptedException {//todo= check this guy
        System.out.println("Started deciphering");
        CandidatesUpdater candidatesUpdater =
                new CandidatesUpdater(
                        this.candidatesDTOConsumer,
                        this.candidates,
                        this.hasMoreAssignments
                );
        Thread thread = new Thread(candidatesUpdater, "Candidates Updater");
        thread.start();
        while(this.hasMoreAssignments.get()){
            this.getAssignments();
            this.assignmentInfo =
                    this.assignments.take();
            if(this.assignmentInfo.getAssignments().size() != 0) {
                countDownLatch = new CountDownLatch(assignmentInfo.getAssignments().size());
                System.out.println("Count down latch created, size: " + assignmentInfo.getAssignments().size());
                for (AssignmentDTO assignmentDTO : this.assignmentInfo.getAssignments()) {
                    System.out.println("Creating runnable assignments");
                    this.threads.execute(
                            new Assignment(
                                    this.staticMachineInventory,
                                    assignmentDTO,
                                    this.candidates,
                                    this.messageToProcess,
                                    this.serMachine,
                                    this.countDownLatch
                            )
                    );
                }
                this.countDownLatch.await();
            }
            else{
                if(!this.assignmentInfo.hasMore()){
                    this.hasMoreAssignments.set(false);
                }
            }
        }

        return Boolean.TRUE;
    }



    private void getAssignments() {

        System.out.println("getting more assignments");
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
                        AssignmentDTOList assignmentDTOList =
                                GSON_INSTANCE.fromJson(
                                        body,
                                        AssignmentDTOList.class
                                );
                        if(assignmentDTOList != null) {
                            assignments.put(assignmentDTOList);
                            System.out.println("Got Assignments " + assignmentDTOList.getAssignments().size());
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Platform.runLater(() -> showErrors(body));
                }
                response.close();
            }
        });
    }
}
