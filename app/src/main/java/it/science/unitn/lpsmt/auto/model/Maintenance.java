package it.science.unitn.lpsmt.auto.model;

/**
 * TODO add doc
 */
public class Maintenance extends Cost {
    String name;
    Maintenance.Type type;
    Place place;
    Reminder reminder;

    public Maintenance( String name, Type type, Float amount, Place place, Reminder reminder,
                        String notes ) {
        super(amount, notes);
        this.setName(name);
        this.setType(type);
        this.setPlace(place);
        this.setReminder(reminder);
    }

    public Maintenance( String name, Float amount ){
        this(name, null, amount, null, null, null);
    }

    public Maintenance(){ super(); }

//==================================================================================================
//  METHOD
//==================================================================================================
    // not sure for these kind of methods
    public static Maintenance getOrdinaryMaintenance(String name, Float amount){
        Maintenance m = new Maintenance();
        m.setType(Type.ORDINARY);
        m.setName(name);
        m.setAmount(amount);
        // TODO maybe set with some place and reminder
        return m;
    }

    public static Maintenance getExtraordinaryMaintenance(String name, Float amount){
        Maintenance m = new Maintenance();
        m.setType(Type.EXTRAORDINARY);
        m.setName(name);
        m.setAmount(amount);
        //TODO maybe set with some place and reminder
        return m;
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setName(String name) {
        if( name != null )
            this.name = name;
    }

    public void setType(Type type) {
        if( type != null )
            this.type = type;
    }

    public void setPlace( Place place ){
        if( place != null )
            this.place = place;
    }

    public void setReminder( Reminder reminder ){
        if( reminder != null )
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
