package it.science.unitn.lpsmt.auto.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import it.science.unitn.lpsmt.auto.ui.fragment.MainFragment;
import lpsmt.science.unitn.it.auto.R;

// TODO maybe implements method to save the app instance when is put onPause
public class MainActivity extends ActionBarActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static MainActivity instance;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        // set up the drawer's list view with items and click listener
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelativeLayout = (LinearLayout) findViewById(R.id.left_drawer);

        ListView drawerList = (ListView) findViewById(R.id.drawer_list_nav);
        drawerList.setAdapter(new DrawerAdapter(
                this,
                getResources().getStringArray(R.array.drawer_items)
        ));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (NullPointerException ignored){}

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.content_descriptor_drawer_open,
                R.string.content_descriptor_drawer_close
        ){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(savedInstanceState == null)
            selectFragment(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        try { // sometime rise a null point.. mah..
            mDrawerToggle.syncState();
        }catch (NullPointerException ignored){}
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){
        // Catch back action and pops from back stack
        // (if you called previously to addToBackStack() in your transaction)
        FragmentManager fm = getFragmentManager();
        if( fm.getBackStackEntryCount() > 1){
            fm.popBackStackImmediate();
        }else{
            // Default action on back pressed
            super.onBackPressed();
        }
    }

//==================================================================================================
//  METHOD
//==================================================================================================
    private void selectFragment( int position ){
        mDrawerLayout.closeDrawer(mDrawerRelativeLayout);

        FragmentManager m = getFragmentManager();
        Fragment f = null;
        switch (position){
            default:{
                f = m.findFragmentById(R.id.frag_main);
                if( f == null ){
                    f = new MainFragment();
                }
                break;
            }
        }

        m.beginTransaction()
            .replace(R.id.content, f)
            .addToBackStack(MainFragment.TAG)
            .commit();
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    /**
     * Trick to get the application context for class PersistenceDAO.
     * @return Context the App context.
     */
    public static Context getAppContext(){
        return instance.getApplicationContext();
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectFragment(position);
        }
    }

    private class DrawerAdapter extends ArrayAdapter<String> {
        public DrawerAdapter(Context context, String[] data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            DrawerItemHolder holder;

            // Check if an existing view is being reused, otherwise inflate the view
            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_drawer_list,
                    parent,
                    false
                );

                holder = new DrawerItemHolder();
                holder.text = (TextView) row.findViewById(R.id.adapter_drawer_item_text);
                holder.icon = (ImageView) row.findViewById(R.id.adapter_drawer_item_icon);
            }else{
                holder = (DrawerItemHolder)row.getTag();
            }

            holder.text.setText(getItem(position));
            holder.icon.setImageResource(getIcon(position));
            return row;
        }

        private int getIcon( int position ){
            switch (position){
                default: return R.drawable.ic_local_gas_station_black_24dp; // STUB
            }
        }

        class DrawerItemHolder{
            ImageView icon;
            TextView text;
        }
    }
}
