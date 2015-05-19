package it.science.unitn.lpsmt.auto.controller.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.PlaceDAO;
import it.science.unitn.lpsmt.auto.model.Place;

/**
 * TODO add doc
 */
//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOPlace implements PlaceDAO {
    private final SQLiteDatabase db;

    public DAOPlace(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    @Override
    public Long save(Place p) {
        return null;
    }

    @Override
    public Place get(Long id) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public void delete(Place p) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Place> getAll() {
        return null;
    }

    @Override
    public void close(){
        this.db.close();
    }
}
