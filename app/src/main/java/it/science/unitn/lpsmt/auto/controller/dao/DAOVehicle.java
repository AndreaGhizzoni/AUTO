package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.util.Converter;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOVehicle implements VehicleDAO{
    private final SQLiteDatabase db;

    public DAOVehicle(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    public DAOVehicle( Context testContext ){
        db = new PersistenceDAO(testContext).getWritableDatabase();
    }

    @Override
    public Long save(Vehicle v) {
        if( v == null || v.isDefaultVehicle() )
            return Const.NO_DB_ID_SET;

        Vehicle alreadySaved = get(v.getId()); // check if there is already a Place save with that id.
        if( alreadySaved == null ) {
            Long id = Const.NO_DB_ID_SET;
            db.beginTransaction();
            try {
                ContentValues cv = Converter.vehicleToContentValues(v);
                id = db.insert(Vehicle.SQLData.TABLE_NAME, null, cv);
                v.setId(id);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                Log.e(DAOVehicle.class.getSimpleName(), t.getMessage());
            } finally {
                db.endTransaction();
            }
            return id;
        }else{
            return alreadySaved.getId();
        }
    }

    @Override
    public Vehicle get(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return null;

        Cursor c = db.query(               // select from
            Vehicle.SQLData.TABLE_NAME,
            Vehicle.SQLData.ALL_COLUMNS,   // where
            Vehicle.SQLData.ID+" = ?",
            new String[]{id.toString()},
            null, null, null
        );
        if( c.getCount() == 0 ) { // means that select returns no rows
            return null;
        }else{
            c.moveToFirst();
            // TODO query the Cost table to populate the list's costs of this vehicle
            return Converter.cursorToVehicle(c, true);
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
                Vehicle.SQLData.TABLE_NAME,
                Vehicle.SQLData.ID+" = ?",
                new String[]{id.toString()}
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOVehicle.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void delete(Vehicle v) {
        if( v == null || v.isDefaultVehicle() )
            return;
        delete(v.getId());
    }

    @Override
    public List<Vehicle> getAll() {
        ArrayList<Vehicle> list = new ArrayList<>();
        Cursor c = db.query(
            Vehicle.SQLData.TABLE_NAME,
            Vehicle.SQLData.ALL_COLUMNS,
            null, null, null, null, null
        );
        if( c.getCount() != 0 ) {  // means that there are rows
            c.moveToFirst();
            Vehicle tmp;
            while (!c.isAfterLast()) {
                tmp = Converter.cursorToVehicle(c, false);
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
