package it.science.unitn.lpsmt.auto;

import android.location.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.controller.util.Const.LOCATION_PROVIDER;

public final class Generator {
    private static SimpleDateFormat s = new SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    );

    private static Random r = new Random(new Date().getTime());

    public static Date getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getDate( String yyyymmdd ){
        try {
            return s.parse(yyyymmdd);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Location getLocationInstance(){
        Location l = new Location(LOCATION_PROVIDER);
        l.setLongitude(r.nextInt(9) + 1);
        l.setLatitude(r.nextInt(9) + 1);
        return l;
    }

    public static Vehicle getVehicleInstance(){
        return new Vehicle(
            Const.NO_DB_ID_SET,
            "MyVehicle_"+(r.nextInt(100)+1),
            "plate"+(r.nextInt(100)+1),
            Vehicle.Fuel.GASOLINE,
            new Date()
        );
    }

    public static Place getPlaceInstance(){
        return new Place(
            Const.NO_DB_ID_SET,
            "address_"+(r.nextInt(10)+1),
            getLocationInstance()
        );
    }

    public static Refuel getRefuelInstance( Vehicle v ){
        return new Refuel(
            Const.NO_DB_ID_SET,
            v,
            (r.nextFloat()*10)+1,
            "notes",
            (r.nextFloat())+0.4f,
            new Date(),
            r.nextInt(1000)+1,
            getPlaceInstance()
        );
    }

    public static Maintenance getMaintenanceInstance( Vehicle v ){
        return new Maintenance(
            Const.NO_DB_ID_SET,
            v,
            (r.nextFloat()*100)+1,
            "notes",
            "name",
            Maintenance.Type.EXTRAORDINARY,
            getPlaceInstance(),
            (r.nextInt(10)+1)
        );
    }
}
