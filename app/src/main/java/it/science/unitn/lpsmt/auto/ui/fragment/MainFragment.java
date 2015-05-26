package it.science.unitn.lpsmt.auto.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();
    private String title;
    private Integer pos;

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        title = (String)getArguments().get("title");
        pos = (Integer)getArguments().get("position");
        View v = inflater.inflate(R.layout.frag_main, container, false);
        this.populate(v);
        return v;
    }

    private void populate( View v ){

    }

//==================================================================================================
//  GETTER
//==================================================================================================
    public String getTitle(){ return this.title; }

    public int getPos(){ return this.pos; }
}
