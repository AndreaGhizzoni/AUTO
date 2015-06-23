package it.science.unitn.lpsmt.auto.ui.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;

import lpsmt.science.unitn.it.auto.R;

//GoogleAuthAsyncTask g = new GoogleAuthAsyncTask(this);
//g.execute();
//try {
//    GoogleApiClient m = g.get( 5, TimeUnit.MINUTES );
//    Toast.makeText(getApplicationContext(), "get from the task", Toast.LENGTH_SHORT).show();
//} catch (InterruptedException | ExecutionException | TimeoutException e) {
//    e.printStackTrace();
//}
public class GoogleAuthAsyncTask extends AsyncTask<Void, Void, GoogleApiClient>//params, progress, result
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
    protected GoogleApiClient doInBackground(Void... voids) {
        this.dialog.setMessage(getString(R.string.async_task_google_login_connecting));
        if( this.mGoogleApiClient == null ){
            this.mGoogleApiClient = new GoogleApiClient.Builder(this.activity)
                    .addApi(LocationServices.API)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        this.mGoogleApiClient.connect();
        return this.mGoogleApiClient;
    }

    @Override
    protected void onPostExecute( GoogleApiClient result ){
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
        Log.e(GoogleAuthAsyncTask.class.getSimpleName(), connectionResult.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {}
}
