package it.science.unitn.lpsmt.auto.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.controller.util.DateUtils;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.ui.util.GoogleAuthAsyncTask;
import lpsmt.science.unitn.it.auto.R;

public class ExportActivity extends ActionBarActivity {
    public static final String TAG = ExportActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private List<Vehicle> vehicleList;
    private int selection;

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
//            Toast.makeText(this, "get api from the task", Toast.LENGTH_SHORT).show();
        }catch (InterruptedException | ExecutionException | TimeoutException e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Dialog d = onCreateDialogSingleChoice();
        d.show();
    }

    private Dialog onCreateDialogSingleChoice(){
        this.vehicleList = new DAOVehicle().getAll();
        if( this.vehicleList.isEmpty() ) return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList<CharSequence> list = new ArrayList<>();
        list.add("all");
        for( Vehicle v : this.vehicleList )
                list.add(v.getName());
        CharSequence[] array = list.toArray(new CharSequence[list.size()]);
        builder.setTitle("Choose a vehicle");
        builder.setSingleChoiceItems(array, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int witch) {
                selection = witch;
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                export();
                // Toast
                ExportActivity.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ExportActivity.this.finish();
            }
        });
        return builder.create();
    }

    private void export(){
        if(selection == 0){// all vehicle
            for( Vehicle v : vehicleList ){
                File csv = createCSV(v);

            }
        }else{ // single vehicle
            Vehicle vehicleToExport = this.vehicleList.get(selection-1);
            File csv = createCSV(vehicleToExport);

        }
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
    }

    private File createCSV(Vehicle v){
        File csv = null;
        try {
            csv = new File(this.getFilesDir(), (v.getName().hashCode()+v.getId())+".csv");
            if(csv.exists() ){
                boolean del  = csv.delete();
                Log.e(TAG, "old csv delete = "+del);
            }
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv, true));
            String[] record;
            for( Cost c : v.getCosts() ){
                record = getStringVector(c);
                csvWriter.writeNext(record);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return csv;
    }

    private String[] getStringVector( Cost c ){
        String pattern = "%s,%s,%s,%s,%s,%s";
        String type, name, data, ppl, locAdd;
        if( c instanceof Refuel ) {
            Refuel r = (Refuel)c;
            type = "Refuel";
            name = "";
            data = DateUtils.getStringFromDate(r.getDate());
            ppl = r.getPricePerLiter().toString();
            locAdd = r.getPlace().getAddress().replace("\n", "").replace(",", "");
        }else{
            Maintenance m = (Maintenance)c;
            type = "Maintenance";
            name = m.getType().toString();
            data = "";
            ppl = "";
            locAdd = m.getPlace().getAddress().replace("\n", "").replace(",", "");
        }

        String allTogether = String.format(pattern, type, name, data,
                c.getAmount().toString(), ppl, locAdd );
        Log.e(TAG, allTogether);
        return allTogether.split(",");
    }

    final private ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG, "Error while trying");
                        return;
                    }

                    MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                            .setMimeType("text/html").build();
                    IntentSender intentSender = Drive.DriveApi
                            .newCreateFileActivityBuilder()
                            .setInitialMetadata(metadataChangeSet)
                            .setInitialDriveContents(result.getDriveContents())
                            .build(mGoogleApiClient);
                    try {
                        startIntentSenderForResult(intentSender, 1, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        // Handle the exception
                    }


//                    final DriveContents driveContents = result.getDriveContents();
//                    new Thread() {
//                        @Override
//                        public void run() {
//                             write content to DriveContents
//                            OutputStream outputStream = driveContents.getOutputStream();
//                            Writer writer = new OutputStreamWriter(outputStream);
//                            try {
//                                writer.write("Hello World!");
//                                writer.close();
//                            } catch (IOException e) {
//                                Log.e(TAG, e.getMessage());
//                            }
//
//                            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
//                                    .setTitle("New file")
//                                    .setMimeType("text/plain")
//                                    .setStarred(true).build();
//
//                             create a file on root folder
//                            Drive.DriveApi.getRootFolder( mGoogleApiClient )
//                                    .createFile(mGoogleApiClient, changeSet, driveContents)
//                                    .setResultCallback(fileCallback);
//                        }
//                    }.start();
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.d(TAG, "Error while trying to create the file") ;
                return;
            }
            Log.d(TAG, "Created a file with content: " + result.getDriveFile().getDriveId());
        }
    };
}
