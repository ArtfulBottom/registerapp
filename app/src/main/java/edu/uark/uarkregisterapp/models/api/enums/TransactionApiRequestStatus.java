package edu.uark.uarkregisterapp.models.api.enums;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public enum TransactionApiRequestStatus {
    OK(0),
    INVALID_INPUT(1),
    UNKNOWN_ERROR(2),
    NOT_FOUND(3);

    public int getValue() {
        return value;
    }

    public static TransactionApiRequestStatus mapValue(int key) {
        if (valueMap == null) {
            valueMap = new SparseArray<>();

            for (TransactionApiRequestStatus status : TransactionApiRequestStatus.values()) {
                valueMap.put(status.getValue(), status);
            }
        }

        return ((valueMap.indexOfKey(key) >= 0) ? valueMap.get(key) : TransactionApiRequestStatus.UNKNOWN_ERROR);
    }

    public static TransactionApiRequestStatus mapName(String name) {
        if (nameMap == null) {
            nameMap = new HashMap<>();

            for (TransactionApiRequestStatus status : TransactionApiRequestStatus.values()) {
                nameMap.put(status.name(), status);
            }
        }

        return (nameMap.containsKey(name) ? nameMap.get(name) : TransactionApiRequestStatus.UNKNOWN_ERROR);
    }

    private int value;

    private static Map<String, TransactionApiRequestStatus> nameMap = null;
    private static SparseArray<TransactionApiRequestStatus> valueMap = null;

    private TransactionApiRequestStatus(int value) {
        this.value = value;
    }
}
