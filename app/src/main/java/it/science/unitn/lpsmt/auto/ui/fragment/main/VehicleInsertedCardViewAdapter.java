package it.science.unitn.lpsmt.auto.ui.fragment.main;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.controller.util.DateUtils;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.ui.MainActivity;
import it.science.unitn.lpsmt.auto.ui.VehicleInsertion;
import it.science.unitn.lpsmt.auto.ui.fragment.view.ViewCostsFragment;
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

    public boolean isAdapterEmpty(){ return this.vehicles.isEmpty(); }

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
        String vehicleString = v.getName()+"  "+getFuel(v.getFuel());
        if( v.getPurchaseDate() != null )
            vehicleString = vehicleString+"  -  "+DateUtils.getStringFromDate(v.getPurchaseDate(), "dd/MM/yyyy");
        holder.name.setText( vehicleString );
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
    public Drawable DEFAULT_BG;
    public MyActionMode mode;

    /**
     *  Holder for card data
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Vehicle associatedVehicle;
        public TextView name;
        public TextView data;

        public ViewHolder(final View itemView) {
            super(itemView);
            DEFAULT_BG = itemView.getBackground();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode == null) {
                        Bundle args = new Bundle();
                        args.putLong(ViewCostsFragment.ARG_VEHICLE_TO_DISPLAY, associatedVehicle.getId());
                        MainActivity.getApp().selectFragment(4, args);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mode == null) {
                        mode = new MyActionMode(itemView, associatedVehicle.getId());
                        view.startActionMode(mode);
                        itemView.setBackgroundColor(Color.rgb(197, 202, 233));
                        return true;
                    }else{
                        return false;
                    }
                }
            });
            name = (TextView) itemView.findViewById(R.id.card_view_vehicle_name);
            data = (TextView) itemView.findViewById(R.id.card_view_vehicle_data);
        }
    }

    // https://goo.gl/1jIz4a
    public class MyActionMode implements ActionMode.Callback {
        private View item;
        private Long vehicleID;
        public MyActionMode(View item, Long vehicleID){
            this.item = item;
            this.vehicleID = vehicleID;
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
                    Bundle args = new Bundle();
                    args.putLong(VehicleInsertion.UPDATE_VEHICLE, vehicleID);
                    MainActivity.getApp().selectFragment(1, args);
                    actionMode.finish();
                    return true;
                }
                case R.id.delete: {
                    new AlertDialog.Builder(MainActivity.getApp())
                        .setTitle(context.getResources().getString(R.string.vehicle_card_view_adapter_delete_dialog_title))
                        .setMessage(context.getResources().getString(R.string.vehicle_card_view_adapter_delete_dialog_message))
                        .setIcon(R.drawable.ic_alert_48dp)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new DAOVehicle().delete(vehicleID);
                                MainActivity.getApp().updateVehicleInFragment();
                                MainActivity.getApp().updateDeadlineFragment();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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
