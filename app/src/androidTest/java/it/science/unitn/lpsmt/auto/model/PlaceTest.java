package it.science.unitn.lpsmt.auto.model;

import junit.framework.TestCase;

import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.Generator.*;

/**
 * TODO add doc
 */
public class PlaceTest extends TestCase {

    //TODO add description
    public void testStress(){
        Place p = getPlaceInstance();

        // menu_action_delete_and_modify set id
        p.setId(null);
        assertTrue(
            "I can pass null to setId and will set NO_DB_ID_SET.",
            p.getId().equals(Const.NO_DB_ID_SET)
        );

        try{
            p.setId( Const.NO_DB_ID_SET - 1 );
            fail("I can pass a constant less then NO_DB_ID_SET.");
        }catch (IllegalArgumentException ex){}

        // menu_action_delete_and_modify set get tag
        p.setGeoTag(null);
        assertTrue("I can set a null location as geo tag.", p.getGeoTag() == null);

        // menu_action_delete_and_modify set address
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
