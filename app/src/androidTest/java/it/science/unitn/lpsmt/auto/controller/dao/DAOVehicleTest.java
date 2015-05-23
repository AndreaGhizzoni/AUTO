package it.science.unitn.lpsmt.auto.controller.dao;

import android.test.AndroidTestCase;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.Generator.getVehicleInstance;


/**
 * TODO add doc
 */
public class DAOVehicleTest extends AndroidTestCase {
    private DAOVehicle daoVehicle;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        // >>>>> NB <<<<< remember to uninstall the test application
        // from the device before run this test
        daoVehicle = new DAOVehicle(getContext());
    }

    @Override
    public void tearDown() throws Exception {
        this.daoVehicle.close();
        super.tearDown();
    }

    public void testIODB(){
        // test the storing procedure.
        Vehicle toStore = getVehicleInstance();

        Long idFromDB = this.daoVehicle.save(toStore);
        assertTrue("Id from DB must be different from NO_DB_ID_SET", !idFromDB.equals(Const.NO_DB_ID_SET));

        // test if there is at least one vehicle stored
        int vehicleStored = this.daoVehicle.getAll().size();
        assertTrue("There must be at least one Vehicle stored.", vehicleStored == 1);

        // check if there is the same object into db.
        Vehicle fromDb = this.daoVehicle.get(toStore.getId());
        assertTrue("Vehicle from DB must be not null.", fromDb != null);
        assertTrue("Actual and stored Vehicle must be the same", toStore.equals(fromDb));

        idFromDB = this.daoVehicle.save(Vehicle.Default.get());
        assertTrue("I can not save default vehicle into DB", idFromDB.equals(Const.NO_DB_ID_SET));
    }
}
