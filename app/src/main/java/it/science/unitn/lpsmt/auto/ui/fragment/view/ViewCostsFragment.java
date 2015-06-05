package it.science.unitn.lpsmt.auto.ui.fragment.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class ViewCostsFragment extends Fragment {
    public static final String TAG = ViewCostsFragment.class.getSimpleName();
    public static final String ARG_VEHICLE_TO_DISPLAY = "vehicle";

    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    private Vehicle vehicleToDisplay;

    public ViewCostsFragment(){
        Bundle args = getArguments();
        if( args.containsKey(ARG_VEHICLE_TO_DISPLAY))
            vehicleToDisplay = (Vehicle) args.get(ARG_VEHICLE_TO_DISPLAY);
    }

    private void initSpinner( View v ){
        spinnerAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item);
        // populate the spinner
//        for( Vehicle i : daoVehicle.getAll() ){
//            arrayAdapter.add(i.getName());
//        }
        spinner = (Spinner) v.findViewById(R.id.frag_view_costs_spinner_vehicles);
        spinner.setOnItemSelectedListener(new SpinnerSelectionListener());
        spinner.setAdapter(spinnerAdapter);

        // display the correct costs associated with the vehicle passed to this fragment
        if( vehicleToDisplay != null ){
            int pos = spinnerAdapter.getPosition(vehicleToDisplay.getName());
            spinner.setSelection(pos);
            // load the cost associated at vehicle "vehicleToDisplay
        }
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_view_costs, container, false);
        this.initSpinner(v);
        return v;
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    private class SpinnerSelectionListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
