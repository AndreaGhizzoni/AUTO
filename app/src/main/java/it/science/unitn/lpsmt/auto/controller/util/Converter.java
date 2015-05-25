package it.science.unitn.lpsmt.auto.controller.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;

import java.util.Date;

import it.science.unitn.lpsmt.auto.controller.PlaceDAO;
import it.science.unitn.lpsmt.auto.controller.VehicleDAO;
import it.science.unitn.lpsmt.auto.controller.dao.DAOPlace;
import it.science.unitn.lpsmt.auto.controller.dao.DAOVehicle;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;

import static it.science.unitn.lpsmt.auto.controller.util.Date.getDateFromString;
import static it.science.unitn.lpsmt.auto.controller.util.Date.getStringFromDate;
import static it.science.unitn.lpsmt.auto.controller.util.Const.LOCATION_PROVIDER;

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
        c.put(Vehicle.SQLData.NAME, v.getName());
        c.put(Vehicle.SQLData.PLATE, v.getPlate());
        c.put(Vehicle.SQLData.FUEL, v.getFuel().toString());
        c.put(Vehicle.SQLData.PURCHASE_DATA, getStringFromDate(v.getPurchaseDate()));
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Vehicle cursorToVehicle( Cursor c, boolean forceClose ){
        if( c == null )
            return null;

        Long id = c.getLong(0);
        String name = c.getString(1);
        String plate = c.getString(2);
        Vehicle.Fuel fuel = Vehicle.Fuel.valueOf(c.getString(3));
        Date purchase_date = getDateFromString(c.getString(4));

        if(forceClose) c.close();
        return new Vehicle(id, name, plate, fuel, purchase_date);
    }

    /**
     * TODO add doc
     * @param o
     * @return
     */
    public static ContentValues costToContentValues(Cost o){
        if( o == null )
            return null;

        VehicleDAO vehicleDAO = new DAOVehicle();
        PlaceDAO placeDAO = new DAOPlace();
        ContentValues c = new ContentValues();

        // this is because in Cost table there is a foreign key to Vehicle
        Vehicle v = o.getVehicle();
        vehicleDAO.save(v);
        c.put(Cost.SQLData.VEHICLE_ID, v.getId());

        c.put(Cost.SQLData.AMOUNT, o.getAmount());
        c.put(Cost.SQLData.NOTES, o.getNotes());

        if(o instanceof Refuel) {
            Refuel tmp = (Refuel) o;
            c.put(Cost.SQLData.CLASS, Refuel.class.getSimpleName().toLowerCase());

            // this is because in Cost table there is a foreign key to Place
            Place p = tmp.getPlace();
            placeDAO.save(p);
            c.put(Cost.SQLData.PLACE_ID, p.getId() );

            c.put(Cost.SQLData.PRICE_PER_LITER, tmp.getPricePerLiter());
            c.put(Cost.SQLData.DATE, getStringFromDate(tmp.getDate()));
            c.put(Cost.SQLData.AT_KM, tmp.getKm());
        }else if(o instanceof Maintenance) {
            Maintenance tmp = (Maintenance) o;
            c.put(Cost.SQLData.CLASS, Maintenance.class.getSimpleName().toLowerCase());

            // this is because in Cost table there is a foreign key to Place
            Place p = tmp.getPlace();
            if( p == null ){ // no place set for this maintenance
                c.put(Cost.SQLData.PLACE_ID, -1);
            }else{
                placeDAO.save(p);
                c.put(Cost.SQLData.PLACE_ID, p.getId() );
            }

            c.put(Cost.SQLData.NAME, tmp.getName());
            c.put(Cost.SQLData.TYPE, tmp.getType().toString());
            c.put(Cost.SQLData.CALENDAR_ID, tmp.getCalendarID());
        }

        vehicleDAO.close();
        placeDAO.close();
        return c;
    }

    /**
     * TODO add doc
     * @param c
     * @return
     */
    public static Cost cursorToCost( Cursor c, boolean forceClose ){
        if( c == null )
            return null;

        VehicleDAO vehicleDAO = new DAOVehicle();
        PlaceDAO placeDAO = new DAOPlace();

        Cost cost = null;
        Long id = c.getLong(0);
        Vehicle vehicle = vehicleDAO.get(c.getLong(1));
        Float amount = c.getFloat(2);
        String notes = c.getString(3);
        String clazz = c.getString(4);
        Place place = placeDAO.get(c.getLong(5));

        if(clazz.equals(Refuel.class.getSimpleName().toLowerCase())){
            Float pricePerLiter = c.getFloat(6);
            Date d = getDateFromString(c.getString(7));
            Integer atKm = c.getInt(8);
            cost = new Refuel(id, vehicle, amount, notes, pricePerLiter, d, atKm, place);
        }else if(clazz.equals(Maintenance.class.getSimpleName().toLowerCase())){
            String name = c.getString(9);
            Maintenance.Type type = Maintenance.Type.valueOf(c.getString(10));
            Integer calendarID = c.getInt(11);
            cost = new Maintenance(id, vehicle, amount, notes, name, type, place, calendarID);
        }

        vehicleDAO.close();
        placeDAO.close();
        if( forceClose ) c.close();
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
    public static Place cursorToPlace( Cursor c, boolean forceClose ){
        if( c == null )
            return null;

        Long id = c.getLong(0);
        Location l = null;
        double lat = c.getDouble(1);
        double lon = c.getDouble(2);
        if( lat != -1 && lon != -1 ){
            l = new Location(LOCATION_PROVIDER);
            l.setLatitude(lat);
            l.setLongitude(lon);
        }
        String address = c.getString(3);

        if( forceClose ) c.close();
        return new Place(id, address, l);
    }
}
