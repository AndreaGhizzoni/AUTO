package it.science.unitn.lpsmt.auto.controller.dao;

import android.test.AndroidTestCase;

import it.science.unitn.lpsmt.auto.controller.CostDAO;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.Generator.getMaintenanceInstance;
import static it.science.unitn.lpsmt.auto.Generator.getRefuelInstance;
import static it.science.unitn.lpsmt.auto.Generator.getVehicleInstance;

/**
 * TODO add doc
 */
public class DAOCostTest extends AndroidTestCase {
    private CostDAO daoCost;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        // >>>>> NB <<<<< remember to uninstall the test application
        // from the device before run this test
        this.daoCost = new DAOCost(getContext());
    }

    @Override
    public void tearDown() throws Exception{
        this.daoCost.close();
        super.tearDown();
    }

    public void testRefuelIODB(){
        Vehicle v = getVehicleInstance();
        Refuel toStore = getRefuelInstance(v);

        // testing the storing procedure
        Long idFromDB = this.daoCost.save(toStore);
        assertTrue(
                "Id from DB must be different from NO_DB_ID_SET.",
                !idFromDB.equals(Const.NO_DB_ID_SET)
        );

        // test if there is at least one Cost stored
        int costStored = this.daoCost.countObject();
        assertTrue("There must be at least on Cost stored.", costStored != 0);

        // check if there are the same object into db.
        Cost fromDB = this.daoCost.get(toStore.getId());
        assertTrue("Cost from DB must be not null.", fromDB != null);
        assertTrue("Actual and stored Refuel must be the same.", fromDB.equals(toStore));

        // check if not save the same object
        this.daoCost.save(toStore);
        int newCostStored = this.daoCost.countObject();
        assertTrue("I can not save the same object twice.", costStored == newCostStored);

        // testing the deleting procedure
        this.daoCost.delete(fromDB);
        Cost nullCost = this.daoCost.get(idFromDB);
        assertTrue("Try to get a deleted cost from DB, will returns null.", nullCost == null );
    }

    public void testMaintenanceIODB(){
        Vehicle v = getVehicleInstance();
        Maintenance toStore = getMaintenanceInstance(v);

        // testing the storing procedure
        Long idFromDB = this.daoCost.save(toStore);
        assertTrue(
            "Id from DB must be different from NO_DB_ID_SET.",
            !idFromDB.equals(Const.NO_DB_ID_SET)
        );

        // test if there is at least one Cost stored
        int costStored = this.daoCost.countObject();
        assertTrue("There must be at least on Cost stored.", costStored != 0);

        // check if there are the same object into db.
        Cost fromDB = this.daoCost.get(toStore.getId());
        assertTrue("Cost from DB must be not null.", fromDB != null);
        assertTrue("Actual and stored Maintenance must be the same.", fromDB.equals(toStore));

        // check if not save the same object
        this.daoCost.save(toStore);
        int newCostStored = this.daoCost.countObject();
        assertTrue("I can not save the same object twice.", costStored == newCostStored);

        // testing the deleting procedure
        this.daoCost.delete(fromDB);
        Cost nullCost = this.daoCost.get(idFromDB);
        assertTrue("Try to get a deleted cost from DB, will returns null.", nullCost == null );
    }
}
