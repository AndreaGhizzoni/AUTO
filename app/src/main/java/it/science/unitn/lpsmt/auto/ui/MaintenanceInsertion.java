package it.science.unitn.lpsmt.auto.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class MaintenanceInsertion extends ActionBarActivity {
    public static MaintenanceInsertion instance;
    private List<Vehicle> vehicleList;
    private Vehicle vehicleSelectedBySpinner;
    private Maintenance.Type maintenanceSelectedBySpinner;

    private Spinner vehicleAssociated;
    private Spinner maintenanceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_insertion);

        this.initSpinnerVehicleAssociated();
        this.initSpinnerTypeMaintenance();
        instance = this;
    }

    private void initSpinnerVehicleAssociated(){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),
            R.layout.frag_view_costs_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if( new DAOVehicle().countObject() != 0 ) {
            spinnerAdapter.add(getResources().getString(R.string.frag_view_costs_no_vehicle_selected));
            // populate the spinner
            vehicleList = new DAOVehicle().getAll();
            for( Vehicle i : vehicleList ){
                spinnerAdapter.add(i.getName());
            }
        }else {
            spinnerAdapter.add("No Vehicle");
            // TODO disable the form
        }

        vehicleAssociated = (Spinner) findViewById(R.id.maintenance_insertion_vehicle_associated);
        vehicleAssociated.setOnItemSelectedListener(new SpinnerVehicleSelection());
        vehicleAssociated.setAdapter(spinnerAdapter);
    }

    private void initSpinnerTypeMaintenance(){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),
            R.layout.frag_view_costs_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.add(getResources().getString(R.string.activity_maintenance_insertion_select_type));
        spinnerAdapter.add(Maintenance.Type.EXTRAORDINARY.toString());
        spinnerAdapter.add(Maintenance.Type.ORDINARY.toString());
        spinnerAdapter.add(Maintenance.Type.TAX.toString());

        maintenanceType = (Spinner) findViewById(R.id.maintenance_insertion_type);
        maintenanceType.setOnItemSelectedListener(new SpinnerMaintenanceTypeSelection());
        maintenanceType.setAdapter(spinnerAdapter);
    }

    private void selectMaintenanceFragment(){
        Bundle args = new Bundle();
        args.putString(MaintenanceTypeArgsFragment.TYPE, maintenanceSelectedBySpinner.toString());

        Fragment f = new MaintenanceTypeArgsFragment();
        f.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.maintenance_insertion_type_args, f).commit();
    }

    public void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintenance_insertion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.done) {
            Toast.makeText(getApplicationContext(), "Done button", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public static class MaintenanceTypeArgsFragment extends Fragment {
        public static final String TYPE = "type";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            Maintenance.Type type = null;
            if (args != null && args.containsKey(TYPE)) {
                type = Maintenance.Type.valueOf((String) args.get(TYPE));
            }

            View v;
            switch (type) {
                case EXTRAORDINARY: {
                    v = inflater.inflate(R.layout.activity_maintenance_inner_frag_type_extraordinary,
                            container, false);
                    v.findViewById(R.id.maintenance_insertion_inner_frag_extraordinary_date)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MaintenanceInsertion.instance.showDatePickerDialog();
                                }
                            });
                    break;
                }
                default: { // tax and ordinary
                    v = inflater.inflate(R.layout.activity_maintenance_inner_frag_type_tax_ordinary,
                            container, false);
                    v.findViewById(R.id.btnAddCalendarEvent).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    break;
                }
            }
            return v;
        }
    }


    private class SpinnerVehicleSelection implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if( pos == 0 ) {
                vehicleSelectedBySpinner = null;
            }else{
                vehicleSelectedBySpinner = vehicleList.get(pos-1);
                Toast.makeText(getApplicationContext(), vehicleSelectedBySpinner.getId()+"",
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }


    private class SpinnerMaintenanceTypeSelection implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if( pos == 0 ){
                maintenanceSelectedBySpinner = null;
                Toast.makeText(getApplicationContext(), "pos == 0", Toast.LENGTH_LONG).show();
                findViewById(R.id.maintenance_insertion_type_args).setVisibility(View.INVISIBLE);
            }else{
                Maintenance.Type newT = Maintenance.Type.valueOf(maintenanceType.getSelectedItem().toString());
                if( !newT.equals(maintenanceSelectedBySpinner)) {
                    maintenanceSelectedBySpinner = newT;
                            Toast.makeText(getApplicationContext(), maintenanceSelectedBySpinner.toString(),
                                    Toast.LENGTH_LONG).show();
                    selectMaintenanceFragment();
                    findViewById(R.id.maintenance_insertion_type_args).setVisibility(View.VISIBLE);
                }
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
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
            EditText edt = (EditText) getActivity()
                    .findViewById(R.id.maintenance_insertion_inner_frag_extraordinary_date);
            edt.setText(date);
        }
    }
}
