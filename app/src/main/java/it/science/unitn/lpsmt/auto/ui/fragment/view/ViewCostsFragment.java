package it.science.unitn.lpsmt.auto.ui.fragment.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class ViewCostsFragment extends Fragment {
    public static final String TAG = ViewCostsFragment.class.getSimpleName();
    public static final String ARG_VEHICLE_TO_DISPLAY = "vehicle";

    private List<Vehicle> vehicleList;
    private Long idVehicleToDisplay = null;
    private RefuelsCardViewAdapter recyclerViewAdapter;
    private Spinner spinner;

    public ViewCostsFragment(){}

    // init the spinner components
    private void initSpinner( View v ){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(
            v.getContext(),
            R.layout.spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if( new DAOVehicle().countObject() != 0 ) {
            spinnerAdapter.add(getResources().getString(R.string.frag_view_costs_no_vehicle_selected));
            // populate the spinner
            vehicleList = new DAOVehicle().getAll();
            for( Vehicle i : vehicleList ){
                spinnerAdapter.add(i.getName());
            }
        }else {
            spinnerAdapter.add(getResources().getString(R.string.frag_main_no_vehicle_inserted));
        }

        spinner = (Spinner) v.findViewById(R.id.frag_view_costs_spinner_vehicles);
        spinner.setOnItemSelectedListener(new SpinnerSelectionListener());
        spinner.setAdapter(spinnerAdapter);
    }

    private  void initRecycleView( View v ){
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.frag_view_costs_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAdapter = new RefuelsCardViewAdapter(v.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if( args != null && args.containsKey(ARG_VEHICLE_TO_DISPLAY)) {
            idVehicleToDisplay = (Long) args.get(ARG_VEHICLE_TO_DISPLAY);
        }

        View v = inflater.inflate(R.layout.frag_view_costs, container, false);
        this.initSpinner( v );
        this.initRecycleView(v);
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        // display the correct costs associated with the vehicle passed to this fragment
        if( idVehicleToDisplay != null ){
            Vehicle tmp = null;
            for( Vehicle i : vehicleList ){
                if( i.getId().equals(idVehicleToDisplay) ) {
                    tmp = i;
                    break;
                }
            }
            int pos = vehicleList.indexOf(tmp)+1;
            spinner.setSelection(pos);
            // load the cost associated at vehicle "vehicleToDisplay
            spinner.getOnItemSelectedListener().onItemSelected(null, null, pos, -1);
        }
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if( pos == 0 ) {
                recyclerViewAdapter.setData(null);
                getActivity().findViewById(R.id.frag_view_costs_no_cost).setVisibility(View.INVISIBLE);
            }else{
                Vehicle i = vehicleList.get(pos - 1);
                List<Cost> r = i.getCosts();
                if( r.isEmpty() ){
                    getActivity().findViewById(R.id.frag_view_costs_no_cost).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.frag_view_costs_recycle_view).setVisibility(View.INVISIBLE);
                }else{
                    getActivity().findViewById(R.id.frag_view_costs_no_cost).setVisibility(View.INVISIBLE);
                    getActivity().findViewById(R.id.frag_view_costs_recycle_view).setVisibility(View.VISIBLE);
                    recyclerViewAdapter.setData(r);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {}
    }
}
