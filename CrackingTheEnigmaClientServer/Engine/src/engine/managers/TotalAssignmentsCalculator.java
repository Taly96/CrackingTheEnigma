package engine.managers;

import dto.loadedmachine.LoadedMachineDTO;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class TotalAssignmentsCalculator implements Runnable {

    private String level = null;

    private LoadedMachineDTO machineInfo = null;

    private Consumer<BigDecimal> totalAssignmentsDelegate = null;

    public TotalAssignmentsCalculator(
            String level,
            LoadedMachineDTO machineInfo,
            Consumer<BigDecimal> totalAssignmentsDelegate
    ) {
        this.level = level;
        this.machineInfo = machineInfo;
        this.totalAssignmentsDelegate = totalAssignmentsDelegate;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Total assignments calculator.");
        BigDecimal res = BigDecimal.ZERO;

        switch (this.level) {
            case "Easy": {
                res = this.calcEasy();
                break;
            }
            case "Medium": {
                res = this.calcAdvanced();
                break;
            }
            case "Hard": {
                res = this.calcHard();
                break;
            }
            case "Impossible": {
                res = this.calcImpossible();
                break;
            }
            default: {
                break;
            }
        }
        this.totalAssignmentsDelegate.accept(res);
        Thread.currentThread().interrupt();
//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    private BigDecimal calcEasy() {
        long abcLength = this.machineInfo.getAbc().length();
        long numOfRotors = this.machineInfo.getRotorsCount();
        BigDecimal startingPositionsPermutations = BigDecimal.valueOf((long)(Math.pow(abcLength, numOfRotors)));

        return startingPositionsPermutations;
    }

    private BigDecimal calcImpossible() {
        BigDecimal numerator =this.getFactorial(this.machineInfo.getAvailableRotors().size());
        long denominatorLimit = this.machineInfo.getAvailableRotors().size() - this.machineInfo.getRotorsCount();
        BigDecimal denominator = this.getFactorial(denominatorLimit);
        BigDecimal numOfNeededRotorsFactorial = this.getFactorial(this.machineInfo.getRotorsCount());
        denominator = denominator.multiply(numOfNeededRotorsFactorial);
        numerator = numerator.divide(denominator);
        BigDecimal Cnr = numerator.multiply(this.calcHard());

        return Cnr ;
    }

    private BigDecimal calcHard() {
        BigDecimal numOfRotorsFactorial = this.getFactorial(this.machineInfo.getRotorsCount());

        return (numOfRotorsFactorial.multiply(calcAdvanced()));
    }

    private BigDecimal calcAdvanced() {
        long numOfAvailableReflectors = this.machineInfo.getAvailableReflectors().size();

        return BigDecimal.valueOf(numOfAvailableReflectors).multiply(this.calcEasy());
    }

    private BigDecimal getFactorial(long limit){
        BigDecimal factorial = BigDecimal.ONE;

        for(int i = 2; i <= limit; i++){
            factorial = factorial.multiply(BigDecimal.valueOf(i));
        }

        return factorial;
    }
}