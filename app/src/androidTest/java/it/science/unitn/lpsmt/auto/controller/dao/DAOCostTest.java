package it.science.unitn.lpsmt.auto.controller.dao;

import android.test.AndroidTestCase;
import it.science.unitn.lpsmt.auto.model.Cost;
import it.science.unitn.lpsmt.auto.model.Maintenance;
import it.science.unitn.lpsmt.auto.model.Refuel;
import it.science.unitn.lpsmt.auto.model.util.Const;

import static it.science.unitn.lpsmt.auto.Generator.*;

/**
 * TODO add doc
 */
public class DAOCostTest extends AndroidTestCase {
    private DAOCost daoCost;

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
        Refuel toStore = getRefuelInstance();

        // testing the storing procedure
        Long idFromDB = this.daoCost.save(toStore);
        assertTrue(
            "Id from DB must be different from NO_DB_ID_SET.",
            !idFromDB.equals(Const.NO_DB_ID_SET)
        );

        // test if there is at least one Cost stored
        int costStored = this.daoCost.getAll().size();
        assertTrue("There must be at least on Cost stored.", costStored != 0);

        // set the id from db to actual cost and if there are the same object into db.
        toStore.setId(idFromDB);
        Cost fromDB = this.daoCost.get(idFromDB);
        assertTrue("Cost from DB must be not null.", fromDB != null);
        //TODO add check when equals and hashCode are implemented

        // testing the deleting procedure
        this.daoCost.delete(fromDB);
        Cost nullCost = this.daoCost.get(idFromDB);
        assertTrue("Try to get a deleted cost from DB, will returns null.", nullCost == null );
    }

    public void testMaintenanceIODB(){
        Maintenance toStore = getMaintenanceInstance();

        // testing the storing procedure
        Long idFromDB = this.daoCost.save(toStore);
        assertTrue(
            "Id from DB must be different from NO_DB_ID_SET.",
            !idFromDB.equals(Const.NO_DB_ID_SET)
        );

        // test if there is at least one Cost stored
        int costStored = this.daoCost.getAll().size();
        assertTrue("There must be at least on Cost stored.", costStored != 0);

        // set the id from db to actual cost and if there are the same object into db.
        toStore.setId(idFromDB);
        Cost fromDB = this.daoCost.get(idFromDB);
        assertTrue("Cost from DB must be not null.", fromDB != null);
        //TODO add check when equals and hashCode are implemented

        // testing the deleting procedure
        this.daoCost.delete(fromDB);
        Cost nullCost = this.daoCost.get(idFromDB);
        assertTrue("Try to get a deleted cost from DB, will returns null.", nullCost == null );
    }
}
