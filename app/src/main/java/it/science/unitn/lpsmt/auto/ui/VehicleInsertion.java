package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class VehicleInsertion extends ActionBarActivity {
    // gui components
    private EditText editName;
    private EditText editPlate;
    private Spinner spinnerFuel;
    private EditText editPurchaseDate;

//==================================================================================================
//  METHOD
//==================================================================================================
    private void initComponents(){
        this.editName = (EditText)findViewById(R.id.vehicle_insertion_name);
        this.editPlate = (EditText)findViewById(R.id.vehicle_insertion_plate);

        this.spinnerFuel = (Spinner)findViewById(R.id.vehicle_insertion_spinner_fuel);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),R.layout.spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.add("select a fuel");
        for( String s : getResources().getStringArray(R.array.fuel) )
            spinnerAdapter.add(s);
        this.spinnerFuel.setAdapter(spinnerAdapter);

        this.editPurchaseDate = (EditText)findViewById(R.id.vehicle_insertion_purchase_data);
        this.editPurchaseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        DialogFragment newFragment = new MaintenanceInsertion.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_insertion);

        this.initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.done) {
            // check and save
            Toast.makeText(getApplicationContext(), "Done button", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
