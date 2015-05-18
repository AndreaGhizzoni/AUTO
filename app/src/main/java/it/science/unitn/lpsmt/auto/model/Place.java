package it.science.unitn.lpsmt.auto.model;

import android.location.Location;

/**
 * TODO add doc
 */
public class Place {
    private Long id;
    private Location geoTag;
    private String address;

    public Place( Location geoTag, String address, Long id ){
        this.setGeoTag(geoTag);
        this.setAddress(address);
        this.setId(id);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setGeoTag( Location geoTag ){
        if( geoTag != null )
            this.geoTag = geoTag;
    }

    public void setAddress( String address ){
        if( address != null )
            this.address = address;
    }

    public void setId( Long id ){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Long getId(){ return id; }

    public Location getGeoTag() { return geoTag; }

    public String getAddress() { return address; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static final class SQLData{
        public static final String TABLE_NAME = Place.class.getSimpleName().toLowerCase();
        public static final String ID         = "id";
        public static final String LATITUDE   = "latitude";
        public static final String LONGITUDE  = "longitude";
        public static final String ADDRESS    = "address";

        public static final String SQL_CREATE =
                "create table "+ Place.SQLData.TABLE_NAME+"( " +
                Place.SQLData.ID+" integer primary key autoincrement, " +
                Place.SQLData.LATITUDE+" real, " +
                Place.SQLData.LONGITUDE+" real, " +
                Place.SQLData.ADDRESS+" text);";
    }
}
