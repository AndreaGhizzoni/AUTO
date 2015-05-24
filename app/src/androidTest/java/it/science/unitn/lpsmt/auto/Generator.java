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
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.controller.util.Const.LOCATION_PROVIDER;

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

    public static Location getLocationInstance(){
        Location l = new Location(LOCATION_PROVIDER);
        l.setLongitude(10);
        l.setLatitude(10);
        return l;
    }

    public static Refuel getRefuelInstance(){
        return new Refuel(
            Const.NO_DB_ID_SET,
            10f, "notes",
            1.4f,
            new Date(),
            1000,
            getPlaceInstance()
        );
    }

    public static Maintenance getMaintenanceInstance(){
        return new Maintenance(
            Const.NO_DB_ID_SET,
            100f,
            "notes",
            "name",
            Maintenance.Type.EXTRAORDINARY,
            getPlaceInstance(),
            10
        );
    }

    public static Vehicle getVehicleInstance(){
        return new Vehicle(
            Const.NO_DB_ID_SET,
            "MyVehicle",
            "qwe123",
            Vehicle.Fuel.GASOLINE,
            new Date()
        );
    }

    public static Place getPlaceInstance(){
        return new Place(Const.NO_DB_ID_SET, "address", getLocationInstance());
    }
}
