package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.ProductApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.enums.TransactionClassification;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;

public class CreateTransactionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        //setRelativeLayout();
        setTableLayout();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // Respond to the action bar's Up/Home button
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

 /*   private RelativeLayout layout;
    public void setRelativeLayout() {
        layout = (RelativeLayout) this.findViewById(R.id.transaction_relative_layout);
    }
    public RelativeLayout getRelativeLayout() {
        return this.layout;
    }
*/
    private TableLayout tableLayout;
    public void setTableLayout() {
        tableLayout = (TableLayout) this.findViewById(R.id.transaction_table_layout);
    }
    public TableLayout getTableLayout() {
        return this.tableLayout;
    }

    public void addProductButtonOnClick(View view) {
        final EditText input = new EditText(CreateTransactionActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(CreateTransactionActivity.this).
            setView(input).
            setTitle(R.string.alert_dialog_enter_product_lookupcode).
            setPositiveButton(R.string.alert_dialog_add_product, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    (new AddProductTask()).execute(input.getText().toString());
                }
            }).
            setCancelable(true).
            create().
            show();
    }


    private ArrayList<TransactionEntry> entries = null;
    private int lastId;
    public void addProductToView(Product product) {
        if (entries == null) {
            entries = new ArrayList<>();
        }

        TransactionEntry entry = new TransactionEntry();
        int selectedQuantity = 1;
        entry.setProductId(product.getId());
        entry.setUnitPrice(product.getPrice());
        entry.setQuantity(selectedQuantity);
        this.entries.add(entry);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        row.setLayoutParams(lp);

        TextView lookup = new TextView(this);
        lookup.setText(product.getLookupCode());
        row.addView(lookup);

        TextView price = new TextView(this);
        price.setText("" + product.getPrice());
        row.addView(price);

        TextView quantity = new TextView(this);
        quantity.setText("" + selectedQuantity);
        row.addView(quantity);

        ImageButton removeBtn = new ImageButton(this);
        removeBtn.setImageResource(R.drawable.trash_can);
        row.addView(removeBtn);

        this.getTableLayout().addView(row);

/*        text.setTextSize(20);
        text.setLayoutParams(params);
        text.setTextColor(Color.BLACK);
*/
    }

    private class AddProductTask extends AsyncTask<String, Void, Product> {
        @Override
        protected Product doInBackground(String... params) {
            if (params.length > 0) {
                return (new ProductService()).getProductByLookupCode(params[0]);
            } else {
                return (new Product()).
                        setApiRequestStatus(ProductApiRequestStatus.NOT_FOUND);
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            if (product.getApiRequestStatus() != ProductApiRequestStatus.OK ||
                product.getApiRequestStatus() == ProductApiRequestStatus.NOT_FOUND) {
                new AlertDialog.Builder(CreateTransactionActivity.this).
                        setMessage(R.string.alert_dialog_retrieve_product_failed).
                        create().
                        show();
            }
            else {
                addProductToView(product);
            }
        }
    }
}
