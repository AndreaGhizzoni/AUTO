package it.science.unitn.lpsmt.auto.model;

import junit.framework.TestCase;

import java.util.Date;
import it.science.unitn.lpsmt.auto.model.util.Const;
import static it.science.unitn.lpsmt.auto.Generator.*;

/**
 * TODO add doc
 */
public class VehicleTest extends TestCase {

    // TODO add description
    public void testVehicleCreation(){
        Vehicle tmp = new Vehicle(
                "someName",
                "XXX123",
                Vehicle.Fuel.GAS,
                new Date(),
                Const.NO_DB_ID_SET
        );

        try{
            tmp.setId(null);
            boolean cond = tmp.getId().equals(Const.NO_DB_ID_SET);
            assertTrue("passing null to setId() will be set to NO_DB_ID_SET", cond);
        }catch (IllegalArgumentException ex){
            fail("Expecting setId(null) will set id to NO_DB_ID_SET");
        }

        //TODO add test tmp.setId( Const.NO_DB_ID_SET -1 )

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
            tmp.setFuel(null);
            fail("I can set null to fuel");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.setPurchaseDate(null);
            assertTrue("I can set null as purchase date.", tmp.getPurchaseDate() == null);

            Date d = getDate("1999-01-01");
            tmp.setPurchaseDate(d);
            assertTrue("I can set a passed date as purchase date.", tmp.getPurchaseDate().equals(d));

            tmp.setPurchaseDate(getTomorrowDate());
            fail("I can set future date for purchase");
        }catch (IllegalArgumentException ex){}

        //TODO test cost
    }
}
