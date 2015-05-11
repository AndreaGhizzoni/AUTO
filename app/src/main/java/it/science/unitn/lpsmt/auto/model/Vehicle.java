package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

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

    public Vehicle( String name, String plate, Date purchaseDate ){
        this(name, plate, purchaseDate, -1L);
    }

    public Vehicle( String name, String plate ){
        this(name, plate, null);
    }

    public Vehicle(){
        this(null, null);
    }

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

    public void setId(Long id){
        if( id != null && !id.equals(this.id) )
            this.id = id;
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
