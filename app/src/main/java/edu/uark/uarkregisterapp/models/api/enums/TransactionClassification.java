package edu.uark.uarkregisterapp.models.api.enums;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public enum TransactionClassification {
    NOT_DEFINED(0),
    SALE(1),
    RETURN(2);

    public int getValue() {
        return value;
    }

    public static TransactionClassification mapValue(int key) {
        if (valueMap == null) {
            valueMap = new SparseArray<>();

            for (TransactionClassification status : TransactionClassification.values()) {
                valueMap.put(status.getValue(), status);
            }
        }

        return ((valueMap.indexOfKey(key) >= 0) ? valueMap.get(key) : TransactionClassification.NOT_DEFINED);
    }

    public static TransactionClassification mapName(String name) {
        if (nameMap == null) {
            nameMap = new HashMap<>();

            for (TransactionClassification status : TransactionClassification.values()) {
                nameMap.put(status.name(), status);
            }
        }

        return (nameMap.containsKey(name) ? nameMap.get(name) : TransactionClassification.NOT_DEFINED);
    }

    private int value;

    private static Map<String, TransactionClassification> nameMap = null;
    private static SparseArray<TransactionClassification> valueMap = null;

    private TransactionClassification(int value) {
        this.value = value;
    }
}
