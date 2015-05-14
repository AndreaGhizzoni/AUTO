package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.science.unitn.lpsmt.auto.controller.Const;

/**
 * TODO add doc
 */
class PersistenceDAO extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public PersistenceDAO(Context context) {
        super(context, Const.DB_NAME, null, Const.DB_VERSION);
    }

//==================================================================================================
//  METHOD
//==================================================================================================
    /**
     * TODO add doc
     */
    public void open(){
        if(db == null)
            db = getWritableDatabase();
    }

    /**
     * TODO add doc
     */
    public void close(){
        if(db != null)
            db.close();
    }

//==================================================================================================
//  GETTER
//==================================================================================================
    /**
     * TODO add doc
     * @return
     */
    public SQLiteDatabase getDb(){
        return this.db;
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO insert here sqLiteDatabase.execSQL( sql )
        // where sql is the create sql statement for all the table we need
        open();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO here insert the drop sql statement for old table
        onCreate(sqLiteDatabase);
    }
}
