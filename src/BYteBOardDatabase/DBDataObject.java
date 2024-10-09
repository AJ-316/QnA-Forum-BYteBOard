package BYteBOardDatabase;

import Tools.DEBUG;

import java.util.HashMap;

public class DBDataObject extends DBOperation {
    public DBDataObject() {
        super(new HashMap<>(), null);
    }

    @Override
    public String toString() {
        if (keyValueMap == null) return super.toString();
        StringBuilder builder = new StringBuilder("\n").append(DEBUG.PURPLE).append(super.toString()).append(": ");

        for (String key : keyValueMap.keySet()) {
            builder.append(DEBUG.PURPLE).append("\n\tkeyValue => ")
                    .append(DEBUG.BLUE).append(key)
                    .append(DEBUG.PURPLE).append("=")
                    .append(DEBUG.BLUE).append(keyValueMap.get(key));
        }

        return builder.append(DEBUG.NONE).append("\n").toString();
    }
}
