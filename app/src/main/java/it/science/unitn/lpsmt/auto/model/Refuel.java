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

//==================================================================================================
// SETTER
//==================================================================================================
    public void setPricePerLiter(Float pricePerLiter) {
        if(pricePerLiter != null)
            this.pricePerLiter = pricePerLiter;
    }

    public void setDate(Date date) {
        if(date != null)
            this.date = date;
    }

    public void setKm(Integer km) {
        if(km != null)
            this.km = km;
    }

    public void setPlace( Place location ){
        if( location != null )
            this.place = location;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public float getPricePerLiter() { return pricePerLiter; }

    public Date getDate() { return date; }

    public Integer getKm() { return km; }

    public Place getPlace(){ return place; }
}
