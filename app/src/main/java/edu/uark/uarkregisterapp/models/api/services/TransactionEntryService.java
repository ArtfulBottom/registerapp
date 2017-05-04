package edu.uark.uarkregisterapp.models.api.services;

import org.json.JSONObject;

import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.ApiLevel;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.enums.TransactionEntryApiMethod;
import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class TransactionEntryService extends BaseRemoteService {
    public TransactionEntry putTransactionEntry(TransactionEntry entry) {
        JSONObject rawJsonObject = this.putSingle(
                (new PathElementInterface[]{TransactionEntryApiMethod.TRANSACTION, ApiLevel.ONE}), entry.convertToJson()
        );

        if (rawJsonObject != null) {
            return (new TransactionEntry()).loadFromJson(rawJsonObject);
        } else {
            return new TransactionEntry().setApiRequestStatus(TransactionApiRequestStatus.UNKNOWN_ERROR);
        }
    }
}
