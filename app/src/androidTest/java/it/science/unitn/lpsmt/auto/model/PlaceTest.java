package it.science.unitn.lpsmt.auto.model;

import android.location.Location;

import junit.framework.TestCase;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class PlaceTest extends TestCase {
    //TODO add description
    public void testPlace(){
        try{
            Location l = new Location("provider");
            l.setLatitude(46);
            l.setLongitude(11);

            new Place(l, "address", Const.NO_DB_ID_SET);
        }catch (Exception e){
            fail(e.getMessage());
        }
    }

    public void testStress(){
        Place p = new Place(null, "address", Const.NO_DB_ID_SET);

        // test set id
        p.setId(null);
        assertTrue(
            "I can pass null to setId and will set NO_DB_ID_SET.",
            p.getId().equals(Const.NO_DB_ID_SET)
        );

        try{
            p.setId( Const.NO_DB_ID_SET - 1 );
            fail("I can pass a constant less then NO_DB_ID_SET.");
        }catch (IllegalArgumentException ex){}

        // test set get tag
        p.setGeoTag(null);
        assertTrue("I can set a null location as geo tag.", p.getGeoTag() == null);

        // test set address
        try{
            p.setAddress(null);
            fail("I can not set a null address.");
        }catch (IllegalArgumentException ex){}

        try{
            p.setAddress("");
            fail("I can not set a empty string as address.");
        }catch (IllegalArgumentException ex){}
    }
}
