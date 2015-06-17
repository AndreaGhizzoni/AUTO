package it.science.unitn.lpsmt.auto.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class MaintenanceInsertion extends ActionBarActivity {
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
        spinnerAdapter.add("-----");
        spinnerAdapter.add(Maintenance.Type.EXTRAORDINARY.toString());
        spinnerAdapter.add(Maintenance.Type.ORDINARY.toString());
        spinnerAdapter.add(Maintenance.Type.TAX.toString());

        maintenanceType = (Spinner) findViewById(R.id.maintenance_insertion_type);
        maintenanceType.setOnItemSelectedListener(new SpinnerMaintenanceTypeSelection());
        maintenanceType.setAdapter(spinnerAdapter);
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
            }else{
                maintenanceSelectedBySpinner = Maintenance.Type.valueOf(
                        maintenanceType.getSelectedItem().toString());
                Toast.makeText(getApplicationContext(), maintenanceSelectedBySpinner.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }
}
