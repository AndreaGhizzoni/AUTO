package it.science.unitn.lpsmt.auto.model;

import junit.framework.TestCase;

import static it.science.unitn.lpsmt.auto.Generator.*;

/**
 * TODO add doc
 */
public class MaintenanceTest extends TestCase {

    //TODO add description
    public void testMaintenanceCreation(){
        Maintenance m = getMaintenanceInstance();

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
            fail("I can not set a null Maintenance.Type.");
        }catch (IllegalArgumentException ex){}

        try{
            Maintenance.Type t = Maintenance.Type.TAX;
            m.setType(t);
            assertTrue("I can set a valid Maintenance.Type.", m.getType().equals(t));
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
            m.setCalendarID(null);
            assertTrue("I can set null to calendarID.", m.getCalendarID() == null);

            Integer newCalendarID = 100;
            m.setCalendarID(newCalendarID);
            boolean c = m.getCalendarID().equals(newCalendarID);
            assertTrue("I can set an arbitrary calendarID.", c);
        }catch (IllegalArgumentException ex){
            fail("Expecting that setCalendarID() will set reminder to a value.");
        }
    }
}
