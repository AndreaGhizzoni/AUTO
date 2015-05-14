package it.science.unitn.lpsmt.auto.controller.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.model.Vehicle;

/**
 * TODO add doc
 */
//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOVehicle implements VehicleDAO{
    private final SQLiteDatabase db;

    public DAOVehicle(){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    @Override
    public Long save(Vehicle v) {
        return null;
    }

    @Override
    public Vehicle get(Long id) {
        return null;
    }

    @Override
    public void delete(Vehicle v) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Vehicle> getAll() {
        return null;
    }
}
