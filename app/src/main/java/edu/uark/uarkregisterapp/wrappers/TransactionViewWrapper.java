package edu.uark.uarkregisterapp.wrappers;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.enums.TransactionClassification;
import edu.uark.uarkregisterapp.wrappers.constants.TransactionViewConstants;

public class TransactionViewWrapper extends TransactionViewConstants {
    private TransactionViewWrapper addButtonToRow(Context context, int resId, TableRow row) {
        ImageButton button = new ImageButton(context);
        button.setImageResource(resId);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View row = (View) view.getParent();
                ViewGroup container = (ViewGroup) row.getParent();
                container.removeView(row);
                container.invalidate();

                getTransactionEntries().remove(row.getId() - getMinimumRequiredRows());
                resetRowIds();
            }
        });
        row.addView(button);

        return this;
    }

    private TransactionViewWrapper addEditTextToRow(Context context, int inputType, int color, int textSize, String text, int gravity, TableRow row) {
        EditText editText = new EditText(context);
        editText.setInputType(inputType);
        editText.setTextColor(color);
        editText.setTextSize(textSize);
        editText.setText(text);
        editText.setGravity(gravity);
        row.addView(editText);

        return this;
    }

    private TransactionViewWrapper addTextViewToRow(Context context, int color, int textSize, String text, int gravity, TableRow row) {
        TextView textView = new TextView(context);
        textView.setTextColor(color);
        textView.setTextSize(textSize);
        textView.setText(text);
        textView.setGravity(gravity);
        row.addView(textView);

        return this;
    }

    private TransactionViewWrapper addRowToTable(TableRow row, Product product, Context context) {
        this.addTextViewToRow(context, Color.BLACK, this.defaultTextSize, product.getLookupCode(), Gravity.LEFT, row).
                addTextViewToRow(context, Color.BLACK, this.defaultTextSize, String.format("$%.2f", product.getPrice()), Gravity.RIGHT, row).
                addEditTextToRow(context, InputType.TYPE_CLASS_NUMBER, Color.BLACK, this.defaultTextSize, Integer.toString(defaultQuantity), Gravity.RIGHT, row).
                addButtonToRow(context, R.drawable.trash_can, row);

        this.getTableLayout().addView(row);
        return this;
    }

    private TransactionViewWrapper createTableRow (Product product, Context context) {
        TableRow row = new TableRow(context);
        LayoutParams layoutParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        row.setLayoutParams(layoutParams);
        row.setId(this.rowId++);
        this.addRowToTable(row, product, context);

        return this;
    }

    private TransactionViewWrapper addTransactionEntryToTable(Product product) {
        TransactionEntry entry = new TransactionEntry();
        entry.setProductId(product.getId());
        entry.setUnitPrice(product.getPrice());
        entry.setQuantity(product.getCount());
        this.entries.add(entry);

        return this;
    }

    private TransactionViewWrapper resetRowIds() {
        this.rowId = this.minimumRequiredRows;

        for (int i = this.minimumRequiredRows; i < this.getTableLayout().getChildCount(); ++i) {
            (this.getTableLayout().getChildAt(i)).setId(this.rowId++);
        }

        return this;
    }

    private int getMinimumRequiredRows() {
        return this.minimumRequiredRows;
    }

    public Transaction constructTransactionObject(UUID employeeId) {
        double totalAmount = 0;
        for (int i = this.minimumRequiredRows; i < this.getTableLayout().getChildCount(); ++i) {
            TableRow row = (TableRow) this.getTableLayout().getChildAt(i);
            TransactionEntry entry = this.entries.get(i - this.minimumRequiredRows);
            int selectedQuantity = Integer.parseInt(((EditText) row.getChildAt(this.quantityEditTextPosition)).getText().toString());

            if (selectedQuantity > entry.getQuantity()) {
                return (new Transaction()).
                        setApiRequestStatus(TransactionApiRequestStatus.INVALID_INPUT).
                        setApiRequestMessage("Quantity for " +
                                ((TextView) row.getChildAt(this.lookupCodeTextViewPosition)).getText().toString() +
                                " is too high.");
            }
            else {
                entry.setQuantity(selectedQuantity);
                totalAmount += entry.getUnitPrice() * entry.getQuantity();
            }
        }

        return (new Transaction()).
                setCashierId(employeeId).
                setClassification(TransactionClassification.SALE).
                setTotalAmount((int) totalAmount);
    }

    public void addProductToTable(Product product, Context context) {
        // TODO: Check for product uniqueness with this.checkProductIsUnique(product).
        this.createTableRow(product, context).
                addTransactionEntryToTable(product);
    }

    public boolean isTableEmpty() {
        return this.getTableLayout().getChildCount() == this.minimumRequiredRows;
    }

    private int rowId = this.minimumRequiredRows; // First two rows are field descriptions and solid black line.

    private ArrayList<TransactionEntry> entries;
    public ArrayList<TransactionEntry> getTransactionEntries() {
        return this.entries;
    }

    private TableLayout tableLayout;
    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }
    public TableLayout getTableLayout() {
        return this.tableLayout;
    }

    public TransactionViewWrapper(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
        entries = new ArrayList<>();
    }
}
