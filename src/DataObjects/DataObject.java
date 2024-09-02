/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataObjects;

import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * @author AJ
 */
public class DataObject {

    private final LinkedHashMap<String, String> valuesMap;
    private final String table;

    public DataObject(String table) {
        this.valuesMap = new LinkedHashMap<>();
        this.table = table;
    }

    public DataObject(String table, String[] keys, String... values) {
        this(table);
        for (int i = 0; i < keys.length; i++) {
            valuesMap.put(keys[i], values[i]);
        }
    }

    public String table() {
        return table;
    }

    public DataObject put(String key, String value) {
        valuesMap.put(key, value);
        return this;
    }

    public String get(String key) {
        return valuesMap.get(key);
    }

    public String[] values() {
        return valuesMap.values().toArray(new String[0]);
    }

    public String[] keys() {
        //System.out.println(Arrays.toString(values()));
        return valuesMap.keySet().toArray(new String[0]);
    }
}
