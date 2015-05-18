package it.science.unitn.lpsmt.auto.controller.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.science.unitn.lpsmt.auto.controller.util.Const;
import it.science.unitn.lpsmt.auto.ui.MainActivity;

/**
 * TODO add doc
 */
class PersistenceDAO extends SQLiteOpenHelper {
    private static PersistenceDAO instance;

    // TODO add doc
    private PersistenceDAO() {
        super(
            MainActivity.getAppContext(), // static reference to context
            Const.DB_NAME,                // db name
            null,                         // default cursor factory
            Const.DB_VERSION              // db version
        );
    }

    /**
     * TODO add doc
     * @return
     */
    public static PersistenceDAO getInstance(){
        if(instance == null)
            instance = new PersistenceDAO();
        return instance;
    }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO insert here sqLiteDatabase.execSQL( sql )
        // where sql is the create sql statement for all the table we need
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO here insert the drop sql statement for old table
        onCreate(sqLiteDatabase);
    }
}
