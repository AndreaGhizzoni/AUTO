package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import lpsmt.science.unitn.it.auto.R;

/**
 * This is the card adapter for the Recycle view of MainFragment.
 */
public class VehicleInsertedCardViewAdapter extends RecyclerView.Adapter<VehicleInsertedCardViewAdapter.ViewHolder>{
    private Context context;

    private ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleInsertedCardViewAdapter( Context c ){
        this.context = c;
        this.populateVehicle();
    }

    private void populateVehicle(){
        VehicleDAO dao = new DAOVehicle();
        this.vehicles.clear();
        this.vehicles.addAll(dao.getAll());
        dao.close();
    }

    public void notifyVehiclesChanges(){
        populateVehicle();
        notifyDataSetChanged();
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
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.name.setText( v.getName()+"  "+getFuel(v.getFuel())+"  -  "+s.format(v.getPurchaseDate()) );
        holder.data.setText( v.getPlate() );
        holder.associatedVehicle = v;
    }

    public String getFuel( Vehicle.Fuel f ){
        if( !Locale.getDefault().getDisplayLanguage().equals("it") ) {
            switch (f){
                case GASOLINE: return "Diesel";
                case LPG: return "GPL";
                case NATURAL_GAS: return "Metano";
                case PETROL: return "Benzina";
            }
        }
        return "";
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
        public Vehicle associatedVehicle;
        public TextView name;
        public TextView data;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Vehicle id: "+associatedVehicle.getId(),
                            Toast.LENGTH_LONG).show();
                }
            });
            name = (TextView) itemView.findViewById(R.id.card_view_vehicle_name);
            data = (TextView) itemView.findViewById(R.id.card_view_vehicle_data);
        }
    }
}
