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
public class ReminderTest extends TestCase{

    //TODO add description
    public void testReminderAsCurrentEvent(){
        try{
            new Reminder(new Date(), null, Const.NO_DB_ID_SET);
        }catch (Exception e){
           fail(e.getMessage());
        }
    }

    //TODO add description
    public void testReminderAsFutureEvent(){
        try{
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR,1);
            Date tomorrow = c.getTime();
            new Reminder(tomorrow, 1000, Const.NO_DB_ID_SET);
        }catch (Exception e){
           fail(e.getMessage());
        }
    }

    //TODO add description
    public void testStress(){
        Reminder r = new Reminder(new Date(), 1, Const.NO_DB_ID_SET);

        //test set id
        r.setId(null);
        assertTrue(
            "I can pass null to setId and will set NO_DB_ID_SET.",
            r.getId().equals(Const.NO_DB_ID_SET)
        );

        try{
            r.setId( Const.NO_DB_ID_SET - 1 );
            fail("I can pass a constant less then NO_DB_ID_SET.");
        }catch (IllegalArgumentException ex){}

        //test set date
        try{
            r.setDate(null);
            fail("I can pass null as date.");
        }catch (IllegalArgumentException ex){}

        try{
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date past = s.parse("1999-01-01");

            r.setDate(past);
            fail("I can pass a date before today.");
        }catch (IllegalArgumentException ex){}
        catch (ParseException e) { fail("with parsing date."); }

        //test set calendar id
        try{
            r.setCalendarID(null);
            assertTrue("I can set null to calendar id.", r.getCalendarID() == null);

            r.setCalendarID(-1);
            fail("I can set a id less than zero as calendar id.");
        }catch (IllegalArgumentException ex){}
    }
}
