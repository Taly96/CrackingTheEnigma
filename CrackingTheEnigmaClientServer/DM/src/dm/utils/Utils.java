package dm.utils;

import dto.codeconfig.CodeConfigInfo;

import java.io.*;
import java.util.Collections;

public class Utils {

    public enum Levels {
        EASY,
        ADVANCED,
        HARD,
        IMPOSSIBLE
    }
    public static byte[] fromObjectToByteArray(Serializable object){

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();

            return baos.toByteArray();
        } catch (IOException e){
            throw new RuntimeException();
        }
    }

    public static Object fromBytesArrayToObject(byte[] bytes) {

        try{
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object o = ois.readObject();
            ois.close();

            return o;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createStringCodeConfig(CodeConfigInfo currentCodeConfig){
        StringBuilder codeConfigToShow = new StringBuilder("<");
        int index = 0;
        int notchPos = 0;

        Collections.reverse(currentCodeConfig.getRotorsID());

        for (Integer rotorID : currentCodeConfig.getRotorsID()) {
            codeConfigToShow.append(String.format("%s", rotorID + ","));
        }

        Collections.reverse(currentCodeConfig.getRotorsID());
        codeConfigToShow.replace(codeConfigToShow.length() - 1, codeConfigToShow.length(), ">");
        codeConfigToShow.append("<");
        Collections.reverse(currentCodeConfig.getRotorsNotchPos());

        for(int i = currentCodeConfig.getRotorsStartingPos().length() - 1; i >=0; i--){
            notchPos = currentCodeConfig.getRotorsNotchPos().get(index);
            codeConfigToShow.append(String.format("%s", currentCodeConfig.getRotorsStartingPos().charAt(i) + "(" + notchPos + ")" + ","));
            index++;
        }

        codeConfigToShow.replace(codeConfigToShow.length() - 1, codeConfigToShow.length(), ">");
        Collections.reverse(currentCodeConfig.getRotorsNotchPos());
        codeConfigToShow.append(String.format("%s", "<" + currentCodeConfig.getReflectorID() + ">"));

        return codeConfigToShow.toString();
    }
}
