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

import it.science.unitn.lpsmt.auto.ui.fragment.main.MainFragment;
import it.science.unitn.lpsmt.auto.ui.fragment.view.ViewCostsFragment;
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

        // enable ActionBar app icon to behave as action to toggle nav drawer
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }catch (NullPointerException ignored){}

        this.initDrawer();

        if(savedInstanceState == null)
            selectFragment(0, savedInstanceState );
    }

    private void initDrawer(){
        ListView drawerList = (ListView) findViewById(R.id.drawer_list);
        drawerList.setAdapter(new DrawerAdapter(
                this,
                getResources().getStringArray(R.array.drawer_items)
        ));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set up the drawer's list view with items and click listener
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerRelativeLayout = (LinearLayout) findViewById(R.id.left_drawer);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
    public void selectFragment( int position, Bundle args ){
        mDrawerLayout.closeDrawer(mDrawerRelativeLayout);

        FragmentManager m = getFragmentManager();
        Fragment f = null;
        String TAG = null;
        switch (position){
            case 0:{ // home frag main
                f = m.findFragmentById(R.id.frag_main);
                if( f == null )
                    f = new MainFragment();
                TAG = MainActivity.TAG;
                break;
            }

            case 4:{ // frag view costs
                f = m.findFragmentById(R.id.frag_view_costs);
                if( f == null )
                    f = new ViewCostsFragment();
                TAG = ViewCostsFragment.TAG;
                break;
            }
        }

        if( args != null )
            f.setArguments(args);
        m.beginTransaction().replace(R.id.content, f).addToBackStack(TAG).commit();
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    /**
     * Trick to get the application context.
     * @return MainActivity the App main activity.
     */
    public static MainActivity getApp(){ return instance; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectFragment(position, null);
        }
    }

    private class DrawerAdapter extends ArrayAdapter<String> {
        public DrawerAdapter(Context context, String[] data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DrawerItemHolder holder = new DrawerItemHolder();

            if( convertView == null ){
                convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.adapter_drawer_list,
                    parent,
                    false
                );
            }
            holder.text = (TextView) convertView.findViewById(R.id.adapter_drawer_item_text);
            holder.icon = (ImageView) convertView.findViewById(R.id.adapter_drawer_item_icon);

            String item = getItem(position);
            if(item != null){
                holder.text.setText(item);
                holder.icon.setImageResource(getIcon(position));
            }
            return convertView;
        }

        private int getIcon( int position ){
            switch (position){
                case 0: return R.drawable.ic_action_home_48dp;
                default: return R.drawable.ic_placeholder; // STUB
            }
        }

        public class DrawerItemHolder{
            public ImageView icon;
            public TextView text;
        }
    }
}
