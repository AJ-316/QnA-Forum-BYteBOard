package BYteBOardInterface.StructurePackage;

import BYteBOardDatabase.DBDataObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BoardFrameSwitchDelegate {

    public static final String DELIMITER = "\0";
    private final Map<String, DBDataObject> dataMap;
    private final DataRetriever dataRetriever;

    public BoardFrameSwitchDelegate(DataRetriever dataRetriever, boolean isOrderedContext) {
        this.dataRetriever = dataRetriever;
        this.dataMap = isOrderedContext ? new LinkedHashMap<>() : new HashMap<>();
    }

    public String retrieveData(String... context) {
        return dataRetriever.retrieve(this, context);
    }

    public void putContext(String key, DBDataObject value) {
        dataMap.put(key, value);
    }

    public void putContextList(String keyPrefix, DBDataObject[] values) {
        getMap().keySet().removeIf(key -> key.startsWith(keyPrefix));

        for (int i = 0; i < values.length; i++)
            putContext(keyPrefix + "_" + i, values[i]);
    }

    public void putContextList(String keyPrefix, String idKey, DBDataObject[] values) {
        getMap().keySet().removeIf(key -> key.startsWith(keyPrefix));

        for (DBDataObject value : values)
            putContext(keyPrefix + "_" + value.getValue(idKey), value);
    }

    public DBDataObject getContext(String key) {
        return dataMap.get(key);
    }

    public String[] getContextOrDefault(String[] context, String... defaults) {
        if (context != null && context.length >= defaults.length / 2) return context;

        context = new String[defaults.length / 2];

        int j = 0;
        for (int i = 0; i < context.length; i++) {
            DBDataObject contextDataObject = getContext(defaults[j++]);
            context[i] = contextDataObject.getValue(defaults[j++]);
        }

        return context;
    }

    public DBDataObject[] getContextList(String keyPrefix, List<DBDataObject> data) {
        data.clear();

        for (String key : getMap().keySet())
            if (key.startsWith(keyPrefix))
                data.add(getContext(key));

        return data.toArray(new DBDataObject[0]);
    }

    public Map<String, DBDataObject> getMap() {
        return dataMap;
    }

    @FunctionalInterface
    public interface DataRetriever {
        String retrieve(BoardFrameSwitchDelegate delegate, String... context);
    }

}
