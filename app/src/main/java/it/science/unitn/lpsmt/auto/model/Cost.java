package it.science.unitn.lpsmt.auto.model;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add description
 */
public abstract class Cost {
    private Long id;
    private Float amount;
    private String notes;

    public Cost(Float amount, String notes, Long id){
        this.setAmount(amount);
        this.setNotes(notes);
        this.setId(id);
    }

    public Cost(Long id, Float amount){
        this.setId(id);
        this.setAmount(amount);
        this.setNotes(null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId(Long id) throws IllegalArgumentException{
        if( id == null )
            this.id = Const.NO_DB_ID_SET;
        else if( id < Const.NO_DB_ID_SET )
            throw new IllegalArgumentException("ID of Cost can not be less then "+Const.NO_DB_ID_SET);
        else
            this.id = id;
    }

    public void setAmount(Float amount) throws IllegalArgumentException{
        if( amount == null )
            throw new IllegalArgumentException("Amount of Cost can not be null.");
        if( amount < 1 )
            throw new IllegalArgumentException("Amount of Cost can not be equal or less then zero.");
        this.amount = amount;
    }

    public void setNotes(String notes) {
        if( notes == null )
            this.notes = "";
        else
            this.notes = notes;
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public Long getId(){ return this.id; }

    public Float getAmount() { return amount; }

    public String getNotes() { return notes; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static final class SQLData{
        public static final String TABLE_NAME = Cost.class.getSimpleName().toLowerCase();
        public static final String ID         = "id" ;
        public static final String AMOUNT     = "amount";
        public static final String NOTES      = "notes";
        public static final String CLASS      = "class"; // subclass of Cost
        public static final String PLACE_ID   = "place_id";

        // if class == refuel these fields are set
        public static final String PRICE_PER_LITER = "price_per_liter";
        public static final String DATE            = "date";
        public static final String AT_KM           = "at_km";

        // if class == maintenance these fields are set
        public static final String NAME        = "name";
        public static final String TYPE        = "type";
        public static final String CALENDAR_ID = "calendar_id";

        public static final String[] ALL_COLUMNS = {ID, AMOUNT, NOTES, CLASS, PLACE_ID,
                PRICE_PER_LITER, DATE, AT_KM, NAME, TYPE, CALENDAR_ID};

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( "+
                    ID+" integer primary key autoincrement, "+
                    AMOUNT+" real not null, "+
                    NOTES+" text, "+
                    CLASS+" text not null, "+
                    PLACE_ID+" integer, "+
                    PRICE_PER_LITER+" real, "+
                    DATE+" datetime, "+
                    AT_KM+" integer, "+
                    NAME+" text, "+
                    TYPE+" text, "+
                    CALENDAR_ID+" integer, "+
                    "foreign key ("+PLACE_ID+") references " +
                        Place.SQLData.TABLE_NAME+" ("+Place.SQLData.ID+") "+
                ");";
    }
}
