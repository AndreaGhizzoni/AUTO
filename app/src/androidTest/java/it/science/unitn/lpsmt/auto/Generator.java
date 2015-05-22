package it.science.unitn.lpsmt.auto;

import android.location.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.util.Const;

public final class Generator {
    private Generator(){}

    public static Date getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getDate( String yyyymmdd ){
        SimpleDateFormat s = new SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        );
        try {
            return s.parse(yyyymmdd);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Refuel getRefuelInstance(){
        Location l = new Location("db");
        l.setLongitude(10);
        l.setLatitude(10);
        Place p = new Place(Const.NO_DB_ID_SET, "address", l);
        return new Refuel(Const.NO_DB_ID_SET, 10f, "notes", 1.4f, new Date(), 1000, p);
    }

    public static Maintenance getMaintenanceInstance(){
        Location l = new Location("db");
        l.setLongitude(10);
        l.setLatitude(10);
        Place p = new Place(Const.NO_DB_ID_SET, "address", l);
        return new Maintenance(Const.NO_DB_ID_SET, 100f, "notes", "name",
                Maintenance.Type.EXTRAORDINARY, p, 10);
    }
}
