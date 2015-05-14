package it.science.unitn.lpsmt.auto.controller.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import it.science.unitn.lpsmt.auto.controller.ReminderDAO;
import it.science.unitn.lpsmt.auto.model.Reminder;

/**
 * TODO add doc
 */

//https://stackoverflow.com/questions/8147440/android-database-transaction
public class DAOReminder implements ReminderDAO {
    private final SQLiteDatabase db;

    public DAOReminder(Context c){
        db = PersistenceDAO.getInstance().getWritableDatabase();
    }

    @Override
    public Long save(Reminder r) {
        return null;
    }

    @Override
    public Reminder get(Long id) {
        return null;
    }

    @Override
    public void delete(Reminder r) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Reminder> getAll() {
        return null;
    }
}
