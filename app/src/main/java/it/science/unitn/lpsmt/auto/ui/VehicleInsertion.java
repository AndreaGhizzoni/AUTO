package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.controller.util.DateUtils;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class VehicleInsertion extends ActionBarActivity {
    public static final int RESULT_CODE = 1001;
    public static final String UPDATE_VEHICLE = "update_vehicle_id";

    private Vehicle vehicleToUpdate;

    // gui components
    private EditText editName;
    private EditText editPlate;
    private Spinner spinnerFuel;
    private EditText editPurchaseDate;

//==================================================================================================
//  SAVING METHOD
//==================================================================================================
    private Vehicle buildVehicleFromUserData(){
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
                displayToast(R.string.activity_vehicle_insertion_purchase_date_parse_error);
                return null;
            }
        }

        return new Vehicle(Const.NO_DB_ID_SET, n, plate, f, purchaseDate);
    }

    private boolean checkFields(){
        if( this.editName.getText().toString().isEmpty() ){
            displayToast(R.string.activity_vehicle_insertion_name_missing);
            return false;
        }

        if( this.editPlate.getText().toString().isEmpty() ){
            displayToast(R.string.activity_vehicle_insertion_plate_missing);
            return false;
        }

        if( this.spinnerFuel.getSelectedItemPosition() == 0 ){
            displayToast(R.string.activity_vehicle_insertion_fuel_not_selected);
            return false;
        }

        return true;
    }

//==================================================================================================
//  INIT METHOD
//==================================================================================================
    private void initComponents(){
        this.editName = (EditText)findViewById(R.id.vehicle_insertion_name);
        this.editPlate = (EditText)findViewById(R.id.vehicle_insertion_plate);

        ArrayList<CharSequence> lst = new ArrayList<>();
        lst.add(getResources().getString(R.string.activity_vehicle_insertion_select_fuel));
        Collections.addAll(lst, getResources().getStringArray(R.array.fuel));
        CustomArrayAdapter<CharSequence> spinnerAdapter = new CustomArrayAdapter<>(
            getApplicationContext(), lst.toArray(new CharSequence[lst.size()])
        );

        this.spinnerFuel = (Spinner)findViewById(R.id.vehicle_insertion_spinner_fuel);
        this.spinnerFuel.setAdapter(spinnerAdapter);

        this.editPurchaseDate = (EditText)findViewById(R.id.vehicle_insertion_purchase_data);
        this.editPurchaseDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
    }

    private void populateWithVehicleToUpdate(){
        this.editName.setText( this.vehicleToUpdate.getName() );
        this.editPlate.setText( this.vehicleToUpdate.getPlate() );
        if( this.vehicleToUpdate.getPurchaseDate() != null ){
            this.editPurchaseDate.setText(DateUtils.getStringFromDate(vehicleToUpdate.getPurchaseDate(), "dd/MM/yyyy"));
        }
        switch( this.vehicleToUpdate.getFuel() ){
            case GASOLINE: {
                this.spinnerFuel.setSelection(1);
                break;
            }
            case LPG: {
                this.spinnerFuel.setSelection(2);
                break;
            }
            case NATURAL_GAS: {
                this.spinnerFuel.setSelection(3);
                break;
            }
            case PETROL: {
                this.spinnerFuel.setSelection(4);
                break;
            }
        }
    }

//==================================================================================================
//  UTILITIES METHOD
//==================================================================================================
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
        Bundle args = getIntent().getExtras();
        if( args != null && args.containsKey(UPDATE_VEHICLE) ){
            Long id = (Long) args.get(UPDATE_VEHICLE);
            vehicleToUpdate = new DAOVehicle().get(id);
            this.populateWithVehicleToUpdate();
        }
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
        if (id == R.id.done){
            if( checkFields() ){
                Vehicle newest = buildVehicleFromUserData();
                if( newest == null ) {
                    return false;
                }else {
                    if(vehicleToUpdate != null) {
                        int rowUpdated = new DAOVehicle().update(vehicleToUpdate, newest);
                        if( rowUpdated == -1 ) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(R.string.activity_vehicle_insertion_vehicle_update_error),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }else if( rowUpdated == 0 ){
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(R.string.activity_vehicle_insertion_vehicle_update_nothing_todo),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources().getString(R.string.activity_vehicle_insertion_vehicle_update_success),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    } else {
                        Toast.makeText(
                            getApplicationContext(),
                            R.string.activity_vehicle_insertion_vehicle_save_success,
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
            return super.onOptionsItemSelected(item);
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

    static class CustomArrayAdapter<T> extends ArrayAdapter<T>{
        public CustomArrayAdapter(Context ctx, T [] objects){
            super(ctx, R.layout.spinner_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent){
            return super.getView(position, convertView, parent);
        }
    }
}
