package it.science.unitn.lpsmt.auto.model;

import android.location.Location;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class MaintenanceTest extends TestCase {

    //TODO add description
    public void testMaintenanceCreation(){
        Location l = new Location("asd");
        l.setLongitude(1);
        l.setLatitude(1);
        Place p = new Place(l, "address", Const.NO_DB_ID_SET);
        Maintenance m = new Maintenance(
            Maintenance.Type.EXTRAORDINARY,
            "maintenance name",
            10f,
            p,
            new Reminder(getReminderDate(), 1, Const.NO_DB_ID_SET ),
            "notes",
            Const.NO_DB_ID_SET
        );

        try{
            m.setName(null);
            fail("I can not set a null name.");
        }catch (IllegalArgumentException ex){}

        try{
            m.setName("");
            fail("I can not set an empty string as name.");
        }catch (IllegalArgumentException ex){}

        try{
            m.setType(null);
            fail("I can not set a null Maintenance.Type");
        }catch (IllegalArgumentException ex){}

        try{
            m.setPlace(null);
            boolean c = m.getPlace() == null;
            assertTrue("I can set a null place.", c);
        }catch (IllegalArgumentException ex){
            fail("Expecting that setPlace(null) will set place to null.");
        }

        try{
            m.setPlace(null);
            boolean c = m.getPlace() == null;
            assertTrue("I can set a null place.", c);
        }catch (IllegalArgumentException ex){
            fail("Expecting that setPlace(null) will set place to null.");
        }

        try{
            m.setReminder(null);
            boolean c = m.getReminder() == null;
            assertTrue("I can set a null reminder.", c);
        }catch (IllegalArgumentException ex){
            fail("Expecting that setReminder(null) will set reminder to null.");
        }
    }

    private Date getReminderDate(){
        SimpleDateFormat s = new SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        );
        try {
            return s.parse("2015-12-01");
        } catch (ParseException e) {
            return null;
        }
    }

}
