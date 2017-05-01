package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.wrappers.TransactionViewWrapper;
import edu.uark.uarkregisterapp.models.api.enums.ProductApiRequestStatus;
import edu.uark.uarkregisterapp.models.api.services.ProductService;

public class CreateTransactionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.transactionViewWrapper = new TransactionViewWrapper((TableLayout) this.findViewById(R.id.transaction_table_layout));

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // Respond to the action bar's Up/Home button
                confirmExit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmExit() {
        new AlertDialog.Builder(CreateTransactionActivity.this).
            setTitle(R.string.alert_dialog_exit_transaction).
            setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).
            setNegativeButton(R.string.button_dismiss, null).
            setCancelable(false).
            create().
            show();
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

    public void addProductToView(Product product) {
        transactionViewWrapper.addProductToTable(product, this.getApplicationContext());
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
            if (product.getApiRequestStatus() != ProductApiRequestStatus.OK) {
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

    private TransactionViewWrapper transactionViewWrapper;
}
