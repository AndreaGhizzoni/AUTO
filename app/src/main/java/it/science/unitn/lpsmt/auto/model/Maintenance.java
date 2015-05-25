package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add doc
 */
public class Maintenance extends Cost {
    private String name;
    private Maintenance.Type type;

    private Place place;
    private Integer calendarID;

    public Maintenance( Long id, Vehicle vehicle, Float amount, String notes, String name,
                        Maintenance.Type type, Place place, Integer calendarID){
        super(id, vehicle, amount, notes);
        this.setName(name);
        this.setType(type);
        this.setPlace(place);
        this.setCalendarID(calendarID);
    }

    public Maintenance( Long id, Vehicle vehicle, Float amount, String name,
                        Maintenance.Type type ){
        this(id, vehicle, amount, null, name, type, null, null);
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
// OVERRIDE
//==================================================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Maintenance that = (Maintenance) o;

        if (!name.equals(that.name)) return false;
        if (type != that.type) return false;
        if (place != null ? !place.equals(that.place) : that.place != null) return false;
        return !(calendarID != null ? !calendarID.equals(that.calendarID) : that.calendarID != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (calendarID != null ? calendarID.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Maintenance{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", place=" + place +
                ", calendarID=" + calendarID +
                "} " + super.toString();
    }

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
