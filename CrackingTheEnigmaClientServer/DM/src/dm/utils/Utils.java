package dm.utils;

import java.io.*;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
