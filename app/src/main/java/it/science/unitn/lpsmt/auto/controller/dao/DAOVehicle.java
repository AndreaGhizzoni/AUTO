package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    //TODO IMPLEMENTS TRANSACTIONS

    @Override
    public Long save(Vehicle v) {
        if( v == null || v.isDefaultVehicle() )
            return -1L;

        //TODO check if is already inserted ??
        ContentValues cv = Converter.vehicleToContentValues(v);
        return db.insert( Vehicle.SQLData.TABLE_NAME, null, cv );
    }

    @Override
    public Vehicle get(Long id) {
        if( id == null || id.equals(-1L) )
            return null;

        Cursor c = db.query(               // select from
            Vehicle.SQLData.TABLE_NAME,
            Vehicle.SQLData.ALL_COLUMNS,   // where
            Vehicle.SQLData.ID + "+ ?",
            new String[]{"" + id},
            null, null, null
        );
        c.moveToFirst();
        Vehicle v = Converter.cursorToVehicle(c);
        // TODO query the Cost table to populate the list's costs of this vehicle
        c.close();
        return v;
    }

    @Override
    public void delete(Long id) {
        if( id == null || id.equals(-1L) )
            return;

        db.delete(
                Vehicle.SQLData.TABLE_NAME,
                Vehicle.SQLData.ID + " = ?",
                new String[]{"" + id}
        );
    }

    @Override
    public void delete(Vehicle v) {
        if( v == null || v.getId().equals(Const.NO_DB_ID_SET) || v.isDefaultVehicle() )
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

        c.moveToFirst();
        Vehicle tmp;
        while( !c.isAfterLast() ){
            tmp = Converter.cursorToVehicle(c);
            list.add(tmp);
            c.moveToNext();
        }
        c.close();

        return list;
    }
}
