package it.science.unitn.lpsmt.auto.controller.dao;

import android.test.AndroidTestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.model.Vehicle;
import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class DAOVehicleTest extends AndroidTestCase {
    private DAOVehicle daoVehicle;

    // default value for local vehicle
    private Long id = Const.NO_DB_ID_SET;
    private String name = "someName";
    private Date purchaseDate = new Date();
    private String plate = "XXX123";


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

//==================================================================================================
//  TESTS
//==================================================================================================
    public void testVehicleCreation(){
        Vehicle tmp = new Vehicle(name, plate, purchaseDate, id);
        try{
            tmp.setId(null);
            boolean cond = tmp.getId() == Const.NO_DB_ID_SET;
            assertTrue("passing null to setId() will be set to NO_DB_ID_SET", cond);
        }catch (IllegalArgumentException ex){
            fail("Expecting setId(null) will set id to NO_DB_ID_SET");
        }

        try{
            tmp.setName(null);
            fail("I can set null string name.");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.setName("");
            fail("I can set empty name.");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.setPlate(null);
            fail("I can set null string as plate.");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.setPlate("");
            fail("I can set empty plate.");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.setPurchaseDate(null);
            assertTrue("I can set null as purchase date.", tmp.getPurchaseDate() == null);

            Date d = getPastDate();
            tmp.setPurchaseDate(d);
            assertTrue("I can set a passed date as purchase date.", tmp.getPurchaseDate().equals(d));

            tmp.setPurchaseDate(getTomorrowDate());
            fail("I can set future date for purchase");
        }catch (IllegalArgumentException ex){}

        //TODO test cost
    }

    public void testIODB(){
        // test the storing procedure.
        Vehicle toStore = new Vehicle(name, plate, purchaseDate, id);
        Long idFromDB = this.daoVehicle.save(toStore);
        assertTrue("Id from DB must be different from NO_DB_ID_SET", !idFromDB.equals(Const.NO_DB_ID_SET));

        // test if there is at least one vehicle stored
        int vehicleStored = this.daoVehicle.getAll().size();
        assertTrue("There must be at least one Vehicle stored.", vehicleStored == 1);


        // set the id from db to actual vehicle and check if there is the same object into db.
        toStore.setId(idFromDB);
        Vehicle fromDb = this.daoVehicle.get(idFromDB);
        assertTrue("Vehicle from DB must be not null.", fromDb != null);
        assertTrue("Actual and stored Vehicle must be the same", toStore.equals(fromDb));
    }


//==================================================================================================
//  METHOD
//==================================================================================================
    private Date getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    private Date getPastDate(){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return s.parse("1999-12-01");
        } catch (ParseException e) {
            return null;
        }
    }
}
