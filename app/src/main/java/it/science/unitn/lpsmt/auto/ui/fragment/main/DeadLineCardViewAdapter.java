package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOCost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import lpsmt.science.unitn.it.auto.R;

/**
 * this is the card adapter for the Recycle view of MainFragment
 */
public class DeadLineCardViewAdapter extends RecyclerView.Adapter<DeadLineCardViewAdapter.ViewHolder>{
    private Context context;

    private ArrayList<Maintenance> deadlines = new ArrayList<>();

    public DeadLineCardViewAdapter( Context c ){
        this.context = c;
        this.populateDeadLines();
    }

    private void populateDeadLines(){
        CostDAO dao = new DAOCost();
        this.deadlines.clear();
        this.deadlines.addAll(dao.getAllMaintenanceWhereTypeIs(Maintenance.Type.TAX));
        dao.close();
    }

    public void notifyDeadLinesChanges(){
        populateDeadLines();
        notifyDataSetChanged();
    }

    public boolean isAdapterEmpty(){ return this.deadlines.isEmpty(); }

    public String getPaymentSymbol(){
        Locale def = Locale.getDefault();
        if( def.equals(Locale.ITALY) )
            return " €";
        else if( def.equals(Locale.US) )
            return " $";
        else
            return "";
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = R.layout.adapter_frag_main_deadlines;
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Maintenance m = this.deadlines.get(position);
        holder.maintenanceAssociated = m;
        holder.name.setText(m.getName());
        holder.amount.setText(m.getAmount()+getPaymentSymbol());
    }

    @Override
    public int getItemCount() { return this.deadlines.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public Maintenance maintenanceAssociated;
        public TextView name;
        public TextView amount;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Maintenance id: " + maintenanceAssociated.getId(),
                            Toast.LENGTH_LONG).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), "Long press on Maintenance id: " + maintenanceAssociated.getId(),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            name = (TextView) itemView.findViewById(R.id.card_view_deadlines_name);
            amount = (TextView) itemView.findViewById(R.id.card_view_deadlines_amount);
        }
    }
}
