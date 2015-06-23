package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.calendar.CalendarUtils;
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
    public static final int RESULT_CODE = 1000;
    private List<Vehicle> vehicleList;

    // graphical components necessary to save Maintenance object.
    private Spinner spinnerVehicle;
    private Switch switchGetCurrentPlace;
    private EditText editCurrentPlace;
    private Location locationFromGPS;
    private EditText editName;
    private EditText editAmount;
    private EditText editNotes;
    private Spinner spinnerMaintenanceType;
    private Switch switchAddCalendarEvent;
    private EditText editCalendarDate;
    private Switch switchTodayDate;

    // filed for gps service
    private ServiceConnection mConnection = new MyServiceConnection();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private Messenger mService;

    private boolean checkField(){
        if( this.spinnerVehicle.getSelectedItemPosition() == 0 ) {
            displayToast(R.string.activity_maintenance_insertion_vehicle_missing);
            return false;
        }

        if( this.editName.getText().toString().isEmpty() ){
            displayToast(R.string.activity_maintenance_insertion_name_missing);
            return false;
        }

        if( this.editAmount.getText().toString().isEmpty() ){
            displayToast(R.string.activity_maintenance_insertion_amount_missing);
            return false;
        }

        if( this.spinnerMaintenanceType.getSelectedItemPosition() == 0 ){
            displayToast(R.string.activity_maintenance_insertion_maintenance_type_missing);
            return false;
        }

        if( this.switchAddCalendarEvent.isChecked() &&
                this.editCalendarDate.getText().toString().isEmpty() ){
            displayToast(R.string.activity_maintenance_insertion_calendar_date_missing);
            return false;
        }

        return true;
    }

    private boolean save(){
        // get the vehicle from the spinner
        Vehicle v = vehicleList.get(this.spinnerVehicle.getSelectedItemPosition() - 1);

        // build a place from the location and the address from GPSService
        Place p = null;
        if( !editCurrentPlace.getText().toString().isEmpty() ){
            p = new Place(Const.NO_DB_ID_SET, editCurrentPlace.getText().toString(), locationFromGPS);
        }

        // get the cost name form edit
        String name = this.editName.getText().toString();

        // get the amount from edit
        Float amount = Float.parseFloat(this.editAmount.getText().toString());

        // get the notes if is not empty
        String notes = "";
        if( !this.editNotes.getText().toString().isEmpty() ){
            notes = this.editNotes.getText().toString();
        }

        // parse the maintenance type because metalanguages support I can not use Type.valueOf()
        Maintenance.Type t;
        int pos = this.spinnerMaintenanceType.getSelectedItemPosition()-1;
        if( pos == 0 )
            t = Maintenance.Type.EXTRAORDINARY;
        else if( pos == 1 )
            t = Maintenance.Type.ORDINARY;
        else t = Maintenance.Type.TAX;

        // create the calendar event if the switch is enabled.
        Long calendarID = null;// stub
        if( this.switchAddCalendarEvent.isChecked() ){
            String date = this.editCalendarDate.getText().toString();
            CalendarUtils.Holder h = new CalendarUtils.Holder();
            h.title = "[AUTO] "+name;
            if( p != null ) h.location = p.getAddress();
            h.description = notes;
            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dStart;
            try {
                dStart = f.parse(date);
            } catch (ParseException ignored){
                displayToast(R.string.activity_maintenance_insertion_calendar_date_parse_error);
                return false;
            }
            h.dStart = dStart;
            h.dEnd = dStart;
            switch( t ){
                case ORDINARY: {
                    h.rRule = CalendarUtils.FREQ_YEARLY;
                    h.hasAlarm = true;
                    calendarID = CalendarUtils.getInstance().putEventWithReminder(
                            h,
                            CalendarUtils.REMINDER_FIVE_DAYS
                    );
                    break;
                }
                case TAX: {
                    h.rRule = CalendarUtils.FREQ_YEARLY;
                    h.hasAlarm = true;
                    calendarID = CalendarUtils.getInstance().putEventWithReminder(
                            h,
                            CalendarUtils.REMINDER_FIFTEEN_DAYS
                    );
                    break;
                }
                case EXTRAORDINARY: {
                    h.hasAlarm = false;
                    calendarID = CalendarUtils.getInstance().putEvent(h);
                    break;
                }
            }
        }

        // create the maintenance object and save it
        Maintenance m = new Maintenance(Const.NO_DB_ID_SET, v, amount, notes, name, t, p, calendarID.intValue() );
        new DAOCost().save(m);
        return true;
    }

    private void displayToast(int resources){
        Toast.makeText(getApplicationContext(), getResources().getString(resources),
                Toast.LENGTH_LONG).show();
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

    private void initEditNotes(){
        this.editNotes = (EditText)findViewById(R.id.maintenance_insertion_notes);
    }

    private void initSpinnerTypeMaintenance(){
        ArrayList<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.activity_maintenance_insertion_select_type));
        Collections.addAll(list, getResources().getStringArray(R.array.maintenance_type));
        CustomArrayAdapter<CharSequence> spinnerAdapter = new CustomArrayAdapter<>(
            getApplicationContext(), list.toArray(new CharSequence[list.size()])
        );
        spinnerMaintenanceType = (Spinner) findViewById(R.id.maintenance_insertion_type);
        spinnerMaintenanceType.setAdapter(spinnerAdapter);
    }

    private void initSwitchAddCalendarEvent(){
        this.switchAddCalendarEvent = (Switch)findViewById(R.id.maintenance_insertion_switch_add_calendar_event);
        this.switchAddCalendarEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinearLayout layoutDate = (LinearLayout) findViewById(R.id.maintenance_insertion_calendar_event);
                if (b)
                    layoutDate.setVisibility(View.VISIBLE);
                else
                    layoutDate.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initDatePickerEvent(){
        this.editCalendarDate = (EditText)findViewById(R.id.maintenance_insertion_calendar_event_edit);
        this.editCalendarDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
    }

    private void initSwitchToday(){
        this.switchTodayDate = (Switch)findViewById(R.id.maintenance_insertion_switch_current_date);
        this.switchTodayDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EditText edt = (EditText)findViewById(R.id.maintenance_insertion_calendar_event_edit);
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    String format = "%d/%d/%d";
                    String date = String.format(format, day, month + 1, year);
                    edt.setText(date);
                }else{
                    edt.setText("");
                }
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
        this.initEditNotes();
        this.initSpinnerTypeMaintenance();
        this.initSwitchAddCalendarEvent();
        this.initDatePickerEvent();
        this.initSwitchToday();
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
        getMenuInflater().inflate(R.menu.menu_action_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.done) {
            if( checkField() && save() ) {
                Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(R.string.activity_maintenance_insertion_maintenance_saved_success),
                    Toast.LENGTH_LONG
                ).show();

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
            EditText edt = (EditText) getActivity()
                    .findViewById(R.id.maintenance_insertion_calendar_event_edit);
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
                    removeMessages(msg.what);
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
        public void onServiceDisconnected(ComponentName name){
            mService = null;
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
