package it.science.unitn.lpsmt.auto.model;

import java.util.ArrayList;
import java.util.Date;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class Vehicle {
    private Long id;
    private String name;
    private String plate;
    private Fuel fuel;

    private Date purchaseDate;
    private final ArrayList<Cost> costs = new ArrayList<>();

    //TODO add simplify constructor

    public Vehicle( String name, String plate, Fuel fuel, Date purchaseDate, Long id  ){
        this.setId(id);
        this.setName(name);
        this.setPlate(plate);
        this.setFuel(fuel);
        this.setPurchaseDate(purchaseDate);
    }

//==================================================================================================
// METHOD
//==================================================================================================
    public void addCost( Cost c ) throws IllegalArgumentException{
        if( c == null )
            throw new IllegalArgumentException("Cost to add can not be null.");

        this.costs.add(c);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId(Long id) throws IllegalArgumentException{
        if( id == null )
            this.id = Const.NO_DB_ID_SET;
        else if( id < Const.NO_DB_ID_SET )
                throw new IllegalArgumentException("ID of vehicle can not be less then "+Const.NO_DB_ID_SET);
        else
            this.id = id;
    }

    public void setPlate(String plate) throws IllegalArgumentException{
        if( plate == null || plate.isEmpty() )
            throw new IllegalArgumentException("Plate can not be null or empty string.");

        this.plate = plate;
    }

    public void setPurchaseDate(Date purchaseDate) throws IllegalArgumentException{
        if( purchaseDate != null && purchaseDate.after(new Date()) )
            throw new IllegalArgumentException("Purchase Date can not be in the future.");

        this.purchaseDate = purchaseDate;
    }

    public void setName(String name) throws IllegalArgumentException{
        if( name == null || name.isEmpty() )
            throw new IllegalArgumentException("Name of vehicle can not be null or empty string");

        this.name = name;
    }

    public void setFuel(Fuel fuel) throws IllegalArgumentException{
        if( fuel == null )
            throw new IllegalArgumentException("Fuel of vehicle can not be null.");

        this.fuel = fuel;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public String getPlate() { return plate; }

    public Date getPurchaseDate() { return purchaseDate; }

    public String getName() { return name; }

    public Long getId(){ return this.id; }

    public ArrayList<Cost> getCosts(){ return this.costs; }

    public Fuel getFuel(){ return  this.fuel; }

    public boolean isDefaultVehicle(){ return this.equals(Default.get()); }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (id != null ? !id.equals(vehicle.id) : vehicle.id != null) return false;
        return !(plate != null ? !plate.equals(vehicle.plate) : vehicle.plate != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (plate != null ? plate.hashCode() : 0);
        return result;
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    public enum Fuel{
        //TODO add some fuel
        GAS
    }

    /**
     * TODO add doc
     */
    public static class Default{
        public static Vehicle instance;
        private static String DEF_PLATE = "XX123XX";

        public static Vehicle get(){
            if(instance == null){
                instance = new Vehicle(
                        "Default",
                        DEF_PLATE,
                        Fuel.GAS,
                        null,
                        Const.NO_DB_ID_SET
                );
            }
            return instance;
        }
    }

    /**
     * TODO add doc
     */
    public static final class SQLData{
        public static final String TABLE_NAME    = Vehicle.class.getSimpleName().toLowerCase();
        public static final String ID            = "id";
        public static final String NAME          = "name";
        public static final String PLATE         = "plate";
        public static final String FUEL          = "fuel";
        public static final String PURCHASE_DATA = "purchase_data";

        public static final String[] ALL_COLUMNS = {ID, NAME, PLATE, PURCHASE_DATA};

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( "+
                    ID+" integer primary key autoincrement, "+
                    NAME+" text not null, "+
                    PLATE+" text not null, "+
                    PURCHASE_DATA+" datetime not null "+
                ");";
    }
}
