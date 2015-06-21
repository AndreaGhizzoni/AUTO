package it.science.unitn.lpsmt.auto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public Vehicle( Long id, String name, String plate, Fuel fuel, Date purchaseDate ){
        this.setId(id);
        this.setName(name);
        this.setPlate(plate);
        this.setFuel(fuel);
        this.setPurchaseDate(purchaseDate);
    }

    public Vehicle( Long id, String name, String plate, Fuel fuel ){
        this(id, name, plate, fuel, null);
    }

//==================================================================================================
// METHOD
//==================================================================================================
    public void addCost( Cost c ) throws IllegalArgumentException{
        if( this.isDefaultVehicle() )
            return;
        if( c == null )
            throw new IllegalArgumentException("Cost to add can not be null.");
        if( !c.getVehicle().equals(this)  )
            throw new IllegalArgumentException("Cost is already associated with another Vehicle.");
        this.costs.add(c);
    }

    public void removeCost( Long id ) throws IllegalArgumentException{
        if( id == null )
            throw new IllegalArgumentException("Id to remove cost can not be null.");
        if( id <= Const.NO_DB_ID_SET )
            throw new IllegalArgumentException("Id to remove cost can not be equal or " +
                    "less than Const.NO_DB_ID_SET.");

        for( Cost c : this.costs ){
            if( c.getId().equals(id) ){
                this.removeCost(c);
                break;
            }
        }
    }

    public void removeCost( int index ) throws IllegalArgumentException{
        if( index < 0 || index >= this.costs.size() )
            throw new IllegalArgumentException("Index of cost to remove is out of bound.");
        this.costs.remove(index);
    }

    public void removeCost( Cost c ) throws IllegalArgumentException{
        if( c == null )
            throw new IllegalArgumentException("Cost to remove can not be null.");
        if( this.costs.indexOf(c) == -1 )
            throw new IllegalArgumentException("Cost to remove not found in this Vehicle.");
        this.costs.remove(c);
    }

    public void removeAllCosts(){ this.costs.clear(); }

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

    public void setName(String name) throws IllegalArgumentException{
        if( name == null || name.isEmpty() )
            throw new IllegalArgumentException("Name of vehicle can not be null or empty string");
        this.name = name;
    }

    public void setPlate(String plate) throws IllegalArgumentException{
        if( plate == null || plate.isEmpty() )
            throw new IllegalArgumentException("Plate can not be null or empty string.");
        this.plate = plate;
    }

    public void setFuel(Fuel fuel) throws IllegalArgumentException{
        if( fuel == null )
            throw new IllegalArgumentException("Fuel of vehicle can not be null.");
        this.fuel = fuel;
    }

    public void setPurchaseDate(Date purchaseDate) throws IllegalArgumentException{
        if( purchaseDate != null && purchaseDate.after(new Date()) )
            throw new IllegalArgumentException("Purchase Date can not be in the future.");
        this.purchaseDate = purchaseDate;
    }

    public void setCosts( List<Cost> list ) throws IllegalArgumentException{
        if( list == null )
            throw new IllegalArgumentException("List of Cost to set can not be null.");
        this.costs.clear();
        this.costs.addAll(list);
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Long getId(){ return this.id; }

    public String getName() { return name; }

    public String getPlate() { return plate; }

    public Fuel getFuel(){ return  this.fuel; }

    public Date getPurchaseDate() { return purchaseDate; }

    public ArrayList<Cost> getCosts(){ return this.costs; }

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

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", plate='" + plate + '\'' +
                ", fuel=" + fuel +
                ", purchaseDate=" + purchaseDate +
                ", costs=" + costs +
                '}';
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add dok
     */
    public enum Fuel{
        GASOLINE,    //diesel
        LPG,         //gpl
        NATURAL_GAS, //metano
        PETROL       //benzina
    }

    /**
     * TODO add doc
     */
    public static class Default{
        private static Vehicle instance;
        private static String DEF_PLATE = "XX123XX";
        private static String DEF_NAME  = "Default";

        public static Vehicle get(){
            if(instance == null){
                instance = new Vehicle(Const.NO_DB_ID_SET, DEF_NAME, DEF_PLATE, Fuel.PETROL );
            }
            return instance;
        }
    }

    /**
     * This is the representation of the Database Table.
     * The fields name are in the same order as there are in the sql create statement.
     * =============================== DO NOT MOVE UP OR DOWN! =====================================
     */
    public static final class SQLData{
        public static final String ID            = "id";
        public static final String NAME          = "name";
        public static final String PLATE         = "plate";
        public static final String FUEL          = "fuel";
        public static final String PURCHASE_DATA = "purchase_data";

        public static final String TABLE_NAME    = Vehicle.class.getSimpleName().toLowerCase();
        public static final String[] ALL_COLUMNS = {ID, NAME, PLATE, FUEL, PURCHASE_DATA};

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( "+
                    ID+" integer primary key autoincrement, "+
                    NAME+" text not null, "+
                    PLATE+" text not null, "+
                    FUEL+" text not null, "+
                    PURCHASE_DATA+" datetime "+
                ");";
    }
}
