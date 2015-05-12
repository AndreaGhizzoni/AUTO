package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class Vehicle {
    private Long id;
    private String name;
    private Date purchaseDate;
    private String plate;
    // photo ?

    public Vehicle( String name, String plate, Date purchaseDate, Long id  ){
        this.setName(name);
        this.setPlate(plate);
        this.setPurchaseDate(purchaseDate);
        this.setId(id);
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

    public void setPlate(String plate) {
        if( plate != null )
            this.plate = plate;
    }

    // TODO do not check if is null, null can be passed to constructor to say "not set"
    public void setPurchaseDate(Date purchaseDate) throws IllegalArgumentException{
        if( purchaseDate == null )
            throw new IllegalArgumentException("Purchase Date of vehicle can not be null.");
        if( purchaseDate.after(new Date()) )
            throw new IllegalArgumentException("Purchase Date can not be in the future.");

        this.purchaseDate = purchaseDate;
    }

    public void setName(String name) throws IllegalArgumentException{
        if( name == null || name.isEmpty() )
            throw new IllegalArgumentException("Name of vehicle can not be null or empty string");

        this.name = name;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public String getPlate() { return plate; }

    public Date getPurchaseDate() { return purchaseDate; }

    public String getName() { return name; }

    public Long getId(){ return this.id; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static class Default{
        public static Vehicle instance;

        public Vehicle get(){
            if(instance == null){
                instance = new Vehicle(
                        "Default",
                        "XX123XX",
                        new Date(),
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
        public static final String PURCHASE_DATA = "purchase_data";
        public static final String PLATE         = "plate";
    }
}
