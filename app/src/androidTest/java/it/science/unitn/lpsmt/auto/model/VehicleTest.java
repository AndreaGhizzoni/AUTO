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
        Vehicle tmp = getVehicleInstance();

        try{
            tmp.setId(null);
            boolean cond = tmp.getId().equals(Const.NO_DB_ID_SET);
            assertTrue("passing null to setId() will be set to NO_DB_ID_SET", cond);
        }catch (IllegalArgumentException ex){
            fail("Expecting setId(null) will set id to NO_DB_ID_SET");
        }

        try{
           tmp.setId(Const.NO_DB_ID_SET-1);
           fail("I can not set a ID less then Const.NO_DB_ID_SET");
        }catch (IllegalArgumentException ex){}

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

        try{
            tmp.addCost(null);
            fail("I can add a null cost to a Vehicle.");
        }catch (IllegalArgumentException ex){}

        try{
            Cost c = null;
            tmp.removeCost(c);
            fail("I can remove a cost by a null object reference.");
        }catch (IllegalArgumentException ex){}

        try{
            Long id = null;
            tmp.removeCost(id);
            fail("I can remove a cost by a null id references.");
        }catch (IllegalArgumentException ex){}

        try{
            tmp.removeCost(Const.NO_DB_ID_SET);
            fail("I can remove a cost by id == Const.NO_DB_ID_SET.");
        }catch (IllegalArgumentException ex){}

        try{
            int minusOne = -1;
            tmp.removeCost(minusOne);
            fail("I can remove a cost with index == -1.");
        }catch (IllegalArgumentException ex){}

        try{
            int over = tmp.getCosts().size()+2;
            tmp.removeCost(over);
            fail("I can remove a cost with index >> getCosts().size().");
        }catch (IllegalArgumentException ex){}

        try {
            getRefuelInstance(Vehicle.Default.get());
            fail("I can assign the default Vehicle on a Refuel.");
        }catch (IllegalArgumentException ex){}

        try {
            getMaintenanceInstance(Vehicle.Default.get());
            fail("I can assign the default Vehicle on a Maintenance.");
        }catch (IllegalArgumentException ex){}

        // testing the adding procedure
        int costsBefore = tmp.getCosts().size();
        Cost ref = getRefuelInstance(tmp);
        int costsAfter = tmp.getCosts().size();
        assertTrue("Costs list before and after the insertion must be different.", costsBefore != costsAfter);
        assertTrue("Costs list before and after the insertion must be different by one.", costsBefore+1 == costsAfter);

        // testing remove by object reference
        tmp.removeCost(ref);
        int afterRemoving = tmp.getCosts().size();
        assertTrue("After removeCost(Cost obj), the size must be as the original", costsBefore == afterRemoving);

        ref.setId(1L);
        tmp.addCost(ref);// add again

        // testing removing by id
        tmp.removeCost(ref.getId());
        afterRemoving = tmp.getCosts().size();
        assertTrue("After removeCost(Long id), the size must be as the original", costsBefore == afterRemoving);

        tmp.addCost(ref);// and add again

        // testing removing by position
        int index = tmp.getCosts().indexOf(ref);
        tmp.removeCost(index);
        afterRemoving = tmp.getCosts().size();
        assertTrue("After removeCost(int index), the size must be as the original", costsBefore == afterRemoving);
    }
}
