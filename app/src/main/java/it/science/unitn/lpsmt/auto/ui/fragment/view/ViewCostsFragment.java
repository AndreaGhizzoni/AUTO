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

import it.science.unitn.lpsmt.auto.controller.dao.DAOCost;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class ViewCostsFragment extends Fragment {
    public static final String TAG = ViewCostsFragment.class.getSimpleName();
    public static final String ARG_VEHICLE_TO_DISPLAY = "vehicle";

    private List<Vehicle> vehicleList;
    private Vehicle vehicleToDisplay;
    private RefuelsCardViewAdapter recyclerViewAdapter;

    /**
     * TODO add doc
     */
    public ViewCostsFragment(){
        Bundle args = getArguments();
        if( args != null && args.containsKey(ARG_VEHICLE_TO_DISPLAY))
            vehicleToDisplay = (Vehicle) args.get(ARG_VEHICLE_TO_DISPLAY);
    }

    // init the spinner components
    private void initSpinner( View v ){
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if( new DAOVehicle().countObject() != 0 ) {
            spinnerAdapter.add("");
            // populate the spinner
            vehicleList = new DAOVehicle().getAll();
            for( Vehicle i : vehicleList ){
                spinnerAdapter.add(i.getName());
            }
        }else {
            spinnerAdapter.add("No Vehicle");
        }

        Spinner spinner = (Spinner) v.findViewById(R.id.frag_view_costs_spinner_vehicles);
        spinner.setOnItemSelectedListener(new SpinnerSelectionListener());
        spinner.setAdapter(spinnerAdapter);

        // display the correct costs associated with the vehicle passed to this fragment
        if( vehicleToDisplay != null ){
            int pos = spinnerAdapter.getPosition(vehicleToDisplay.getName());
            spinner.setSelection(pos);
            // load the cost associated at vehicle "vehicleToDisplay
        }
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
        View v = inflater.inflate(R.layout.frag_view_costs, container, false);
        this.initSpinner( v );
        this.initRecycleView( v );
        return v;
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if( pos == 0 ) {
                recyclerViewAdapter.setData(null);
            }else{
                Vehicle i = vehicleList.get(pos-1);
                List<Refuel> r = new DAOCost().getAllRefuelWhereVehicleIs(i);
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
