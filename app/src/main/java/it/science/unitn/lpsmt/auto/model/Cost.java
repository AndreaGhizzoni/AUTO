package it.science.unitn.lpsmt.auto.model;

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

//==================================================================================================
// SETTER
//==================================================================================================
    public void setAmount(Float amount) {
        if( amount != null && amount > 0 )
            this.amount = amount;
    }

    public void setNotes(String notes) {
        if( notes != null )
            this.notes = notes;
    }

    public void setId( Long id ){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public Float getAmount() { return amount; }

    public String getNotes() { return notes; }

    public Long getId(){ return this.id; }

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
        public static final String REMINDER_ID = "reminder_id";

        public static final String SQL_CREATE =
                "create table "+ Cost.SQLData.TABLE_NAME+"( " +
                Cost.SQLData.ID+" integer primary key autoincrement, " +
                Cost.SQLData.AMOUNT+" real not null, " +
                Cost.SQLData.NOTES+" text, " +
                Cost.SQLData.CLASS+" text not null, " +
                Cost.SQLData.PLACE_ID+" integer, " +
                Cost.SQLData.PRICE_PER_LITER+" real, " +
                Cost.SQLData.DATE+" datetime, " +
                Cost.SQLData.AT_KM+" integer, " +
                Cost.SQLData.NAME+" text, " +
                Cost.SQLData.TYPE+" text, " +
                Cost.SQLData.REMINDER_ID+" integer, " +
                "foreign key ("+Cost.SQLData.PLACE_ID+") references " +
                Place.SQLData.TABLE_NAME+" ("+Place.SQLData.ID+"), " +
                "foreign key ("+Cost.SQLData.REMINDER_ID+") references " +
                Reminder.SQLData.ID+" ("+Reminder.SQLData.ID+") );";
    }
}
