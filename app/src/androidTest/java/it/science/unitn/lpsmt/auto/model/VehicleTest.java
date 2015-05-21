package it.science.unitn.lpsmt.auto.model;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class VehicleTest extends TestCase {

    // TODO add description
    public void testVehicleCreation(){
        Vehicle tmp = new Vehicle("someName", "XXX123", new Date(), Const.NO_DB_ID_SET);
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
