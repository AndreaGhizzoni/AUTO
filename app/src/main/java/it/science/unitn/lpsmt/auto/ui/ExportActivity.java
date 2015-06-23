package it.science.unitn.lpsmt.auto.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.science.unitn.lpsmt.auto.ui.util.GoogleAuthAsyncTask;
import lpsmt.science.unitn.it.auto.R;

public class ExportActivity extends ActionBarActivity {
    public static final String TAG = ExportActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if( mGoogleApiClient != null ) return;
        try{
            GoogleAuthAsyncTask g = new GoogleAuthAsyncTask(this);
            g.execute();
            this.mGoogleApiClient = g.get(5, TimeUnit.MINUTES);
            Toast.makeText(this, "get api from the task", Toast.LENGTH_SHORT).show();
        }catch (InterruptedException | ExecutionException | TimeoutException e){
            Log.e(TAG, e.getMessage());
        }
    }
}
