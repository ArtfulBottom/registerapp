package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.InputMismatchException;

import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiRequestStatus;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;
import edu.uark.uarkregisterapp.wrappers.DataWrapper;
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
        this.employeeTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_employee));

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (!this.transactionViewWrapper.isTableEmpty()) {
            confirmExit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // Respond to the action bar's Up/Home button
                if (!this.transactionViewWrapper.isTableEmpty()) {
                    confirmExit();
                } else {
                    this.finish();
                }
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
            setNegativeButton(R.string.button_cancel, null).
            setCancelable(false).
            create().
            show();
    }

    public void checkoutButtonOnClick(View view) {
        if (this.transactionViewWrapper.isTableEmpty()) {
            this.displayAlertDialog(R.string.alert_dialog_no_transaction_products);
        } else {
            try {
                this.transactionViewWrapper.constructTransactionAndTransactionEntries(this.employeeTransition.getId());
            } catch (NumberFormatException e) {
                this.displayAlertDialog(R.string.alert_dialog_transaction_validation_quantity);
                return;
            }

            Transaction transaction = this.transactionViewWrapper.getTransaction();
            ArrayList<TransactionEntry> entries = this.transactionViewWrapper.getTransactionEntries();

            if (transaction.getApiRequestStatus() == TransactionApiRequestStatus.INVALID_INPUT) {
                this.displayAlertDialog(transaction.getApiRequestMessage());
            }
            else {
                Intent intent = new Intent(getApplicationContext(), SummaryActivity.class);

                intent.putExtra(
                        getString(R.string.intent_extra_transaction),
                        new TransactionTransition(transaction)
                );

                intent.putExtra(
                        getString(R.string.intent_extra_transaction_entries),
                        new DataWrapper(entries)
                );

                this.startActivity(intent);
            }
        }
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
        if (!transactionViewWrapper.addProductToTable(product, this.getApplicationContext())) {
            this.displayAlertDialog(R.string.alert_dialog_product_already_exists);
        }
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
                displayAlertDialog(R.string.alert_dialog_retrieve_product_failed);
            }
            else {
                addProductToView(product);
            }
        }
    }

    private void displayAlertDialog(String message) {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(message).
                create().
                show();
    }

    private void displayAlertDialog(int stringId) {
        new AlertDialog.Builder(CreateTransactionActivity.this).
                setMessage(stringId).
                create().
                show();
    }

    private EmployeeTransition employeeTransition;
    private TransactionViewWrapper transactionViewWrapper;
}
