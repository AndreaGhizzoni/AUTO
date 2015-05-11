package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

/**
 * TODO add doc
 */
public class Vehicle {
    String name;
    Date purchaseDate;
    String plate;
    // photo ?

    public Vehicle( String name, String plate, Date purchaseDate ){
        this.setName(name);
        this.setPlate(plate);
        this.setPurchaseDate(purchaseDate);
    }

    public Vehicle( String name, String plate ){
        this(name, plate, null);
    }

    public Vehicle(){}

//==================================================================================================
// SETTER
//==================================================================================================
    public void setPlate(String plate) {
        if( plate != null )
            this.plate = plate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        if( purchaseDate != null )
            this.purchaseDate = purchaseDate;
    }

    public void setName(String name) {
        if( name != null )
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
                defInstance = new Vehicle();
                defInstance.setName("Default");
                defInstance.setPlate("XX123XX");
                defInstance.setPurchaseDate(new Date());
            }
            return defInstance;
        }
    }
}
