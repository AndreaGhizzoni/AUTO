package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private FloatingActionsMenu fab;
    private VehicleInsertedCardViewAdapter vehicleInsertedAdapter;

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);
        this.initFAB(v);
        this.initRecycleView(v);
        return v;
    }

    private void initFAB(View v){
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

    private void initRecycleView( View v ){
        RecyclerView vehicleInserted = (RecyclerView) v.findViewById(R.id.frag_main_vehicle_inserted_view);
        vehicleInserted.setLayoutManager(new LinearLayoutManager(v.getContext()));
        vehicleInserted.setItemAnimator(new DefaultItemAnimator());
        this.vehicleInsertedAdapter = new VehicleInsertedCardViewAdapter(v.getContext(),
                R.layout.adapter_frag_main_vehicle_inserted);
        vehicleInserted.setAdapter(this.vehicleInsertedAdapter);

        RecyclerView lastRefuel = (RecyclerView) v.findViewById(R.id.frag_main_last_refuel);
        lastRefuel.setLayoutManager(new LinearLayoutManager(v.getContext()));
        lastRefuel.setItemAnimator(new DefaultItemAnimator());

        RecyclerView deadlines = (RecyclerView) v.findViewById(R.id.frag_main_deadlines);
        deadlines.setLayoutManager(new LinearLayoutManager(v.getContext()));
        deadlines.setItemAnimator(new DefaultItemAnimator());

    }

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
