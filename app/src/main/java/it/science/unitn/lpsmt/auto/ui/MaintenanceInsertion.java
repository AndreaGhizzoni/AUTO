package it.science.unitn.lpsmt.auto.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.ui.service.GPSService;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class MaintenanceInsertion extends ActionBarActivity {
    public static MaintenanceInsertion instance;
    private List<Vehicle> vehicleList;
    private Vehicle vehicleSelectedBySpinner;
    private Maintenance.Type maintenanceSelectedBySpinner;

    private ServiceConnection mConnection = new MyServiceConnection();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private Messenger mService;

    private Spinner spinnerVehicle;
    private Switch switchGetCurrentPlace;
    private EditText editCurrentPlace;
    private Spinner spinnerMaintenanceType;

    private void initSpinnerVehicleAssociated(){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),
            R.layout.frag_view_costs_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if( new DAOVehicle().countObject() != 0 ) {
            spinnerAdapter.add(getResources().getString(R.string.frag_view_costs_no_vehicle_selected));
            vehicleList = new DAOVehicle().getAll();
            for( Vehicle i : vehicleList ){
                spinnerAdapter.add(i.getName());
            }
        }else {
            spinnerAdapter.add("No Vehicle");
            // TODO disable the form
        }

        spinnerVehicle = (Spinner) findViewById(R.id.maintenance_insertion_vehicle_associated);
        spinnerVehicle.setOnItemSelectedListener(new SpinnerVehicleSelection());
        spinnerVehicle.setAdapter(spinnerAdapter);
    }

    private void initCurrentPlace(){
        this.editCurrentPlace = (EditText)findViewById(R.id.maintenance_insertion_place_edit);
        this.switchGetCurrentPlace = (Switch) findViewById(R.id.maintenance_insertion_switch_current_place);
        this.switchGetCurrentPlace.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if( b ) doBind(); else doUnBind();
                        editCurrentPlace.setEnabled(!b);
                    }
                }
        );
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

        spinnerMaintenanceType = (Spinner) findViewById(R.id.maintenance_insertion_type);
        spinnerMaintenanceType.setOnItemSelectedListener(new SpinnerMaintenanceTypeSelection());
        spinnerMaintenanceType.setAdapter(spinnerAdapter);
    }

    private void initSwitchAddCalendarEvent(){
        ((Switch)findViewById(R.id.maintenance_insertion_switch_add_calendar_event)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        LinearLayout layoutDate = (LinearLayout) findViewById(R.id.maintenance_insertion_inner_frag_extraordinary);
                        if (b)
                            layoutDate.setVisibility(View.VISIBLE);
                        else
                            layoutDate.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void initDatePickerEvent(){
        findViewById(R.id.maintenance_insertion_inner_frag_extraordinary_date).setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) showDatePickerDialog();
                    }
                });
    }

    public void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void doBind(){
        Intent gps = new Intent( getApplicationContext(), GPSService.class );
        getApplicationContext().bindService(gps, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnBind(){
       if( switchGetCurrentPlace.isEnabled() ) {
           if( mService != null ){
               Message msg = Message.obtain(null, GPSService.Protocol.REQUEST_UNBIND);
               msg.replyTo = mMessenger;
               try {
                   mService.send(msg);
               } catch (RemoteException e) {
                   Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
               }
               getApplicationContext().unbindService(mConnection);
               switchGetCurrentPlace.setEnabled(false);
           }
       }
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_insertion);

        this.initSpinnerVehicleAssociated();
        this.initCurrentPlace();
        this.initSpinnerTypeMaintenance();
        this.initSwitchAddCalendarEvent();
        this.initDatePickerEvent();
        instance = this;
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(this.switchGetCurrentPlace.isEnabled())
           doUnBind();
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
                Maintenance.Type newT = Maintenance.Type.valueOf(spinnerMaintenanceType.getSelectedItem().toString());
                if( !newT.equals(maintenanceSelectedBySpinner)) {
                    maintenanceSelectedBySpinner = newT;
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

    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage( Message msg ){
            switch (msg.what){
                case GPSService.Protocol.SEND_LOCATION:{
                    Bundle receivedBundle = msg.getData();
                    String address = receivedBundle.getString("address");
                    editCurrentPlace.setText(address);
                    break;
                }
                default: super.handleMessage(msg);
            }
        }
    }

    private class MyServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, GPSService.Protocol.REQUEST_BIND);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                Toast.makeText(
                    MaintenanceInsertion.this.getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { mService = null; }
    }
}
