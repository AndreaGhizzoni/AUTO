package it.science.unitn.lpsmt.auto.ui.fragment.view;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.model.Refuel;
import lpsmt.science.unitn.it.auto.R;

/**
 * this is the card adapter for the Recycle view of MainFragment
 */
public class RefuelsCardViewAdapter extends RecyclerView.Adapter<RefuelsCardViewAdapter.ViewHolder> {
    private Context context;

    private ArrayList<Refuel> refuels = new ArrayList<>();

    /**
     * TODO add doc
     * @param c
     */
    public RefuelsCardViewAdapter(Context c){
        this.context = c;
    }

    /**
     * TODO add doc
     * @param refuels
     */
    public void setData( List<Refuel> refuels ){
        this.refuels.clear();
        if(refuels != null)
            this.refuels.addAll(refuels);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int rowLayout = R.layout.adapter_frag_view_refuels;
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Refuel r = this.refuels.get(position);
        holder.refuelAssociated = r;
        holder.address.setText( r.getPlace().getAddress() );
        String format = context.getResources().getString(R.string.refuel_card_view_adapter_amount);
        String d = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(r.getDate());
        holder.amount.setText( String.format(format, d, r.getAmount()+"", r.getPricePerLiter()+"") );
    }

    @Override
    public int getItemCount() { return this.refuels.size(); }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public Drawable DEFAULT_BG;
    public MyActionMode mode;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Refuel refuelAssociated;
        public TextView address;
        public TextView amount;
        public ViewHolder( final View itemView) {
            super(itemView);
            DEFAULT_BG = itemView.getBackground();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode == null) {
                        Toast.makeText(view.getContext(), "Refuel id: " + refuelAssociated.getId(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if( mode == null ) {
                        mode = new MyActionMode(itemView);
                        view.startActionMode(mode);
                        itemView.setBackgroundColor(Color.rgb(197, 202, 233));
                        return true;
                    }else{
                        return false;
                    }
                }
            });
            address = (TextView) itemView.findViewById(R.id.card_view_last_refuel_address);
            amount  = ( TextView ) itemView.findViewById(R.id.card_view_last_refuel_amount);
        }
    }

    // https://goo.gl/1jIz4a
    public class MyActionMode implements ActionMode.Callback {
        private View item;
        public MyActionMode(View item) { this.item = item; }

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
                    Toast.makeText(
                        RefuelsCardViewAdapter.this.context,
                        "modify action",
                        Toast.LENGTH_LONG
                    ).show();
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
