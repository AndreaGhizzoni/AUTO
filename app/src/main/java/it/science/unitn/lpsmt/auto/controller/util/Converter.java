package it.science.unitn.lpsmt.auto.controller.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;

import java.util.Date;

import it.science.unitn.lpsmt.auto.controller.dao.DAOPlace;
import it.science.unitn.lpsmt.auto.controller.dao.DAOReminder;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Reminder;
import it.science.unitn.lpsmt.auto.model.Vehicle;

import static it.science.unitn.lpsmt.auto.controller.util.Date.getDateFromString;
import static it.science.unitn.lpsmt.auto.controller.util.Date.getStringFromDate;

/**
 * TODO add doc
 */
public final class Converter {
    /**
     * TODO add doc
     * @param v
     * @return
     */
    public static ContentValues vehicleToContentValues( Vehicle v ){
        if( v == null )
            return null;

        ContentValues c = new ContentValues();
//        c.put(Vehicle.SQLData.ID, v.getId());
        c.put(Vehicle.SQLData.NAME, v.getName());
        c.put(Vehicle.SQLData.PLATE, v.getPlate());
        c.put(Vehicle.SQLData.PURCHASE_DATA, getStringFromDate(v.getPurchaseDate()));
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Vehicle cursorToVehicle( Cursor c ){
        if( c == null )
            return null;

        Long id = c.getLong(0);
        String name = c.getString(1);
        String plate = c.getString(2);
        Date purchase_date = getDateFromString(c.getString(3));
        return new Vehicle(name, plate, purchase_date, id);
    }

    /**
     * TODO add doc
     * @param r
     * @return
     */
    public static ContentValues reminderToContentValues( Reminder r ) {
        if( r == null )
            return null;

        ContentValues c = new ContentValues();
        c.put(Reminder.SQLData.ID, r.getId());
        c.put(Reminder.SQLData.DATE, getStringFromDate(r.getDate()));
        c.put(Reminder.SQLData.CALENDAR_ID, r.getCalendarID());
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Reminder cursorToReminder( Cursor c ){
        if( c == null )
            return null;

        Long id = c.getLong(0);
        Date date = getDateFromString(c.getString(1));
        Integer calendarID = c.getInt(2);
        return new Reminder(date, calendarID, id);
    }

    /**
     * TODO add doc
     * @param o
     * @return
     */
    public static ContentValues refuelToContentValues( Cost o ){
        if( o == null )
            return null;

        ContentValues c = new ContentValues();
        c.put(Cost.SQLData.ID, o.getId());
        c.put(Cost.SQLData.AMOUNT, o.getAmount());
        c.put(Cost.SQLData.NOTES, o.getNotes());
        if(o instanceof Refuel) {
            Refuel tmp = (Refuel) o;
            c.put(Cost.SQLData.CLASS, Refuel.class.getSimpleName().toLowerCase());
            c.put(Cost.SQLData.PRICE_PER_LITER, tmp.getPricePerLiter());
            c.put(Cost.SQLData.DATE, getStringFromDate(tmp.getDate()));
            c.put(Cost.SQLData.AT_KM, tmp.getKm());
            c.put(Cost.SQLData.PLACE_ID, tmp.getPlace().getId() );
        }else if(o instanceof Maintenance) {
            Maintenance tmp = (Maintenance) o;
            c.put(Cost.SQLData.CLASS, Maintenance.class.getSimpleName().toLowerCase());
            c.put(Cost.SQLData.NAME, tmp.getName());
            c.put(Cost.SQLData.TYPE, tmp.getType().toString());
            c.put(Cost.SQLData.PLACE_ID, tmp.getPlace() != null ? tmp.getPlace().getId(): -1);
            c.put(Cost.SQLData.REMINDER_ID, tmp.getReminder() != null ? tmp.getReminder().getId(): -1 );
        }
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Cost cursorToCost( Cursor c ){
        if( c == null )
            return null;

        Cost cost = null;
        Long id = c.getLong(0);
        Float amount = c.getFloat(1);
        String notes = c.getString(2);
        String clazz = c.getString(3);
        Place place = new DAOPlace().get(c.getLong(7));

        if(clazz.equals(Refuel.class.getSimpleName().toLowerCase())){
            Float pricePerLiter = c.getFloat(4);
            Date d = getDateFromString(c.getString(5));
            Integer atKm = c.getInt(6);
            cost = new Refuel(amount, pricePerLiter, atKm, place, d, notes, id);
        }else if(clazz.equals(Maintenance.class.getSimpleName().toLowerCase())){
            String name = c.getString(7);
            Maintenance.Type type = Maintenance.Type.valueOf(c.getString(8));
            Reminder reminder = new DAOReminder().get(c.getLong(8));
            cost = new Maintenance(type, name, amount, place, reminder, notes, id);
        }
        return cost;
    }

    /**
     * TODO add doc
     * @param p
     * @return
     */
    public static ContentValues placeToContentValues( Place p ){
        if( p == null )
            return null;

        ContentValues c = new ContentValues();
        c.put(Place.SQLData.ID, p.getId());
        if( p.getGeoTag() != null ) {
            c.put(Place.SQLData.LATITUDE, p.getGeoTag().getLatitude());
            c.put(Place.SQLData.LONGITUDE, p.getGeoTag().getLongitude());
        }else{
            double minusOne = -1;
            c.put(Place.SQLData.LATITUDE, minusOne );
            c.put(Place.SQLData.LONGITUDE, minusOne );
        }
        c.put(Place.SQLData.ADDRESS, p.getAddress());
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Place cursorToPlace( Cursor c ){
        if( c == null )
            return null;

        Long id = c.getLong(0);
        Location l = null;
        double lat = c.getDouble(1);
        double lon = c.getDouble(2);
        if( lat != -1 && lon != -1 ){
            l = new Location("db");
            l.setLatitude(lat);
            l.setLongitude(lon);
        }
        String address = c.getString(3);

        return new Place(l, address, id);
    }
}
