package it.science.unitn.lpsmt.auto.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import lpsmt.science.unitn.it.auto.R;


public class MainActivity extends Activity{
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
    }


    public static Context getAppContext(){
        return instance.getApplicationContext();

    }
}
