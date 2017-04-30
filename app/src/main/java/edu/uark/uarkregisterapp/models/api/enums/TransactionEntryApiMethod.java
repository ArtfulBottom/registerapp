package edu.uark.uarkregisterapp.models.api.enums;

import java.util.HashMap;
import java.util.Map;

import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum TransactionEntryApiMethod implements PathElementInterface {
    NONE(""),
    TRANSACTION("transactionEntry");

    @Override
    public String getPathValue() {
        return value;
    }

    public static TransactionEntryApiMethod map(String key) {
        if (valueMap == null) {
            valueMap = new HashMap<>();

            for (TransactionEntryApiMethod value : TransactionEntryApiMethod.values()) {
                valueMap.put(value.getPathValue(), value);
            }
        }

        return (valueMap.containsKey(key) ? valueMap.get(key) : TransactionEntryApiMethod.NONE);
    }

    private String value;

    private static Map<String, TransactionEntryApiMethod> valueMap = null;

    TransactionEntryApiMethod(String value) {
        this.value = value;
    }
}
