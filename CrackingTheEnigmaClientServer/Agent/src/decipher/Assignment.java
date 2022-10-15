package decipher;

//import dto.code.config.CodeConfigDTO;
//import dto.codeconfig.CodeConfigInfo;
//import dto.staticinfo.StaticMachineDTO;
//import enigma.containers.EnigmaContainer;
//import enigma.containers.StaticMachineInfo;
//import enigma.machine.codeconfiguration.CodeConfiguration;
//import enigma.utils.Utils;
//
//import java.util.List;
//import java.util.StringTokenizer;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class Assignment implements Runnable {
//
//    private List<CodeConfiguration> assignments = null;
//
//    private BlockingQueue<CandidatesStats> candidates = null;
//
//    private String encryptedMessage = null;
//
//    private StaticMachineDTO info = null;
//
//    private byte[] enigmaContainer = null;
//
//    private Object pauseBlock = null;
//
//    private AtomicReference<Double> agentAvg = null;
//
//    public Assignment(
//            BlockingQueue<CandidatesStats> candidates,
//            List<CodeConfiguration> assignments,
//            String encryptedMessage,
//            byte[] enigmaContainer,
//            StaticMachineDTO info,
//            AtomicReference<Double> agentAvg
//             ) {
//        this.assignments = assignments;
//        this.candidates = candidates;
//        this.encryptedMessage = encryptedMessage;
//        this.info = info;
//        this.enigmaContainer = enigmaContainer;
//        this.pauseBlock = new Object();
//        this.agentAvg = agentAvg;
//
//    }
//
//    @Override
//    public void run() {
//        boolean isCandidate = true;
//        EnigmaContainer enigmaContainer =
//                (EnigmaContainer) Utils.fromBytesArrayToObject(this.enigmaContainer);
//        long begin = System.nanoTime();
//        StringBuilder res = new StringBuilder();
//
//        for (CodeConfiguration code : this.assignments) {
//            CodeConfigDTO codeConfigDTO = enigmaContainer.getMachine().setCodeConfig(code);
//            enigmaContainer.getMachine().setABC(this.info.getABC());
//
//            String processedWords = enigmaContainer.getMachine().process(this.encryptedMessage);
//            String word = "";
//            StringTokenizer st = new StringTokenizer(processedWords, " ");
//            while(st.hasMoreTokens()){
//                word = st.nextToken();
//                if (!this.info.getWords().contains(word)) {
//                    isCandidate = false;
//                    break;
//                } else {
//                    res.append(word + " ");
//                }
//            }
//            long timeElapsed = (System.nanoTime() - begin);
//            if (isCandidate && !res.equals("")) {
//                this.addCandidate(res, codeConfigDTO, timeElapsed);
//            }
//            this.agentAvg.set(this.agentAvg.get() + timeElapsed);
//            isCandidate = true;
//            res.delete(0, res.length());
//        }
//    }
//
//    private void addCandidate(StringBuilder res, CodeConfigInfo codeConfigInfo, long timeElapsed) {
//        try {
//            this.candidates.put(
//                    new CandidatesStats(
//                            res.toString().trim(),
//                            timeElapsed,
//                            Thread.currentThread().getName(),
//                            codeConfigInfo
//                    )
//            );
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//}
