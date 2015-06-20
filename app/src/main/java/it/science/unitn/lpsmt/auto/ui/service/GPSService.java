package it.science.unitn.lpsmt.auto.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import it.science.unitn.lpsmt.auto.ui.MainActivity;

public class GPSService extends Service {
//    private static final String TAG = GPSService.class.getSimpleName();

    private static final long SERVICE_DATA_SEND_FREQUENCY = 1000L; // in ms
    private static final long GPS_CHECK_FREQUENCY = 500L; // in ms
    private static final long GPS_ACCURACY = 15; // in meter

    private Location lastLocation;
    private String addressFromLocation;
    private Timer timer = new Timer();
    private Messenger mMessenger = new Messenger(new ServiceHandler());
    private ArrayList<Messenger> mBandedClients = new ArrayList<>();

    private boolean running = false;

    public GPSService(){}

    @Override
    public void onCreate(){
        super.onCreate();
        this.setupProviders();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if( !mBandedClients.isEmpty() )
                    sendAddressToClients();
            }
        }, 0, SERVICE_DATA_SEND_FREQUENCY);
        this.running = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(this.timer != null)
            this.timer.cancel();
        this.lastLocation = null;
        this.addressFromLocation = null;
        this.mBandedClients.clear();
        this.running = false;
    }

//==================================================================================================
//  METHOD
//==================================================================================================
    public void setupProviders() {
        Criteria myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        myCriteria.setPowerRequirement(Criteria.POWER_LOW);

        // let Android select the right location provider for you
        LocationManager locationManager = (LocationManager)getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = locationManager.getBestProvider(myCriteria, true);
        lastLocation = new Location(bestProvider);

        locationManager.requestLocationUpdates(bestProvider, GPS_CHECK_FREQUENCY, GPS_ACCURACY,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if( doLocationUpdate(location) ){
                            doAddressChange();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}
                }
        );
    }

    private boolean doLocationUpdate( Location l ){
        if( l != null && lastLocation != null  ){
            float curAccuracy = l.getAccuracy();
            float distanceNowFromLast = l.distanceTo(lastLocation);

            // false if positions did not change or accuracy got worst
            boolean isNotLocationChanged = distanceNowFromLast < GPS_ACCURACY ||
                    ( curAccuracy >= lastLocation.getAccuracy() && distanceNowFromLast < curAccuracy);
            if(isNotLocationChanged)
                return false;

            this.lastLocation = l; // last location update;
            return true;
        }else{
            return false;
        }
    }

    private void doAddressChange(){
        Geocoder geocoder = new Geocoder(MainActivity.getApp(), Locale.getDefault());
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocation(
                this.lastLocation.getLatitude(),
                this.lastLocation.getLongitude(),
                1 // just get a single address.
            );
        }catch(IOException ignored) {}

        // Handle case where no address was found.
        if(addressList != null || addressList.size()  != 0) {
            Address address = addressList.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            this.addressFromLocation = (TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }
    }

    private void sendAddressToClients(){
        Message message = Message.obtain(null, Protocol.SEND_LOCATION);
        Bundle b = new Bundle();
        b.putParcelable(Protocol.RETRIEVED_LOCATION, this.lastLocation);
        b.putString(Protocol.RETRIEVED_ADDRESS, this.addressFromLocation);
        message.setData(b);

        for(Messenger m : mBandedClients){
            try {
                m.send(message);
            } catch (RemoteException e) {
                mBandedClients.remove(m);
            }
        }
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    public boolean isRunning(){ return this.running; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    // Handler that receives messages from the thread
    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //Handler of incoming messages from client
            switch (msg.what){
                case Protocol.REQUEST_BIND:{
                    mBandedClients.add(msg.replyTo);
                    break;
                }
                case Protocol.REQUEST_UNBIND:{
                    mBandedClients.remove(msg.replyTo);
                    break;
                }
                default: super.handleMessage(msg);
            }
        }
    }

    public static final class Protocol {
        public static final int REQUEST_BIND = 0;
        public static final int REQUEST_UNBIND = -1;
        public static final int SEND_LOCATION = 1;
        public static final String RETRIEVED_ADDRESS = "address";
        public static final String RETRIEVED_LOCATION = "location";
    }
}
