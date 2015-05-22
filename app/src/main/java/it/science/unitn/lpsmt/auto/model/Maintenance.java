package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add doc
 */
public class Maintenance extends Cost {
    private String name;
    private Maintenance.Type type;

    private Place place;
    private Integer calendarID;

    public Maintenance( Type type, String name, Float amount, Place place, Integer calendarID,
                        String notes, Long id ) {
        super(amount, notes, id);
        this.setType(type);
        this.setName(name);
        this.setPlace(place);
        this.setCalendarID(calendarID);
    }

    public Maintenance( Long id, Float amount, String name, Maintenance.Type type ){
        super(id, amount);
        this.setName(name);
        this.setType(type);
        this.setPlace(null);
        this.setCalendarID(null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setName(String name) throws IllegalArgumentException{
        if( name == null || name.isEmpty() )
            throw new IllegalArgumentException("Name can not be null or empty string.");
        this.name = name;
    }

    public void setType(Type type) throws IllegalArgumentException{
        if( type == null )
            throw new IllegalArgumentException("Type can not be null");
        this.type = type;
    }

    public void setPlace( Place place ){
        this.place = place;
    }

    public void setCalendarID( Integer id ) throws IllegalArgumentException{
        if( id != null && id <= 0 )
            throw new IllegalArgumentException("Calendar ID to set can not be <= then zero.");
        this.calendarID = id;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public String getName() { return name; }

    public Type getType() { return type; }

    public Place getPlace(){ return place; }

    public Integer getCalendarID(){ return this.calendarID; }

//==================================================================================================
// INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public enum Type {
        ORDINARY,
        EXTRAORDINARY,
        TAX
    }
}
