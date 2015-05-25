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

    public Refuel( Long id, Vehicle vehicle, Float amount, String notes, Float ppl,
                   Date date, Integer km,Place place ){
        super(id, vehicle, amount, notes);
        this.setPricePerLiter(ppl);
        this.setDate(date);
        this.setKm(km);
        this.setPlace(place);
    }

    public Refuel( Long id, Vehicle vehicle, Float amount, Float ppl,
                   Date date, Integer km, Place place ){
        this(id, vehicle, amount, null, ppl, date, km, place);
    }

//==================================================================================================
//  SETTER
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
//  GETTER
//==================================================================================================
    public Float getPricePerLiter() { return pricePerLiter; }

    public Date getDate() { return date; }

    public Integer getKm() { return km; }

    public Place getPlace(){ return place; }

//==================================================================================================
//  OVERRIDE
//==================================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Refuel refuel = (Refuel) o;

        if (!pricePerLiter.equals(refuel.pricePerLiter)) return false;
        if (!date.toString().equals(refuel.date.toString())) return false;
        if (!km.equals(refuel.km)) return false;
        return place.equals(refuel.place);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + pricePerLiter.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + km.hashCode();
        result = 31 * result + place.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Refuel{" +
                "pricePerLiter=" + pricePerLiter +
                ", date=" + date +
                ", km=" + km +
                ", place=" + place +
                "} " + super.toString();
    }
}
