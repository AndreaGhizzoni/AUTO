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
    public void setId(Long id){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

    public void setPlate(String plate) {
        if( plate != null )
            this.plate = plate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        // TODO check if future date
        if( purchaseDate != null )
            this.purchaseDate = purchaseDate;
    }

    public void setName(String name) {
        // TODO check if null or empty string

        this.name = name;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public String getPlate() {
        return plate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getName() {
        return name;
    }


//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static class Default{
        public static Vehicle defInstance;

        public Vehicle get(){
            if(defInstance == null){
                defInstance = new Vehicle(
                        "Default",
                        "XX123XX",
                        new Date(),
                        Const.NO_DB_ID_SET
                );
            }
            return defInstance;
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
