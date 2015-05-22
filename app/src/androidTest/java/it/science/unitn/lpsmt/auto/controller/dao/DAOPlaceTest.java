package it.science.unitn.lpsmt.auto.controller.dao;

import android.location.Location;
import android.test.AndroidTestCase;

import it.science.unitn.lpsmt.auto.model.Place;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class DAOPlaceTest extends AndroidTestCase{
    private DAOPlace daoPlace;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        // >>>>> NB <<<<< remember to uninstall the test application
        // from the device before run this test
        this.daoPlace = new DAOPlace(getContext());

    }

    @Override
    public void tearDown() throws Exception{
        this.daoPlace.close();
        super.tearDown();
    }

    public void testIODB(){
        Location geoTag = new Location("test");
        geoTag.setLatitude(10);
        geoTag.setLongitude(10);
        String address = "myAddress";
        Place toStore = new Place(Const.NO_DB_ID_SET, address, geoTag );

        // testing the storing procedure
        Long idFromDB = this.daoPlace.save(toStore);
        assertTrue(
                "Id from DB must be different from NO_DB_ID_SET",
                !idFromDB.equals(Const.NO_DB_ID_SET)
        );

        // test if there is at least one place stored
        int placeStored = this.daoPlace.getAll().size();
        assertTrue("There must be at least one Place stored.", placeStored != 0);

        // set the id from db to actual place and check if there is the same object into db.
        toStore.setId(idFromDB);
        Place fromDB = this.daoPlace.get(idFromDB);
        assertTrue("Place from DB must be not null.", fromDB != null);
        assertTrue("Actual and stored Place must be the same.", toStore.equals(fromDB));

        // test the deleting procedure
        this.daoPlace.delete(fromDB);

        // test if there isn't the place deleted before
        Place nullPlace = this.daoPlace.get(fromDB.getId());
        assertTrue("Try to get a invalid place from invalid id, returns null.", nullPlace == null);
    }
}
