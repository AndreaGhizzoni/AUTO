package it.science.unitn.lpsmt.auto.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * TODO add doc
 */
public final class Preferences {
    private static Preferences instance;
    private static final String SHARED_PREFS_NAME = "auto_prefs";

    private SharedPreferences sharedPreferences;

    private Preferences(){
        sharedPreferences = MainActivity.getApp().getApplicationContext().getSharedPreferences(
            SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        );
    }

//=================================================================================================
//  METHOD
//=================================================================================================
    public static Preferences getInstance(){
        if(instance == null)
            instance = new Preferences();
        return instance;
    }

//    public static Preferences putS(String key, String value){
//        Preferences p = Preferences.getInstance();
//        p.putString(key, value);
//        return p;
//    }
//
//    public static Preferences putI(String key, Integer value){
//        Preferences p = Preferences.getInstance();
//        p.putInt(key, value);
//        return p;
//    }

//=================================================================================================
//  SETTER
//=================================================================================================
    /**
     * Put a given object into a persistence key-value file.
     * Object parameter must be one of those: String, Integer, Boolean, Float or Long.
     * @param key String the key.
     * @param obj Object the object to store.
     */
    public void put( String key, Object obj ){
         if( key == null ) return;

        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        if( contains(key) )
            editor.remove(key);

        if( obj instanceof  String )
            editor.putString( key, (String)obj );
        else if( obj instanceof Integer )
            editor.putInt( key, (Integer)obj );
        else if( obj instanceof Boolean )
            editor.putBoolean( key, (Boolean)obj );
        else if( obj instanceof Float )
            editor.putFloat( key, (Float)obj );
        else if( obj instanceof Long )
            editor.putLong( key, (Long)obj );

        editor.apply();
    }

    /**
     * Put a set of Strings into a persistence key-value file.
     * @param key String the key.
     * @param value Set a set of Strings.
     */
    public void putStringSet( String key, Set<String> value ){
        if( key == null ) return;
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        if( contains(key) )
            editor.remove(key);
        editor.putStringSet(key, value);
        editor.apply();
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    /**
     * Return the String associated to given key.
     * @param key String the key,
     * @return String associated with the key, otherwise null if key is not present.
     */
    public String pullString( String key ){
        return this.sharedPreferences.getString(key, null);
    }

    /**
     * Return a String Set associated to given key.
     * @param key String the key,
     * @return Set of Strings associated with the key, otherwise null if key is not present.
     */
    public Set<String> pullStringSet( String key ){
        return this.sharedPreferences.getStringSet( key, null );
    }

    /**
     * Return the Integer associated to given key.
     * @param key String the key,
     * @return Integer associated with the key, otherwise null if key is not present.
     */
    public Integer pullInteger( String key ){
        Integer r = this.sharedPreferences.getInt(key, Integer.MAX_VALUE);
        return r.equals(Integer.MAX_VALUE) ? null : r;
    }

    /**
     * Return the Float associated to given key.
     * @param key String the key,
     * @return Float associated with the key, otherwise null if key is not present.
     */
    public Float pullFloat( String key ){
        Float f = this.sharedPreferences.getFloat(key, Float.MAX_VALUE);
        return f.equals(Float.MAX_VALUE) ? null : f;
    }

    /**
     * Return the Long associated to given key.
     * @param key String the key,
     * @return Long associated with the key, otherwise null if key is not present.
     */
    public Long pullLong( String key ){
        Long l = this.sharedPreferences.getLong(key, Long.MAX_VALUE);
        return l.equals(Long.MAX_VALUE) ? null : l;
    }

    /**
     * Return the Boolean associated to given key.
     * @param key String the key,
     * @return Boolean associated with the key, otherwise null if key is not present.
     */
    public Boolean pullBoolean( String key ){
        if(contains(key))
            return this.sharedPreferences.getBoolean(key, false);
        else
            return null;
    }

    /**
     * Check if a key is associated with some value.
     * @param key String the key to check.
     * @return Boolean true if the key is associated with some value, false otherwise.
     */
    public boolean contains( String key ){ return this.sharedPreferences.contains(key); }
}

