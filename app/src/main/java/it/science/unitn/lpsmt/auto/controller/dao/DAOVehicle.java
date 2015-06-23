package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.util.Converter;
import it.science.unitn.lpsmt.auto.controller.util.DateUtils;
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
    public int update(Vehicle old, Vehicle newest) {
        if( old == null || old.isDefaultVehicle() || old.getId().equals(Const.NO_DB_ID_SET) )
            return -1;
        if( newest == null || newest.isDefaultVehicle() )
            return -1;

        int rowUpdated = 0;
        db.beginTransaction();
        try{
            ContentValues cv = new ContentValues();
            if( !old.getName().equals(newest.getName()) )
                cv.put(Vehicle.SQLData.NAME, newest.getName());
            if( !old.getPlate().equals(newest.getPlate()) )
                cv.put(Vehicle.SQLData.PLATE, newest.getPlate());
            if( !old.getFuel().equals(newest.getFuel()) )
                cv.put(Vehicle.SQLData.FUEL, newest.getFuel().toString());
            if( old.getPurchaseDate() != null && !old.getPurchaseDate().equals(newest.getPurchaseDate()) )
                cv.put(Vehicle.SQLData.PURCHASE_DATA, DateUtils.getStringFromDate(newest.getPurchaseDate()));

            if( cv.size() != 0 ) {
                rowUpdated = db.update(
                    Vehicle.SQLData.TABLE_NAME,
                    cv,
                    Vehicle.SQLData.ID + " = ?",
                    new String[]{old.getId() + ""}
                );
            }
            db.setTransactionSuccessful();
        }catch ( Throwable t){
            Log.e(DAOVehicle.class.getSimpleName(), t.getMessage());
            rowUpdated = -1;
        }finally {
            db.endTransaction();
        }
        return rowUpdated;
    }

    @Override
    public Vehicle get(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return null;

        Cursor c = db.query(               // select from
            Vehicle.SQLData.TABLE_NAME,
            Vehicle.SQLData.ALL_COLUMNS,   // where
            Vehicle.SQLData.ID + " = ?",
            new String[]{id.toString()},
            null, null, null
        );
        if( c.getCount() == 0 ) { // means that select returns no rows
            return null;
        }else{
            c.moveToFirst();
            Vehicle v = Converter.cursorToVehicle(c, true);
            v.setCosts( new DAOCost().getAllWhereVehicleIs(v) );
            return v;
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
            new DAOCost().deleteAllWhereVehicleID(id);

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
    public void deleteAll(){
        // deleting all vehicles cause deleting all costs associated.
        db.beginTransaction();
        try{
            // deleting all the cost associated at a vehicle
            CostDAO costDAO = new DAOCost();
            for( Vehicle v : this.getAll() ){
                costDAO.deleteAllWhereVehicleID(v.getId());
            }

            // deleting all the vehicle
            db.delete(
                Vehicle.SQLData.TABLE_NAME,
                null,
                null
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOVehicle.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
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

            CostDAO costDAO = new DAOCost();
            for( Vehicle v : list )
                v.setCosts(costDAO.getAllWhereVehicleIs(v));
        }
        return list;
    }

    @Override
    public int countObject(){
        Cursor c = db.rawQuery("select count(*) from " + Vehicle.SQLData.TABLE_NAME, null);
        c.moveToFirst();
        int counter = c.getInt(0);
        c.close();
        return counter;
    }

    @Override
    public void close(){
        this.db.close();
    }
}
