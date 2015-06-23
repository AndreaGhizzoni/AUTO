package it.science.unitn.lpsmt.auto.ui.fragment.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.util.DateUtils;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.ui.MainActivity;
import it.science.unitn.lpsmt.auto.ui.MaintenanceInsertion;
import it.science.unitn.lpsmt.auto.ui.RefuelInsertion;
import lpsmt.science.unitn.it.auto.R;

/**
 * this is the card adapter for the Recycle view of MainFragment
 */
public class RefuelsCardViewAdapter extends RecyclerView.Adapter<RefuelsCardViewAdapter.ViewHolder> {
    private Context context;

    private ArrayList<Cost> costs = new ArrayList<>();

    public RefuelsCardViewAdapter(Context c){
        this.context = c;
    }

    public void setData( List<Cost> costs ){
        this.costs.clear();
        if( costs != null )
            this.costs.addAll(costs);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = R.layout.adapter_frag_view_costs;
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cost c = this.costs.get(position);
        holder.cost = c;
        if( c instanceof Refuel ){
            Refuel r = (Refuel)c;
            holder.icon.setImageResource(R.drawable.ic_maps_local_gas_station_48dp);
            holder.textTop.setText(r.getPlace().getAddress());
            String format = context.getResources().getString(R.string.refuel_card_view_adapter_amount);
            holder.textBottom.setText(
                    String.format(format, DateUtils.getStringFromDate(r.getDate(), "dd/MM/yyyy"),
                            r.getAmount()+"", r.getPricePerLiter()+"")
            );
        }else{ // c instanceof Maintenance
            Maintenance m = (Maintenance)c;
            if( m.getType().equals(Maintenance.Type.TAX) )
                holder.icon.setImageResource(R.drawable.ic_editor_attach_money_48dp);
            else
                holder.icon.setImageResource(R.drawable.ic_maintenance_48dp);
            if( m.getPlace() == null )
                holder.textTop.setText(context.getResources().getString(R.string.frag_view_costs_no_place));
            else
                holder.textTop.setText(m.getPlace().getAddress());
            String format = "%s  -  %s";
            holder.textBottom.setText(String.format(format, getMaintenanceType(m.getType()), m.getAmount()+""));
        }
    }

    public String getMaintenanceType( Maintenance.Type t ){
        if( !Locale.getDefault().getDisplayLanguage().equals("it") ) {
            switch (t){
                case ORDINARY: return "Ordinartia";
                case EXTRAORDINARY: return "Straordinaria";
                case TAX: return "Tassa";
            }
        }
        return "";
    }

    @Override
    public int getItemCount() { return this.costs.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public Drawable DEFAULT_BG;
    public MyActionMode mode;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Cost cost;
        public TextView textTop;
        public TextView textBottom;
        public ImageView icon;

        public ViewHolder( final View itemView) {
            super(itemView);
            DEFAULT_BG = itemView.getBackground();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode == null) {
                        String note = cost.getNotes() == null ||
                                cost.getNotes().isEmpty() ?
                                view.getResources().getString(R.string.deadline_no_note) :
                                cost.getNotes();
                        Toast.makeText(view.getContext(), note, Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mode == null) {
                        mode = new MyActionMode(itemView, cost.getId(), cost instanceof Refuel);
                        view.startActionMode(mode);
                        itemView.setBackgroundColor(Color.rgb(197, 202, 233));
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            icon = (ImageView) itemView.findViewById(R.id.adapter_frag_view_costs_image);
            textTop = (TextView) itemView.findViewById(R.id.adapter_frag_view_cost_text_top);
            textBottom = (TextView) itemView.findViewById(R.id.adapter_frag_view_cost_text_bottom);
        }
    }

    // https://goo.gl/1jIz4a
    public class MyActionMode implements ActionMode.Callback {
        private View item;
        private Long costID;
        private boolean isRefuel;
        public MyActionMode(View item, Long costID, boolean isRefuel){
            this.item = item;
            this.costID = costID;
            this.isRefuel = isRefuel;
        }

        @Override // Called when the action mode is created; startActionMode() was called
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle("Actions");
            actionMode.getMenuInflater().inflate(R.menu.menu_action_delete_and_modify, menu);
            return true;
        }

        @Override // Called each time the action mode is shown.
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override // Called when the user selects a contextual menu item
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.modify: {
                    if( isRefuel ) {
                        Intent i = new Intent(MainActivity.getApp(), RefuelInsertion.class);
                        i.putExtra(RefuelInsertion.UPDATE_REFUEL, costID);
                        MainActivity.getApp().startActivity(i);
                    }else{
                        Intent i = new Intent(MainActivity.getApp(), MaintenanceInsertion.class);
                        i.putExtra(MaintenanceInsertion.UPDATE_MAINTENANCE, costID);
                        MainActivity.getApp().startActivity(i);
                    }
                    actionMode.finish();
                    return true;
                }
                case R.id.delete: {
                    Toast.makeText(
                        RefuelsCardViewAdapter.this.context,
                        "delete action",
                        Toast.LENGTH_LONG
                    ).show();
                    actionMode.finish();
                    return true;
                }
                default: return false;
            }
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override // Called when the user exits the action mode
        public void onDestroyActionMode(ActionMode actionMode) {
            mode = null;
            item.setBackground(DEFAULT_BG);
        }
    }
}
