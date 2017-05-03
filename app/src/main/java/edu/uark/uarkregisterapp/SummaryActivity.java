package edu.uark.uarkregisterapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;
import edu.uark.uarkregisterapp.wrappers.DataWrapper;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        this.transactionTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_transaction));
        this.entries = ((DataWrapper) this.getIntent().getSerializableExtra(this.getString(R.string.intent_extra_transaction_entries))).getEntries();

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.getTotalPriceTextView().setText("$" + this.transactionTransition.getTotalAmount());
        log();
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

    private TextView getTotalPriceTextView() {
        return (TextView)this.findViewById(R.id.text_view_price_number);
    }

    public void log() {
        for (TransactionEntry entry : this.entries) {
            Log.d("ProductId = ", "" + entry.getProductId());
            Log.d("Quantity = ", "" + entry.getQuantity());
            Log.d("Unit price = ", "" + entry.getUnitPrice());
        }

        Log.d("CashierId = ", "" + this.transactionTransition.getCashierId());
        Log.d("Classification = ", "" + this.transactionTransition.getClassification());
        Log.d("ReferenceId = ", "" + this.transactionTransition.getReferenceId());
        Log.d("TotalAmount = ", "" + this.transactionTransition.getTotalAmount());
    }

    private ArrayList<TransactionEntry> entries;
    private TransactionTransition transactionTransition;
}
