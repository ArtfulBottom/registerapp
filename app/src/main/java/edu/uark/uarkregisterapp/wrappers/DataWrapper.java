package edu.uark.uarkregisterapp.wrappers;

import java.io.Serializable;
import java.util.ArrayList;

import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class DataWrapper implements Serializable {
    private ArrayList<TransactionEntry> entries;

    public DataWrapper(ArrayList<TransactionEntry> data) {
        this.entries = data;
    }

    public ArrayList<TransactionEntry> getEntries() {
        return this.entries;
    }
}
