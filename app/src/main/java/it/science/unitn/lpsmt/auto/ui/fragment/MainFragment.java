package it.science.unitn.lpsmt.auto.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();
    private String title;
    private Integer pos;
    private FloatingActionsMenu fab;

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
        // get the instanced one for onClick method: avoiding null references
        fab = (FloatingActionsMenu)v.findViewById(R.id.btnFab);

        FloatingActionButton bntAddRefuel = (FloatingActionButton)v.findViewById(R.id.btnAddRefuel);
        FloatingActionButton bntAddCost   = (FloatingActionButton)v.findViewById(R.id.btnAddCost);
        FloatingActionButton bntAddVehicle= (FloatingActionButton)v.findViewById(R.id.btnAddVehicle);
        FABActionListener fabal = new FABActionListener();
        bntAddRefuel.setOnClickListener(fabal);
        bntAddCost.setOnClickListener(fabal);
        bntAddVehicle.setOnClickListener(fabal);
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    public String getTitle(){ return this.title; }

    public int getPos(){ return this.pos; }

//==================================================================================================
//  GETTER
//==================================================================================================
    private final class FABActionListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch ( view.getId() ){
                case R.id.btnAddRefuel:{
                    break;
                }
                case R.id.btnAddCost:{
                    break;
                }
                case R.id.btnAddVehicle: {
                    break;
                }
            }
            fab.collapse();
        }
    }
}
