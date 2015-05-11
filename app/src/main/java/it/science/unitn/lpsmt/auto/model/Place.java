package it.science.unitn.lpsmt.auto.model;

import android.location.Location;

/**
 * TODO add doc
 */
public class Place {
    private Location geoTag;
    private String address;

    public Place( Location geoTag, String address ){
        this.setGeoTag(geoTag);
        this.setAddress(address);
    }

    public Place(){
        this( null, null );
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

//==================================================================================================
// GETTER
//==================================================================================================
    public Location getGeoTag() { return geoTag; }

    public String getAddress() { return address; }
}
