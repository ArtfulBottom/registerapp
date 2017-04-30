package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionEntryFieldName implements FieldNameInterface {
    ID("id"),
    TRANSACTION_ID("transactionId"),
    PRODUCT_ID("productId"),
    QUANTITY("quantity"),
    UNIT_PRICE("unitPrice"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    TransactionEntryFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}