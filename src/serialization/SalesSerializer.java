package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Sale;

public class SalesSerializer {
    private static SalesSerializer instance;

    private SalesSerializer() {
        // private constructor to enforce singleton pattern
    }

    public static SalesSerializer getInstance() {
        if (instance == null) {
            instance = new SalesSerializer();
        }
        return instance;
    }

    public void serializeSalesData(List<Sale> sales, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(sales);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Sale> deserializeSalesData(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Sale>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
