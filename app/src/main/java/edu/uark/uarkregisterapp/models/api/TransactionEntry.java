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
import edu.uark.uarkregisterapp.models.api.fields.TransactionEntryFieldName;
import edu.uark.uarkregisterapp.models.api.fields.TransactionEntryFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class TransactionEntry implements ConvertToJsonInterface, LoadFromJsonInterface<TransactionEntry>, Serializable {
    private UUID id;
    public UUID getId() {
        return this.id;
    }
    public TransactionEntry setId(UUID id) {
        this.id = id;
        return this;
    }

    private UUID transactionId;
    public UUID getTransactionId() {
        return this.transactionId;
    }
    public TransactionEntry setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    private UUID productId;
    public UUID getProductId() {
        return this.productId;
    }
    public TransactionEntry setProductId(UUID productId) {
        this.productId = productId;
        return this;
    }

    private int quantity;
    public int getQuantity() {
        return this.quantity;
    }
    public TransactionEntry setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    private double unitPrice;
    public double getUnitPrice() {
        return this.unitPrice;
    }
    public TransactionEntry setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    private TransactionApiRequestStatus apiRequestStatus;
    public TransactionApiRequestStatus getApiRequestStatus() {
        return this.apiRequestStatus;
    }
    public TransactionEntry setApiRequestStatus(TransactionApiRequestStatus apiRequestStatus) {
        if (this.apiRequestStatus != apiRequestStatus) {
            this.apiRequestStatus = apiRequestStatus;
        }

        return this;
    }

    private String apiRequestMessage;
    public String getApiRequestMessage() {
        return this.apiRequestMessage;
    }
    public TransactionEntry setApiRequestMessage(String apiRequestMessage) {
        if (!StringUtils.equalsIgnoreCase(this.apiRequestMessage, apiRequestMessage)) {
            this.apiRequestMessage = apiRequestMessage;
        }

        return this;
    }

    @Override
    public TransactionEntry loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionEntryFieldName.ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.id = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionEntryFieldName.TRANSACTION_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.transactionId = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionEntryFieldName.PRODUCT_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.productId = UUID.fromString(value);
        }

        this.quantity = rawJsonObject.optInt(TransactionEntryFieldName.QUANTITY.getFieldName());
        this.unitPrice = rawJsonObject.optDouble(TransactionEntryFieldName.UNIT_PRICE.getFieldName());

        this.apiRequestMessage = rawJsonObject.optString(TransactionEntryFieldName.API_REQUEST_MESSAGE.getFieldName());

        value = rawJsonObject.optString(TransactionEntryFieldName.API_REQUEST_STATUS.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.apiRequestStatus = TransactionApiRequestStatus.mapName(value);
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionEntryFieldName.ID.getFieldName(), this.id.toString());
            jsonObject.put(TransactionEntryFieldName.TRANSACTION_ID.getFieldName(), this.transactionId.toString());
            jsonObject.put(TransactionEntryFieldName.PRODUCT_ID.getFieldName(), this.productId.toString());
            jsonObject.put(TransactionEntryFieldName.QUANTITY.getFieldName(), this.quantity);
            jsonObject.put(TransactionEntryFieldName.UNIT_PRICE.getFieldName(), this.unitPrice);
            jsonObject.put(TransactionEntryFieldName.API_REQUEST_MESSAGE.getFieldName(), this.apiRequestMessage);
            jsonObject.put(TransactionEntryFieldName.API_REQUEST_STATUS.getFieldName(), this.apiRequestStatus.name());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public TransactionEntry() {
        this.id = new UUID(0, 0);
        this.transactionId = new UUID(0, 0);
        this.productId = new UUID(0,0);
        this.quantity = 0;
        this.unitPrice = 0.00;
        this.apiRequestMessage = StringUtils.EMPTY;
        this.apiRequestStatus = TransactionApiRequestStatus.OK;
    }
}
