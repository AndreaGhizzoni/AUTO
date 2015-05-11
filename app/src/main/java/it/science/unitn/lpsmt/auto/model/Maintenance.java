package it.science.unitn.lpsmt.auto.model;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class Maintenance extends Cost {
    private String name;
    private Maintenance.Type type;
    private Place place;
    private Reminder reminder;

    public Maintenance( Type type, String name, Float amount, Place place, Reminder reminder,
                        String notes ) {
        super(amount, notes, Const.NO_DB_ID_SET);
        this.setType(type);
        this.setName(name);
        this.setPlace(place);
        this.setReminder(reminder);
    }

    public Maintenance( String name, Float amount ){
        this(null, name, amount, null, null, null);
    }

    public Maintenance(){
        this(null, null);
    }

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

    public static Maintenance getTaxMaintenance(String name, Float amount){
        Maintenance m = new Maintenance();
        m.setType(Type.TAX);
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
