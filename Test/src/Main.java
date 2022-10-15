public class Main {

    public static void main(String[] args) {
        String abc = "ABC";
        int counter = 0;
        int numOfRotors = 2;
        double maxValue = Math.pow(abc.length(), numOfRotors);
        String pos = "";

        while(counter < maxValue){
            int counterCopy = counter;
            for(int i =0; i < numOfRotors; i++) {
                pos += String.format("%s", counterCopy %abc.length());
                counterCopy /= abc.length();

            }
            System.out.println(pos);
            pos = "";
            counter++;
        }
    }
}
