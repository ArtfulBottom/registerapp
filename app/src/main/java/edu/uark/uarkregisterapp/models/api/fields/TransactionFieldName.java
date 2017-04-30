package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    ID("id"),
    CASHIER_ID("cashierId"),
    TOTAL_AMOUNT("totalAmount"),
    CLASSIFICATION("classification"),
    CREATED_ON("createdOn"),
    REFERENCE_ID("referenceId"),
    API_REQUEST_STATUS("apiRequestStatus"),
    API_REQUEST_MESSAGE("apiRequestMessage");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }

    TransactionFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}