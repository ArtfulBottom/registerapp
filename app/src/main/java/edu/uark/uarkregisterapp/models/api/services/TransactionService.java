package edu.uark.uarkregisterapp.models.api.services;

import org.json.JSONObject;

import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.enums.ApiLevel;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiMethod;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class TransactionService extends BaseRemoteService {
    public Transaction putTransaction(Transaction transaction) {
        JSONObject rawJsonObject = this.putSingle(
                (new PathElementInterface[]{TransactionApiMethod.TRANSACTION, ApiLevel.ONE}), transaction.convertToJson()
        );

        if (rawJsonObject != null) {
            return (new Transaction()).loadFromJson(rawJsonObject);
        } else {
            return new Transaction().setApiRequestStatus(TransactionApiRequestStatus.UNKNOWN_ERROR);
        }
    }
}
