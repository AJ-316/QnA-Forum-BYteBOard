package BoardStructurePackage;

import DatabasePackage.DBDataObject;

import java.util.HashMap;
import java.util.Map;

public class BoardFrameSwitchDelegate {

    public static final String DELIMITER = "\0";
    private final Map<String, DBDataObject> dataMap = new HashMap<>();
    private final DataRetriever dataRetriever;

    public BoardFrameSwitchDelegate(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    public boolean retrieveData(String context) {
        return dataRetriever.retrieve(context, this);
    }

    public void putContext(String key, DBDataObject value) {
        dataMap.put(key, value);
    }

    public DBDataObject getContext(String key) {
        return dataMap.get(key);
    }

    public Map<String, DBDataObject> getMap() {
        return dataMap;
    }

    @FunctionalInterface
    public interface DataRetriever {
        boolean retrieve(String context, BoardFrameSwitchDelegate delegate);
    }

}
