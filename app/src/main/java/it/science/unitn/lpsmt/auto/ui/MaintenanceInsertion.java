package it.science.unitn.lpsmt.auto.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
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

import it.science.unitn.lpsmt.auto.controller.dao.DAOCost;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;
import it.science.unitn.lpsmt.auto.ui.service.GPSService;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class MaintenanceInsertion extends ActionBarActivity {
    private List<Vehicle> vehicleList;

    // graphical components necessary to save Maintenance object.
    private Spinner spinnerVehicle;
    private Switch switchGetCurrentPlace;
    private EditText editCurrentPlace;
    private Location locationFromGPS;
    private EditText editName;
    private EditText editAmount;
    private Spinner spinnerMaintenanceType;
    private Switch switchAddCalendarEvent;
    private EditText editCalendarDate;

    // filed for gps service
    private ServiceConnection mConnection = new MyServiceConnection();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private Messenger mService;

    private boolean checkField(){
        if( this.spinnerVehicle.getSelectedItemPosition() == 0 ) {
            displayToast("Select a vehicle");
            return false;
        }

        if( this.editName.getText().toString().isEmpty() ){
            displayToast("Name missing");
            return false;
        }

        if( this.editAmount.getText().toString().isEmpty() ){
            displayToast("Amount missing");
            return false;
        }

        if( this.spinnerMaintenanceType.getSelectedItemPosition() == 0 ){
            displayToast("Select a maintenance type");
            return false;
        }

        if( this.switchAddCalendarEvent.isChecked() &&
                this.editCalendarDate.getText().toString().isEmpty() ){
            displayToast("Calendar date missing");
            return false;
        }

        return true;
    }

    private void save(){
        Vehicle v = vehicleList.get(this.spinnerVehicle.getSelectedItemPosition()-1);
        Place p = null;
        if( !editCurrentPlace.getText().toString().isEmpty() ){
            p = new Place(Const.NO_DB_ID_SET, editCurrentPlace.getText().toString(), locationFromGPS);
        }
        String name = this.editName.getText().toString();
        Float amount = Float.parseFloat(this.editAmount.getText().toString());
        Maintenance.Type t;
        int pos = this.spinnerMaintenanceType.getSelectedItemPosition()-1;
        if( pos == 0 )
            t = Maintenance.Type.EXTRAORDINARY;
        else if( pos == 1 )
            t = Maintenance.Type.ORDINARY;
        else t = Maintenance.Type.TAX;
        Integer calendarID = 1;// stub
        if( this.switchAddCalendarEvent.isChecked() ){
            // set calendar id
        }

        Maintenance m = new Maintenance(Const.NO_DB_ID_SET, v, amount, "", name, t, p, calendarID);
        new DAOCost().save(m);
    }

    private void displayToast(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

//==================================================================================================
//  GPS SERVICE METHODS
//==================================================================================================
    private void doBind(){
        Intent gps = new Intent( getApplicationContext(), GPSService.class );
        getApplicationContext().bindService(gps, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnBind(){
       if( this.switchGetCurrentPlace.isChecked() ) {
           if( this.mService != null ){
               Message msg = Message.obtain(null, GPSService.Protocol.REQUEST_UNBIND);
               msg.replyTo = mMessenger;
               try {
                   mService.send(msg);
               } catch (RemoteException e) {
                   Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
               }
               getApplicationContext().unbindService(mConnection);
           }
       }
    }

//==================================================================================================
//  INIT METHODS
//==================================================================================================
    private void initSpinnerVehicleAssociated(){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),
            R.layout.spinner_item
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
        spinnerVehicle.setAdapter(spinnerAdapter);
    }

    private void initEditTextCurrentPlace(){
        this.editCurrentPlace = (EditText)findViewById(R.id.maintenance_insertion_place_edit);

    }

    private void initSwitchGetCurrentPlace(){
        this.switchGetCurrentPlace = (Switch) findViewById(R.id.maintenance_insertion_switch_current_place);
        this.switchGetCurrentPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) doBind();
                    else doUnBind();
                    editCurrentPlace.setEnabled(!b);
                }
            }
        );
    }

    private void initEditTextName(){
        this.editName = (EditText)findViewById(R.id.maintenance_insertion_name);
    }

    private void initEditTextAmount(){
        this.editAmount = (EditText)findViewById(R.id.maintenance_insertion_amount);
    }

    private void initSpinnerTypeMaintenance(){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            getApplicationContext(),
            R.layout.spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.add(getResources().getString(R.string.activity_maintenance_insertion_select_type));
        for( String s : getResources().getStringArray(R.array.maintenance_type) )
            spinnerAdapter.add(s);

        spinnerMaintenanceType = (Spinner) findViewById(R.id.maintenance_insertion_type);
        spinnerMaintenanceType.setAdapter(spinnerAdapter);
    }

    private void initSwitchAddCalendarEvent(){
        this.switchAddCalendarEvent = (Switch)findViewById(R.id.maintenance_insertion_switch_add_calendar_event);
        this.switchAddCalendarEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        this.editCalendarDate = (EditText)findViewById(R.id.maintenance_insertion_inner_frag_extraordinary_date);
        this.editCalendarDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_insertion);

        this.initSpinnerVehicleAssociated();
        this.initEditTextCurrentPlace();
        this.initSwitchGetCurrentPlace();
        this.initEditTextName();
        this.initEditTextAmount();
        this.initSpinnerTypeMaintenance();
        this.initSwitchAddCalendarEvent();
        this.initDatePickerEvent();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(this.switchGetCurrentPlace.isChecked()) {
            doUnBind();
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        if(this.switchGetCurrentPlace.isChecked())
            doBind();
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
            if( checkField() ) {
                save();
                Toast.makeText(getApplicationContext(), "Done button", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
//    private class SpinnerVehicleSelection implements AdapterView.OnItemSelectedListener{
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            if( pos == 0 ) {
//                vehicleSelectedBySpinner = null;
//            }else{
//                vehicleSelectedBySpinner = vehicleList.get(pos-1);
//            }
//        }
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {}
//    }
//
//    private class SpinnerMaintenanceTypeSelection implements AdapterView.OnItemSelectedListener{
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//            if( pos == 0 ){
//                maintenanceSelectedBySpinner = null;
//            }else{
//                Maintenance.Type newT = Maintenance.Type.valueOf(spinnerMaintenanceType.getSelectedItem().toString());
//                if( !newT.equals(maintenanceSelectedBySpinner)) {
//                    maintenanceSelectedBySpinner = newT;
//                }
//            }
//        }
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {}
//    }

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
                    String address = receivedBundle.getString(GPSService.Protocol.RETRIEVED_ADDRESS);
                    locationFromGPS = receivedBundle.getParcelable(GPSService.Protocol.RETRIEVED_LOCATION);
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
