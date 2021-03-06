package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import it.science.unitn.lpsmt.auto.ui.MainActivity;
import lpsmt.science.unitn.it.auto.R;

/**
 * TODO add doc
 */
public class MainFragment extends Fragment {
    public static final String TAG = MainFragment.class.getSimpleName();
    private FloatingActionsMenu fab;
    private RecyclerView vehicleInserted;
    private VehicleInsertedCardViewAdapter vehicleInsertedAdapter;

    private RecyclerView deadlines;
    private DeadLineCardViewAdapter deadLineCardViewAdapter;

    /**
     * TODO add doc
     */
    public MainFragment(){}

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
        vehicleInserted = (RecyclerView) v.findViewById(R.id.frag_main_vehicle_inserted_view);
        vehicleInserted.setLayoutManager(new LinearLayoutManager(v.getContext()));
        vehicleInserted.setItemAnimator(new DefaultItemAnimator());
        this.vehicleInsertedAdapter = new VehicleInsertedCardViewAdapter(v.getContext());
        vehicleInserted.setAdapter(this.vehicleInsertedAdapter);
        if( this.vehicleInsertedAdapter.isAdapterEmpty() ){
            vehicleInserted.setVisibility(View.INVISIBLE);
            v.findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.VISIBLE);
        }else {
            vehicleInserted.setVisibility(View.VISIBLE);
            v.findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.INVISIBLE);
        }

        deadlines = (RecyclerView) v.findViewById(R.id.frag_main_deadlines);
        deadlines.setLayoutManager(new LinearLayoutManager(v.getContext()));
        deadlines.setItemAnimator(new DefaultItemAnimator());
        this.deadLineCardViewAdapter = new DeadLineCardViewAdapter(v.getContext());
        deadlines.setAdapter(this.deadLineCardViewAdapter);
        if( this.deadLineCardViewAdapter.isAdapterEmpty() ){
            deadlines.setVisibility(View.INVISIBLE);
            v.findViewById(R.id.frag_main_no_deadlines).setVisibility(View.VISIBLE);
        }else{
            deadlines.setVisibility(View.VISIBLE);
            v.findViewById(R.id.frag_main_no_deadlines).setVisibility(View.INVISIBLE);
        }
    }

    public void updateDeadlines(){
        deadLineCardViewAdapter.notifyDeadLinesChanges();
        if( deadLineCardViewAdapter.isAdapterEmpty() ){
            deadlines.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.frag_main_no_deadlines).setVisibility(View.VISIBLE);
        }else{
            deadlines.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.frag_main_no_deadlines).setVisibility(View.INVISIBLE);
        }
    }

    public void updateVehicle(){
        vehicleInsertedAdapter.notifyVehiclesChanges();
        if( vehicleInsertedAdapter.isAdapterEmpty() ){
            vehicleInserted.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.VISIBLE);
        }else {
            vehicleInserted.setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.INVISIBLE);
        }
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);
        this.initFAB(v);
        this.initRecycleView(v);
        return v;
    }

    @Override
    public void onActivityResult( int reqCode, int resultCode, Intent data ){
        super.onActivityResult(reqCode, resultCode, data);
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    private final class FABActionListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch ( view.getId() ){
                case R.id.btnAddRefuel:{
                    MainActivity.getApp().selectFragment(2, null);
//                    List<Vehicle> list = new DAOVehicle().getAll();
//                    if( list.size() != 0 ) {
//                        Vehicle v = list.get(0);
//                        Location l = new Location(it.science.unitn.lpsmt.auto.controller.util.Const.LOCATION_PROVIDER);
//                        Place p = new Place(Const.NO_DB_ID_SET, "MyAddress", l);
//                        Refuel r = new Refuel(Const.NO_DB_ID_SET, v, 30f, 1.324f, new Date(), 30000, p);
//                        DAOCost dao = new DAOCost();
//                        dao.save(r);
//                        Toast.makeText(view.getContext(), "Refuel saved.", Toast.LENGTH_LONG ).show();
//                    }else{
//                        Toast.makeText(view.getContext(), "No Vehicle to save the refuel.",
//                                Toast.LENGTH_LONG ).show();
//                    }
                    break;
                }
                case R.id.btnAddCost:{
                    MainActivity.getApp().selectFragment(3, null);
//                    Intent i = new Intent(view.getContext(), MaintenanceInsertion.class);
//                    startActivity(i);
//                    List<Vehicle> list = new DAOVehicle().getAll();
//                    if( list.size() != 0 ){
//                        Vehicle v = list.get(0);
//                        Location l = new Location(it.science.unitn.lpsmt.auto.controller.util.Const.LOCATION_PROVIDER);
//                        Place p = new Place(Const.NO_DB_ID_SET, "MyAddress", l);
//                        Maintenance m = new Maintenance(Const.NO_DB_ID_SET, v, 150f, "notes",
//                                "SomeTax", Maintenance.Type.TAX, p, 100);
//
//                        CostDAO dao = new DAOCost();
//                        dao.save(m);
//                        Toast.makeText(view.getContext(), "Maintenance saved.",
//                                Toast.LENGTH_LONG ).show();
//                        deadLineCardViewAdapter.notifyDeadLinesChanges();
//                        if( deadLineCardViewAdapter.isAdapterEmpty() ){
//                            deadlines.setVisibility(View.INVISIBLE);
//                            getActivity().findViewById(R.id.frag_main_no_deadlines).setVisibility(View.VISIBLE);
//                        }else{
//                            deadlines.setVisibility(View.VISIBLE);
//                            getActivity().findViewById(R.id.frag_main_no_deadlines).setVisibility(View.INVISIBLE);
//                        }
//                    }else{
//                        Toast.makeText(view.getContext(), "No Vehicle to save the refuel.",
//                                Toast.LENGTH_LONG ).show();
//                    }
                    break;
                }
                case R.id.btnAddVehicle: {
                    MainActivity.getApp().selectFragment(1, null);
//                    VehicleDAO dao = new DAOVehicle();
//                    Vehicle v = new Vehicle(Const.NO_DB_ID_SET, "myVehicle", "XX123", Vehicle.Fuel.GASOLINE, new Date());
//                    dao.save(v);
//                    Toast.makeText(view.getContext(), "Vehicle saved.", Toast.LENGTH_LONG ).show();
//                    vehicleInsertedAdapter.notifyVehiclesChanges();
//                    if( vehicleInsertedAdapter.isAdapterEmpty() ){
//                        vehicleInserted.setVisibility(View.INVISIBLE);
//                        getActivity().findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.VISIBLE);
//                    }else {
//                        vehicleInserted.setVisibility(View.VISIBLE);
//                        getActivity().findViewById(R.id.frag_main_no_vehicle_inserted).setVisibility(View.INVISIBLE);
//                    }
                    break;
                }
            }
            fab.collapse();
        }
    }
}
