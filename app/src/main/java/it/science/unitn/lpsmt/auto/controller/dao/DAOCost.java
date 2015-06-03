package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.controller.util.Converter;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */

//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOCost implements CostDAO {
    private final SQLiteDatabase db;

    public DAOCost(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    public DAOCost( Context testContext ){
        db = new PersistenceDAO(testContext).getWritableDatabase();
    }

    @Override
    public Long save(Cost c) {
        if( c == null )
            return Const.NO_DB_ID_SET;

        Cost alreadySaved = get(c.getId()); // check if there is already a Place save with that id.
        if( alreadySaved == null ) {
            Long id = Const.NO_DB_ID_SET;
            db.beginTransaction();
            try {
                ContentValues cv = Converter.costToContentValues(c);
                id = db.insert(Cost.SQLData.TABLE_NAME, null, cv);
                c.setId(id);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                Log.e(DAOCost.class.getSimpleName(), t.getMessage());
            } finally {
                db.endTransaction();
            }
            return id;
        }else{
            return alreadySaved.getId();
        }
    }

    @Override
    public Cost get(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return null;

        Cursor c = db.query(
                Cost.SQLData.TABLE_NAME,
                Cost.SQLData.ALL_COLUMNS,
                Cost.SQLData.ID + " = ?",
                new String[]{id.toString()},
                null, null, null
        );

        if( c.getCount() == 0 ) { // means that select returns no rows
            return null;
        }else{
            c.moveToFirst();
            return Converter.cursorToCost(c, true);
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
    public void deleteAllWhereVehicleID( Long id ){
        // pass null to delete all the entry
        String where = null;
        String[] whereArgs = null;
        if( id != null ){
            where = Cost.SQLData.VEHICLE_ID+" = ?";
            whereArgs = new String[]{id.toString()};
        }

        db.beginTransaction();
        try{
            db.delete(
                Cost.SQLData.TABLE_NAME,
                where,
                whereArgs
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOCost.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteAll(){
        // null means 'skip'. In other words delete all the cost.
        this.deleteAllWhereVehicleID(null);
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
                tmp = Converter.cursorToCost(c, false);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public List<Refuel> getAllRefuel() {
        ArrayList<Refuel> list = new ArrayList<>();
        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.COLUMNS_REFUEL,
            Cost.SQLData.CLASS+" = ?",
            new String[]{Refuel.class.getSimpleName().toLowerCase()},
            null, null,
            Cost.SQLData.DATE
        );
        if( c.getCount() != 0 ){ //means that there are rows
            c.moveToFirst();
            Refuel tmp;
            while( !c.isAfterLast() ){
                Long id = c.getLong(0);
                Vehicle v = new DAOVehicle().get(c.getLong(1));
                Float amount = c.getFloat(2);
                String notes = c.getString(3);
                Place place = new DAOPlace().get(c.getLong(4));
                Float ppl = c.getFloat(5);
                Date date = it.science.unitn.lpsmt.auto.controller.util.Date.getDateFromString(c.getString(6));
                Integer km = c.getInt(7);

                tmp = new Refuel(id, v, amount, notes, ppl, date, km, place);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public List<Maintenance> getAllMaintenance() {
        ArrayList<Maintenance> list = new ArrayList<>();
        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.COLUMNS_MAINTENANCE,
            Cost.SQLData.CLASS+" = ?",
            new String[]{Maintenance.class.getSimpleName().toLowerCase()},
            null, null, null
        );
        if( c.getCount() != 0 ){ //means that there are rows
            c.moveToFirst();
            Maintenance tmp;
            while( !c.isAfterLast() ){
                Long id = c.getLong(0);
                Vehicle v = new DAOVehicle().get(c.getLong(1));
                Float amount = c.getFloat(2);
                String notes = c.getString(3);
                Place place = new DAOPlace().get(c.getLong(4));
                String name = c.getString(5);
                Maintenance.Type type = Maintenance.Type.valueOf(c.getString(6));
                Integer calendarID = c.getInt(7);

                tmp = new Maintenance(id, v, amount, notes, name, type, place, calendarID);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public List<Maintenance> getAllMaintenanceWhereTypeIs(Maintenance.Type type) {
        ArrayList<Maintenance> list = new ArrayList<>();
        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.COLUMNS_MAINTENANCE,
            Cost.SQLData.CLASS+" = ? and "+Cost.SQLData.TYPE+" = ?",
            new String[]{Maintenance.class.getSimpleName().toLowerCase(), type.toString()},
            null, null, null
        );
        if( c.getCount() != 0 ){ //means that there are rows
            c.moveToFirst();
            Maintenance tmp;
            while( !c.isAfterLast() ){
                Long id = c.getLong(0);
                Vehicle v = new DAOVehicle().get(c.getLong(1));
                Float amount = c.getFloat(2);
                String notes = c.getString(3);
                Place place = new DAOPlace().get(c.getLong(4));
                String name = c.getString(5);
                Integer calendarID = c.getInt(7);// this index is right

                tmp = new Maintenance(id, v, amount, notes, name, type, place, calendarID);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }


    @Override
    public List<Cost> getAllWhereVehicleIs(Vehicle v){
        ArrayList<Cost> list = new ArrayList<>();
        Cursor c = db.query(
            Cost.SQLData.TABLE_NAME,
            Cost.SQLData.ALL_COLUMNS_WITHOUT_VEHICLE_FK,
            Cost.SQLData.VEHICLE_ID+" = ?",
            new String[]{v.getId().toString()},
            null, null, null, null
        );

        if( c.getCount() != 0 ){
            c.moveToFirst();
            Cost tmp = null;
            while( !c.isAfterLast() ){
                Long id = c.getLong(0);
                Float amount = c.getFloat(1);
                String notes = c.getString(2);
                String clazz = c.getString(3);
                Place place = new DAOPlace().get(c.getLong(4));

                if(clazz.equals(Refuel.class.getSimpleName().toLowerCase())){
                    Float pricePerLiter = c.getFloat(5);
                    Date d = it.science.unitn.lpsmt.auto.controller.util.Date.
                            getDateFromString(c.getString(6));
                    Integer atKm = c.getInt(7);
                    tmp = new Refuel(id, v, amount, notes, pricePerLiter, d, atKm, place);
                }else if(clazz.equals(Maintenance.class.getSimpleName().toLowerCase())){
                    String name = c.getString(8);
                    Maintenance.Type type = Maintenance.Type.valueOf(c.getString(9));
                    Integer calendarID = c.getInt(10);
                    tmp = new Maintenance(id, v, amount, notes, name, type, place, calendarID);
                }

                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public int countObject(){
        Cursor c = db.rawQuery("select count(*) from "+Cost.SQLData.TABLE_NAME, null);
        c.moveToFirst();
        int counter = c.getInt(0);
        c.close();
        return counter;
    }

    @Override
    public void close(){ this.db.close(); }
}
