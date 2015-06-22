package it.science.unitn.lpsmt.auto.ui;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

public class RefuelInsertion extends ActionBarActivity {
    public static final int REQUEST_CODE = 1010;

    private List<Vehicle> vehicleList;

    // gui components
    private Spinner spinnerVehicleAssociated;
    private EditText editKM;
    private EditText editCurrentPlace;
    private Switch switchGetCurrentPlace;
    private EditText editAmount;
    private EditText editPpl;
    private EditText editData;
    private EditText editNotes;


//==================================================================================================
//  METHOD
//==================================================================================================
    private void initSpinnerVehicleAssociated(){
        ArrayList<String> list = new ArrayList<>();
        if( new DAOVehicle().countObject() != 0 ) {
            list.add(getResources().getString(R.string.frag_view_costs_no_vehicle_selected));
            vehicleList = new DAOVehicle().getAll();
            for( Vehicle i : vehicleList ){
                list.add(i.getName());
            }
        }else {
            list.add(getResources().getString(R.string.frag_main_no_vehicle_inserted));
            // TODO disable the form
        }
        CustomArrayAdapter<CharSequence> spinnerAdapter = new CustomArrayAdapter<>(
            getApplicationContext(), list.toArray(new CharSequence[list.size()])
        );

        this.spinnerVehicleAssociated = (Spinner)findViewById(R.id.refuel_insertion_vehicle_associated);
        this.spinnerVehicleAssociated.setAdapter(spinnerAdapter);
    }

    private void initKm(){
        this.editKM = (EditText)findViewById(R.id.refuel_insertion_km);
    }

    private void initEditTextCurrentPlace(){
        this.editCurrentPlace = (EditText)findViewById(R.id.refuel_insertion_place_edit);
    }

    private void initSwitchGetCurrentPlace(){
        this.switchGetCurrentPlace = (Switch) findViewById(R.id.refuel_insertion_switch_current_place);
        this.switchGetCurrentPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                  if (b) doBind();
//                  else doUnBind();
                  editCurrentPlace.setEnabled(!b);
              }
          }
        );
    }

    private void initEditTextAmount(){
        this.editAmount = (EditText)findViewById(R.id.refuel_insertion_amount);
    }

    private void initEditTextPpl(){
        this.editPpl = (EditText)findViewById(R.id.refuel_insertion_ppl);
    }

    private void initSwitchToday(){
        Switch s = (Switch)findViewById(R.id.refuel_insertion_switch_current_date);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EditText edt = (EditText) findViewById(R.id.refuel_insertion_data_edit);
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    String format = "%d/%d/%d";
                    String date = String.format(format, day, month + 1, year);
                    edt.setText(date);
                } else {
                    edt.setText("");
                }
            }
        });
    }

    private void initEditTextDate(){
        this.editData = (EditText)findViewById(R.id.refuel_insertion_data_edit);
        this.editData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) showDatePickerDialog();
            }
        });
    }

    private void initEditTextNotes(){
        this.editNotes = (EditText)findViewById(R.id.refuel_insertion_notes);
    }

    private void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_insertion);

        this.initSpinnerVehicleAssociated();
        this.initKm();
        this.initEditTextCurrentPlace();
        this.initSwitchGetCurrentPlace();
        this.initEditTextAmount();
        this.initEditTextPpl();
        this.initEditTextDate();
        this.initSwitchToday();
        this.initEditTextNotes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_done_and_tts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch( id ){
            case R.id.done:{

                return true;
            }
            case R.id.tts:{

                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    static class CustomArrayAdapter<T> extends ArrayAdapter<T> {
        public CustomArrayAdapter(Context ctx, T [] objects){
            super(ctx, R.layout.spinner_item, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent){
            return super.getView(position, convertView, parent);
        }
    }

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
            EditText edt = (EditText) getActivity().findViewById(R.id.refuel_insertion_data_edit);
            edt.setText(date);
        }
    }

}
