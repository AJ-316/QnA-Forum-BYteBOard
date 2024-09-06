package BoardStructurePackage;

import java.util.HashMap;
import java.util.Map;

public class BoardFrameSwitchContext {

    private final Map<String, String> dataMap = new HashMap<>();
    private final DataRetriever dataRetriever;

    public BoardFrameSwitchContext(DataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    public boolean retrieveData(String context) {
        return dataRetriever.retrieve(context);
    }

    public void putContext(String key, String value) {
        dataMap.put(key, value);
    }

    public String getContext(String key) {
        return dataMap.get(key);
    }

    public String getContextOrDefault(String key, String defaultKey) {
        if(key != null) return key;

        return dataMap.get(defaultKey);
    }

    @FunctionalInterface
    public interface DataRetriever {
        boolean retrieve(String context);
    }

}
