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
        // create dao
        this.refuels.clear();
//        this.refuels.addAll(dao.get);
        // dao.close();
    }

    private void notifyRefuelChanges(){
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
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.data.setText( s.format(r.getDate()) );
        holder.refuel.setText( r.getPlace().getAddress()+" "+r.getAmount()+" at "+r.getPricePerLiter()+" in "+
                        r.getVehicle().getName());
        holder.refuelAssociated = r;
    }

    @Override
    public int getItemCount() { return this.refuels.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Refuel refuelAssociated;
        public TextView refuel;
        public TextView data;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Refuel id: "+refuelAssociated.getId(),
                            Toast.LENGTH_LONG).show();
                }
            });
            refuel = ( TextView ) itemView.findViewById(R.id.card_view_last_refuel);
            data   = ( TextView ) itemView.findViewById(R.id.card_view_last_refuel_data);
        }
    }
}
