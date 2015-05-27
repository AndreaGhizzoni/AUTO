package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

/**
 * This is the card adapter for the Recycle view of MainFragment.
 */
public class VehicleInsertedCardViewAdapter extends RecyclerView.Adapter<VehicleInsertedCardViewAdapter.ViewHolder> {
    private Context context;

    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleInsertedCardViewAdapter( Context c ){
        this.context = c;
        this.populateVehicle();
    }

    private void populateVehicle(){
        VehicleDAO dao = new DAOVehicle();
        this.vehicles.addAll(dao.getAll());
        dao.close();
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = R.layout.adapter_frag_main_vehicle_inserted;
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Vehicle v = this.vehicles.get(position);
        holder.name.setText( v.getName() );
        holder.data.setText( v.getPlate() );
    }

    @Override
    public int getItemCount() { return this.vehicles.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     *  Holder for card data
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView data;
        public Button modify;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.card_view_vehicle_name);
            data = (TextView) itemView.findViewById(R.id.card_view_vehicle_data);
            modify = (Button) itemView.findViewById(R.id.btnCardViewModifyInsertedVehicle);
        }
    }
}
