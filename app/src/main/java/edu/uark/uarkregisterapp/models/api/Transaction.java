package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.enums.TransactionClassification;
import edu.uark.uarkregisterapp.models.api.fields.TransactionFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class Transaction implements ConvertToJsonInterface, LoadFromJsonInterface<Transaction> {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public Transaction setId(UUID id) {
        this.id = id;
        return this;
    }

    private UUID cashierId;
    public UUID getCashierId() {
        return this.cashierId;
    }
    public Transaction setCashierId(UUID cashierId) {
        this.cashierId = cashierId;
        return this;
    }

    private double totalAmount;
    public double getTotalAmount() {
        return this.totalAmount;
    }
    public Transaction setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    private TransactionClassification classification;
    public TransactionClassification getClassification() {
        return this.classification;
    }
    public Transaction setClassification(TransactionClassification classification) {
        this.classification = classification;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return this.createdOn;
    }
    public Transaction setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    private UUID referenceId;
    public UUID getReferenceId() {
        return this.referenceId;
    }
    public Transaction setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    private TransactionApiRequestStatus apiRequestStatus;
    public TransactionApiRequestStatus getApiRequestStatus() {
        return this.apiRequestStatus;
    }
    public Transaction setApiRequestStatus(TransactionApiRequestStatus apiRequestStatus) {
        if (this.apiRequestStatus != apiRequestStatus) {
            this.apiRequestStatus = apiRequestStatus;
        }

        return this;
    }

    private String apiRequestMessage;
    public String getApiRequestMessage() {
        return this.apiRequestMessage;
    }
    public Transaction setApiRequestMessage(String apiRequestMessage) {
        if (!StringUtils.equalsIgnoreCase(this.apiRequestMessage, apiRequestMessage)) {
            this.apiRequestMessage = apiRequestMessage;
        }

        return this;
    }

    @Override
    public Transaction loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionFieldName.CASHIER_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.cashierId = UUID.fromString(value);
        }

        this.totalAmount = rawJsonObject.optDouble(TransactionFieldName.TOTAL_AMOUNT.getFieldName());
        this.classification = TransactionClassification.mapName(
                rawJsonObject.optString(TransactionFieldName.CLASSIFICATION.getFieldName())
        );

        value = rawJsonObject.optString(TransactionFieldName.REFERENCE_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.referenceId = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        this.apiRequestMessage = rawJsonObject.optString(TransactionFieldName.API_REQUEST_MESSAGE.getFieldName());

        value = rawJsonObject.optString(TransactionFieldName.API_REQUEST_STATUS.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.apiRequestStatus = TransactionApiRequestStatus.mapName(value);
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionFieldName.ID.getFieldName(), this.id.toString());
            jsonObject.put(TransactionFieldName.CASHIER_ID.getFieldName(), this.cashierId);
            jsonObject.put(TransactionFieldName.TOTAL_AMOUNT.getFieldName(), this.totalAmount);
            jsonObject.put(TransactionFieldName.CLASSIFICATION.getFieldName(), this.classification);
            jsonObject.put(TransactionFieldName.REFERENCE_ID.getFieldName(), this.referenceId);
            jsonObject.put(TransactionFieldName.CREATED_ON.getFieldName(), (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).format(this.createdOn));
            jsonObject.put(TransactionFieldName.API_REQUEST_MESSAGE.getFieldName(), this.apiRequestMessage);
            jsonObject.put(TransactionFieldName.API_REQUEST_STATUS.getFieldName(), this.apiRequestStatus.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Transaction() {
        this.id = new UUID(0, 0);
        this.cashierId = new UUID(0, 0);
        this.totalAmount = 0.0;
        this.classification = TransactionClassification.NOT_DEFINED;
        this.createdOn = new Date();
        this.referenceId = new UUID(0, 0);
        this.apiRequestMessage = StringUtils.EMPTY;
        this.apiRequestStatus = TransactionApiRequestStatus.OK;
    }
}
