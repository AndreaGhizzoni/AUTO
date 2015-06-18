package it.science.unitn.lpsmt.auto.ui.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

//params, progress, result
public class GoogleAuthAsyncTask extends AsyncTask<Void, Void, Void>
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private ProgressDialog dialog;

    public GoogleAuthAsyncTask( Activity activity ){ this.dialog = new ProgressDialog(activity); }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    protected void onPreExecute(){
        this.dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
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

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
