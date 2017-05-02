package edu.uark.uarkregisterapp.wrappers;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
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

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.wrappers.constants.TransactionViewConstants;

public class TransactionViewWrapper extends TransactionViewConstants {
    private ArrayList<TransactionEntry> entries = null;

    private TableLayout tableLayout;
    public void setTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }
    public TableLayout getTableLayout() {
        return this.tableLayout;
    }

    public boolean isTableEmpty() {
        return this.getTableLayout().getChildCount() == this.minimumRequiredRows ? true : false;
    }

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
        this.addRowToTable(row, product, context);

        return this;
    }

    private TransactionViewWrapper addTransactionEntryToTable(Product product, Context context) {
        if (entries == null) {
            entries = new ArrayList<>();
        }

        /*TransactionEntry entry = new TransactionEntry();
        entry.setProductId(product.getId());
        entry.setUnitPrice(product.getPrice());
        entry.setQuantity(defaultQuantity);
        this.entries.add(entry);*/

        return this;
    }

    public void addProductToTable(Product product, Context context) {
        this.createTableRow(product, context);
    }

    public TransactionViewWrapper(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }
}
