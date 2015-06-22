package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import it.science.unitn.lpsmt.auto.controller.dao.DAOCost;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;
import it.science.unitn.lpsmt.auto.ui.service.GPSService;
import lpsmt.science.unitn.it.auto.R;

public class RefuelInsertion extends ActionBarActivity {
    public static String TAG = RefuelInsertion.class.getSimpleName();
    public static final int REQUEST_CODE = 1010;
    public static final int STT_REQUEST_CODE = 1011;

    private List<Vehicle> vehicleList;
    private Location locationFromGPS;

    // gui components
    private Spinner spinnerVehicleAssociated;
    private EditText editKM;
    private EditText editCurrentPlace;
    private Switch switchGetCurrentPlace;
    private EditText editAmount;
    private EditText editPpl;
    private EditText editDate;
    private EditText editNotes;

    // filed for gps service
    private ServiceConnection mConnection = new MyServiceConnection();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private Messenger mService;

//==================================================================================================
//  CHECK AND SAVING METHODS
//==================================================================================================
    private boolean save(){
        // get the associated vehicle
        Vehicle v = this.vehicleList.get(this.spinnerVehicleAssociated.getSelectedItemPosition()-1);

        // build new Place
        Place p = new Place(Const.NO_DB_ID_SET, this.editCurrentPlace.getText().toString(), this.locationFromGPS);

        // get the km
        Integer km = Integer.parseInt(this.editKM.getText().toString());

        // get the amount
        Float amount = Float.parseFloat(this.editAmount.getText().toString());

        // get the price per liter
        Float ppl = Float.parseFloat(this.editPpl.getText().toString());

        // get the data
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date atData;
        try {
            atData = f.parse(this.editDate.getText().toString());
        } catch (ParseException i){
            displayToast(R.string.activity_refuel_insertion_refuel_data_parse_error);
            return false;
        }

        // get the notes
        String notes = "";
        if( !this.editNotes.getText().toString().isEmpty() ){
            notes = this.editNotes.getText().toString();
        }

        Refuel r = new Refuel(Const.NO_DB_ID_SET, v, amount, notes, ppl, atData, km, p );
        new DAOCost().save(r);
        return true;
    }

    private boolean checkFields(){
        if( this.spinnerVehicleAssociated.getSelectedItemPosition() == 0 ) {
            displayToast(R.string.activity_refuel_insertion_vehicle_not_selected);
            return false;
        }

        if( this.editKM.getText().toString().isEmpty() ){
            displayToast(R.string.activity_refuel_insertion_vehicle_km_missing);
            return false;
        }

        if( this.editAmount.getText().toString().isEmpty() ){
            displayToast(R.string.activity_refuel_insertion_refuel_amount_missing);
            return false;
        }

        if( this.editPpl.getText().toString().isEmpty() ){
            displayToast(R.string.activity_refuel_insertion_refuel_price_per_liter_missing);
            return false;
        }

        if( this.editDate.getText().toString().isEmpty() ){
            displayToast(R.string.activity_refuel_insertion_refuel_data_missing);
            return false;
        }

        return true;
    }

//==================================================================================================
//  UTILITIES METHODS
//==================================================================================================
    private void showDatePickerDialog(){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                getResources().getString(R.string.activity_refuel_insertion_tts_text)
        );
        try {
            startActivityForResult(intent, STT_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(
                getApplicationContext(),
                getResources().getString(R.string.activity_refuel_insertion_tts_error),
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void parseTTS( String tts ){
        //tts is the best matching tts
        ArrayDeque<String> userSpeech = new ArrayDeque<>();

        StringTokenizer tokenizer = new StringTokenizer(tts, " ");
        while (tokenizer.hasMoreTokens()){
            String token = tokenizer.nextToken();
            userSpeech.add(token);
        }

        // Check for the first item
        String first = userSpeech.pop();
        if (first.equals("oggi"))
            this.editDate.setText(today());
        else if (first.equals("ieri"))
            this.editDate.setText(yesterday());

        //Toast.makeText(getApplicationContext(),userSpeech.toString(),Toast.LENGTH_LONG).show();

        // Go on until there is nothing left on the stack
        String s;
        while (!(userSpeech.isEmpty())){
            s = userSpeech.pop();


            // Match total price
            if (s.matches("\\d+") || s.equals("un")){
                // Saving the value of the integer part of the cost
                Log.d(TAG, s);

                float intPart;

                if (s.equals("un"))
                    intPart = 1.0f;
                else
                    intPart = Float.parseFloat(s);

                // Discard "euro"
                userSpeech.pop();

                // Now, look for a fractional cost
                s = userSpeech.peek();

                float fractPart = 0.0f;
                if (s.equals("e")){
                    // Discard "e"
                    userSpeech.pop();

                    //Match the fractional amount
                    s = userSpeech.pop();
                    fractPart = Float.parseFloat("0." + s);
                }

                if (this.editAmount.getText().toString().equals(""))
                    this.editAmount.setText((intPart + fractPart) + "");
                else
                    this.editPpl.setText((intPart + fractPart) + "");
            }
        }

        this.editNotes.setText(tts);
    }

    private void displayToast(int resources){
        Toast.makeText(getApplicationContext(), getResources().getString(resources),
                Toast.LENGTH_LONG).show();
    }

    private String today(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String format = "%d/%d/%d";
        return String.format(format, day, month + 1, year);
    }

    private String yesterday(){
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, -1);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String format = "%d/%d/%d";

        return String.format(format, day, month + 1, year);
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
                  if (b) doBind();
                  else doUnBind();
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
                    edt.setText(today());
                } else {
                    edt.setText("");
                }
            }
        });
    }

    private void initEditTextDate(){
        this.editDate = (EditText)findViewById(R.id.refuel_insertion_data_edit);
        this.editDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
    }

    private void initEditTextNotes(){
        this.editNotes = (EditText)findViewById(R.id.refuel_insertion_notes);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode == Activity.RESULT_OK ){
            switch( requestCode ){
                case STT_REQUEST_CODE:{
                    if( data == null ) {
                        Toast.makeText(getApplicationContext(), "date == null", Toast.LENGTH_LONG).show();
                    }else{
                        ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        parseTTS(res.get(0));
                    }
                    break;
                }
            }
        }
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
                if( checkFields() && save() ){
                    Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(R.string.activity_refuel_insertion_refuel_save_success),
                        Toast.LENGTH_SHORT
                    ).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                return true;
            }
            case R.id.tts:{
                promptSpeechInput();
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

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain(null, GPSService.Protocol.REQUEST_BIND);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                Toast.makeText(
                    RefuelInsertion.this.getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_LONG
                ).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name){ mService = null; }
    }
}
