package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

		this.employeeTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_employee));
	}

	@Override
	protected void onStart() {
		super.onStart();
        //adding begins
		/*View button_create_employee, button_report_product, button_report_cashier,button_start_transaction;

		button_start_transaction = this.findViewById(R.id.button_begin_transaction);
		button_create_employee = this.findViewById(R.id.button_create_employee);
		button_report_product = this.findViewById(R.id.button_product_sales_report);
		button_report_cashier = this.findViewById(R.id.button_cashier_sales_report);

		if(this.employeeTransition.getClassification().getValue() == 3) // cashier
		{
			button_create_employee.setVisibility(View.GONE);
			button_report_product.setVisibility(View.GONE);
			button_report_cashier.setVisibility(View.GONE);
		}
		else if (this.employeeTransition.getClassification().getValue() == 2) // shift manager
		{
			button_start_transaction.setVisibility(View.GONE);
			button_create_employee.setVisibility(View.GONE);
		}
		else if(this.employeeTransition.getClassification().getValue() == 1) // general manager
		{
			button_start_transaction.setVisibility(View.GONE);
		}
		else // not defined
		{
			button_start_transaction.setVisibility(View.GONE);
			button_create_employee.setVisibility(View.GONE);
			button_report_product.setVisibility(View.GONE);
			button_report_cashier.setVisibility(View.GONE);
		}*/


        // adding ends
		this.getEmployeeWelcomeTextView().setText("Welcome " + this.employeeTransition.getFirstName() + " (" + this.employeeTransition.getClassification().toString() + "  " + this.employeeTransition.getEmployeeId() + ")!");
	}

	@Override
	protected void onResume(){
		super.onResume();
		//adding begins
		View button_create_employee, button_report_product, button_report_cashier,button_start_transaction;

		button_start_transaction = this.findViewById(R.id.button_begin_transaction);
		button_create_employee = this.findViewById(R.id.button_create_employee);
		button_report_product = this.findViewById(R.id.button_product_sales_report);
		button_report_cashier = this.findViewById(R.id.button_cashier_sales_report);

		if(this.employeeTransition.getClassification().getValue() == 3) // cashier
		{
			button_create_employee.setVisibility(View.GONE);
			button_report_product.setVisibility(View.GONE);
			button_report_cashier.setVisibility(View.GONE);
		}
		else if (this.employeeTransition.getClassification().getValue() == 2) // shift manager
		{
			button_start_transaction.setVisibility(View.GONE);
			button_create_employee.setVisibility(View.GONE);
		}
		else if(this.employeeTransition.getClassification().getValue() == 1) // general manager
		{
			button_start_transaction.setVisibility(View.GONE);
		}
		else // not defined
		{
			button_start_transaction.setVisibility(View.GONE);
			button_create_employee.setVisibility(View.GONE);
			button_report_product.setVisibility(View.GONE);
			button_report_cashier.setVisibility(View.GONE);
		}


		// adding ends
	}
	public void beginTransactionButtonOnClick(View view) {
		Intent intent = new Intent(getApplicationContext(), CreateTransactionActivity.class);

		intent.putExtra(
				getString(R.string.intent_extra_employee),
				this.employeeTransition
		);

		this.startActivity(intent);
	}

	public void productSalesReportButtonOnClick(View view) {
		this.displayFunctionalityNotAvailableDialog();
	}

	public void cashierSalesReportButtonOnClick(View view) {
		this.displayFunctionalityNotAvailableDialog();
	}

	public void createEmployeeButtonOnClick(View view) {
		//this.displayFunctionalityNotAvailableDialog();
		// adding start
		Intent intent = new Intent(getApplicationContext(), CreateEmployeeActivity.class);

		intent.putExtra(
				getString(R.string.intent_extra_employee),
				this.employeeTransition
		);
		this.startActivity(intent);
		// adding end
	}

	public void logOutButtonOnClick(View view) {
		this.startActivity(new Intent(getApplicationContext(), LandingActivity.class));
	}

	private TextView getEmployeeWelcomeTextView() {
		return (TextView)this.findViewById(R.id.text_view_employee_welcome);
	}

	private void displayFunctionalityNotAvailableDialog() {
		new AlertDialog.Builder(this).
			setMessage(R.string.alert_dialog_functionality_not_available).
			setPositiveButton(
				R.string.button_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				}
			).
			create().
			show();
	}

	private EmployeeTransition employeeTransition;
}
