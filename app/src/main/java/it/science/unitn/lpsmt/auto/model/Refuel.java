package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

/**
 * TODO add doc
 */
public class Refuel extends Cost {
    private Float pricePerLiter;
    private Date date;
    private Integer km;
    private Place place;

    public Refuel( Float amount, Float pricePerLiter, Integer km, Place place, Date date,
                   String note, Long id ){
        super(amount, note, id);
        this.setPricePerLiter(pricePerLiter);
        this.setKm(km);
        this.setDate(date);
        this.setPlace(place);
    }

    public Refuel( Long id, Float amount, Float ppl, Date date, Integer km, Place place ){
        super(id, amount);
        this.setPricePerLiter(ppl);
        this.setDate(date);
        this.setKm(km);
        this.setPlace(place);
        this.setNotes(null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setPricePerLiter(Float ppl) throws IllegalArgumentException{
        if( ppl == null )
            throw new IllegalArgumentException( "Price per liter can not be null." );
        if( ppl <= 0 )
            throw new IllegalArgumentException( "Price per liter can not be <= then zero." );
        this.pricePerLiter = ppl;
    }

    public void setDate(Date date) throws IllegalArgumentException{
        if( date == null )
            throw new IllegalArgumentException( "Date can not be null." );
        this.date = date;
    }

    public void setKm(Integer km) throws IllegalArgumentException{
        if( km == null )
            throw new IllegalArgumentException( "Km can not be null." );
        if( km < 0 )
            throw new IllegalArgumentException( "Km cab not be less then zero." );
        this.km = km;
    }

    public void setPlace( Place location ){
        this.place = location;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Float getPricePerLiter() { return pricePerLiter; }

    public Date getDate() { return date; }

    public Integer getKm() { return km; }

    public Place getPlace(){ return place; }
}
