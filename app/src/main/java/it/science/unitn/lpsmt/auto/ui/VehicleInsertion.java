package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class VehicleInsertion extends ActionBarActivity {
    public static final int RESULT_CODE = 1001;

    // gui components
    private EditText editName;
    private EditText editPlate;
    private Spinner spinnerFuel;
    private EditText editPurchaseDate;

//==================================================================================================
//  METHOD
//==================================================================================================
    private boolean save(){
        String n = this.editName.getText().toString();

        String plate = this.editPlate.getText().toString();

        Vehicle.Fuel f;
        int p = this.spinnerFuel.getSelectedItemPosition()-1;
        if( p == 0 )
            f = Vehicle.Fuel.GASOLINE;
        else if( p == 1 )
            f = Vehicle.Fuel.LPG;
        else if( p == 2 )
            f = Vehicle.Fuel.NATURAL_GAS;
        else f = Vehicle.Fuel.PETROL;

        Date purchaseDate = null;
        if( !this.editPurchaseDate.getText().toString().isEmpty() ){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                purchaseDate = sdf.parse(this.editPurchaseDate.getText().toString());
            } catch (ParseException e) {
                // toast
                return false;
            }
        }

        Vehicle v = new Vehicle(Const.NO_DB_ID_SET, n, plate, f, purchaseDate);
        new DAOVehicle().save(v);
        return true;
    }

    private boolean checkFields(){
        if( this.editName.getText().toString().isEmpty() ){
            // toast
            return false;
        }

        if( this.editPlate.getText().toString().isEmpty() ){
            // toast
            return false;
        }

        if( this.spinnerFuel.getSelectedItemPosition() == 0 ){
            // toast
            return false;
        }

        return true;
    }

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
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void displayToast(int resources){
        Toast.makeText(getApplicationContext(), getResources().getString(resources),
                Toast.LENGTH_LONG).show();
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
            if( checkFields() && save() ) {
                Toast.makeText(getApplicationContext(), "Done button", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            String format = "%d/%d/%d";
            String date = String.format(format, day, month+1, year);
            EditText edt = (EditText) getActivity().findViewById(R.id.vehicle_insertion_purchase_data);
            edt.setText(date);
        }
    }
}
