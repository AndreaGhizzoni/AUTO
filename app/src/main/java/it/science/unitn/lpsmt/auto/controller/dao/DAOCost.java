package it.science.unitn.lpsmt.auto.controller.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.model.Cost;

/**
 * TODO add doc
 */

//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOCost implements CostDAO {
    private final SQLiteDatabase db;

    public DAOCost(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    @Override
    public Long save(Cost c) {
        return null;
    }

    @Override
    public Cost get(Long id) {
        return null;
    }

    @Override
    public boolean exists(Long id) {
        return false;
    }

    @Override
    public void delete(Cost c) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Cost> getAll() {
        return null;
    }

    @Override
    public void close(){
        this.db.close();
    }
}
