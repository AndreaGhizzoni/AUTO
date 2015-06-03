package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOCost;
import it.science.unitn.lpsmt.auto.model.Refuel;
import lpsmt.science.unitn.it.auto.R;

/**
 * this is the card adapter for the Recycle view of MainFragment
 */
public class LastRefuelsCardViewAdapter extends RecyclerView.Adapter<LastRefuelsCardViewAdapter.ViewHolder> {
    private Context context;

    private ArrayList<Refuel> refuels = new ArrayList<>();

    public LastRefuelsCardViewAdapter( Context c ){
        this.context = c;
        this.populateRefuel();
    }

    private void populateRefuel(){
        CostDAO dao = new DAOCost();
        this.refuels.clear();
        List<Refuel> list = dao.getAllRefuel();
        if(list.size() != 0){
            if( list.size() == 1 )
                this.refuels.add(list.get(0));
            else if( list.size() == 2 )
                this.refuels.addAll(list.subList(0,2));
            else if( list.size() > 2 )
                this.refuels.addAll(list.subList(0,3));
        }
        dao.close();
    }

    public void notifyRefuelChanges(){
        populateRefuel();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = R.layout.adapter_feag_main_last_refuels;
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Refuel r = this.refuels.get(position);
        holder.refuelAssociated = r;
        holder.address.setText( r.getPlace().getAddress() );
        holder.amount.setText( r.getAmount()+" euro at "+r.getPricePerLiter()+" euro/liter");
        holder.vehicle.setText( r.getVehicle().getName()+" - "+r.getVehicle().getPlate() );
    }

    @Override
    public int getItemCount() { return this.refuels.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Refuel refuelAssociated;
        public TextView address;
        public TextView amount;
        public TextView vehicle;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Refuel id: "+refuelAssociated.getId(),
                            Toast.LENGTH_LONG).show();
                }
            });
            address = (TextView) itemView.findViewById(R.id.card_view_last_refuel_address);
            amount  = ( TextView ) itemView.findViewById(R.id.card_view_last_refuel_amount);
            vehicle = ( TextView ) itemView.findViewById(R.id.card_view_last_refuel_vehicle);
        }
    }
}
