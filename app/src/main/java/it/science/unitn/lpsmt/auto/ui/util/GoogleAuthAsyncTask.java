package it.science.unitn.lpsmt.auto.ui.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import lpsmt.science.unitn.it.auto.R;

//params, progress, result
public class GoogleAuthAsyncTask extends AsyncTask<Void, Void, Void>
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog dialog;

    private Activity activity;
    private GoogleApiClient mGoogleApiClient;

    public GoogleAuthAsyncTask( Activity activity ){
        this.activity = activity;
        this.dialog = new ProgressDialog(this.activity);
    }

    private String getString( int i ){
        return this.activity.getResources().getString(i);
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onPreExecute(){
        this.dialog.show();
        this.dialog.setTitle(getString(R.string.async_task_google_login_dialog_title));
        this.dialog.setMessage(getString(R.string.async_task_google_login_try_to_connect));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        this.dialog.setMessage(getString(R.string.async_task_google_login_connecting));
        if( this.mGoogleApiClient == null ){
            this.mGoogleApiClient = new GoogleApiClient.Builder(this.activity)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        this.mGoogleApiClient.connect();
        return null;
    }

    @Override
    protected void onPostExecute( Void result ){
       if( this.dialog.isShowing() ) {
           this.dialog.dismiss();
       }
    }

    @Override
    public void onConnected(Bundle bundle) {
        String m = getString(R.string.async_task_google_connection_success);
        this.dialog.setMessage(m);
        Toast.makeText(this.activity, m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        String m = getString(R.string.async_task_google_connection_fail);
        this.dialog.setMessage(m);
        Toast.makeText(this.activity, m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {}
}
