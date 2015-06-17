package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.science.unitn.lpsmt.auto.controller.PlaceDAO;
import it.science.unitn.lpsmt.auto.controller.util.Converter;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOPlace implements PlaceDAO {
    private final SQLiteDatabase db;

    public DAOPlace(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    public DAOPlace( Context testContext ){
        db = new PersistenceDAO(testContext).getWritableDatabase();
    }

    @Override
    public Long save(Place p) {
        if( p == null )
            return Const.NO_DB_ID_SET;

        Place alreadySaved = get(p.getId()); // check if there is already a Place save with that id.
        if( alreadySaved == null ) {
            Long id = Const.NO_DB_ID_SET;
            db.beginTransaction();
            try {
                ContentValues cv = Converter.placeToContentValues(p);
                id = db.insert(Place.SQLData.TABLE_NAME, null, cv);
                p.setId(id);
                db.setTransactionSuccessful();
            } catch (Throwable t) {
                Log.e(DAOPlace.class.getSimpleName(), t.getMessage());
            } finally {
                db.endTransaction();
            }
            return id;
        }else{
            return alreadySaved.getId();
        }
    }

    @Override
    public Place get(Long id) {
        if( id == null || id.equals(Const.NO_DB_ID_SET) )
            return null;

        Cursor c = db.query(              // select from
                Place.SQLData.TABLE_NAME,
                Place.SQLData.ALL_COLUMNS,    // where
                Place.SQLData.ID + " = ?",
                new String[]{id.toString()},
                null, null, null
        );

        if( c.getCount() == 0 ) { // means that select returns no rows
            return null;
        } else {
            c.moveToFirst();
            return Converter.cursorToPlace(c, true);
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
                Place.SQLData.TABLE_NAME,
                Place.SQLData.ID+" = ?",
                new String[]{id.toString()}
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOPlace.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void delete(Place p) {
        if( p == null )
            return;
        delete(p.getId());
    }

    @Override
    public void deleteAll(){
        db.beginTransaction();
        try{
            db.delete(
                Place.SQLData.TABLE_NAME,
                null,
                null
            );
            db.setTransactionSuccessful();
        }catch (Throwable t){
            Log.e(DAOPlace.class.getSimpleName(), t.getMessage());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public List<Place> getAll() {
        ArrayList<Place> list = new ArrayList<>();
        Cursor c = db.query(
            Place.SQLData.TABLE_NAME,
            Place.SQLData.ALL_COLUMNS,
            null, null, null, null, null
        );
        if( c.getCount() != 0 ) {  // means that there are rows
            c.moveToFirst();
            Place tmp;
            while (!c.isAfterLast()) {
                tmp = Converter.cursorToPlace(c, false);
                list.add(tmp);
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

    @Override
    public int countObject(){
        Cursor c = db.rawQuery("select count(*) from "+Place.SQLData.TABLE_NAME, null);
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
