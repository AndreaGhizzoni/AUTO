package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add doc
 */
public class Maintenance extends Cost {
    private String name;
    private Maintenance.Type type;
    private Place place;
    private Reminder reminder;

    public Maintenance( Type type, String name, Float amount, Place place, Reminder reminder,
                        String notes, Long id ) {
        super(amount, notes, id);
        this.setType(type);
        this.setName(name);
        this.setPlace(place);
        this.setReminder(reminder);
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

    public void setReminder( Reminder reminder ){
        this.reminder = reminder;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public String getName() { return name; }

    public Type getType() { return type; }

    public Place getPlace(){ return place; }

    public Reminder getReminder(){ return reminder; }

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
