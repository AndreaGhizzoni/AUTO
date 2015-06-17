package it.science.unitn.lpsmt.auto.model;

import android.location.Location;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class Place {
    private Long id;
    private Location geoTag;
    private String address;

    public Place( Long id, String address, Location geoTag ){
        this.setId(id);
        this.setAddress(address);
        this.setGeoTag(geoTag);
    }

    public Place( Long id, String address ){
        this(id, address, null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId( Long id ) throws IllegalArgumentException{
        if( id == null )
            this.id = Const.NO_DB_ID_SET;
        else if( id < Const.NO_DB_ID_SET )
            throw new IllegalArgumentException("ID of Place can not be less then "+Const.NO_DB_ID_SET);
        else
            this.id = id;
    }

    public void setAddress( String address ) throws IllegalArgumentException{
        if( address == null || address.isEmpty() )
            throw new IllegalArgumentException("Address can not be null or empty string.");

        this.address = address;
    }

    public void setGeoTag( Location geoTag ){
        this.geoTag = geoTag;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Long getId(){ return id; }

    public String getAddress() { return address; }

    public Location getGeoTag() { return geoTag; }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (!id.equals(place.id)) return false;
        return address.equals(place.address);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (geoTag != null ? geoTag.hashCode() : 0);
        result = 31 * result + address.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", geoTag=" + geoTag +
                ", address='" + address + '\'' +
                '}';
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * This is the representation of the Database Table.
     * The fields name are in the same order as there are in the sql create statement.
     * =============================== DO NOT MOVE UP OR DOWN! =====================================
     */
    public static final class SQLData{
        public static final String ID         = "id";
        public static final String LATITUDE   = "latitude";
        public static final String LONGITUDE  = "longitude";
        public static final String ADDRESS    = "address";

        public static final String TABLE_NAME = Place.class.getSimpleName().toLowerCase();
        public static final String[] ALL_COLUMNS = {ID, LATITUDE, LONGITUDE, ADDRESS};

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( " +
                    ID+" integer primary key autoincrement, " +
                    LATITUDE+" real, "+
                    LONGITUDE+" real, "+
                    ADDRESS+" text "+
                ");";
    }
}
