package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.controller.util.Converter;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */

//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOCost implements CostDAO {
//    private final Context context;
    private final SQLiteDatabase db;

    public DAOCost(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
//        context = MainActivity.getAppContext();
    }

    public DAOCost( Context testContext ){
        db = new PersistenceDAO(testContext).getWritableDatabase();
//        context = testContext;
    }

    @Override
    public Long save(Cost c) {
        if( c == null )
            return Const.NO_DB_ID_SET;

        Long id = Const.NO_DB_ID_SET;
        db.beginTransaction();
        try{
            if( c instanceof Refuel ) {
                Refuel tmp = (Refuel)c;
                if( tmp.getPlace() != null ) {
                    Long i = new DAOPlace().save(tmp.getPlace());
                    tmp.getPlace().setId(i);
                }
            }else{
                Maintenance tmp = (Maintenance)c;
                if( tmp.getPlace() != null ) {
                    Long i = new DAOPlace().save(tmp.getPlace());
                    tmp.getPlace().setId(i);
                }
            }

            ContentValues cv = Converter.costToContentValues(c);
            id = db.insert( Cost.SQLData.TABLE_NAME, null, cv );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOCost.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }

        return id;
    }

    @Override
    public Cost get(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return null;

        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.ALL_COLUMNS,
            Cost.SQLData.ID+" = ?",
            new String[]{id.toString()},
            null, null, null
        );

        if( c.getCount() == 0 ) { // means that select returns no rows
            return null;
        }else{
            c.moveToFirst();
            Cost cost = Converter.cursorToCost(c);
            c.close();
            return cost;
        }
    }

    @Override
    public boolean exists(Long id) {
        return !(id == null || id.equals(Const.NO_DB_ID_SET)) && get(id) != null;
    }

    @Override
    public void delete(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return;

        db.beginTransaction();
        try{
            db.delete(
                Cost.SQLData.TABLE_NAME,
                Cost.SQLData.ID+" = ?",
                new String[]{id.toString()}
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOCost.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void delete(Cost c) {
        if( c == null )
            return;
        delete(c.getId());
    }

    @Override
    public List<Cost> getAll() {
        ArrayList<Cost> list = new ArrayList<>();
        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.ALL_COLUMNS,
            null, null, null, null, null
        );
        if( c.getCount() != 0 ){ //means that there are rows
            c.moveToFirst();
            Cost tmp;
            while( !c.isAfterLast() ){
                tmp = Converter.cursorToCost(c);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public void close(){
        this.db.close();
    }
}
